package edp.elasticsearch;

import edp.moonbox.calcite.plan.RelOptCluster;
import edp.moonbox.calcite.plan.RelTraitSet;
import edp.moonbox.calcite.rel.InvalidRelException;
import edp.moonbox.calcite.rel.RelNode;
import edp.moonbox.calcite.rel.core.Aggregate;
import edp.moonbox.calcite.rel.core.AggregateCall;
import edp.moonbox.calcite.sql.SqlAggFunction;
import edp.moonbox.calcite.sql.fun.SqlStdOperatorTable;
import edp.moonbox.calcite.sql.fun.SqlSumAggFunction;
import edp.moonbox.calcite.sql.fun.SqlSumEmptyIsZeroAggFunction;
import edp.moonbox.calcite.util.ImmutableBitSet;
import edp.moonbox.calcite.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 10/18/16.
 */
public class ElasticsearchAggregate extends Aggregate implements ElasticsearchRel {

	public ElasticsearchAggregate(RelOptCluster cluster,
								  RelTraitSet traitSet,
								  RelNode child,
								  boolean indicator,
								  ImmutableBitSet groupSet,
								  List<ImmutableBitSet> groupSets,
								  List<AggregateCall> aggCalls) throws InvalidRelException {
		super(cluster, traitSet, child, indicator, groupSet, groupSets, aggCalls);
		assert getConvention() == ElasticsearchRel.CONVENTION;
		assert getConvention() == child.getConvention();

		for (AggregateCall aggCall : aggCalls) {
			if (aggCall.isDistinct()) {
				throw new InvalidRelException("distinct aggregation not supported");
			}
		}
		switch (getGroupType()) {
			case SIMPLE:
				break;
			default:
				throw new InvalidRelException("unsupported group type: " + getGroupType());
		}
	}

	@Override
	public void implement(Implementor implementor) {
		implementor.visitChild(0, getInput());
		List<String> list = new ArrayList<>();

		final List<String> inNames = ElasticsearchRules.elasticsearchFieldNames(getInput().getRowType());
		final List<String> outNames = ElasticsearchRules.elasticsearchFieldNames(getRowType());
		List<String> aggsSelectedList = new ArrayList<>();
		for (AggregateCall aggCall : aggCalls) {
			aggsSelectedList.add(toElasticsearch(aggCall, inNames, outNames, aggCall.getArgList()));
		}

		List<String> groupByList = new ArrayList<>();
		for (int group : groupSet) {
			final String groupName = inNames.get(group);

			String groupElement = ElasticsearchRules.quote(groupName) + ";" + "\"terms\":{\"field\":" + ElasticsearchRules.quote(groupName) + "," + "\"size\":10000000}";
			//TODO: set default agg size 100,000
			groupByList.add(
					//ElasticsearchRules.quote(groupName) + "," + "\"terms\":{\"field\":" + ElasticsearchRules.quote(groupName) + "," + "\"size\": 100000" + "}"
					groupElement
			);
		}
		String aggQuery = aggregateString(groupByList, aggsSelectedList, 0);
		implementor.add("\"size\" : 0");
		implementor.add(aggQuery);
	}

	private String aggregateString(List<String> groupByList, List<String> aggsSelectedList, int i) {
		if (i < groupByList.size()) {
			String[] str = groupByList.get(i).split(";");  //
			return "\"aggs\" : {" +
					str[0] + ": {" +
					str[1] + "," +
					aggregateString(groupByList, aggsSelectedList, ++i) +
					"}" +
					"}";
		} else {
			return Util.toString(aggsSelectedList, "\"aggs\": {", ",", "}");
		}
	}

	@Override
	public Aggregate copy(RelTraitSet traitSet, RelNode input, boolean indicator, ImmutableBitSet groupSet, List<ImmutableBitSet> groupSets, List<AggregateCall> aggCalls) {
		try {
			return new ElasticsearchAggregate(getCluster(), traitSet, input, indicator, groupSet, groupSets, aggCalls);
		} catch (InvalidRelException e) {
			throw new AssertionError(e);
		}
	}

	private String toElasticsearch(AggregateCall aggCall, List<String> inNames, List<String> outNames,
								   List<Integer> args) {
		SqlAggFunction aggregation = aggCall.getAggregation();
		if (aggregation == SqlStdOperatorTable.COUNT) { // COUNT
			if (args.size() == 0) { // count(*)
				int cardinality = this.groupSet.cardinality();
				if(cardinality == 0) { // group by absent
					// TODO
					final String inName = "_index";
					final String asName = aggCall.getName();
					return ElasticsearchRules.quote(asName) + ": {\"value_count\": {\"field\": " + ElasticsearchRules.quote(inName) + "}}";
				} else { // group by present
					final String inName = "_index";
					final String asName = aggCall.getName();
					return ElasticsearchRules.quote(asName) + ": {\"value_count\": {\"field\": " + ElasticsearchRules.quote(inName) + "}}";
				}
			} else { // count(field)
				assert args.size() == 1;
				final String inName = inNames.get(args.get(0));
				final String asName = aggCall.getName();
				return ElasticsearchRules.quote(asName) + ": {\"value_count\": {\"field\": " + ElasticsearchRules.quote(inName) + "}}";
			}

		} else if (aggregation instanceof SqlSumAggFunction || aggregation instanceof SqlSumEmptyIsZeroAggFunction) { // SUM
			assert args.size() == 1;
			final String inName = inNames.get(args.get(0));
			final String asName = aggCall.getName();
			return ElasticsearchRules.quote(asName) + ": {\"sum\": {\"field\": " + ElasticsearchRules.quote(inName) + "}}";
		} else if (aggregation == SqlStdOperatorTable.MAX) { // MAX
			assert args.size() == 1;
			final String inName = inNames.get(args.get(0));
			final String asName = aggCall.getName();
			return ElasticsearchRules.quote(asName) + ": {\"max\": {\"field\": " + ElasticsearchRules.quote(inName) + "}}";
		} else if (aggregation == SqlStdOperatorTable.MIN) { // MIN
			assert args.size() == 1;
			final String inName = inNames.get(args.get(0));
			final String asName = aggCall.getName();
			return ElasticsearchRules.quote(asName) + ": {\"min\": {\"field\": " + ElasticsearchRules.quote(inName) + "}}";
		} else if (aggregation == SqlStdOperatorTable.AVG) { // AVG
			assert args.size() == 1;
			final String inName = inNames.get(args.get(0));
			final String asName = aggCall.getName();
			return ElasticsearchRules.quote(asName) + ": {\"avg\": {\"field\": " + ElasticsearchRules.quote(inName) + "}}";
		} else {
			throw new AssertionError("unknown aggregate " + aggregation);
		}
	}
}
