-- Query 3.4

select
   /*+ NO_INDEX(date)
    	  NO_INDEX(lineorder)
	    NO_INDEX(customer)*/
	c_city, s_city, d_year, sum(lo_revenue) as revenue
from
	supplier straight_join lineorder
         straight_join date
         straight_join customer
where
    lo_custkey = c_custkey and
    lo_suppkey = s_suppkey and
    lo_orderdate = d_datekey and
    (c_city='VG1z7Mqb2c' or c_city='Hvd4FpxaTx') and
    (s_city='ZgZwGYleFw' or s_city='RNpSINZjRT') and
    d_yearmonth = 'X4Bt6gz'
group by c_city, s_city, d_year
order by d_year asc, revenue desc;
