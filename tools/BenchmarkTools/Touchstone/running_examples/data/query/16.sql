select
    p_brand,
    p_type,
    p_size,
    count(distinct ps_suppkey) as supplier_cnt
from
    part straight_join
    partsupp
where
        p_partkey = ps_partkey
  and p_brand <> 'wNpBCKHZDr'
  and p_type not like '%04kHLnt9NIWAXGhJUSeNh5%'
  and p_size in (3, 35, 12, 8, 1, 41, 46, 39)
  and ps_suppkey not in (
    select
        s_suppkey
    from
        supplier
    where
            s_comment like '%jhTx5HZPi675DCbaFxJzCBDOlc9yjc%'
)
group by
    p_brand,
    p_type,
    p_size
order by
    supplier_cnt desc,
    p_brand,
    p_type,
    p_size;
