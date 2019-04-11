package com.ccq.myblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

@RestController
public class ImageUploadController {
    @Value("${file.uploadFolder}")
    private String rootPath;
    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @PostMapping(value = {"/u/{userName}/blogs/edit/upload", "/u/{userName}/blogs/upload"})
    public ObjectNode upload(@PathVariable("userName") String userName, @RequestParam(value = "editormd-image-file", required = false) MultipartFile multipartFile) {
        File filePath = new File(rootPath + userName);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        File realFile = new File(filePath + File.separator + multipartFile.getOriginalFilename());
        ObjectNode jsonObject = objectMapper.createObjectNode();
        try {
            OutputStream is = new FileOutputStream(realFile);
            is.write(multipartFile.getBytes());
            is.flush();

            jsonObject.put("success", 1);
            jsonObject.put("message", "上传成功");
//            jsonObject.put("url", "http://localhost:8080/blogImages/" + URLEncoder.encode(userName, "UTF-8") + "/" + multipartFile.getOriginalFilename());
            jsonObject.put("url", "http://47.102.218.113:8080/blogImages/" + URLEncoder.encode(userName, "UTF-8") + "/" + multipartFile.getOriginalFilename());
        } catch (IOException e) {
            jsonObject.put("success", 0);

        }
        return jsonObject;
    }

    @PostMapping("/{userName}/avatar")
    public String avatarUpload(@PathVariable("userName") String userName, @RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        File file = new File(rootPath + File.separator + "avatar" + File.separator + multipartFile.getOriginalFilename() + "_" + userName);
        try {
            OutputStream is = new FileOutputStream(file);
            is.write(multipartFile.getBytes());
            is.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return "http://localhost:8080/blogImages/avatar/" + multipartFile.getOriginalFilename() + "_" + userName;
        return "http://47.102.218.113:8080/blogImages/avatar/" + multipartFile.getOriginalFilename() + "_" + userName;

    }

}
