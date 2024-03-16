package com.assignment.csvDriver.controller;

import com.assignment.csvDriver.exceptions.RecordNotFoundException;
import com.assignment.csvDriver.model.CSVDAO;
import com.assignment.csvDriver.service.implimentation.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/csv")
public class CSVController {

    @Autowired
    private CSVService csvService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        try {
            csvService.upload(file.getInputStream());
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully uploaded");
        } catch (IOException ex) {
            throw new RuntimeException("Storing CSV is failed");
        }
    }

    @GetMapping("/get")
    public ResponseEntity<byte[]> getCSV(@RequestParam("code") String code) throws RecordNotFoundException, IOException {
        return ResponseEntity.status(HttpStatus.OK).headers(createHeaders()).body(csvService.getByCode(code));
    }

    @GetMapping("/getAll")
    public ResponseEntity<byte[]> getAllCSVs() throws IOException {
        byte[] bytes = csvService.getAll();
        if(bytes == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.OK).headers(createHeaders()).body(bytes);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAll() {
        csvService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "all.csv");

        return headers;
    }
}
