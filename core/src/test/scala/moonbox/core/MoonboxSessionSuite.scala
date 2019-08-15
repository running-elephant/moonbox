/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

package moonbox.core

import moonbox.common.MbConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.analysis.{EliminateUnions, UnresolvedRelation}
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.rules.RuleExecutor
import org.scalatest.FunSuite

class MoonboxSessionSuite extends FunSuite {

	test("mbSession") {
		val sql =
			"""
			  |WITH tmp_invest_detail as
			  |  (SELECT fm.finace_id AS trans_id, cast(cast(fm.product_id AS bigint) AS varchar(64)) AS source_financing_code, '200408' AS trans_type, cast(fm.invest_amount*1.0 AS decimal(10,2)) AS trans_money, substr(pbm.create_date,1,19) AS trans_time, pbm.create_date AS partition_date, fm.user_id
			  |   FROM yrd_bs.finaces_manager fm
			  |   INNER JOIN yrd_bs.pay_bill_main pbm ON pbm.pay_id = fm.pay_id
			  |   WHERE fm.d_status = '1'
			  |     AND fm.finace_status IN ('3','4','5','9')
			  |     AND pbm.pay_bill_valid='1'
			  |     AND pbm.pay_bill_status='3'
			  |     AND substr(pbm.create_date,1,10) BETWEEN date_sub('2019-07-14',1) AND '2019-07-14'
			  |     AND cast(fm.user_id AS bigint) NOT IN (72935812,70056641,58162912,70056108)
			  |     UNION ALL
			  |     SELECT cast(l.loan_id AS string) AS trans_id, (CASE
			  |                                                        WHEN l.loan_type = 2 THEN ttla.transfer_code
			  |                                                        ELSE iaiar.apply_no
			  |                                                    END) AS source_financing_code, '200408' AS trans_type, cast(l.original_pri_value*1.0 AS decimal(10,2)) AS trans_money, (CASE
			  |                                                                                                                                                                                WHEN l.loan_type = 2 THEN substr(ttla.succ_date,1,19)
			  |                                                                                                                                                                                ELSE substr(l.create_date,1,19)
			  |                                                                                                                                                                            END) AS trans_time, (CASE
			  |WHEN l.loan_type = 2 THEN ttla.succ_date
			  |                                                                                                                                                                                                     ELSE substr(l.create_date,1,19)
			  |                                                                                                                                                                                                 END) AS partition_date, l.creditor_user_id AS user_id
			  |     FROM yrd_bs.loan l
			  |     INNER JOIN yrd_bs.ifcert_apply_info_annc_record iaiar ON l.apply_id = cast(iaiar.apply_id AS bigint)
			  |     INNER JOIN
			  |       (SELECT acc_id
			  |        FROM yrd_bs.user_account_new
			  |        WHERE acc_type_id = '002') uan ON l.creditor_account_relation_id = uan.acc_id
			  |     LEFT JOIN yrd_bs.tla_transfer_loan_apply ttla ON l.loan_id = ttla.buyer_loan_id
			  |     AND l.loan_type = 2
			  |     AND ttla.status = '1'
			  |     AND substr(ttla.succ_date,1,10) BETWEEN date_sub('2019-07-14',50) AND date_add('2019-07-14',50) WHERE l.loan_type IN(1,2)
			  |     AND substr(l.create_date,1,10) BETWEEN date_sub('2019-07-14',50) AND date_add('2019-07-14',50)
			  |     AND substr(CASE
			  |                    WHEN l.loan_type = 2 THEN ttla.succ_date
			  |                    ELSE l.create_date
			  |                END,1,10) BETWEEN date_sub('2019-07-14',1) AND '2019-07-14'
			  |     UNION ALL
			  |     SELECT fep.process_id AS trans_id, cast(fm.product_id AS bigint) AS source_financing_code, (CASE
			  |                                                                                                     WHEN o.orders = 1 THEN '200506'
			  |                                                                                                     WHEN o.orders = 2 THEN '200507'
			  |                                                                                                     ELSE NULL
			  |                                                                                                 END) AS trans_type, (CASE
			  |                                                                                                                          WHEN o.orders = 1 THEN cast(fm.invest_amount AS decimal(20,2))
			  |                                                                                                                          WHEN o.orders = 2 THEN cast(coalesce(fep.earnings,0)-coalesce(fep.activity_deductions,0) AS decimal(20,2))
			  |                                                                                                                          ELSE NULL
			  |                                                                                                                      END) AS trans_money, substr(fm.exit_time,1,19) AS trans_time, substr(fm.exit_time,1,19) AS partition_date, fm.user_id
			  |     FROM
			  |       (SELECT process_id,finace_id,earnings,activity_deductions,1 AS mark
			  |        FROM yrd_bs.finance_exit_process
			  |        WHERE audit_status='1') fep
			  |     INNER JOIN yrd_bs.finaces_manager fm ON fep.finace_id = fm.finace_id
			  |     INNER JOIN yrd_bs.ifcert_user_info_annc_record iuiar ON fm.user_id = cast(iuiar.user_id AS bigint)
			  |     INNER JOIN
			  |       (SELECT 1 AS mark,1 AS orders
			  |        UNION ALL SELECT 1 AS mark,2 AS orders) o ON fep.mark = o.mark WHERE substr(fm.exit_time,1,10) BETWEEN date_sub('2019-07-14',1) AND '2019-07-14'
			  |     AND fm.finace_status = '9'
			  |     UNION ALL
			  |     SELECT cast(ttla.transfer_id AS varchar(64)) AS trans_id, CASE
			  |                                                                   WHEN l.loan_type=1 THEN iaiar.apply_no
			  |                                                                   WHEN l.loan_type=2 THEN ttla.transfer_code
			  |                                                               END AS source_financing_code, '200506' AS trans_type, cast(ttla.principal AS decimal(20,2)) AS trans_money, substr(ttla.succ_date,1,19) AS trans_time, substr(ttla.succ_date,1,19) AS partition_date, l.creditor_user_id AS user_id
			  |     FROM yrd_bs.loan l
			  |     INNER JOIN yrd_bs.ifcert_apply_info_annc_record iaiar ON l.apply_id = cast(iaiar.apply_id AS bigint)
			  |     INNER JOIN yrd_bs.tla_transfer_loan_apply ttla ON l.loan_id = ttla.seller_loan_id
			  |     INNER JOIN
			  |       (SELECT acc_id
			  |        FROM yrd_bs.user_account_new
			  |        WHERE acc_type_id = '002') uan ON uan.acc_id = l.creditor_account_relation_id WHERE l.loan_type IN (1,2)
			  |     AND ttla.status = '1'
			  |     AND substr(ttla.succ_date,1,10) BETWEEN date_sub('2019-07-14',1) AND '2019-07-14'
			  |     UNION ALL
			  |     SELECT cast(arda.repayment_allot_id AS varchar(64)) AS trans_id, CASE
			  |                                                                          WHEN ttla.transfer_code IS NOT NULL THEN ttla.transfer_code
			  |                                                                          ELSE iaiar.apply_no
			  |                                                                      END AS source_financing_code, (CASE
			  |                                                                                                         WHEN o.orders = 1 THEN '200506'
			  |                                                                                                         WHEN o.orders = 2 THEN '200507'
			  |                                                                                                         ELSE NULL
			  |                                                                                                     END) AS trans_type, (CASE
			  |                                                                                                                              WHEN o.orders = 1 THEN cast(arda.repayment_principal AS decimal(10,2))
			  |                                                                                                                              WHEN o.orders = 2 THEN cast(arda.repayment_interest AS decimal(10,2))
			  |                                                                                                                              ELSE NULL
			  |                                                                                                                          END) AS trans_money, substr(arda.update_date,1,19) AS trans_time, substr(arda.update_date,1,19) AS partition_date, arda.lender_user_id AS user_id
			  |     FROM
			  |       (SELECT repayment_allot_id,repayment_principal,repayment_interest,loan_id,repayment_detail_id,account_id,update_date,lender_user_id,1 AS mark
			  |        FROM yrd_bs.actual_repayment_detail_allot
			  |        WHERE substr(update_date,1,10) BETWEEN date_sub('2019-07-14',1) AND '2019-07-14') arda
			  |     INNER JOIN yrd_bs.actual_repayment_detail ard ON arda.repayment_detail_id = ard.repayment_detail_id
			  |     INNER JOIN yrd_bs.ifcert_apply_info_annc_record iaiar ON ard.apply_id = cast(iaiar.apply_id AS bigint)
			  |     INNER JOIN
			  |       (SELECT 1 AS mark,1 AS orders
			  |        UNION ALL SELECT 1 AS mark,2 AS orders) o ON arda.mark = o.mark
			  |     INNER JOIN
			  |       (SELECT acc_id
			  |        FROM yrd_bs.user_account_new
			  |        WHERE acc_type_id = '002') uan ON cast(uan.acc_id AS bigint) = arda.account_id
			  |     LEFT JOIN yrd_bs.tla_transfer_loan_apply ttla ON arda.loan_id = ttla.buyer_loan_id
			  |     AND ttla.status = '1' ),
			  |                       tmp_trans_type as
			  |  (SELECT '200408' trans_type, '2' trans_type_new
			  |   UNION ALL SELECT '200506' trans_type, '8' trans_type_new
			  |   UNION ALL SELECT '200507' trans_type, '9' trans_type_new)
			  |INSERT INTO TABLE yrd_bs.fact_bjjrj_investment_detail
			  |SELECT concat('CERT20181101005', '_', regexp_replace(substr(d.trans_time,1,10), '-', ''), '1_', '061', from_unixtime(unix_timestamp(),'yyyyMMddHHmmss'), lpad(ceil(row_number() over(partition BY substr(d.trans_time, 1, 10)
			  |                                                                                                                                                                                     ORDER BY d.trans_id) / 3000),5,'0')) AS bi_bn,
			  |       cast('1563091858' AS string) AS bi_ts,
			  |       regexp_replace(reflect('java.util.UUID', 'randomUUID'), '-', '') AS bi_id,
			  |       d.trans_id,
			  |       d.source_financing_code,
			  |       t.trans_type_new trans_type,
			  |       d.trans_money,
			  |       li.sid_card AS user_idcard,
			  |       d.trans_time,
			  |       d.partition_date
			  |FROM
			  |  (SELECT DISTINCT trans_id,
			  |                   source_financing_code,
			  |                   trans_type,
			  |                   trans_money,
			  |                   trans_time,
			  |                   substr(partition_date,1,10) partition_date,
			  |                   user_id
			  |   FROM tmp_invest_detail) d
			  |INNER JOIN tmp_trans_type t ON d.trans_type = t.trans_type
			  |INNER JOIN yrd_bs.ifcert_user_info_annc_record iuiar ON d.user_id = cast(iuiar.user_id AS bigint)
			  |INNER JOIN
			  |  (SELECT iuser_id,
			  |          sid_card
			  |   FROM yrd_bs.lender_info
			  |   WHERE sid_card<>' '
			  |     AND sid_card IS NOT NULL) li ON d.user_id = li.iuser_id
			  |LEFT JOIN
			  |  (SELECT trans_id,
			  |          trans_type,
			  |          user_idcard
			  |   FROM yrd_bs.aaaaaaaaaaaaaa) id ON concat(d.trans_id,t.trans_type_new,li.sid_card) = concat(id.trans_id,id.trans_type,id.user_idcard)
			  |WHERE id.user_idcard IS NOT NULL
			""".stripMargin


		// val spark: SparkSession = SparkSession.builder().appName("test").master("local[*]").getOrCreate()

		/*spark.sql("create table parquet(id int, day string) using parquet options(path '/tmp/ccc') PARTITIONED BY (day)")

		spark.sql("insert into parquet partition(day='2019-08-13-12-00-00') select 1 as id")
		spark.sql("insert into parquet partition(day='2017-08-13-12-00-00') select 2 as id")
		spark.sql("insert into parquet partition(day='2018-08-13-12-00-00') select 3 as id")

		spark.sql("select id from parquet where day='2017'").show()*/
	}
}