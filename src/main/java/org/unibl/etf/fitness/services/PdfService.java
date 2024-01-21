package org.unibl.etf.fitness.services;

import org.springframework.security.core.Authentication;
import org.unibl.etf.fitness.models.dto.PdfDTO;

import java.io.IOException;

public interface PdfService {

    void generatePdfForClient(Long id, Authentication auth);
    PdfDTO downloadPdf(Long id) throws IOException;
}
