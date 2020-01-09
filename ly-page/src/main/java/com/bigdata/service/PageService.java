package com.bigdata.service;

import com.bigdata.client.GoodsClient;
import com.bigdata.client.SpecClient;
import com.leyou.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 规格参数
 */
@Service
public class PageService {
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    SpecClient specClient;

//    public Map<String,Object> toPage(Long spuId) {
//        Map<String, Object> map = new HashMap<String,Object>();
//        //查询spu
//        Spu spu = goodsClient.querySpuById(spuId);
//        map.put("spu",spu);
//        //查询spudetail
//        map.put("spuDetail",goodsClient.querySpuDetailBySpuId(spuId));
//        //查询skus
//        map.put("skus",goodsClient.querySkuBySpuId(spuId));
//        //查询规格参数
//        List<SpecParam> specParamList = specClient.querySpecParam(null, spu.getCid3(), null, null);
//        Map<Long, Object> specMap = new HashMap<Long,Object>();
//        for (SpecParam s:specParamList) {
//            specMap.put(s.getId(),s.getName());
//        }
//        map.put("specParams",specMap);
//
//        //规格组
//        List<SpecGroup> specGroupList = specClient.querySpecGronp(spu.getCid3());
//        map.put("specGroups",specGroupList);
//        return map;
        public Map<String,Object> toPage(Long spuId) {

            Map<String,Object> results = new HashMap<>();

            //根据spu的id查询spu
            Spu spu = this.goodsClient.querySpuById(spuId);

            results.put("spu",spu);

            //根据spuId查询spuDetail
            SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuId);
            results.put("spuDetail",spuDetail);

            //根据spuId查询skus
            List<Sku> skus = this.goodsClient.querySkuBySpuId(spuId);

            results.put("skus",skus);

            // 根据分类查询特有的规格参数，并且处理成map 解构key为规格参数的id，值为规格参数的名字
            List<SpecParam> specailSpecParams = this.specClient.querySpecParam(null, spu.getCid3(), null, false);

            //存放对照表
            Map<Long,String> specParams = new HashMap<>();

            specailSpecParams.forEach(specailSpecParam->{
                specParams.put(specailSpecParam.getId(),specailSpecParam.getName());
            });

            results.put("specParams",specParams);


            //需要根据分类查询规格组，同时查询组内的参数
            List<SpecGroup> specGroups = this.specClient.querySpecGroups(spu.getCid3());

            results.put("specGroups",specGroups);
            return results;
//        }
    }
}
