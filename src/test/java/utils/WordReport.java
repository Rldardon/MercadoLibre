package utils;

import org.apache.commons.io.FileUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WordReport {
    private XWPFDocument document;
    private String screenshotDir;
    private String reportPath;

    public WordReport() {
        this.document = new XWPFDocument();

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        this.screenshotDir = "target/reports/screenshots_" + timestamp;
        this.reportPath = "target/reports/report_" + timestamp + ".docx";

        try {
            Files.createDirectories(Paths.get(screenshotDir));

            // Agregar título inicial
            addTitle("Reporte de Prueba Automatizada - MercadoLibre");
            addSubtitle("Fecha: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            addEmptyLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addTitle(String text) {
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun titleRun = title.createRun();
        titleRun.setText(text);
        titleRun.setBold(true);
        titleRun.setFontSize(16);
        titleRun.setFontFamily("Arial");
    }

    private void addSubtitle(String text) {
        XWPFParagraph subtitle = document.createParagraph();
        subtitle.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun subtitleRun = subtitle.createRun();
        subtitleRun.setText(text);
        subtitleRun.setFontSize(12);
        subtitleRun.setFontFamily("Arial");
    }

    private void addEmptyLine() {
        document.createParagraph().createRun().addBreak();
    }

    public String takeScreenshot(WebDriver driver, String stepName) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String screenshotPath = screenshotDir + "/" + stepName + "_" +
                    new SimpleDateFormat("HHmmssSSS").format(new Date()) + ".png";
            FileUtils.copyFile(screenshot, new File(screenshotPath));
            return screenshotPath;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void addStep(String step, String description, String result, String screenshotPath) {
        // Paso
        XWPFParagraph stepParagraph = document.createParagraph();
        XWPFRun stepRun = stepParagraph.createRun();
        stepRun.setText("Paso " + step + ":");
        stepRun.setBold(true);
        stepRun.setFontSize(12);
        stepRun.setFontFamily("Arial");

        // Descripción
        XWPFParagraph descParagraph = document.createParagraph();
        XWPFRun descRun = descParagraph.createRun();
        descRun.setText("• " + description);
        descRun.setFontSize(11);
        descRun.setFontFamily("Arial");

        // Resultado
        XWPFParagraph resultParagraph = document.createParagraph();
        XWPFRun resultRun = resultParagraph.createRun();
        resultRun.setText("• Resultado: " + result);
        resultRun.setFontSize(11);
        resultRun.setFontFamily("Arial");

        // Captura de pantalla
        if (!screenshotPath.isEmpty()) {
            try {
                XWPFParagraph imageParagraph = document.createParagraph();
                imageParagraph.setAlignment(ParagraphAlignment.CENTER);

                XWPFRun imageRun = imageParagraph.createRun();
                imageRun.addPicture(new FileInputStream(screenshotPath),
                        XWPFDocument.PICTURE_TYPE_PNG,
                        screenshotPath,
                        Units.toEMU(400),
                        Units.toEMU(250));

                // Pie de imagen
                XWPFParagraph caption = document.createParagraph();
                caption.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun captionRun = caption.createRun();
                captionRun.setText("Captura: " + step);
                captionRun.setItalic(true);
                captionRun.setFontSize(10);
                captionRun.setFontFamily("Arial");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        addEmptyLine();
    }

    public void saveReport() {
        try (FileOutputStream out = new FileOutputStream(reportPath)) {
            document.write(out);
            document.close();
            System.out.println("\nReporte Word generado en: " + new File(reportPath).getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
