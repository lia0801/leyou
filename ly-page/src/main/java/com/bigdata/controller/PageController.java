package com.bigdata.controller;

import com.bigdata.service.FileService;
import com.bigdata.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller //返回页面
public class PageController {

    @Autowired
    PageService pageService;

    @Autowired
    FileService fileService;

    @GetMapping("item/{id}.html")
    public String toPage(@PathVariable("id") Long spuId, Model model){
        System.out.println("spuId:"+spuId);
        System.out.println(model+"1");
        model.addAllAttributes(pageService.toPage(spuId));
        System.out.println(model);
        //如果.html不存在
        if(!fileService.exists(spuId)){
            fileService.syncCreateHtml(spuId);
        }
        return "item";
    }

}
