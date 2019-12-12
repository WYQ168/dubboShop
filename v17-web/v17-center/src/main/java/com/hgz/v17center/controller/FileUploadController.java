package com.hgz.v17center.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.hgz.commons.base.ResultBean;
import com.hgz.v17center.pojo.WangEditorResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("file")
public class FileUploadController {

    @Autowired
    private FastFileStorageClient client;

        @Value(("${image.server}"))
    private String imageServer;

    @PostMapping("upload")
    @ResponseBody
    public ResultBean upload(MultipartFile file){
        //1.获取文件的扩展名
        String originalFilename = file.getOriginalFilename();
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
        try {
            StorePath storePath = client.uploadFile(file.getInputStream(), file.getSize(), extName, null);
            //
            StringBuilder path = new StringBuilder(imageServer).append(storePath.getFullPath());
            //String fullPath = storePath.getFullPath();
            System.out.println(path);
            return new ResultBean(200,path);
        } catch (IOException e) {
            e.printStackTrace();
            //TODO 记录异常信息到日志中
            return new ResultBean(500,"文件上传失败");
        }
    }

    @PostMapping("batchUpload")
    @ResponseBody
    public WangEditorResultBean batch(MultipartFile[] files)  {

        String[] data = new String[files.length];

            try {
                for (int i=0;i<files.length; i++) {
                //获取文件的拓展名
                String  originalFilename = files [i].getOriginalFilename();
                String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
                StorePath storePath = client.uploadFile(files[i].getInputStream(), files[i].getSize(), extName, null);
                StringBuilder stringBuilder = new StringBuilder(imageServer).append(storePath.getFullPath());
                data[i] =stringBuilder.toString();
                }
                return new WangEditorResultBean("0",data);

            } catch (IOException e) {
                e.printStackTrace();
                return new WangEditorResultBean("-1",null);
            }
        }

    }

