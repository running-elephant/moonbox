/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

package org.apache.spark.launcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.spark.launcher.CommandBuilderUtils.*;
import static org.apache.spark.launcher.CommandBuilderUtils.checkState;

public class Launcher extends SparkLauncher {

  private static final AtomicInteger COUNTER = new AtomicInteger();

  public Process process;

  @Override
  public SparkAppHandle startApplication(SparkAppHandle.Listener... listeners) throws IOException {
    ChildProcAppHandle handle = LauncherServer.newAppHandle();
    for (SparkAppHandle.Listener l : listeners) {
      handle.addListener(l);
    }

    String loggerName = builder.getEffectiveConfig().get(CHILD_PROCESS_LOGGER_NAME);
    ProcessBuilder pb = createBuilder();
    // Only setup stderr + stdout to logger redirection if user has not otherwise configured output
    // redirection.
    if (loggerName == null) {
      String appName = builder.getEffectiveConfig().get(CHILD_PROCESS_LOGGER_NAME);
      if (appName == null) {
        if (builder.appName != null) {
          appName = builder.appName;
        } else if (builder.mainClass != null) {
          int dot = builder.mainClass.lastIndexOf(".");
          if (dot >= 0 && dot < builder.mainClass.length() - 1) {
            appName = builder.mainClass.substring(dot + 1, builder.mainClass.length());
          } else {
            appName = builder.mainClass;
          }
        } else if (builder.appResource != null) {
          appName = new File(builder.appResource).getName();
        } else {
          appName = String.valueOf(COUNTER.incrementAndGet());
        }
      }
      String loggerPrefix = getClass().getPackage().getName();
      loggerName = String.format("%s.app.%s", loggerPrefix, appName);
      pb.redirectErrorStream(true);
    }

    pb.environment().put(LauncherProtocol.ENV_LAUNCHER_PORT,
        String.valueOf(LauncherServer.getServerInstance().getPort()));
    pb.environment().put(LauncherProtocol.ENV_LAUNCHER_SECRET, handle.getSecret());
    try {
      this.process = pb.start();
      handle.setChildProc(process, loggerName);
    } catch (IOException ioe) {
      handle.kill();
      throw ioe;
    }

    return handle;
  }

  private ProcessBuilder createBuilder() {
    List<String> cmd = new ArrayList<>();
    String script = isWindows() ? "spark-submit.cmd" : "spark-submit";
    cmd.add(join(File.separator, builder.getSparkHome(), "bin", script));
    cmd.addAll(builder.buildSparkSubmitArgs());

    // Since the child process is a batch script, let's quote things so that special characters are
    // preserved, otherwise the batch interpreter will mess up the arguments. Batch scripts are
    // weird.
    if (isWindows()) {
      List<String> winCmd = new ArrayList<>();
      for (String arg : cmd) {
        winCmd.add(quoteForBatchScript(arg));
      }
      cmd = winCmd;
    }

    ProcessBuilder pb = new ProcessBuilder(cmd.toArray(new String[cmd.size()]));
    for (Map.Entry<String, String> e : builder.childEnv.entrySet()) {
      pb.environment().put(e.getKey(), e.getValue());
    }

    if (workingDir != null) {
      pb.directory(workingDir);
    }

    // Only one of redirectError and redirectError(...) can be specified.
    // Similarly, if redirectToLog is specified, no other redirections should be specified.
    checkState(!redirectErrorStream || errorStream == null,
        "Cannot specify both redirectError() and redirectError(...) ");
    checkState(!redirectToLog ||
            (!redirectErrorStream && errorStream == null && outputStream == null),
        "Cannot used redirectToLog() in conjunction with other redirection methods.");

    if (redirectErrorStream || redirectToLog) {
      pb.redirectErrorStream(true);
    }
    if (errorStream != null) {
      pb.redirectError(errorStream);
    }
    if (outputStream != null) {
      pb.redirectOutput(outputStream);
    }

    return pb;
  }
}
