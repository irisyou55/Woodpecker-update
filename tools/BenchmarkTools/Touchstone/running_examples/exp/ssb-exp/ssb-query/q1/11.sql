-- Query 4.1

select			
	  /*+  NO_INDEX(lineorder)
        NO_INDEX(date)
    	   NO_INDEX(part)
	   NO_INDEX(customer) */
	d_year, c_nation, sum(lo_revenue - lo_supplycost) as profit
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
    c_region = 'HX5yt' and
    s_region = 'vcXDnqHF' and
    (p_mfgr = 'G2eSuz' or p_mfgr = 'ou4jhM')
group by d_year, c_nation
order by d_year, c_nation;
