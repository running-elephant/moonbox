package org.apache.calcite.adapter.elasticsearch;

import org.apache.calcite.avatica.util.DateTimeUtils;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.linq4j.function.Function1;
import org.apache.calcite.linq4j.tree.Primitive;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.SingleBucketAggregation;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregation;
import org.elasticsearch.search.aggregations.metrics.geobounds.GeoBounds;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;

import java.util.*;

/**
 * Created by root on 10/18/16.
 * Enumerator that reads from an Elasticsearch aggregation type.
 */
public class ElasticsearchAggregationEnumerator implements Enumerator<Object> {
	private final Iterator<Object[]> cursor;
	private final Aggregations aggregations;
	private final Function1<Object[], Object> getter;
	private final List<Object[]> lines;
	private int currentLineIndex;
	private Object current;
	private List<Map.Entry<String, Class>> fields;
	private Map<String, Class> fieldsNames = new HashMap<>();
	private Map<String, Integer> fieldOrder = new HashMap<>();

	public ElasticsearchAggregationEnumerator(Aggregations aggregations, List<Map.Entry<String, Class>> fields) {
		this.aggregations = aggregations;
		this.fields = fields;

		for(int i=0 ;i < fields.size(); i++) {
			Map.Entry<String, Class> entry = fields.get(i);
			fieldsNames.put(entry.getKey(), entry.getValue());
			fieldOrder.put(entry.getKey(), i);
		}
		this.getter = ElasticsearchAggregationEnumerator.getter(fields);
		this.currentLineIndex = 0;
		this.lines = new ArrayList<Object[]>();
		this.lines.add(new Object[fields.size()]);
		try {
			handleAggregations(this.aggregations, this.lines);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.cursor = this.lines.iterator();
	}

	private void handleAggregations(Aggregations aggregations, List<Object[]> lines) throws Exception {
		if (allNumericAggregations(aggregations)) {
			createLineForNumericAggregations(aggregations, lines.get(this.currentLineIndex));
			return;
		}

		List<Aggregation> aggregationsList = aggregations.asList();
		if (aggregationsList.size() > 1) {
			throw new Exception("currently support only one aggregation at same level");
		}

		Aggregation aggregation = aggregationsList.get(0);
		if (aggregation instanceof SingleBucketAggregation) {
			Aggregations singleBucktAggs = ((SingleBucketAggregation) aggregation).getAggregations();
			handleAggregations(singleBucktAggs, lines);
			return;
		}
		if (aggregation instanceof NumericMetricsAggregation) {
			return;
		}
		if (aggregation instanceof GeoBounds) {
			return;
		}
		if (aggregation instanceof TopHits) {
			return;
		}
		if (aggregation instanceof MultiBucketsAggregation) {
			MultiBucketsAggregation bucketsAggregation = (MultiBucketsAggregation) aggregation;
			Collection<? extends MultiBucketsAggregation.Bucket> buckets = bucketsAggregation.getBuckets();
			Object[] currentLine = lines.get(currentLineIndex);
			Object[] clonedLine = currentLine.clone();

			String fieldName = bucketsAggregation.getName();
			boolean selected = fieldsNames.containsKey(fieldName);
			boolean firstLine = true;
			for(MultiBucketsAggregation.Bucket bucket : buckets) {
				String key = bucket.getKeyAsString();
				if (firstLine) {
					firstLine = false;
				} else {
					currentLineIndex++;
					currentLine = clonedLine;
					lines.add(currentLine);
				}

				if(selected) {
					currentLine[fieldOrder.get(fieldName)] = key;
				}
				handleAggregations(bucket.getAggregations(), lines);
			}
		}
	}

	private boolean allNumericAggregations(Aggregations aggregations) {
		List<Aggregation> aggregationList = aggregations.asList();
		for (Aggregation aggregation : aggregationList) {
			if (!(aggregation instanceof NumericMetricsAggregation)) {
				return false;
			}
		}
		return true;
	}

	private void handleNumnericMetricAggregation(Aggregation aggregation, Object[] line) {

		if (aggregation instanceof NumericMetricsAggregation.SingleValue) {
			NumericMetricsAggregation.SingleValue singleValue = (NumericMetricsAggregation.SingleValue) aggregation;
			line[fieldOrder.get(singleValue.getName())] = singleValue.value();
		} else if (aggregation instanceof NumericMetricsAggregation.MultiValue) {
			// TODO
		}
	}

	private void createLineForNumericAggregations(Aggregations aggregations, Object[] numeric) {
		List<Aggregation> aggregationList = aggregations.asList();
		for (Aggregation aggregation : aggregationList) {
			handleNumnericMetricAggregation(aggregation, numeric);
		}
	}

	@Override
	public Object current() {
		return current;
	}

	@Override
	public boolean moveNext() {
		if (cursor.hasNext()) {
			Object[] next = cursor.next();
			current = getter.apply(next);
			return true;
		}
		current = null;
		return false;
	}

	@Override
	public void reset() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void close() {

	}

	static Function1<Object[], Object> getter(List<Map.Entry<String, Class>> fields) {
		return fields.size() == 1 ?
				(Function1) singletonGetter(fields.get(0).getKey(), fields.get(0).getValue()) :
				(Function1) listGetter(fields);
	}

	private static Function1<Object[], Object> singletonGetter(String fieldName, Class fieldClass) {
		return new Function1<Object[], Object>() {
			@Override
			public Object apply(Object[] objects) {
				return convert(objects[0], fieldClass);
			}
		};
	}

	private static Function1<Object[], Object[]> listGetter(final List<Map.Entry<String, Class>> fields) {
		return new Function1<Object[], Object[]>() {
			@Override
			public Object[] apply(Object[] objects) {
				Object[] lineObjects = new Object[fields.size()];
				for (int i =0; i < fields.size(); i++) {
					Map.Entry<String, Class> entry = fields.get(i);
					lineObjects[i] = convert(objects[i], entry.getValue());
				}
				return lineObjects;
			}
		};
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
