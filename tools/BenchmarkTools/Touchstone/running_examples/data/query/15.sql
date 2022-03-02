select
    l_suppkey,
    sum(l_extendedprice * (1 - l_discount))
from
    lineitem
where
        l_shipdate >= '1994-07-30'
  and l_shipdate < '1994-11-02'
group by
    l_suppkey;

