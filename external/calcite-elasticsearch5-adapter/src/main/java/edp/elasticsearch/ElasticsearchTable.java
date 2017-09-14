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

import edp.moonbox.calcite.adapter.java.AbstractQueryableTable;
import edp.moonbox.calcite.linq4j.*;
import edp.moonbox.calcite.linq4j.function.Function1;
import edp.moonbox.calcite.plan.RelOptCluster;
import edp.moonbox.calcite.plan.RelOptTable;
import edp.moonbox.calcite.rel.RelNode;
import edp.moonbox.calcite.rel.type.*;
import edp.moonbox.calcite.schema.SchemaPlus;
import edp.moonbox.calcite.schema.TranslatableTable;
import edp.moonbox.calcite.schema.impl.AbstractTableQueryable;
import edp.moonbox.calcite.sql.type.SqlTypeFactoryImpl;
import edp.moonbox.calcite.sql.type.SqlTypeName;
import edp.moonbox.calcite.util.Util;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchModule;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Table based on an Elasticsearch type.
 */
public class ElasticsearchTable extends AbstractQueryableTable implements TranslatableTable {
  private final Client client;
  private final String indexName;
  private final String typeName;
  private RelProtoDataType protoRowType = null;
  /**
   * Creates an ElasticsearchTable.
   */
  public ElasticsearchTable(Client client, String indexName,
                            String typeName) {
    super(Object[].class);
    this.client = client;
    this.indexName = indexName;
    this.typeName = typeName;
  }

  @Override public String toString() {
    return "ElasticsearchTable{" + typeName + "}";
  }

  public RelDataType getRowType(RelDataTypeFactory relDataTypeFactory) {
      try {
          if(null == protoRowType) {
              final GetMappingsResponse getMappingsResponse1 = client.admin().indices().prepareGetMappings().addIndices(indexName).addTypes(typeName).get();
              final ImmutableOpenMap<String, MappingMetaData> indexMap = getMappingsResponse1.mappings().get(indexName);
              final Map<String, Object> typeMap = indexMap.get(typeName).getSourceAsMap();
              final Map<String, Map<String, String>> properties = (Map<String, Map<String, String>>) typeMap.get("properties");

              final RelDataTypeFactory typeFactory = new SqlTypeFactoryImpl(RelDataTypeSystem.DEFAULT);
              final RelDataTypeFactory.FieldInfoBuilder fieldInfo = typeFactory.builder();

              for (Map.Entry<String, Map<String, String>> elem : properties.entrySet()) {
                  final String colName = elem.getKey();
                  final String colType = elem.getValue().get("type");
                  //System.out.println("colName " + colName);
                  //System.out.println("colType " + colType);

                  SqlTypeName typeName = SqlTypeName.ANY;
                  if(colType.equals("text")) {
                    typeName = SqlTypeName.VARCHAR;
                  }
                  else if(colType.equals("long")) {
                      typeName = SqlTypeName.BIGINT;
                  }
                  else if(colType.equals("integer")) {
                      typeName = SqlTypeName.INTEGER;
                  }
                  else if(colType.equals("boolean")) {
                      typeName = SqlTypeName.BOOLEAN;
                  }
                  else if(colType.equals("double")) {
                      typeName = SqlTypeName.DOUBLE;
                  }
                  else if(colType.equals("float")) {
                      //typeName = SqlTypeName.FLOAT;
                      //TODO: spark parse the es float error, so set double here
                      typeName = SqlTypeName.DOUBLE;
                  }
                  else if(colType.equals("keyword")) {
                      typeName = SqlTypeName.VARCHAR;
                  }
                  else if(colType.equals("date")) {
                      //typeName = SqlTypeName.DATE;
                      /*TODO: JSON doesn’t have a date datatype, so dates in Elasticsearch can either be:
                        strings containing formatted dates, e.g. "2015-01-01" or "2015/01/01 12:10:30".
                        a long number representing milliseconds-since-the-epoch.
                        an integer representing seconds-since-the-epoch.
                        Internally, dates are converted to UTC (if the time-zone is specified) and stored as a long number representing milliseconds-since-the-epoch.
                       * */
                      typeName = SqlTypeName.VARCHAR;
                  }
                  else {  //TODO: other type
                    typeName = SqlTypeName.VARCHAR;
                  }

                  fieldInfo.add(colName, typeFactory.createSqlType(typeName)).nullable(true);
              }
              protoRowType = RelDataTypeImpl.proto(fieldInfo.build());
          }
          return protoRowType.apply(relDataTypeFactory);

      }
      catch (Exception e){
        e.printStackTrace();
      }
      return null;

    /*final RelDataType mapType = relDataTypeFactory.createMapType(
        relDataTypeFactory.createSqlType(SqlTypeName.VARCHAR),
        relDataTypeFactory.createTypeWithNullability(
            relDataTypeFactory.createSqlType(SqlTypeName.ANY),
            true));
    return relDataTypeFactory.builder().add("_MAP", mapType).build();*/
  }

