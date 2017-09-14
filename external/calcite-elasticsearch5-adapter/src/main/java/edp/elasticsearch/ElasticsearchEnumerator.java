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

import edp.moonbox.calcite.avatica.util.DateTimeUtils;
import edp.moonbox.calcite.linq4j.Enumerator;
import edp.moonbox.calcite.linq4j.function.Function1;
import edp.moonbox.calcite.linq4j.tree.Primitive;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Enumerator that reads from an Elasticsearch type.
 */
public class ElasticsearchEnumerator implements Enumerator<Object> {
  private Iterator<SearchHit> cursor;
  private final Function1<SearchHit, Object> getter;
  private Object current;
  private String scrollId;
  private Client client;
  private Integer fetchLines;
  private Integer passedLines;

  /**
   * Creates an ElasticsearchEnumerator.
   *
   * @param cursor Iterator over Elasticsearch {@link SearchHit} objects
   * @param getter Converts an object into a list of fields
   */
  public ElasticsearchEnumerator(Iterator<SearchHit> cursor, Function1<SearchHit, Object> getter, String scrollId, Client client, Integer fetchLines) {
    this.cursor = cursor;
    this.getter = getter;
    this.scrollId = scrollId;
    this.client = client;
    this.fetchLines = fetchLines;
    this.passedLines = 0;
  }

  public SearchResponse scrollQuery(String scrollId) {
    SearchResponse scrollRsp = null;
    try {
      scrollRsp = client.prepareSearchScroll(scrollId).setScroll(new TimeValue(60000)).execute().actionGet();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return scrollRsp;
  }


  public Object current() {
    return current;
  }

  public boolean moveNext() {
    //for limit parse
    if(fetchLines!= -1 && passedLines >= fetchLines) {
      //System.out.println("limit size " + passedLines);
      current = null;
      return false;
    }

    if (cursor.hasNext()) {
      passedLines++;
      SearchHit map = cursor.next();
      current = getter.apply(map);
      return true;
    }
    else {

      SearchResponse scrollRsp = scrollQuery(scrollId);
      this.scrollId = scrollRsp.getScrollId();

      if(scrollRsp.getHits().getHits().length == 0) {
        current = null;
        return false;
      }
      else{
        cursor = scrollRsp.getHits().iterator();
        if (cursor.hasNext()) {
          passedLines++;
          SearchHit map = cursor.next();
          current = getter.apply(map);
          return true;
        }
        else{
          current = null;
          return false;
        }
      }
    }
  }

  public void reset() {
    throw new UnsupportedOperationException();
  }

  public void close() {
    // nothing to do
  }

  private static Function1<SearchHit, Map> mapGetter() {
    return new Function1<SearchHit, Map>() {
      public Map apply(SearchHit searchHitFields) {
        return (Map) searchHitFields.fields();
      }
    };
  }

  private static Function1<SearchHit, Object> singletonGetter(final String fieldName,
                                                              final Class fieldClass) {
    return new Function1<SearchHit, Object>() {
      public Object apply(SearchHit searchHitFields) {
        if (searchHitFields.fields().isEmpty()) {
          //return convert(searchHitFields.getSource(), fieldClass);
          return convert(searchHitFields.getSource().get(fieldName), fieldClass);
        } else {
          //return convert(searchHitFields.getFields(), fieldClass);
          return convert(searchHitFields.field(fieldName).getValue(), fieldClass);
        }
      }
    };
  }

  /**
   * Function that extracts a given set of fields from {@link SearchHit}
   * objects.
   *
   * @param fields List of fields to project
   */
  private static Function1<SearchHit, Object[]> listGetter(
      final List<Map.Entry<String, Class>> fields) {
    return new Function1<SearchHit, Object[]>() {
      public Object[] apply(SearchHit searchHitFields) {
        Object[] objects = new Object[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
          final Map.Entry<String, Class> field = fields.get(i);
          final String name = field.getKey();
          if (searchHitFields.fields().isEmpty()) {
            objects[i] = convert(searchHitFields.getSource().get(name), field.getValue());
          } else {
            objects[i] = convert(searchHitFields.field(name).getValue(), field.getValue());
          }
        }
        return objects;
      }
    };
  }

  static Function1<SearchHit, Object> getter(List<Map.Entry<String, Class>> fields) {
    //noinspection unchecked
    return fields == null
      ? (Function1) mapGetter()
      : fields.size() == 1
      ? singletonGetter(fields.get(0).getKey(), fields.get(0).getValue())
      : (Function1) listGetter(fields);
  }

  private static Object convert(Object o, Class clazz) {
    if (o == null) {
      return null;
    }
    Primitive primitive = Primitive.of(clazz);
    if (primitive != null) {
      clazz = primitive.boxClass;
    } else {
      primitive = Primitive.ofBox(clazz);
    }
    if (clazz.isInstance(o)) {
      return o;
    }
    if (o instanceof String && primitive != null) {
      o = primitive.parse((String) o);
      return o;
    }

    if (o instanceof Date && primitive != null) {
      o = ((Date) o).getTime() / DateTimeUtils.MILLIS_PER_DAY;
    }
    if (o instanceof Number && primitive != null) {
      return primitive.number((Number) o);
    }
    return o;
  }
}

// End ElasticsearchEnumerator.java
