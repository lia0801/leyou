package com.leyou.item.api;

import com.leyou.common.po.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * 微服务之间的调用
 * goods的使用
 */
public interface GoodsApi {
    //分页
    @GetMapping("spu/page")
    public PageResult<SpuBo> querySpuByPage(@RequestParam(value = "key",required = false) String key,
                                            @RequestParam(value = "saleable",required = false) Boolean saleable,
                                            @RequestParam(value = "page",defaultValue = "1") Integer page,
                                            @RequestParam(value = "rows",defaultValue = "5") Integer rows);
    //添加
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo);

    //回显
    @GetMapping("spu/detail/{spuId}")
    public SpuDetail querySpuDetailBySpuId(@PathVariable("spuId") Long id);
    @GetMapping("sku/list")
    public List<Sku> querySkuBySpuId(@RequestParam("id")Long id);

    //修改
    @PutMapping("goods")
    public  Void updateGoods(@RequestBody SpuBo spuBo);

    @GetMapping("spu/{id}")
    public Spu querySpuById(@PathVariable("id") Long spuId);
}
