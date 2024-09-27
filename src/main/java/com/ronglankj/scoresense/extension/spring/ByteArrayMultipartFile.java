package com.ronglankj.scoresense.extension.spring;

import lombok.Builder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;

@Builder
public class ByteArrayMultipartFile implements MultipartFile {

    private String name;

    private String contentType;

    private byte[] content;

    public ByteArrayMultipartFile(String name, String contentType, byte[] content) {
        this.name = name;
        this.contentType = contentType;
        this.content = content;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    /**
     * This file has no original file name.
     *
     * @return file name
     * @see #getName()
     */
    @NonNull
    @Override
    public String getOriginalFilename() {
        return name;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return content != null && content.length != 0;
    }

    @Override
    public long getSize() {
        return content.length;
    }

    @Override
    public byte[] getBytes() {
        return content;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(content);
    }

    @Override
    public Resource getResource() {
        return new ByteArrayResource(content);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (FileOutputStream out = new FileOutputStream(dest)) {
            out.write(content);
        }
    }

    @Override
    public void transferTo(Path dest) throws IOException, IllegalStateException {
        transferTo(dest.toFile());
    }
}
