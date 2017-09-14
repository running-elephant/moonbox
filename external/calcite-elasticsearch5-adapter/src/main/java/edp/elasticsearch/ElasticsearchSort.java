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

import edp.moonbox.calcite.plan.RelOptCluster;
import edp.moonbox.calcite.plan.RelOptCost;
import edp.moonbox.calcite.plan.RelOptPlanner;
import edp.moonbox.calcite.plan.RelTraitSet;
import edp.moonbox.calcite.rel.RelCollation;
import edp.moonbox.calcite.rel.RelFieldCollation;
import edp.moonbox.calcite.rel.RelNode;
import edp.moonbox.calcite.rel.core.Sort;
import edp.moonbox.calcite.rel.metadata.RelMetadataQuery;
import edp.moonbox.calcite.rel.type.RelDataTypeField;
import edp.moonbox.calcite.rex.RexLiteral;
import edp.moonbox.calcite.rex.RexNode;
import edp.moonbox.calcite.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link Sort}
 * relational expression in Elasticsearch.
 */
public class ElasticsearchSort extends Sort implements ElasticsearchRel {
  public ElasticsearchSort(RelOptCluster cluster, RelTraitSet traitSet, RelNode child,
      RelCollation collation, RexNode offset, RexNode fetch) {
    super(cluster, traitSet, child, collation, offset, fetch);
    assert getConvention() == CONVENTION;
    assert getConvention() == child.getConvention();
  }

  @Override public RelOptCost computeSelfCost(RelOptPlanner planner, RelMetadataQuery mq) {
    return super.computeSelfCost(planner, mq).multiplyBy(0.05);
  }

  @Override public Sort copy(RelTraitSet traitSet, RelNode relNode, RelCollation relCollation,
      RexNode offset, RexNode fetch) {
    return new ElasticsearchSort(getCluster(), traitSet, relNode, collation, offset, fetch);
  }

  @Override public void implement(Implementor implementor) {
    implementor.visitChild(0, getInput());
    if (!collation.getFieldCollations().isEmpty()) {
      final List<String> keys = new ArrayList<String>();
      final List<RelDataTypeField> fields = getRowType().getFieldList();

      for (RelFieldCollation fieldCollation: collation.getFieldCollations()) {
        final String name = fields.get(fieldCollation.getFieldIndex()).getName();
        keys.add(ElasticsearchRules.quote(name) + ": " + direction(fieldCollation));
      }

      implementor.add("\"sort\": [ " + Util.toString(keys, "{", "}, {", "}") + "]");
    }

    if (offset != null) {
      implementor.add("\"from\": " + ((RexLiteral) offset).getValue());
      implementor.offset = RexLiteral.intValue(offset);
    }

    if (fetch != null) {
      implementor.add("\"size\": " + ((RexLiteral) fetch).getValue());
      implementor.fetch = RexLiteral.intValue(fetch);
    }
  }

  private String direction(RelFieldCollation fieldCollation) {
    switch (fieldCollation.getDirection()) {
    case DESCENDING:
    case STRICTLY_DESCENDING:
      return "\"desc\"";
    case ASCENDING:
    case STRICTLY_ASCENDING:
    default:
      return "\"asc\"";
    }
  }
}

// End ElasticsearchSort.java
