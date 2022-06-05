package com.tshepo.service;

import org.springframework.web.multipart.MultipartFile;

public interface IUploadDowloadService {

	String uploadFile(MultipartFile file, String productId);

	boolean deleteFile(String fileName);

}
