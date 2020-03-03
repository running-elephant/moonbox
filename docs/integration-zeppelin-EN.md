---
layout: global
title: Integration Zeppelin
---

#### Acquisition of Related JAR

- Download [moonbox-jdbc_2.11-0.3.0.jar](https://github.com/edp963/moonbox/releases)
- Download [moonbox-zeppelin-interpreter-0.3.0.jar](https://github.com/edp963/moonbox/releases/download/0.3.0-beta/moonbox-zeppelin-interpreter-0.7-0.3.0-beta.jar)

#### Configuration
   
1. Create `moonbox` directory in interpreter path of Zeppelin installation directory. Suppose `/opt/zeppelin` is Zeppelin installation directory.
```
cd /opt/zeppelin/interpreter
mkdir moonbox
```
2. Copy moonbox-jdbc_2.11-0.3.0.jar and moonbox-zeppelin-interpreter-0.3.0.jar into `moonbox` directory created just now.

3. Restart Zeppelin and create interpreter.

![Zeppelin Interpreter](https://raw.githubusercontent.com/edp963/moonbox/master/docs/img/integration-zeppelin.jpg)

Alter the configuration options and save it.

#### About Zeppelin

Please refer to [Zeppelin](http://zeppelin.apache.org) for details.
