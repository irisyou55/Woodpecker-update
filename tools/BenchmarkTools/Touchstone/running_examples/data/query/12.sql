select
             /*+ NO_INDEX(orders)
           NO_INDEX(lineitem)*/
    l_shipmode,
    sum(case
            when o_orderpriority = '1-URGENT'
                or o_orderpriority = '2-HIGH'
                then 1
            else 0
        end) as high_line_count,
    sum(case
            when o_orderpriority <> '1-URGENT'
                and o_orderpriority <> '2-HIGH'
                then 1
            else 0
        end) as low_line_count
from
    orders straight_join
    lineitem
where
        o_orderkey = l_orderkey
  and l_shipmode in ('CmepG', 'Pxx')
  and l_commitdate < date '1993-11-21'
  and l_shipdate < date '1993-11-08'
  and l_receiptdate >= date '1995-07-05'
  and l_receiptdate < date '1997-05-20'
group by
    l_shipmode
order by
    l_shipmode;
