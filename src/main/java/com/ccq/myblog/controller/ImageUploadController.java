package com.ccq.myblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/u/admin/blogs/edit")
public class ImageUploadController {
    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @PostMapping("/upload")
    public ObjectNode upload(HttpServletRequest request, @RequestParam(value = "editormd-image-file", required = false) MultipartFile multipartFile) {
        String rootPath = request.getSession().getServletContext().getRealPath("/blogImages/");
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

}
