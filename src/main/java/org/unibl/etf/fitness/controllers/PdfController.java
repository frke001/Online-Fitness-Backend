package org.unibl.etf.fitness.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.fitness.models.dto.PdfDTO;
import org.unibl.etf.fitness.services.PdfService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/pdf")
public class PdfController {

    private final PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping("/{id}")
    public void generatePdfForClient(@PathVariable Long id, Authentication auth){
        this.pdfService.generatePdfForClient(id,auth);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> downloadPdf(@PathVariable Long id) throws IOException {
        PdfDTO pdfDTO = pdfService.downloadPdf(id);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/pdf"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pdfDTO.getFileName()+".pdf" + "\"")
                .body(pdfDTO.getData());
    }
}
