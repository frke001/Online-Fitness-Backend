package org.unibl.etf.fitness.services.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.unibl.etf.fitness.exceptions.NotFoundException;
import org.unibl.etf.fitness.exceptions.UnauthorizedException;
import org.unibl.etf.fitness.models.dto.JwtUserDTO;
import org.unibl.etf.fitness.models.dto.PdfDTO;
import org.unibl.etf.fitness.models.entities.ExerciseEntity;
import org.unibl.etf.fitness.models.entities.ProgressEntity;
import org.unibl.etf.fitness.repositories.ClientRepository;
import org.unibl.etf.fitness.repositories.ExerciseRepository;
import org.unibl.etf.fitness.repositories.ProgressRepository;
import org.unibl.etf.fitness.services.LogService;
import org.unibl.etf.fitness.services.PdfService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class PdfServiceImpl implements PdfService {

    private final ExerciseRepository exerciseRepository;
    private final ProgressRepository progressRepository;
    private final ClientRepository clientRepository;

    private final LogService logService;
    private File path;

    public PdfServiceImpl(ExerciseRepository exerciseRepository, ProgressRepository progressRepository, ClientRepository clientRepository, LogService logService) {
        this.exerciseRepository = exerciseRepository;
        this.progressRepository = progressRepository;
        this.clientRepository = clientRepository;
        this.logService = logService;
    }

    @PostConstruct
    public void initialize() throws IOException {
        ClassPathResource pdfPath = new ClassPathResource("");
        this.path =new File(pdfPath.getFile().getAbsolutePath() + File.separator + "pdf");/*new File(resourceLoader.getResource("classpath:store/").getFile().toString() + File.separator + FOLDER_PATH);*/
        if (!path.exists())
            path.mkdir();
    }

    @Override
    public void generatePdfForClient(Long id, Authentication auth) {
        var user = clientRepository.findById(id).orElseThrow(NotFoundException::new);
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
        {
            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
            throw new UnauthorizedException();
        }

        List<ExerciseEntity> exercises = exerciseRepository.findAllByClientId(id);
        List<ProgressEntity> progress = progressRepository.getAllByClientId(id);
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(path.getAbsolutePath() + File.separator + "Activities_" + user.getId() + ".pdf"));

            document.open();

            PdfPTable titleTable = new PdfPTable(2);
            titleTable.setWidthPercentage(100);
            float[] columnWidths = {1.5f, 1f};
            titleTable.setWidths(columnWidths);

            PdfPCell titleCell = new PdfPCell(new Paragraph("Activity diary", FontFactory.getFont(FontFactory.TIMES_BOLD,18,BaseColor.BLACK)));
            titleCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            titleCell.setBorder(PdfPCell.NO_BORDER);
            titleTable.addCell(titleCell);

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy");
            PdfPCell dateCell = new PdfPCell(new Paragraph(formatter.format(new Date()), FontFactory.getFont(FontFactory.TIMES_ROMAN,14,BaseColor.BLACK)));
            dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            dateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            dateCell.setBorder(PdfPCell.NO_BORDER);
            titleTable.addCell(dateCell);

            document.add(titleTable);
            document.add(new Paragraph("\n"));
            Paragraph activityTitle = new Paragraph("Exercises:", FontFactory.getFont(FontFactory.TIMES_ROMAN,16,BaseColor.RED));
            document.add(activityTitle);
            document.add(new Paragraph("\n"));

            if(exercises.size() != 0) {

                PdfPTable activitiesTable = new PdfPTable(5);
                activitiesTable.setWidthPercentage(100);
                float[] widths = {0.5f, 2f, 1f, 1f, 1f};
                activitiesTable.setWidths(widths);

                activitiesTable.addCell(createCellWithBottomBorder("#"));
                activitiesTable.addCell(createCellWithBottomBorder("Name"));
                activitiesTable.addCell(createCellWithBottomBorder("Weight[kg]"));
                activitiesTable.addCell(createCellWithBottomBorder("Sets"));
                activitiesTable.addCell(createCellWithBottomBorder("Reps"));

                final Integer[] counter = {1};
                exercises.forEach(el->{
                    activitiesTable.addCell(createCellWithBottomBorderNoBold(counter[0] + "."));
                    activitiesTable.addCell(createCellWithBottomBorderNoBold(el.getExercise()));
                    activitiesTable.addCell(createCellWithBottomBorderNoBold(el.getWeight().toString()));
                    activitiesTable.addCell(createCellWithBottomBorderNoBold(el.getSets().toString()));
                    activitiesTable.addCell(createCellWithBottomBorderNoBold(el.getReps().toString()));
                    counter[0]++;
                });
                document.add(activitiesTable);
            }else{
                Paragraph noExercises = new Paragraph("You do not have any exercises!", FontFactory.getFont(FontFactory.TIMES_BOLD,16,BaseColor.BLACK));
                noExercises.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(noExercises);
            }
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));
            Paragraph progressTitle = new Paragraph("Weight progress:", FontFactory.getFont(FontFactory.TIMES_ROMAN,16,BaseColor.RED));
            document.add(progressTitle);
            document.add(new Paragraph("\n"));

            if(progress.size() != 0){
                PdfPTable progressTable = new PdfPTable(3);
                progressTable.setWidthPercentage(100);
                float[] widths = {0.5f,1f,1f};
                progressTable.setWidths(widths);

                progressTable.addCell(createCellWithBottomBorder("#"));
                progressTable.addCell(createCellWithBottomBorder("Weight[kg]"));
                progressTable.addCell(createCellWithBottomBorder("Date"));
                final Integer[] counter = {1};
                progress.stream().sorted(Comparator.comparing(ProgressEntity::getDate)).forEach(el->{
                    progressTable.addCell(createCellWithBottomBorderNoBold(counter[0] + "."));
                    progressTable.addCell(createCellWithBottomBorderNoBold(el.getWeight().toString()));
                    progressTable.addCell(createCellWithBottomBorderNoBold(new SimpleDateFormat("dd MMM, yyyy").format(el.getDate())));
                    counter[0]++;
                });
                document.add(progressTable);
            }else{
                Paragraph noProgress = new Paragraph("You do not have any wight progress info!", FontFactory.getFont(FontFactory.TIMES_BOLD,16,BaseColor.BLACK));
                noProgress.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(noProgress);
            }
            document.close();
            writer.close();
            logService.info("PDF generated! User: " + user.getUsername());
        }catch(IOException | DocumentException e){
            System.out.println(e.getMessage());
        }
    }

    private static PdfPCell createCellWithBottomBorder(String content) {
        PdfPCell cell = new PdfPCell(new Paragraph(content, FontFactory.getFont(FontFactory.TIMES_BOLD,16,BaseColor.BLACK)));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(PdfPCell.BOTTOM);
        cell.setFixedHeight(25f);
        return cell;
    }
    private static PdfPCell createCellWithBottomBorderNoBold(String content) {
        PdfPCell cell = new PdfPCell(new Paragraph(content, FontFactory.getFont(FontFactory.TIMES_ROMAN,14,BaseColor.BLACK)));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(PdfPCell.BOTTOM);
        cell.setFixedHeight(25f);
        return cell;
    }

    @Override
    public PdfDTO downloadPdf(Long id, Authentication auth) throws IOException {
        var user = clientRepository.findById(id).orElseThrow(NotFoundException::new);
//        var jwtUser =(JwtUserDTO)auth.getPrincipal();
//        if(!jwtUser.getId().equals(user.getId()))
//        {
//            logService.warning("Access to someone else's account attempted! User: " + jwtUser.getUsername() + ".");
//            throw new UnauthorizedException();
//        }
        File pdfFile = new File(path + File.separator + "Activities_" + id + ".pdf");
        if(!pdfFile.exists())
            throw new NotFoundException();
        logService.info("PDF downloaded! User: " + user.getUsername());
        var data = Files.readAllBytes(Paths.get(pdfFile.toString()));
        return new PdfDTO("Activities_" + id, data);
    }
}
