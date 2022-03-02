select
    sum(l_extendedprice * l_discount) as revenue
from
    lineitem
where
        l_shipdate >= date '1992-12-10'
  and l_shipdate < date '1994-10-15'
  and l_discount between 0.015272319316864014 and  0.0419670045375824
  and l_quantity < 14.080395758152008;
