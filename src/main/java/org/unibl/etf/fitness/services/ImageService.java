package org.unibl.etf.fitness.services;

import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.fitness.models.dto.ImageDTO;
import org.unibl.etf.fitness.models.entities.ImageEntity;

import java.io.IOException;

public interface ImageService {

    Long uploadImage(MultipartFile file) throws IOException;

    ImageDTO downloadImage(Long id) throws IOException;

    //ImageDTO uploadFile(MultipartFile file) throws IOException;

    void deleteImage(ImageEntity image) throws IOException;

    String getPathById(Long id);
    String getNameById(Long id);

    void deleteImageById(Long id) throws IOException;
}
