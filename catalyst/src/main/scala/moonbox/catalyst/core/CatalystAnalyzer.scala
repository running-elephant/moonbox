package moonbox.catalyst.core

import org.apache.spark.sql.catalyst.analysis.{AliasViewChild, Analyzer, CleanupAliases, EliminateUnions, ResolveCreateNamedStruct, ResolveHints, ResolveInlineTables, ResolveTableValuedFunctions, ResolveTimeZone, SubstituteUnresolvedOrdinals, TimeWindowing, TypeCoercion, UpdateOuterReferences}
import org.apache.spark.sql.catalyst.catalog.SessionCatalog
import org.apache.spark.sql.internal.SQLConf

class CatalystAnalyzer(catalog: SessionCatalog,
                       conf: SQLConf,
                       maxIterations: Int) extends Analyzer(catalog, conf, maxIterations){


    override lazy val batches: Seq[Batch] = Seq(
        Batch("Hints", fixedPoint,
            new ResolveHints.ResolveBroadcastHints(conf),
            ResolveHints.RemoveAllHints),
        Batch("Simple Sanity Check", Once,
            LookupFunctions),
        Batch("Substitution", fixedPoint,
            CTESubstitution,
            WindowsSubstitution,
            EliminateUnions,
            new SubstituteUnresolvedOrdinals(conf)),
        Batch("Resolution", fixedPoint,
            ResolveTableValuedFunctions ::
                    ResolveRelations ::
                    ResolveReferences ::
                    ResolveCreateNamedStruct ::
                    ResolveDeserializer ::
                    ResolveNewInstance ::
                    ResolveUpCast ::
                    ResolveGroupingAnalytics ::
                    ResolvePivot ::
                    ResolveOrdinalInOrderByAndGroupBy ::

                    ResolveAggAliasInGroupBy ::
                    ResolveMissingReferences ::
                    ExtractGenerator ::
                    ResolveGenerate ::
                    ResolveFunctions ::
                    ResolveAliases ::
                    ResolveSubquery ::
                    ResolveWindowOrder ::
                    ResolveWindowFrame ::
                    ResolveNaturalAndUsingJoin ::
                    ExtractWindowExpressions ::
                    GlobalAggregates ::
                    ResolveAggregateFunctions ::
                    TimeWindowing ::
                    ResolveInlineTables(conf) ::
                    ResolveTimeZone(conf) ::
                    TypeCoercion.typeCoercionRules ++
                            extendedResolutionRules : _*),
        Batch("Post-Hoc Resolution", Once, postHocResolutionRules: _*),
        Batch("View", Once,
            AliasViewChild(conf)),
        Batch("Nondeterministic", Once,
            PullOutNondeterministic),
        Batch("UDF", Once,
            HandleNullInputsForUDF),
        Batch("FixNullability", Once,
            FixNullability),
        Batch("Subquery", Once,
            UpdateOuterReferences),
        Batch("Cleanup", fixedPoint,
            CleanupAliases)
    )
}
