package com.alfarays.service;

import com.alfarays.configuration.StorageProperties;
import com.alfarays.entity.Image;
import com.alfarays.model.ImageResponse;
import com.alfarays.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final StorageProperties storageProperties;

    @Value("${image.max-size:10485760}") // 10MB default
    private long maxFileSize;

    @Value("${image.allowed-extensions:jpg,jpeg,png,gif,webp}")
    private String allowedExtensions;

    private static final String FILE_SEPARATOR = "_";

    @Transactional
    public Image save(MultipartFile file) {

        validateFile(file);

        try {
            // 1. Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(storageProperties.getUploadDir());
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                log.info("Created upload directory at: {}", uploadPath);
            }

            // 2. Generate a secure filename and save the file
            String originalFilename = file.getOriginalFilename();
            String fileName = generateSecureFileName(originalFilename);
            Path filePath = uploadPath.resolve(fileName);

            // 3. Save the file to disk
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 4. Store metadata in the database
            Image image = new Image();
            image.setName(fileName);
            image.setOriginalName(originalFilename); // Store original filename separately
            image.setPath(filePath.toString());
            image.setSize(file.getSize());
            image.setContentType(file.getContentType());
            image.setFileExtension(getFileExtension(originalFilename)); // Store file extension for validation

            Image saved = imageRepository.save(image);

            log.info("Image uploaded successfully: id={}, name={}, size={}", saved.getId(), fileName, file.getSize());

            // 5. Return saved image metadata
            return saved;

        } catch (IOException ioEx) {
            log.error("IO error while uploading image: {}", ioEx.getMessage(), ioEx);
            throw new RuntimeException("Error saving image file: " + ioEx.getMessage(), ioEx);
        } catch (Exception ex) {
            log.error("Unexpected error uploading image: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error uploading image: " + ex.getMessage(), ex);
        }
    }

    public ImageResponse getImage(Long id) {

        if (id == null || id <= 0) {
            log.warn("Invalid image ID requested: {}", id);
            throw new IllegalArgumentException("Invalid image ID");
        }

        Image image = imageRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Image not found: id={}", id);
                    return new RuntimeException("Image not found with id: " + id);
                });

        return mapToResponse(image);
    }

    public List<ImageResponse> getAll() {

        try {
            List<ImageResponse> responses = imageRepository.findAll().stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            log.debug("Retrieved {} images from database", responses.size());
            return responses;
        } catch (Exception ex) {
            log.error("Error retrieving all images: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error retrieving images: " + ex.getMessage(), ex);
        }
    }

    @Transactional
    public void delete(Long id) {

        if (id == null || id <= 0) {
            log.warn("Invalid image ID for deletion: {}", id);
            throw new IllegalArgumentException("Invalid image ID");
        }

        Image image = imageRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Image not found for deletion: id={}", id);
                    return new RuntimeException("Image not found with id: " + id);
                });

        try {
            // 1. Delete the physical file from the directory
            Path filePath = Paths.get(image.getPath());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("Image file deleted: {}", filePath);
            } else {
                log.warn("Image file not found on disk: {}", filePath);
            }

            // 2. Delete the database record
            imageRepository.deleteById(id);
            log.info("Image record deleted from database: id={}", id);

        } catch (IOException ioEx) {
            log.error("IO error while deleting image file: {}", ioEx.getMessage(), ioEx);
            throw new RuntimeException("Error deleting image file: " + ioEx.getMessage(), ioEx);
        } catch (Exception ex) {
            log.error("Error deleting image: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error deleting image: " + ex.getMessage(), ex);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null) throw new IllegalArgumentException("File cannot be null");
        if (file.isEmpty()) throw new IllegalArgumentException("File cannot be empty");

        String originalFilename = file.getOriginalFilename();
        if (originalFilename.trim().isEmpty()) throw new IllegalArgumentException("File name cannot be empty");

        if (file.getSize() > maxFileSize) throw new IllegalArgumentException("File size exceeds maximum allowed size of " + maxFileSize + " bytes");

        String fileExtension = getFileExtension(originalFilename).toLowerCase();
        if (!isAllowedExtension(fileExtension)) throw new IllegalArgumentException("File type not allowed. Allowed types: " + allowedExtensions);
        log.debug("File validation passed: name={}, size={}", originalFilename, file.getSize());
    }

    private String generateSecureFileName(String originalFilename) {
        // Remove path traversal attempts
        String sanitized = originalFilename.replaceAll("[\\\\/:]", "");

        // Generate unique filename
        String uuid = UUID.randomUUID().toString();
        String extension = getFileExtension(originalFilename);

        String secureFileName = uuid + FILE_SEPARATOR + sanitized;
        log.debug("Generated secure filename: {} from: {}", secureFileName, originalFilename);

        return secureFileName;
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    private boolean isAllowedExtension(String extension) {
        String[] allowed = allowedExtensions.toLowerCase().split(",");
        for (String ext : allowed) {
            if (ext.trim().equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    private ImageResponse mapToResponse(Image image) {
        ImageResponse response = new ImageResponse();
        response.setId(image.getId());
        response.setName(image.getName());
        response.setOriginalName(image.getOriginalName());
        response.setPath(image.getPath());
        response.setSize(image.getSize());
        response.setContentType(image.getContentType());
        // Optionally, include uploadedAt or other metadata here
        return response;
    }
}
