package com.ahgtgk.scoresense.extension.spring.web.multipart;

import lombok.Builder;
import lombok.NonNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
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
        return content.length != 0;
    }

    @Override
    public long getSize() {
        return content.length;
    }

    @Override
    public byte @NonNull [] getBytes() {
        return content;
    }

    @Override
    public @NonNull InputStream getInputStream() {
        return new ByteArrayInputStream(content);
    }

    @Override
    public @NonNull Resource getResource() {
        return new ByteArrayResource(content);
    }

    @Override
    public void transferTo(@NonNull File dest) throws IOException, IllegalStateException {
        try (FileOutputStream out = new FileOutputStream(dest)) {
            out.write(content);
        }
    }

    @Override
    public void transferTo(@NonNull Path dest) throws IOException, IllegalStateException {
        transferTo(dest.toFile());
    }
}
