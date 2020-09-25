package com.example.crudbasics.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.crudbasics.controller.BucketController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class AmazonClient {

    private static Logger log = LoggerFactory.getLogger(AmazonClient.class);

    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon(){
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    public String uploadFile(MultipartFile multipartFile) {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String filename = generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + bucketName + "/" + filename;
            fileUrl = uploadFileTos3bucket(filename, file);
            log.info("FileUrl is: "+ fileUrl);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File conFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(conFile);
        fos.write(file.getBytes());
        fos.close();
        return conFile;
    }

    private String generateFileName(MultipartFile multipartFile){
        return new Date().getTime() + "-" + multipartFile.getOriginalFilename().replace(" ", "_");
    }

    private String uploadFileTos3bucket(String fileName, File file){
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        String fileUrl = s3client.getUrl(bucketName, fileName ).toExternalForm();
        log.info("File url in line 88 is: " + fileUrl);
        return fileUrl;
    }

    public String deleteFileFromS3Bucket(String fileUrl){
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/")+1);
        s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        return "Successfully deleted";
    }

    public List<Bucket> listBuckets() {
        return s3client.listBuckets();
    }
}
