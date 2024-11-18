package com.xcelerate.cafeManagementSystem.Controller;


/*
    TODO: REMOVE THIS CONTROLLER CLASS AFTER TESTING FILE UPLOADING TO CLOUDINARY
    THIS IS A TEMPORARY CONTROLLER CLASS TO TEST FILE UPLOADING TO CLOUDINARY

 */


import com.xcelerate.cafeManagementSystem.Service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/cloudinary")
public class CloudinaryController {

    @Autowired
    private CloudinaryService cloudinaryService;

    // Endpoint to upload an image
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            cloudinaryService.uploadImage(file);
            return ResponseEntity.ok("Image uploaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading image: " + e.getMessage());

        }
    }

    // Endpoint to delete an image
    @DeleteMapping("/delete")
    public String deleteImage(@RequestParam("publicId") String publicId) {
        try {
            cloudinaryService.deleteImage(publicId);
            return "Image deleted successfully!";
        } catch (Exception e) {
            return "Error deleting image: " + e.getMessage();
        }
    }
}
