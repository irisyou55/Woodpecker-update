select
	nation,
	o_year,
	sum(amount) as sum_profit
from
	(
		select  /*+ NO_INDEX(lineitem)
		            NO_INDEX(partsupp)
                    NO_INDEX(orders) */
			n_name as nation,
			extract(year from o_orderdate) as o_year,
			l_extendedprice * (1 - l_discount) - ps_supplycost * l_quantity as amount
		from
			nation straight_join
                  supplier straight_join
                  partsupp straight_join
                  part straight_join
                  lineitem straight_join
                  orders
		where
			s_suppkey = l_suppkey
			and ps_suppkey = l_suppkey
			and ps_partkey = l_partkey
			and p_partkey = l_partkey
			and o_orderkey = l_orderkey
			and s_nationkey = n_nationkey
			and p_name like '%medium%'
	) as profit
group by
	nation,
	o_year
order by
	nation,
	o_year desc;
