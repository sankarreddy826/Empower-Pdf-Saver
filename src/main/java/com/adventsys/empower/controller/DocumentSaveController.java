package com.adventsys.empower.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@RestController
@RequestMapping("/api/document")
@CrossOrigin(origins = "*")
public class DocumentSaveController {

    private static final Logger log = LoggerFactory.getLogger(DocumentSaveController.class);

    @Value("${app.save.directory}")
    private String saveDirectory;

    @PostMapping("/save-to-network")
    public ResponseEntity<String> saveToNetwork(
            @RequestParam("file") MultipartFile file,
            @RequestParam("docId") String docId) {

        String filename = docId + ".pdf";
        Path target = Paths.get(saveDirectory, filename);

        try {
            Files.createDirectories(target.getParent());
            Files.write(target, file.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            log.info("Saved: {}", target);
            return ResponseEntity.ok("Saved: " + filename);
        } catch (IOException e) {
            log.error("Failed to save file", e);
            return ResponseEntity.internalServerError()
                    .body("Save failed: " + e.getMessage());
        }
    }
}