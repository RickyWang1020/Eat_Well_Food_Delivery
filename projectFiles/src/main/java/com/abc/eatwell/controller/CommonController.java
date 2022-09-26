package com.abc.eatwell.controller;

import com.abc.eatwell.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * file upload and download
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${eat-well.path}")
    private String basePath;

    /**
     * file upload
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        // file is a temporary file, if not saved to an assigned position, then it will be deleted after the execution
        log.info(file.toString());

        // original file name
        String originalFileName = file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        // use uuid to generate a new name for file
        String fileName = UUID.randomUUID().toString() + suffix;

        // create a directory if the corresponding directory does not exist
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath + fileName).toPath().toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * download file
     * @param response
     * @param name
     */
    @GetMapping("/download")
    public void download(HttpServletResponse response, String name) {

        try {
            // input stream to read file
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            // output stream to write the file back to browser
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/png");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
