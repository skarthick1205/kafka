package com.example.springbatch.controller;

import com.example.springbatch.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/springbatch")
public class MainController {

    @Autowired
    private FileService fileService;

    @PostMapping("/postdata")
    public ResponseEntity<String> postdata(@RequestParam MultipartFile multipartFile) throws Exception {
        return ResponseEntity.ok(fileService.handleFile(multipartFile));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportToCsv() throws IOException {
        File file = fileService.exportToCsv();
        byte[] content = Files.readAllBytes(file.toPath());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().filename(file.getName()).build());
        headers.setContentType(MediaType.parseMediaType("text/csv"));

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }
}
