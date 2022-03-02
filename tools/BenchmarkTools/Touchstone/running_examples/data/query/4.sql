select
	 /*+ JOIN_ORDER(orders, lineitem@subq1) */    
    o_orderpriority,
    count(*) as order_count
from
    orders
where
        o_orderdate >= date '1992-02-20'
  and o_orderdate < date '1992-05-21'
  and exists (
        select
            *
        from
            lineitem
        where
                l_orderkey = o_orderkey
          and l_commitdate < date '1996-05-03'
    )
group by
    o_orderpriority
order by
    o_orderpriority;
