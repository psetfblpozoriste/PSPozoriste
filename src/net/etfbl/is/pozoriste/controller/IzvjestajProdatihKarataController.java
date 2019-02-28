/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.controller;

import com.itextpdf.text.BaseColor;
import java.io.File;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.model.dao.mysql.GostujucaPredstavaDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.KartaDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.PredstavaDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.RepertoarDAO;
import net.etfbl.is.pozoriste.model.dto.GostujucaPredstava;
import net.etfbl.is.pozoriste.model.dto.Igranje;
import net.etfbl.is.pozoriste.model.dto.Karta;
import net.etfbl.is.pozoriste.model.dto.Predstava;
import net.etfbl.is.pozoriste.model.dto.Repertoar;

/**
 *
 * @author Ognjen
 */
public class IzvjestajProdatihKarataController {

    // File file = new File(System.getProperty("user.dir") + File.separator + "izvjestaj.pdf");
    private File file;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

    public IzvjestajProdatihKarataController(File file) {
        File temp = null;
        File tempPravo = null;
        try {
            temp = new File(file.getAbsolutePath() + File.separator + "Izvjestaj"+Calendar.getInstance().getTime().toString().replace(".", "_").replace(":", "_")+".pdf");
            if (!temp.exists()) {
                tempPravo = new File(file.getAbsolutePath() + File.separator + temp.getName().substring(0, temp.getName().length() - 4) + "(1)"+Calendar.getInstance().getTime().toString().replace(".", "_").replace(":", "_")+".pdf");
                tempPravo.createNewFile();
                this.file = tempPravo;
            } else {
                temp.createNewFile();
                this.file = temp;
            }
        } catch (IOException ex) {
            Logger.getLogger(IzvjestajProdatihKarataController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void metoda() {
        if (file == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Fajl ne moze biti kreiran", ButtonType.OK);
            alert.setTitle("Upozorenje");
            alert.setHeaderText("Upozorenje");
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(PregledKarataController.class.getResourceAsStream("/net/etfbl/is/pozoriste/resursi/warning.png")));
            alert.showAndWait();
            return;
        }

        if (file == null || !file.exists() || !file.renameTo(file)) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Fajl je otvoren od strane drugog programa", ButtonType.OK);
            alert.setTitle("Upozorenje");
            alert.setHeaderText("Upozorenje");
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(PregledKarataController.class.getResourceAsStream("/net/etfbl/is/pozoriste/resursi/warning.png")));
            alert.showAndWait();
            return;
        }
        if (file != null && file.exists()) {
            Document document = new Document();
            try {
                file.createNewFile();
                PdfWriter.getInstance(document, new FileOutputStream(file));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(IzvjestajProdatihKarataController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                Logger.getLogger(IzvjestajProdatihKarataController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(IzvjestajProdatihKarataController.class.getName()).log(Level.SEVERE, null, ex);
            }
            document.open();
            addMetaData(document);
            try {
                addContent(document);
            } catch (DocumentException ex) {
                Logger.getLogger(IzvjestajProdatihKarataController.class.getName()).log(Level.SEVERE, null, ex);
            }
            document.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Fajl se ne moze sacuvati u folderu, molimo izaberite drugi folder", ButtonType.OK);
            alert.setTitle("Upozorenje");
            alert.setHeaderText("Upozorenje");
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(PregledKarataController.class.getResourceAsStream("/net/etfbl/is/pozoriste/resursi/warning.png")));
            alert.showAndWait();
        }

    }

    public void addMetaData(Document document) {
        document.addTitle("Izvjestaj prodatih karata");
        document.addSubject("Using iText");
        document.addKeywords("PDF, Izvjestaj");
        document.addAuthor("Pozoriste");
        document.addCreator("Pozoriste");
    }

    public void addContent(Document document) throws DocumentException {
        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
        } catch (IOException ex) {
            Logger.getLogger(IzvjestajProdatihKarataController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Font font = new Font(baseFont);

        // Paragraph subPara = new Paragraph("Subcategory 1    ŠŠŠŠ Đ ĐĐ Š Č Ć ŠŠŽŽ  đ ć ", font);
        List<Repertoar> listaRepertoara = RepertoarDAO.repertoars();
        List<Predstava> listaPredstava = PredstavaDAO.predstave();
        List<GostujucaPredstava> gostujucePredstava = GostujucaPredstavaDAO.gostujucePredstave();

        Font fontNaziva = new Font(FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph nazivIzvjestaja = new Paragraph("STATISTIKA PRODATIH KARATA", fontNaziva);
        nazivIzvjestaja.setAlignment(Paragraph.ALIGN_CENTER);
        document.newPage();
        document.add(nazivIzvjestaja);

        PdfPTable table = new PdfPTable(5);//broj kolona imace 4 kolone

        // the cell object
        PdfPCell celija1 = new PdfPCell(new Phrase("Statistika prodatih karata po repertoarima", font));
        celija1.setColspan(5);
        celija1.setRowspan(1);
        celija1.setCalculatedHeight(10);
        celija1.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celija1.setVerticalAlignment(Element.ALIGN_CENTER);
        celija1.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(celija1);
        document.newPage();
        Consumer<Repertoar> r = p -> {

            for (Integer i = 0; i < p.getIgranja().size(); i++) {

                Igranje igranje = p.getIgranja().get(i);
                String naziv = "";
                Optional<Predstava> optPredtsva = Optional.ofNullable(null);
                Optional<GostujucaPredstava> optGostujuca = Optional.ofNullable(null);
                List<Karta> karte = KartaDAO.karte().stream().filter(e -> e.getTermin().equals(igranje.getTermin()) && e.getIdScene() == igranje.getIdScene()).collect(Collectors.toList());
                if (igranje.getIdPredstave() != 0) {
                    optPredtsva = listaPredstava.stream().filter(e -> e.getId() == igranje.getIdPredstave()).findFirst();
                    if (optPredtsva.isPresent()) {
                        naziv = optPredtsva.get().getNaziv();
                        System.out.println("dodao obicnu predstavu");
                    }
                } else if (igranje.getIdGostujucePredstave() != 0) {
                    optGostujuca = gostujucePredstava.stream().filter(e -> e.getId() == igranje.getIdGostujucePredstave()).findFirst();
                    if (optGostujuca.isPresent()) {
                        naziv = optGostujuca.get().getNaziv();
                        System.out.println("dodao gostujucu");
                    }
                }
                PdfPCell celija2 = new PdfPCell(new Phrase("Repertoar " + p.getMjesecIGodina().toString(), font));
                celija2.setRowspan(1);
                celija2.setCalculatedHeight(10);
                celija2.setColspan(1);
                celija2.setVerticalAlignment(Element.ALIGN_CENTER);
                celija2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(celija2);

                PdfPCell celija3 = new PdfPCell(new Phrase(naziv, font));
                celija3.setRowspan(1);
                celija3.setCalculatedHeight(10);
                celija3.setColspan(1);
                celija3.setVerticalAlignment(Element.ALIGN_CENTER);
                celija3.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(celija3);

                String cijenaPojedinacno = "";
                if (!karte.isEmpty()) {
                    cijenaPojedinacno = String.format("%.2f", karte.get(0).getIznos());
                } else {
                    cijenaPojedinacno = "--";
                }
                PdfPCell celija4 = new PdfPCell(new Phrase(cijenaPojedinacno, font));
                celija4.setRowspan(1);
                celija4.setCalculatedHeight(10);
                celija4.setColspan(1);
                celija4.setVerticalAlignment(Element.ALIGN_CENTER);
                celija4.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(celija4);

                Long brojProdatihKarata = new Long(0);
                if (!karte.isEmpty()) {
                    brojProdatihKarata = karte.stream().count();
                }
                PdfPCell celija6 = new PdfPCell(new Phrase(brojProdatihKarata.toString(), font));
                celija6.setRowspan(1);
                celija6.setCalculatedHeight(10);
                celija6.setColspan(1);
                celija6.setVerticalAlignment(Element.ALIGN_CENTER);
                celija6.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(celija6);

                String cijenaUkupno = "";
                if (!karte.isEmpty()) {
                    cijenaUkupno = String.format("%.2f", karte.stream().mapToDouble(e -> (double) e.getIznos()).sum());
                } else {
                    cijenaUkupno = "--";
                }
                PdfPCell celija5 = new PdfPCell(new Phrase(cijenaUkupno, font));
                celija5.setRowspan(1);
                celija5.setCalculatedHeight(10);
                celija5.setColspan(1);
                celija5.setVerticalAlignment(Element.ALIGN_CENTER);
                celija5.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(celija5);
            }
        };

        PdfPCell celija7 = new PdfPCell(new Phrase("Repertoar", font));
        celija7.setColspan(1);
        celija7.setRowspan(1);
        celija7.setCalculatedHeight(10);
        celija7.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celija7.setVerticalAlignment(Element.ALIGN_CENTER);
        celija7.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(celija7);

        PdfPCell celija8 = new PdfPCell(new Phrase("Naziv predstave", font));
        celija8.setColspan(1);
        celija8.setRowspan(1);
        celija8.setCalculatedHeight(10);
        celija8.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celija8.setVerticalAlignment(Element.ALIGN_CENTER);
        celija8.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(celija8);

        PdfPCell celija9 = new PdfPCell(new Phrase("Cijena karte", font));
        celija9.setColspan(1);
        celija9.setRowspan(1);
        celija9.setCalculatedHeight(10);
        celija9.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celija9.setVerticalAlignment(Element.ALIGN_CENTER);
        celija9.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(celija9);

        PdfPCell celija10 = new PdfPCell(new Phrase("Broj prodatih karata", font));
        celija10.setColspan(1);
        celija10.setRowspan(1);
        celija10.setCalculatedHeight(10);
        celija10.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celija10.setVerticalAlignment(Element.ALIGN_CENTER);
        celija10.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(celija10);

        PdfPCell celija11 = new PdfPCell(new Phrase("Ukupan iznos", font));
        celija11.setColspan(1);
        celija11.setRowspan(1);
        celija11.setCalculatedHeight(10);
        celija11.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celija11.setVerticalAlignment(Element.ALIGN_CENTER);
        celija11.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(celija11);

        listaRepertoara.forEach(r);
        document.add(table);

    }

}
