package com.java.filestorageapi.controller;

import com.java.filestorageapi.config.SecurityConfig;
import com.java.filestorageapi.model.AuthenticationRequest;
import com.java.filestorageapi.model.AuthenticationResponse;
import com.java.filestorageapi.model.FileEntity;
import com.java.filestorageapi.model.RegisterRequest;
import com.java.filestorageapi.service.AuthenticationService;
import com.java.filestorageapi.service.FileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@RunWith(SpringRunner.class)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class FileControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private FileService fileService;

    private List<FileEntity> sampleFiles;

    @Before
    public void setup() {
        sampleFiles = new ArrayList<>();
        sampleFiles.add(new FileEntity(1L, "file1.txt", "/path/file1.txt", 100L, "txt", "Sample content".getBytes()));
        sampleFiles.add(new FileEntity(2L, "file2.txt", "/path/file2.txt", 150L, "txt", "Another sample content".getBytes()));

    }

    @Test
    public void testRegistration() throws Exception {
        when(authenticationService.register(Mockito.any(RegisterRequest.class))).thenReturn(new AuthenticationResponse("token"));
        String registerRequest = "{\"username\": \"jane\", \"password\": \"1234\"}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/authenticate/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    public void testAuthentication() throws Exception {
        when(authenticationService.authenticate(Mockito.any(AuthenticationRequest.class))).thenReturn(new AuthenticationResponse("token"));
        String authenticationRequest = "{\"username\": \"jane\", \"password\": \"1234\"}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/authenticate/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authenticationRequest)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    @WithMockUser
    public void testUploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "filename.pdf", "application/pdf", "test content".getBytes(StandardCharsets.UTF_8));

        FileEntity fileEntity = new FileEntity(1L,"filename", "/path/to/file", Long.valueOf("10"), "pdf", "test content".getBytes(StandardCharsets.UTF_8));

        when(fileService.uploadFile(Mockito.any(MultipartFile.class))).thenReturn(fileEntity);

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Content-Type","multipart/form-data");
        mockMvc.perform(MockMvcRequestBuilders
                        .fileUpload("/api/v1/files/upload")
                        .file(file)
                        .headers(httpHeaders)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("File uploaded successfully. File ID: 1"));
    }

    @Test
    @WithMockUser
    public void testDownloadFileContent() throws Exception {
        when(fileService.downloadFileContent(1L)).thenReturn(sampleFiles.get(0).getContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/files/content/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
    }

    @Test
    @WithMockUser
    public void testListFiles() throws Exception {
        when(fileService.listFiles()).thenReturn(sampleFiles);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/files/list"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(sampleFiles.size()));
    }

    @Test
    @WithMockUser
    public void testDeleteFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/files/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}