package com.ronglankj.scoresense.service;

import com.onixbyte.guid.GuidCreator;
import com.ronglankj.scoresense.entity.Attachment;
import com.ronglankj.scoresense.exception.BaseBizException;
import com.ronglankj.scoresense.model.request.UploadAttachmentRequest;
import com.ronglankj.scoresense.repository.AttachmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Slf4j
@Service
public class AttachmentService {

    private final GuidCreator<Long> attachmentIdCreator;
    private final AttachmentRepository attachmentRepository;

    public AttachmentService(@Qualifier("attachmentIdCreator") GuidCreator<Long> attachmentIdCreator,
                             AttachmentRepository attachmentRepository) {
        this.attachmentIdCreator = attachmentIdCreator;
        this.attachmentRepository = attachmentRepository;
    }

    public Attachment saveAttachment(UploadAttachmentRequest request) {
        try {
            // 检查存储文件夹是否存在
            var folder = new File("uploads");
            // 如果文件存在且不是文件夹，进行备份
            if (folder.exists() && !folder.isDirectory()) {
                Files.move(folder.toPath(), Path.of("uploads.file.backup"));
                log.info("File uploads has been moved to uploads.file.backup");
            }
            // 创建文件夹
            if (!folder.exists()) {
                if (folder.mkdir()) {
                    log.info("uploads directory created.");
                } else {
                    throw new BaseBizException(HttpStatus.INTERNAL_SERVER_ERROR, "无法创建上传文件夹");
                }
            }

            // 构建 Attachment 记录
            var attachmentBuilder = Attachment.builder()
                    .id(attachmentIdCreator.nextId())
                    .contentType(request.file().getContentType())
                    .name(request.name());

            // 获取文件原始后缀
            var destinationPath = getDestinationPath(request.file(), request.name());
            request.file().transferTo(destinationPath);
            attachmentBuilder.path(destinationPath.toString());

            // 保存附件记录
            var attachment = attachmentBuilder.build();
            attachmentRepository.insert(attachment);
            return attachment;
        } catch (IOException e) {
            throw new BaseBizException(HttpStatus.INTERNAL_SERVER_ERROR, "无法保存文件！");
        }
    }

    public Attachment getAttachment(Long attachmentId) {
        return attachmentRepository.selectOneByEntityId(Attachment
                .builder()
                .id(attachmentId)
                .build());
    }

    private Path getDestinationPath(MultipartFile file, String name) {
        var originalFilename = file.getOriginalFilename();
        if (Objects.isNull(originalFilename)) {
            throw new BaseBizException(HttpStatus.BAD_REQUEST, "文件名不能为空");
        }
        var _indexOfSeparator = originalFilename.lastIndexOf('.');
        var extension = originalFilename.substring(_indexOfSeparator + 1);
        var fileName = name + "." + extension;

        // 存储文件
        return Path.of("uploads", fileName);
    }

}
