package com.leyou.item.api;

import com.leyou.common.po.PageResult;
import com.leyou.item.pojo.Brand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface BrandApi {
    @GetMapping("brand/page")
    public PageResult<Brand> pagerQuery(@RequestParam(value = "page",defaultValue = "1") int page,
                                                        @RequestParam(value = "rows",defaultValue = "5") int rows,
                                                        @RequestParam(value = "sortBy",required =false) String sortBy,
                                                        @RequestParam(value = "desc",required = false) Boolean desc,
                                                        @RequestParam(value = "key",required = false) String  key);
    @PostMapping
    public Void addBrand(Brand brand, @RequestParam("cids")List<Long> cids);

    @PutMapping
    public Void updateBrand(Brand brand,@RequestParam("cids") List<Long> cids);

    @GetMapping("brnad/cid/{cid}")
    public List<Brand> QueryBrandByCategory(@PathVariable("cid") Long id);
}
