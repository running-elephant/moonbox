/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edp.elasticsearch;

import edp.moonbox.calcite.adapter.java.JavaTypeFactory;
import edp.moonbox.calcite.plan.RelOptCluster;
import edp.moonbox.calcite.plan.RelOptCost;
import edp.moonbox.calcite.plan.RelOptPlanner;
import edp.moonbox.calcite.plan.RelTraitSet;
import edp.moonbox.calcite.rel.RelNode;
import edp.moonbox.calcite.rel.core.Project;
import edp.moonbox.calcite.rel.metadata.RelMetadataQuery;
import edp.moonbox.calcite.rel.type.RelDataType;
import edp.moonbox.calcite.rex.RexNode;
import edp.moonbox.calcite.util.Pair;
import edp.moonbox.calcite.util.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of {@link Project}
 * relational expression in Elasticsearch.
 */
public class ElasticsearchProject extends Project implements ElasticsearchRel {
  public ElasticsearchProject(RelOptCluster cluster, RelTraitSet traitSet, RelNode input,
      List<? extends RexNode> projects, RelDataType rowType) {
    super(cluster, traitSet, input, projects, rowType);
    assert getConvention() == ElasticsearchRel.CONVENTION;
    assert getConvention() == input.getConvention();
  }

  @Override public Project copy(RelTraitSet relTraitSet, RelNode input, List<RexNode> projects,
      RelDataType relDataType) {
    return new ElasticsearchProject(getCluster(), traitSet, input, projects, relDataType);
  }

  @Override public RelOptCost computeSelfCost(RelOptPlanner planner, RelMetadataQuery mq) {
    return super.computeSelfCost(planner, mq).multiplyBy(0.1);
  }

  @Override public void implement(Implementor implementor) {
    implementor.visitChild(0, getInput());

    final ElasticsearchRules.RexToElasticsearchTranslator translator =
      new ElasticsearchRules.RexToElasticsearchTranslator(
        (JavaTypeFactory) getCluster().getTypeFactory(),
        ElasticsearchRules.elasticsearchFieldNames(getInput().getRowType()));

    final List<String> findItems = new ArrayList<>();
    final List<String> scriptFieldItems = new ArrayList<>();
    for (Pair<RexNode, String> pair: getNamedProjects()) {
      final String name = pair.right;
      final String expr = pair.left.accept(translator);

      if (expr.equals("\"" + name + "\"")) {
        findItems.add(ElasticsearchRules.quote(name));
      } else if (expr.matches("\"literal\":.+")) {
        scriptFieldItems.add(ElasticsearchRules.quote(name) + ":{\"script\": "
          + expr.split(":")[1] + "}");
      } else {
//        scriptFieldItems.add(ElasticsearchRules.quote(name) + ":{\"script\":\"_source."
//          + expr.replaceAll("\"", "") + "\"}");
        scriptFieldItems.add(ElasticsearchRules.quote(name) + ":{\"script\":\"doc[\\\""
                + expr.replaceAll("\"", "") + "\\\"]\"}");
      }
    }

    //www
    final String sourceString = "\"_source\"" + " : " + "true";
    final String findString = Util.toString(findItems, "", ", ", "");
    final String scriptFieldString = "\"script_fields\": {"
            + Util.toString(scriptFieldItems, "", ", ", "") + "}";

    final String storeString = "\"stored_fields\" : [" + findString + "]";

    //final String fieldString = "\"fields\" : [" + findString + "]" + ", " + scriptFieldString;

    // cause java.util.ConcurrentModificationException
    /*for (String opfield : implementor.list) {
      if (opfield.startsWith("\"fields\"")) {
        implementor.list.remove(opfield);
      }
    }*/
    Iterator<String> iter = implementor.list.iterator();
    while (iter.hasNext()) {
      String item = iter.next();

      if (item.startsWith("\"_source\"")) {
        iter.remove();
      }

      if (item.startsWith("\"stored_fields\"")) {
        iter.remove();
      }

      if (item.startsWith("\"script_fields\"")) {
        iter.remove();
      }
    }


    implementor.add(sourceString);
    implementor.add(storeString);
    implementor.add(scriptFieldString);

  }


}

// End ElasticsearchProject.java
