package org.unibl.etf.fitness.services.impl;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.fitness.exceptions.NotFoundException;
import org.unibl.etf.fitness.models.dto.ImageDTO;
import org.unibl.etf.fitness.models.entities.ImageEntity;
import org.unibl.etf.fitness.repositories.ImageRepository;
import org.unibl.etf.fitness.services.ImageService;
import org.unibl.etf.fitness.services.LogService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${spring.servlet.multipart.location}")
    private String FOLDER_PATH;

    private File path;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;
    private final LogService logService;
    private final HttpServletRequest request;

    @PersistenceContext
    private EntityManager entityManager;


    @PostConstruct
    public void init() throws IOException {
        ClassPathResource imgPath = new ClassPathResource("");


        this.path =new File(imgPath.getFile().getAbsolutePath() + File.separator + FOLDER_PATH);/*new File(resourceLoader.getResource("classpath:store/").getFile().toString() + File.separator + FOLDER_PATH);*/
        System.out.println(this.path.getAbsolutePath());
        if (!path.exists())
            path.mkdir();
//        this.path = htmlPath.getFile();/*new File(resourceLoader.getResource("classpath:store/").getFile().toString() + File.separator + FOLDER_PATH);*/
//        System.out.println(this.path.getAbsolutePath());
//        if (!path.exists())
//            path.mkdir();
    }

    public ImageServiceImpl(ImageRepository imageRepository, ModelMapper modelMapper, LogService logService, HttpServletRequest request) {
        this.imageRepository = imageRepository;
        this.modelMapper = modelMapper;
        this.logService = logService;
        this.request = request;
    }

    @Override
    @Transactional
    public Long uploadImage(MultipartFile file) throws IOException {
        var name = StringUtils.cleanPath(file.getOriginalFilename());
        var image = ImageEntity.builder().name(name).type(file.getContentType()).size(file.getSize()).build();
        imageRepository.saveAndFlush(image);
        entityManager.refresh(image);//dobio sam id od baze sada cuvamo na fajl sistemu
        Files.write(Path.of(getPath(image)), file.getBytes());
        logService.info("New image uploaded! Name: " + name);
        return image.getId();
    }
    @Override
    public ImageDTO downloadImage(Long id) throws IOException {
        ImageEntity image = imageRepository.findById(id).orElseThrow(NotFoundException::new);
        var path = getPath(image);
        var data = Files.readAllBytes(Path.of(path));
        ImageDTO imageDto = modelMapper.map(image, ImageDTO.class);
        imageDto.setData(data);
        logService.info("New image downloaded! Address: " + request.getRemoteAddr());
        return imageDto;
    }

    @Override
    public void deleteImage(ImageEntity image) throws IOException {
        imageRepository.delete(image);
        var path = getPath(image);
        File file=new File(path);
        file.delete();
        logService.info("Image deleted! Image: " + image.getName());
    }

    private String getPath(ImageEntity image) {
        var tmp = image.getType().split("/");
        var name = image.getId() + "." + tmp[1];
        var file = this.path + File.separator + name; // /images/id.png npr
        return file;
    }

    public String getPathById(Long id){
        var entity = imageRepository.findById(id).get();
        return getPath(entity);
    }

    public String getNameById(Long id){
        var entity = imageRepository.findById(id).get();
        return entity.getName();
    }

    @Override
    public void deleteImageById(Long id) throws IOException {
        var entity = imageRepository.findById(id).get();
        deleteImage(entity);
    }
}
