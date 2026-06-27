//package com.ecommerce.project.service;
//
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.UUID;
//
//@Service
//public class FileServiceImpl implements FileService{
//
//    @Override
//    public String uploadImage(String path, MultipartFile file) throws IOException {
//        //File names of current/original file
//        String originalFileName = file.getOriginalFilename();
//
//        //Generate a unique file name for avoiding name conflicts
//        String randomId = UUID.randomUUID().toString();
//        // ex.jpg --> 121 --> 121.jpg
//        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
//        String filePath = path + File.separator + fileName;
//
//        //check if path exist and create
//        File folder = new File(path);
//        if(!folder.exists())
//            folder.mkdir();
//
//        //upload to server
//        Files.copy(file.getInputStream(), Paths.get(filePath));
//
//        //returning file name
//        return fileName;
//    }
//}



package com.ecommerce.project.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.emptyMap()
        );

        return uploadResult.get("secure_url").toString();
    }
}
