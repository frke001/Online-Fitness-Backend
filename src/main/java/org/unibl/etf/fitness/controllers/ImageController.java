package org.unibl.etf.fitness.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.fitness.services.ImageService;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/image")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long uploadImage(@RequestParam("image") MultipartFile file) throws IOException{
        return this.imageService.uploadImage(file);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> downloadImage(@PathVariable Long id) throws IOException {
        var image = imageService.downloadImage(id);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"")
                .body(image.getData());
    }
}
