package com.java.filestorageapi.controller;

import com.java.filestorageapi.model.FileEntity;
import com.java.filestorageapi.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@Api(value = "File Entity Api documentation")
public class FileController {
    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    @ApiOperation(value = "New File uploading method")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            FileEntity savedFile = fileService.uploadFile(file);
            return ResponseEntity.ok("File uploaded successfully. File ID: " + savedFile.getId());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to upload the file: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    @ApiOperation(value = "File listing method")
    public ResponseEntity<List<FileEntity>> listFiles() {
        List<FileEntity> files = fileService.listFiles();
        return ResponseEntity.ok(files);
    }

    @GetMapping("/content/{fileId}")
    @ApiOperation(value = "File content downloading method")
    public ResponseEntity<byte[]> downloadFileContent(@PathVariable Long fileId) {
        try {
            byte[] fileContent = fileService.downloadFileContent(fileId);
            return ResponseEntity.ok(fileContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Failed to download the file: " + e.getMessage()).getBytes());
        }
    }

    @PutMapping("/update/{fileId}")
    @ApiOperation(value = "File updating method")
    public ResponseEntity<String> updateFile(@PathVariable Long fileId, @RequestBody FileEntity updatedFile) {
        try {
            fileService.updateFile(fileId, updatedFile);
            return ResponseEntity.ok("File updated successfully.");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to update the file: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{fileId}")
    @ApiOperation(value = "File deleting method")
    public ResponseEntity<String> deleteFile(@PathVariable Long fileId) {
        try {
            fileService.deleteFile(fileId);
            return ResponseEntity.ok("File deleted successfully.");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to delete the file: " + e.getMessage());
        }
    }
}