package com.java.filestorageapi.service;

import com.java.filestorageapi.model.FileEntity;
import com.java.filestorageapi.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FileServiceTests {

    @InjectMocks
    private FileService fileService;

    @Mock
    private FileRepository fileRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testListFiles() {
        List<FileEntity> fileEntities = new ArrayList<>();
        fileEntities.add(new FileEntity("file1.jpg", "path1", 1024L, "jpg", "content1".getBytes()));
        fileEntities.add(new FileEntity("file2.pdf", "path2", 2048L, "pdf", "content2".getBytes()));
        when(fileRepository.findAll()).thenReturn(fileEntities);

        List<FileEntity> fileList = fileService.listFiles();

        assertNotNull(fileList);
        assertEquals(2, fileList.size());

        assertEquals("file1.jpg", fileList.get(0).getName());
        assertEquals("path1", fileList.get(0).getPath());
        assertEquals(1024L, fileList.get(0).getSize());
        assertEquals("jpg", fileList.get(0).getExtension());
        assertArrayEquals("content1".getBytes(), fileList.get(0).getContent());

        assertEquals("file2.pdf", fileList.get(1).getName());
        assertEquals("path2", fileList.get(1).getPath());
        assertEquals(2048L, fileList.get(1).getSize());
        assertEquals("pdf", fileList.get(1).getExtension());
        assertArrayEquals("content2".getBytes(), fileList.get(1).getContent());
    }

    @Test
    public void testUploadFile() {

        MockMultipartFile file = new MockMultipartFile("file", "test-file.pdf", "application/pdf", "content".getBytes());

        when(fileRepository.save(any(FileEntity.class))).thenReturn(new FileEntity("test-file.pdf", "test-path", 1024L, "pdf", "content".getBytes()));

        try {
            FileEntity uploadedFile = fileService.uploadFile(file);

            assertNotNull(uploadedFile);
            assertEquals("test-file.pdf", uploadedFile.getName());
            assertEquals("test-path", uploadedFile.getPath());
            assertEquals(1024L, uploadedFile.getSize());
            assertEquals("pdf", uploadedFile.getExtension());
            assertArrayEquals("content".getBytes(), uploadedFile.getContent());
        } catch (IOException e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test
    public void testDownloadFile() {
        FileEntity fileEntity = new FileEntity("sample-filesea873fa4-41bb-4385-af9c-175d91987f9d.pdf", "src/main/resources/static/sample-filesea873fa4-41bb-4385-af9c-175d91987f9d.pdf", 1024L, "pdf", "content".getBytes());
        when(fileRepository.findById(1L)).thenReturn(java.util.Optional.of(fileEntity));

        try {
            byte[] fileContent = fileService.downloadFileContent(1L);

            assertNotNull(fileContent);
            assertArrayEquals("content".getBytes(), fileContent);
        } catch (IOException e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteFile() throws IOException {
        FileEntity fileEntity = new FileEntity("test-file.pdf", "test-path", 1024L, "pdf", "content".getBytes());
        when(fileRepository.findById(1L)).thenReturn(java.util.Optional.of(fileEntity));

        Mockito.doNothing().when(fileRepository).delete(fileEntity);
        fileService.deleteFile(1L);
    }
}