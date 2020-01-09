package com.bigdate;

import com.leyou.LySearchService;
import com.leyou.client.GoodsClient;
import com.leyou.common.po.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.pojo.Goods;
import com.leyou.repository.GoodsRepository;
import com.leyou.service.IndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySearchService.class)
public class IndexTest {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private IndexService indexService;

    //建索引 表
    @Test
    public void init(){
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);
    }

    @Test
    public void loadData(){
        int page=1;//
        while (true){
            //使用feignClient调用商品微服务,(发出HTTP请求)
            PageResult<SpuBo> spuBoPageResult = goodsClient.querySpuByPage(null, null, page, 50);

            if(spuBoPageResult==null){
                break;
                //为空则为有数据了，停止查询
            }
            page++;
            //获取商品list
            List<SpuBo> boList=spuBoPageResult.getItems();
            //转化成goods
            List<Goods> goods = new ArrayList<>();
            for (SpuBo spuBo:boList){
                //spubbo===goods
                Goods goods1=indexService.buildGoods(spuBo);
                goods.add(goods1);
            }
            //保存到索引库
            goodsRepository.saveAll(goods);
        }
    }
}
