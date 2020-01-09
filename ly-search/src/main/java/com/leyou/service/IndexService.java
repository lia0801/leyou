package com.leyou.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.client.CategoryClient;
import com.leyou.client.GoodsClient;
import com.leyou.client.SpecClient;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.pojo.Goods;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IndexService {
    @Autowired
    GoodsClient goodsClient;

    @Autowired
    CategoryClient categoryClient;

    @Autowired
    SpecClient specClient;

    public Goods buildGoods(SpuBo spuBo) {
        Goods goods = new Goods();
        //spubo拷贝到goods
        //根据属性名相同拷贝
        BeanUtils.copyProperties(spuBo,goods);
        //all 标题 加上分类
        String title = spuBo.getTitle();
        List<String> names = categoryClient.queryNamesByIds(Arrays.asList(spuBo.getCid1(), spuBo.getCid2(), spuBo.getCid3()));
        String join = StringUtils.join(names," ");
        goods.setAll(title+" "+join);
        //price
        //skus
        //查询根据spuid查询sku
        List<Sku> skuList = goodsClient.querySkuBySpuId(spuBo.getId());
        ArrayList<Long> prices = new ArrayList<>();
        List<Map<String, Object>> maps = new ArrayList<>();
        for (Sku sku:skuList){
            prices.add(sku.getPrice());
            //构建map
            HashMap<String, Object> map = new HashMap<>();
            //（skuid，标题，价格，图片）
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            map.put("price",sku.getPrice());
            map.put("image",StringUtils.isBlank(sku.getImages())?"":sku.getImages().split(",")[0]);
            maps.add(map);
        }
        goods.setPrice(prices);
        goods.setSkus(JsonUtils.serialize(maps));
        //规格参数
        //specs
        Map<String, Object> specs = getSpecs(spuBo);
        goods.setSpecs(specs);
        return goods;
    }
    //规格参数
    private Map<String, Object> getSpecs(SpuBo spuBo) {

        //key规格参数的名称，值为对应的规格参数的值（取决于商品本身）
        Map<String, Object> specs = new HashMap<>();//操作系统：android

        //1,获取到所有的可搜索的规格参数
        List<SpecParam> searchingParams = this.specClient.querySpecParam(null, spuBo.getCid3(), true,null);

        //2,循环可搜索的规格参数集合，判断通用还是特有，通用从通用规格中取值，特有从特有规格中取值

        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuBo.getId());

        //由于要取值，为了方便我们把通用规格和特有规格都转换为Map
        //通用规格键值对集合map
        Map<Long, Object> genericMap = JsonUtils.nativeRead(spuDetail.getGenericSpec(), new TypeReference<Map<Long, Object>>() {
        });
        //特有规格键值对集合map
        Map<Long, List<String>> specialMap = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
        });

        //3,通用和特有的值来自于spuDetail
        searchingParams.forEach(specParam -> {
            Long id = specParam.getId();//对应取值，规格参数的id就是通用规格和特有规格保存时map的key，当key一致可以直接取值
            String name = specParam.getName();//具体的key的值

            //通用参数
            Object value = null;
            if (specParam.getGeneric()) {
                //通用参数
                value = genericMap.get(id);

                if (null != value && specParam.getNumeric()) {
                    //数值类型可能需要加分段,以及单位
                    value = this.chooseSegment(value.toString(), specParam);
                }
            } else {//特有参数
                value = specialMap.get(id);

            }
            if (null == value) {
                value = "其他";
            }
            specs.put(name, value);
        });
        return  specs;

    }
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {//segment:1000-2000
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();//添加单位
                }
                break;
            }
        }
        return result;
    }
}
