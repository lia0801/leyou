package com.leyou.item.api;

import com.leyou.item.pojo.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CategoryApi {
    @GetMapping("category/list")
    public List<Category> queryByParentId(@RequestParam("pid") Long id);

    @GetMapping("category/bid/{bid}")
    public List<Category> queryByBrandId(@PathVariable("bid") Long id);

    //给fe提供接口 ,提供搜索微服务
    @GetMapping("category/names")
    public List<String> queryNamesByIds(@RequestParam("ids") List<Long> ids);
}
