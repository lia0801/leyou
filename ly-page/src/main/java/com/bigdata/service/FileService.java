package com.bigdata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 *
 */
@Service
public class FileService {

    @Value("${ly.thymeleaf.destPath}")
    private String destPath;

    @Autowired
    PageService pageService;
    //判断文件是否存在

    @Autowired
    TemplateEngine templateEngine;
    public boolean exists(Long spuId) {
        File file = new File(destPath);
        if(!file.exists()){
            file.mkdirs();
        }
        return new File(file,spuId+".html").exists();
    }

    //不存在就创建html
    public void syncCreateHtml(Long spuId) {

        //创建上下文对象
        Context context = new Context();
        //放入数据
        context.setVariables(pageService.toPage(spuId));
        //创建文件对象
        File file = new File(destPath, spuId + ".html");
        //输出打印流
        try {
            //打印流
            PrintWriter printWriter = new PrintWriter(file,"utf-8");
            //产生静态文件
            templateEngine.process("item",context,printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
