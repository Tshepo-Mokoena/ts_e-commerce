package com.tshepo.service.impl;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.tshepo.service.IUploadDowloadService;
import com.tshepo.util.Utilities;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UploadDowloadService implements IUploadDowloadService{
	
	private Storage storage;
	
	@EventListener
    public void initialize(ApplicationReadyEvent event)
    {
    	try {
    		FileInputStream serviceAccount =
    				  new FileInputStream("./tshepo-9129c-firebase-adminsdk-su9wh-c8876f7600.json");
    		
    		storage = StorageOptions.newBuilder()
    				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
    				.setProjectId("tshepo-9129c")
    				.build().getService();			
		} catch (Exception e) {e.printStackTrace();}
    }
	
	@SuppressWarnings("deprecation")
	@Override
	@Async
	public String uploadFile(MultipartFile file, String productId) 
	{
		String imageName = generateImageName(file.getOriginalFilename(), productId);		
        BlobId blobId = BlobId.of(getBucketName(), imageName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();
        try {
			storage.create(blobInfo, file.getInputStream());
		} catch (IOException e) {e.printStackTrace();}        
        String fileUrlString = generateImageUrl(getBucketName(), imageName);
        log.info(fileUrlString);
        return fileUrlString;              
	}
	
	@Override
	@Async
	public boolean deleteFile(String fileName) 
	{
		BlobId blobId = BlobId.of(getBucketName(), fileName);		
		boolean result =  storage.delete(blobId);		
		if(result==true)
			return true;
		else
			return false;		
	}
	
	private String generateImageName(String originalFileName, String productId) {
        return productId + "." + Utilities.getExtension(originalFileName);
    }
	
	private String generateImageUrl(String bucketName, String imageName) {
		return "https://firebasestorage.googleapis.com/v0/b/"+bucketName+"/o/"+imageName+"?alt=media";
	}
	
	private String getBucketName() {
		return "tshepo-9129c.appspot.com";
	}

}
