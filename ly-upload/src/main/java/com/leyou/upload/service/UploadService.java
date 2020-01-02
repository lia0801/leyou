package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class UploadService {

    @Autowired
    FastFileStorageClient client;

    public String uploadImage(MultipartFile file) {
//        //创建file对象
//        File file1 = new File("e://upload");
//        if(!file1.exists()){
//            //如果文件夹不存在，创建
//            file1.mkdirs();
//        }
//        //保存图片
//        try {
//            file.transferTo(new File(file1,file.getOriginalFilename()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "http://image.leyou.com/"+file.getOriginalFilename();

//        StorePath storePath = this.storageClient.uploadFile(
//                new FileInputStream(file), file.length(), "png", null);

        String url=null;

        //获取浏览器传过来的浏览器的图片
        String originalFilename = file.getOriginalFilename();
        //获取图片后缀
        String ext = StringUtils.substringAfter(originalFilename, ".");//后缀   例如png
        //上传
        try {
            StorePath storePath = client.uploadFile(file.getInputStream(), file.getSize(), ext, null);
            url= "http://image.leyou.com/"+storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;


    }
}
