-- Query 4.2

select
   /*+  NO_INDEX(lineorder)
        NO_INDEX(date)
    	   NO_INDEX(part)
	   NO_INDEX(customer) */
	d_year, s_nation, p_category, sum(lo_revenue - lo_supplycost) as profit
from
	supplier straight_join lineorder 
	straight_join date
	straight_join part 
	straight_join customer
where
    lo_custkey = c_custkey and
    lo_suppkey = s_suppkey and
    lo_partkey = p_partkey and
    lo_orderdate = d_datekey and
    c_region = '#34,0,0#' and
    s_region = '#10,0,0#' and
    (d_year = #32,0,0# or d_year = #33,0,0#) and
    (p_mfgr = '#35,0,0#' or p_mfgr = '#36,0,0#')
group by d_year, s_nation, p_category
order by d_year, s_nation, p_category;
