package com.java.filestorageapi.service;

import com.java.filestorageapi.model.FileEntity;
import com.java.filestorageapi.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {
    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public FileEntity uploadFile(MultipartFile file) throws IOException {
        // Validate the file size and extension
        if (file.getSize() > 5 * 1024 * 1024) { // 5MB
            throw new IllegalArgumentException("File size exceeds the limit.");
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            throw new IllegalArgumentException("Invalid file name.");
        }

        String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        if (!isValidExtension(extension)) {
            throw new IllegalArgumentException("Invalid file extension.");
        }

        // Generate a unique filename for storage
        String uniqueFileName = generateUniqueFileName(extension);

        // Save the file to a storage location
        // Replace "your-storage-path" with the actual storage path
        String storagePath = "src/main/resources/static/sample-files" + uniqueFileName;
        Path storageLocation = Paths.get(storagePath);
        Files.write(storageLocation, file.getBytes());

        // Store metadata in the database
        FileEntity newFile = new FileEntity(originalFileName, storagePath, file.getSize(), extension, file.getBytes());
        return fileRepository.save(newFile);
    }

    // Helper method to validate file extension
    private boolean isValidExtension(String extension) {
        String[] validExtensions = {"png", "jpeg", "jpg", "docx", "pdf", "xlsx"};
        for (String validExtension : validExtensions) {
            if (validExtension.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    // Helper method to generate a unique filename
    private String generateUniqueFileName(String extension) {
        return UUID.randomUUID().toString() + "." + extension;
    }


    public List<FileEntity> listFiles() {
        return fileRepository.findAll();
    }

    public byte[] downloadFile(Long fileId) throws IOException {
        // Retrieve the file metadata from the database
        Optional<FileEntity> fileOptional = fileRepository.findById(fileId);
        if (!fileOptional.isPresent()) {
            throw new IllegalArgumentException("File not found");
        }

        FileEntity fileEntity = fileOptional.get();

        // Retrieve the file content from the storage location
        Path filePath = Paths.get(fileEntity.getPath());

        if (!Files.exists(filePath)) {
            throw new IOException("File not found on the server.");
        }

        return Files.readAllBytes(filePath);
    }

    public void updateFile(Long fileId, FileEntity updatedFile) throws IOException {
        // Retrieve the file metadata from the database
        FileEntity existingFile = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("File not found"));

        // Update the metadata
        existingFile.setName(updatedFile.getName());
        existingFile.setExtension(updatedFile.getExtension());

        // Optionally, update the file content in the storage location
        if (updatedFile.getContent() != null) {
            // Retrieve the existing file's path from the database
            String filePath = existingFile.getPath();

            // Update the file content in the storage location
            Files.write(Paths.get(filePath), updatedFile.getContent(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            existingFile.setSize((long) updatedFile.getContent().length);
        }

        // Save the updated metadata in the database
        fileRepository.save(existingFile);
    }






    public void deleteFile(Long fileId) throws IOException {
        // Retrieve the file metadata from the database
        Optional<FileEntity> fileOptional = fileRepository.findById(fileId);
        if (!fileOptional.isPresent()) {
            throw new IllegalArgumentException("File not found");
        }

        FileEntity fileEntity = fileOptional.get();
        String filePath = fileEntity.getPath();

        // Delete the file content from the storage location
        Path fileLocation = Paths.get(filePath);
        if (Files.exists(fileLocation)) {
            Files.delete(fileLocation);
        }

        // Remove the metadata from the database
        fileRepository.delete(fileEntity);
    }
}