  public <T> Queryable<T> asQueryable(QueryProvider queryProvider, SchemaPlus schema,
                                      String tableName) {
    return new ElasticsearchQueryable<>(queryProvider, schema, this, tableName);
  }

  public RelNode toRel(RelOptTable.ToRelContext context, RelOptTable relOptTable) {
    final RelOptCluster cluster = context.getCluster();
    return new ElasticsearchTableScan(cluster, cluster.traitSetOf(ElasticsearchRel.CONVENTION),
        relOptTable, this, null);
  }

  /** Executes a "find" operation on the underlying type.
   *
   * <p>For example,
   * <code>client.prepareSearch(index).setTypes(type)
   * .setSource("{\"fields\" : [\"state\"]}")</code></p>
   *
   * @param index Elasticsearch index
   * @param ops List of operations represented as Json strings.
   * @param fields List of fields to project; or null to return map
   * @return Enumerator of results
   */


  private Enumerable<Object> find(String index, List<String> ops, List<Map.Entry<String, Class>> fields,
                                  Integer fetch) {
    final String dbName = index;

    final String queryString = "{" + Util.toString(ops, "", ", ", "") + "}";

    return new AbstractEnumerable<Object>() {

        @Override
        protected Enumerable<Object> getThis() {
            return super.getThis();
        }


        public Enumerator<Object> enumerator() {
          try{
              String content = queryString /*"{\"_source\" : true , \"stored_fields\" : [\"title\"], \"script_fields\": {}}"{\"query\":{\"match_all\":{}}}"*/;

              //System.out.println(content);

              SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

              SearchModule searchModule = new SearchModule(Settings.EMPTY, false, Collections.emptyList());

              XContentParser parser = XContentFactory.xContent(XContentType.JSON)
                      .createParser(new NamedXContentRegistry(searchModule.getNamedXContents()), content);

              searchSourceBuilder.parseXContent(new QueryParseContext(parser));

              SearchResponse searchResponse = client.prepareSearch(dbName).setTypes(typeName)
                      .setScroll(new TimeValue(60000))
                      .setSize(10000)
                      .setSource(searchSourceBuilder).get();

              if (searchResponse.getAggregations() != null) {
                  Aggregations aggregations = searchResponse.getAggregations();  //TODO: reset agg
                  return new ElasticsearchAggregationEnumerator(aggregations, fields, fetch);
              } else {
                  final Function1<SearchHit, Object> getter = ElasticsearchEnumerator.getter(fields);
                  final Iterator<SearchHit> cursor = searchResponse.getHits().iterator();
                  return new ElasticsearchEnumerator(cursor, getter, searchResponse.getScrollId(), client, fetch);
              }
          }
          catch(Exception e) {
              e.printStackTrace();
          }
          return null;
        }

    };
  }


  /**
   * Implementation of {@link Queryable} based on
   * a {@link ElasticsearchTable}.
   */
  public static class ElasticsearchQueryable<T> extends AbstractTableQueryable<T> {
    public ElasticsearchQueryable(QueryProvider queryProvider, SchemaPlus schema,
                                  ElasticsearchTable table, String tableName) {
      super(queryProvider, schema, table, tableName);
    }

    public Enumerator<T> enumerator() {
      return null;
    }

    private String getIndex() {
      return schema.unwrap(ElasticsearchSchema.class).index;
    }

    private ElasticsearchTable getTable() {
      return (ElasticsearchTable) table;
    }

    /** Called via code-generation.
     *
     * @see
     */
    public Enumerable<Object> find(List<String> ops,
        List<Map.Entry<String, Class>> fields, Integer fetch) {
      return getTable().find(getIndex(), ops, fields, fetch);
    }

    /*public Enumerable<Object> xfind(List<QueryBuilder> ops,
                                 List<Map.Entry<String, Class>> fields) {
      return getTable().xfind(getIndex(), ops, fields);
    }*/

  }
}

// End ElasticsearchTable.java
