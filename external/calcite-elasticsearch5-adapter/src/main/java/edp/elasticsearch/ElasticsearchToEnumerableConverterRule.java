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

import edp.moonbox.calcite.adapter.enumerable.EnumerableConvention;
import edp.moonbox.calcite.plan.RelTraitSet;
import edp.moonbox.calcite.rel.RelNode;
import edp.moonbox.calcite.rel.convert.ConverterRule;

/**
 * Rule to convert a relational expression from
 * {@link ElasticsearchRel#CONVENTION} to {@link EnumerableConvention}.
 */
public class ElasticsearchToEnumerableConverterRule extends ConverterRule {
  public static final ConverterRule INSTANCE = new ElasticsearchToEnumerableConverterRule();

  private ElasticsearchToEnumerableConverterRule() {
    super(RelNode.class, ElasticsearchRel.CONVENTION, EnumerableConvention.INSTANCE,
        "ElasticsearchToEnumerableConverterRule");
  }

  @Override public RelNode convert(RelNode relNode) {
    RelTraitSet newTraitSet = relNode.getTraitSet().replace(getOutConvention());
    return new ElasticsearchToEnumerableConverter(relNode.getCluster(), newTraitSet, relNode);
  }
}

// End ElasticsearchToEnumerableConverterRule.java
