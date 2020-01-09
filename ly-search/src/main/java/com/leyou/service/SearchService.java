package com.leyou.service;

import com.leyou.Utiles.SearchRequest;
import com.leyou.common.po.PageResult;
import com.leyou.pojo.Goods;
import com.leyou.repository.GoodsRepository;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    @Autowired
    GoodsRepository goodsRepository;

    public PageResult<Goods> search(SearchRequest searchRequest) {
        //取用户搜索的关键字吧
        String key = searchRequest.getKey();
        //第几页
        Integer page = searchRequest.getPage();
        //创建查询对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //查询条件
        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("all",key).operator(Operator.AND));
        // 通过sourceFilter设置返回的结果字段,我们只需要id、skus、subTitle
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"}, null));
        //分页
        PageRequest of = PageRequest.of(page-1,searchRequest.getSize());
        nativeSearchQueryBuilder.withPageable(of);
        //搜索
        Page<Goods> search = goodsRepository.search(nativeSearchQueryBuilder.build());

        return new PageResult<>(search.getTotalElements(),new Long(search.getTotalPages()),search.getContent());

    }
}
