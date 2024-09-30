package com.ahgtgk.scoresense.controller;

import com.ahgtgk.scoresense.exception.BizException;
import com.ahgtgk.scoresense.model.request.UploadAttachmentRequest;
import com.ahgtgk.scoresense.service.AttachmentService;
import com.ahgtgk.scoresense.view.AttachmentView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/")
    public AttachmentView uploadAttachment(@ModelAttribute UploadAttachmentRequest request) {
        return attachmentService.saveAttachment(request).toView();
    }

    @GetMapping("/{attachmentId}")
    public ResponseEntity<Resource> getAttachment(@PathVariable Long attachmentId) {
        try {
            var attachment = attachmentService.getAttachment(attachmentId);
            // 定位文件
            var file = new File(attachment.getPath());
            var resource = new InputStreamResource(new FileInputStream(file));

            // 返回文件响应
            return ResponseEntity.ok()
                    .header("Content-Disposition", "inline; filename=" +
                            URLEncoder.encode(attachment.getName(), StandardCharsets.UTF_8))
                    .header("Content-Type", attachment.getContentType())
                    .contentLength(file.length())
                    .body(resource);
        } catch (IOException e) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, "无法打开文件");
        }
    }

}
