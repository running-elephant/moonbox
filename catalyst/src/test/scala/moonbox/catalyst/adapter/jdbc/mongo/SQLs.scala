/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2018 EDP
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

package moonbox.catalyst.adapter.jdbc.mongo

object SQLs {
  // passed: normal sqls
  val sql1 = "select * from books"
  val sql2 = "select * from books where price < 20 and price > 10 or price = 24"
  val sql3 = "select max(price) from books where price > 10"
  val sql4 = "select name, author from books where price > 10 and pages > 100"
  val sql5 = "select name, author from books where price > 10 and pages > 100 or pages < 50"
  val sql6 = "select name, author, max(price) from books where price > 10 and 100 < pages or pages < 50 group by name, author, price having price <30" // group by
  val sql7 = "select min(price) from books where price != 10 group by name" // group by !=
  val sql8 = "select price, pages, Abs(price+pages) from books where price >10 and price <20 or pages =500" // Abs in project +
  val sql9 = "select price, pages, Abs(price-pages) from books where price >10 and price <20 or pages =500" // Abs in project -
  val sql10 = "select price, pages, Abs(price*pages) from books where price >10 and price <20 or pages =500" // Abs in project *
  val sql11 = "select price, pages, Abs(price/pages) from books where price >10 and price <20 or pages =500" // Abs in project /
  val sql12 = "select price, pages, price + 1 from books where price >10 and price <20 or pages =500" // field + 1
  val sql13 = "select name, author, price, pages from books where price > 10 and pages > 100 or pages < 50 order by pages limit 10" // sort, limit
  val sql14 = "select name, author, price, pages from books where price > 10 and pages > 100 or pages < 50 order by pages desc limit 10" // sort(desc), limit
  val sql15 = "select distinct name from books where price > 10 and pages > 100 order by name" //distinct support
  val sql16 = "select count(name) from books"
  val sql17 = "select sum(price) from books"
  val sql18 = "select count(*) from books"
  val sql19 = "select count(1) from books"
  //------------------------------------------
  val sql20 = "select price, pages, price * 2 + 1 from books where price >10 and price <20 or pages =500" // continuous operation
  val sql21 = "select name, author, max(price) from books where price > 10 and 100 < pages or pages < 50 group by name, author, price having max(price) <20" // group by
  val sql22 = "select name, author, price from books where price between 10 and 20 or pages =500" // between and
  val sql23 = "select first(price) from books where price between 10 and 20 or pages =500" // first

  // with problem
  val sql40 = "select distinct price from books where price > 10 and pages > 100 group by name order by price" // two aggregates in the logical plan
  val sql66 = "select name, author from books group by name, author, price having price > 0 order by price "
  //    {$project: {_id: 1}}
  //    {$group: {_id: null, "count(1)": {$sum: 1}}}
  //    {$project: {_id: 0}}
  //    result:
  //    Document{{count(1)=103}}
  // -------------------------------------------------------------
  // temporarily unsupported
  val sql99 = "select * from books left outer join user on books.author = user.name" // LEFT OUTER JOIN
  // =============================================================
  // collections nested: book_nested_normal
  // passed
  val sql100 = "select bookname, bookauthor, bookinfo.info_nested.info_nested1 from book_nested_normal"
  val sql101 = "select bookname, bookauthor, bookinfo.info_nested.info_nested1 from book_nested_normal where bookinfo.info_nested.info_nested1 = 'info_nested_1'"
  // temporarily unsupported
  val sql102 = "select * from book_nested_normal where bookinfo.info_nested.info_nested1 = 'info_nested_1'"
  // where nest3.nest3_nest.nest3_nest_name = 'nest3_nest_name1'
  // =============================================================
  // collections with array: user_withArray
  // passed
  val sql200 = "explain codegen select * from user_withArray where filter(username)"
  // temporarily unsupported
  val sql260 = "select username, age, books.bookname from user_withArray"
  // =============================================================
  // collections with nested and array: user_nested3
  val sql500 = ""

}
