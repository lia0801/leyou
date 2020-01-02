package com.bigdata.upload.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class UploadService {
    public String uploadImage(MultipartFile file) {
        //创建file对象
        File file1 = new File("e://upload");
        if(!file1.exists()){
            //如果文件夹不存在，创建
            file1.mkdirs();
        }
        //保存图片
        try {
            file.transferTo(new File(file1,file.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "http://image.leyou.com/"+file.getOriginalFilename();
    }
}
