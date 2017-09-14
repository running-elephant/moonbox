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

import com.google.common.collect.ImmutableList;
import edp.moonbox.calcite.plan.*;
import edp.moonbox.calcite.rel.RelNode;
import edp.moonbox.calcite.rel.core.TableScan;
import edp.moonbox.calcite.rel.metadata.RelMetadataQuery;
import edp.moonbox.calcite.rel.rules.*;
import edp.moonbox.calcite.rel.type.RelDataType;

import java.util.List;

/**
 * Relational expression representing a scan of an Elasticsearch type.
 *
 * <p> Additional operations might be applied,
 * using the "find" method.</p>
 */
public class ElasticsearchTableScan extends TableScan implements ElasticsearchRel {
  private final ElasticsearchTable elasticsearchTable;
  private final RelDataType projectRowType;

  /**
   * Creates an ElasticsearchTableScan.
   *
   * @param cluster Cluster
   * @param traitSet Trait set
   * @param table Table
   * @param elasticsearchTable Elasticsearch table
   * @param projectRowType Fields and types to project; null to project raw row
   */
  protected ElasticsearchTableScan(RelOptCluster cluster, RelTraitSet traitSet, RelOptTable table,
                                   ElasticsearchTable elasticsearchTable, RelDataType projectRowType) {
    super(cluster, traitSet, table);
    this.elasticsearchTable = elasticsearchTable;
    this.projectRowType = projectRowType;

    assert elasticsearchTable != null;
    assert getConvention() == CONVENTION;
  }

  @Override public RelNode copy(RelTraitSet traitSet, List<RelNode> inputs) {
    assert inputs.isEmpty();
    return this;
  }

  @Override public RelDataType deriveRowType() {
    return projectRowType != null ? projectRowType : super.deriveRowType();
  }

  @Override public RelOptCost computeSelfCost(RelOptPlanner planner, RelMetadataQuery mq) {
    final float f = projectRowType == null ? 1f : (float) projectRowType.getFieldCount() / 100f;
    return super.computeSelfCost(planner, mq).multiplyBy(.1 * f);
  }

  @Override public void register(RelOptPlanner planner) {
    final ImmutableList<RelOptRule> rules = ImmutableList.of(
            AggregateStarTableRule.INSTANCE,
            AggregateStarTableRule.INSTANCE2,
            TableScanRule.INSTANCE,
            JoinAssociateRule.INSTANCE,
            ProjectMergeRule.INSTANCE,
            FilterTableScanRule.INSTANCE,
            ProjectFilterTransposeRule.INSTANCE,
            FilterProjectTransposeRule.INSTANCE,
            FilterJoinRule.FILTER_ON_JOIN,
            JoinPushExpressionsRule.INSTANCE,
            AggregateExpandDistinctAggregatesRule.INSTANCE,
            AggregateReduceFunctionsRule.INSTANCE,
            FilterAggregateTransposeRule.INSTANCE,
            ProjectWindowTransposeRule.INSTANCE,
            JoinCommuteRule.INSTANCE,
            JoinPushThroughJoinRule.RIGHT,
            JoinPushThroughJoinRule.LEFT,
            SortProjectTransposeRule.INSTANCE,
            SortJoinTransposeRule.INSTANCE,
            SortUnionTransposeRule.INSTANCE);

    for (RelOptRule rule :rules) {
      planner.removeRule(rule);
    }
    planner.addRule(ElasticsearchToEnumerableConverterRule.INSTANCE);
    for (RelOptRule rule: ElasticsearchRules.RULES) {
      planner.addRule(rule);
    }
  }

  @Override public void implement(Implementor implementor) {
    implementor.elasticsearchTable = elasticsearchTable;
    implementor.table = table;
  }
}

// End ElasticsearchTableScan.java
