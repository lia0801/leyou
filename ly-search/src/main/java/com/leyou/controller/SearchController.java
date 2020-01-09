package com.leyou.controller;

import com.leyou.Utiles.SearchRequest;
import com.leyou.common.po.PageResult;
import com.leyou.pojo.Goods;
import com.leyou.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {
    @Autowired
    SearchService searchService;
    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest searchRequest){
            PageResult<Goods> PageResult=searchService.search(searchRequest);
        if (PageResult != null  &&0!=PageResult.getItems().size()) {
            return ResponseEntity.ok(PageResult);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
