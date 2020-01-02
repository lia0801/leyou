package com.leyou.item.controller;

import com.leyou.common.po.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    BrandService brandService;

    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> pagerQuery(@RequestParam(value = "page",defaultValue = "1") int page,
                                                        @RequestParam(value = "rows",defaultValue = "5") int rows,
                                                        @RequestParam(value = "sortBy",required =false) String sortBy,
                                                        @RequestParam(value = "desc",required = false) Boolean desc,
                                                        @RequestParam(value = "key",required = false) String  key){
       PageResult<Brand> pageResult=brandService.pageQuery(page,rows,sortBy,desc,key);
       if (null!=pageResult&&pageResult.getItems().size()>0){
           return ResponseEntity.ok(pageResult);
       }
       return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Void> addBrand(Brand brand, @RequestParam("cids")List<Long> cids){
        brandService.addBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand,@RequestParam("cids") List<Long> cids){
        brandService.updateBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
