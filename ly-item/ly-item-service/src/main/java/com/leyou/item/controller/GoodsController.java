package com.leyou.item.controller;

import com.leyou.common.po.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GoodsController {
    @Autowired
    GoodsService goodsService;

    //分页
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(@RequestParam(value = "key",required = false) String key,
                                                            @RequestParam(value = "saleable",required = false) Boolean saleable,
                                                            @RequestParam(value = "page",defaultValue = "1") Integer page,
                                                            @RequestParam(value = "rows",defaultValue = "5") Integer rows){
        PageResult<SpuBo> spuPageResult= goodsService.querySpuByPage(key,saleable,page,rows);
        if (spuPageResult != null  &&0!=spuPageResult.getItems().size()) {
            return ResponseEntity.ok(spuPageResult);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    //添加
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){
        goodsService.saveGoods(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //回显
    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId") Long id){
        SpuDetail spuDetail =goodsService.querySpuDetailBySpuId(id);
        if(null!=spuDetail){
            return ResponseEntity.ok(spuDetail);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id")Long id){
        List<Sku> skus=goodsService.querySkuBySpuId(id);
        if (skus!= null && skus.size()>0) {
            return ResponseEntity.ok(skus);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    //修改
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo){

        this.goodsService.updateGoods(spuBo);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
