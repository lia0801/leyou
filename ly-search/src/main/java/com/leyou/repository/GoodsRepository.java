package com.leyou.repository;

import com.leyou.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//dao
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
