package com.java.filestorageapi.repository;

import com.java.filestorageapi.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    // Custom queries
}
