package com.ccq.myblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
public class ImageUploadController {
    @Value("${file.uploadFolder}")
    private String rootPath;
    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @PostMapping("/u/admin/blogs/edit/upload")
    public ObjectNode upload(HttpServletRequest request, @RequestParam(value = "editormd-image-file", required = false) MultipartFile multipartFile) {
        System.out.println(request.getContextPath());
        File filePath = new File(rootPath);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        System.out.println(filePath);
        File realFile = new File(rootPath + File.separator + multipartFile.getOriginalFilename());
        ObjectNode jsonObject = objectMapper.createObjectNode();
        try {
            OutputStream is = new FileOutputStream(realFile);
            is.write(multipartFile.getBytes());
            is.flush();

            jsonObject.put("success", 1);
            jsonObject.put("message", "上传成功");
            jsonObject.put("url", "http://localhost:8080/blogImages/" + multipartFile.getOriginalFilename());
        } catch (IOException e) {
            jsonObject.put("success", 0);

        }
        return jsonObject;
    }

    @PostMapping("/avatar")
    public String avatarUpload(@RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        File file = new File(rootPath + File.separator + "avatar" + File.separator + multipartFile.getOriginalFilename());
        try {
            OutputStream is = new FileOutputStream(file);
            is.write(multipartFile.getBytes());
            is.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "http://localhost:8080/blogImages/avatar/" + multipartFile.getOriginalFilename();

    }

}
