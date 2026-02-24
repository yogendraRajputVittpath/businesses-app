package com.user.business.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.UUID;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.user.business.entity.User;
import com.user.business.exception.FileStorageException;
import com.user.business.repository.UserRepository;

//import com.papertrading.entity.User;
//import com.papertrading.exception.FileStorageException;
//import com.papertrading.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadingImage {

	// ✅ Values loaded from application.properties
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.max-size}")
    private long maxSize;
    
    
    private final UserRepository userRepository;

    public String uploadProfilePic(User user, MultipartFile multipartFile) {

        try {
            // Validate file
            validateFile(multipartFile);

            // Ensure folder exists
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                log.info("Directory not exists, creating: {}", directory);
                directory.mkdirs();
            }

            // Extract data for filename
            String extension = getFileExtension(multipartFile.getOriginalFilename());
            String originalName = Paths.get(multipartFile.getOriginalFilename())
                                       .getFileName()
                                       .toString()
                                       .replaceAll("\\s+", "_");

            String firstName = user.getFirstName() == null ? "usr" : user.getFirstName();
            String firstThreeChars = firstName.length() >= 3
                    ? firstName.substring(0, 3)
                    : firstName;

            // Generate filename
            String newFileName = user.getId()
                    + "_" + firstThreeChars.toLowerCase()
                    + "_" + originalName;

            log.info("Generated filename: {}", newFileName);

            Path filePath = Paths.get(uploadDir, newFileName);

            // Compress if large
            if (multipartFile.getSize() > maxSize) {
                log.info("File size > limit, compressing...");
                compressImage(multipartFile, filePath.toFile(), extension);
            } else {
                Files.copy(
                        multipartFile.getInputStream(),
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }

            // Save ONLY filename in DB
            user.setProfilePic(newFileName);
            userRepository.save(user);

            log.info("Profile picture saved for user {} as {}", user.getId(), newFileName);

            return newFileName;

        } catch (IOException e) {
            log.error("File upload failed", e);
            throw new FileStorageException("Error saving image");
        }
    }


	private void validateFile(MultipartFile /*String*/ file) {
        String name = file.getOriginalFilename();
//		String name = file;
		log.info("");
        if (name == null) {
            throw new FileStorageException("Invalid file name");
        }

        String ext = getFileExtension(name);
        
        if (!ext.equalsIgnoreCase("jpg") && !ext.equalsIgnoreCase("png")) {
            throw new FileStorageException("Only JPG and PNG files are allowed");
        }
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    private void compressImage(MultipartFile input, File output, String ext) throws IOException {
        BufferedImage image = ImageIO.read(input.getInputStream());
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(ext);
        if (!writers.hasNext()) {
            throw new IllegalStateException("No writer found for " + ext);
        }

        ImageWriter writer = writers.next();
        try (OutputStream os = new FileOutputStream(output);
             ImageOutputStream ios = ImageIO.createImageOutputStream(os)) {
            writer.setOutput(ios);
            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(0.7f); // 70% quality
            }
            writer.write(null, new IIOImage(image, null, null), param);
        } finally {
            writer.dispose();
        }
    }
    
//    Method to convert String value to MultiPartfile value
    public static MultipartFile pathToMultipart(String path) throws IOException {
        File file = new File(path);
        FileInputStream input = new FileInputStream(file);
        return new MockMultipartFile(
                file.getName(),
                file.getName(),
                "image/jpeg",
                input
        );
    }
}

