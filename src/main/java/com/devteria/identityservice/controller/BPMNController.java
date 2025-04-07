package com.devteria.identityservice.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/api/bpmn")
@CrossOrigin(origins = "http://localhost:3000") // Cho phép React truy cập
public class BPMNController {

    private static final String BPMN_FOLDER = "src/main/resources/";

    @GetMapping("/{filename}")
    public ResponseEntity<String> getBpmnXml(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(BPMN_FOLDER + filename);
            String xmlContent = Files.readString(filePath);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_XML)
                    .body(xmlContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi đọc file BPMN");
        }
    }
}

