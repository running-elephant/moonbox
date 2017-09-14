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

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import edp.moonbox.calcite.adapter.enumerable.*;
import edp.moonbox.calcite.linq4j.tree.BlockBuilder;
import edp.moonbox.calcite.linq4j.tree.Expression;
import edp.moonbox.calcite.linq4j.tree.Expressions;
import edp.moonbox.calcite.linq4j.tree.MethodCallExpression;
import edp.moonbox.calcite.plan.*;
import edp.moonbox.calcite.prepare.CalcitePrepareImpl;
import edp.moonbox.calcite.rel.RelNode;
import edp.moonbox.calcite.rel.convert.ConverterImpl;
import edp.moonbox.calcite.rel.metadata.RelMetadataQuery;
import edp.moonbox.calcite.rel.type.RelDataType;
import edp.moonbox.calcite.runtime.Hook;
import edp.moonbox.calcite.util.BuiltInMethod;
import edp.moonbox.calcite.util.Pair;

import javax.annotation.Nullable;
import java.util.AbstractList;
import java.util.List;

/**
 * Relational expression representing a scan of a table in an Elasticsearch data source.
 */
public class ElasticsearchToEnumerableConverter extends ConverterImpl implements EnumerableRel {
  protected ElasticsearchToEnumerableConverter(RelOptCluster cluster, RelTraitSet traits,
                                               RelNode input) {
    super(cluster, ConventionTraitDef.INSTANCE, traits, input);
  }

  @Override public RelNode copy(RelTraitSet traitSet, List<RelNode> inputs) {
    return new ElasticsearchToEnumerableConverter(getCluster(), traitSet, sole(inputs));
  }

  @Override public RelOptCost computeSelfCost(RelOptPlanner planner, RelMetadataQuery mq) {
    return super.computeSelfCost(planner, mq).multiplyBy(.1);
  }

  @Override public Result implement(EnumerableRelImplementor implementor, Prefer prefer) {
    final BlockBuilder list = new BlockBuilder();
    final ElasticsearchRel.Implementor elasticsearchImplementor =
        new ElasticsearchRel.Implementor();
    elasticsearchImplementor.visitChild(0, getInput());
    final RelDataType rowType = getRowType();
    final PhysType physType = PhysTypeImpl.of(implementor.getTypeFactory(), rowType, prefer.prefer(JavaRowFormat.ARRAY));

    final Expression fields = list.append("fields",
        constantArrayList(
            Pair.zip(ElasticsearchRules.elasticsearchFieldNames(rowType),
                new AbstractList<Class>() {
                  @Override public Class get(int index) {
                    return physType.fieldClass(index);
                  }

                  @Override public int size() {
                    return rowType.getFieldCount();
                  }
                }),
            Pair.class));
    final Expression table = list.append("table",
        elasticsearchImplementor.table
            .getExpression(ElasticsearchTable.ElasticsearchQueryable.class));

    //wwww
    List<String> opList = elasticsearchImplementor.list;
    final Expression ops = list.append("ops", constantArrayList(opList, String.class));

    //final Expression offset = list.append("offset", Expressions.constant(elasticsearchImplementor.offset));

    final Expression fetch = list.append("fetch", Expressions.constant(elasticsearchImplementor.fetch));



/*    List<QueryBuilder> opList = elasticsearchImplementor.listQ;
    final Expression ops = list.append("ops", constantArrayList(new AbstractList<QueryBuilder>() {
      @Override
      public int size() {
        return opList.size();
      }

      @Override
      public QueryBuilder get(int index) {
        return opList.get(index);
      }
    }, List.class));*/

    Expression enumerable = list.append("enumerable",
        Expressions.call(table,
            ElasticsearchMethod.ELASTICSEARCH_QUERYABLE_FIND.method,
            ops,
            fields,
            fetch));

    if (CalcitePrepareImpl.DEBUG) {
      System.out.println("Elasticsearch: " + opList);
    }
    Hook.QUERY_PLAN.run(opList);
    list.add(Expressions.return_(null, enumerable));
    return implementor.result(physType, list.toBlock());
  }

  /** E.g. {@code constantArrayList("x", "y")} returns
   * "Arrays.asList('x', 'y')". */
  private static <T> MethodCallExpression constantArrayList(List<T> values, Class clazz) {
    return Expressions.call(BuiltInMethod.ARRAYS_AS_LIST.method,
        Expressions.newArrayInit(clazz, constantList(values)));
  }

  /** E.g. {@code constantList("x", "y")} returns
   * {@code {ConstantExpression("x"), ConstantExpression("y")}}. */
  private static <T> List<Expression> constantList(List<T> values) {
    return Lists.transform(values,
        new Function<T, Expression>() {
          @Nullable
          @Override public Expression apply(@Nullable T t) {
            return Expressions.constant(t);
          }
        });
  }
}

// End ElasticsearchToEnumerableConverter.java
