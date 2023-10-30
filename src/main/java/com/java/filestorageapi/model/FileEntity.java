package com.java.filestorageapi.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name="files")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "File Entity Api model documentation", description = "Model")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "Unique id field of file entity object")
    private Long id;
    @Column
    @ApiModelProperty(value = "Name field of file entity object")
    private String name;
    @Column
    @ApiModelProperty(value = "Path field of file entity object")
    private String path;
    @Column
    @ApiModelProperty(value = "Size field of file entity object")
    private Long size;
    @Column
    @ApiModelProperty(value = "Extension field of file entity object")
    private String extension;
    @Column
    @ApiModelProperty(value = "Content field of file entity object")
    private byte[] content;


    public FileEntity(String name, String path, Long size, String extension, byte[] content) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.extension = extension;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
