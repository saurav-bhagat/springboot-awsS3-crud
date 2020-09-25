package com.example.crudbasics.controller;

import com.amazonaws.services.s3.model.Bucket;
import com.example.crudbasics.services.AmazonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/storage")
public class BucketController {

    private static Logger log = LoggerFactory.getLogger(BucketController.class);

    private AmazonClient amazonClient;

    @Autowired
    BucketController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @GetMapping("/buckets")
    public List<Bucket> getBuckets() {
        return this.amazonClient.listBuckets();
    }

    @GetMapping("/test")
    public String test(){
        return "test2";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "file")MultipartFile file) {
        return this.amazonClient.uploadFile(file);
    }

    @DeleteMapping("/deleteFile")
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
    }
}
