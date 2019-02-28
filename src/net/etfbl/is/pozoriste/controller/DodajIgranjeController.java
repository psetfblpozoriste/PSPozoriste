/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.model.dao.mysql.ConnectionPool;
import net.etfbl.is.pozoriste.model.dao.mysql.GostujucaPredstavaDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.IgranjeDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.PredstavaDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.RepertoarDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.ScenaDAO;
import net.etfbl.is.pozoriste.model.dto.GostujucaPredstava;
import net.etfbl.is.pozoriste.model.dto.Igranje;
import net.etfbl.is.pozoriste.model.dto.Predstava;
import net.etfbl.is.pozoriste.model.dto.Repertoar;
import net.etfbl.is.pozoriste.model.dto.Scena;

/**
 * FXML Controller class
 *
 * @author djord
 */
public class DodajIgranjeController implements Initializable {

    @FXML
    private ComboBox<Object> cmbPredstave;

    @FXML
    private DatePicker dpTerminPredstave;

    @FXML
    private Button bDodaj;

    @FXML
    private Button bZavrsi;

    @FXML
    private Button bUkloni;

    @FXML
    private ComboBox<Object> cmbIgranjaZaRepertoar;
    @FXML
    private Label lUkloniIgranje;

    @FXML
    void ukloniPredstavuAction(ActionEvent event) {
        if (!cmbIgranjaZaRepertoar.getSelectionModel().isEmpty()) {
            Predstava predstavaIzCmb = (Predstava) cmbIgranjaZaRepertoar.getSelectionModel().getSelectedItem();
            System.out.println("IGRANJE CMB: " + predstavaIzCmb);
            LinkedList<Igranje> igranjeIzCmb = new LinkedList<>();
            igranjeIzCmb.addAll(PregledSvihRepertoaraController.izabraniRepertoar.getIgranja().stream().filter(x -> x.getIdPredstave().equals(predstavaIzCmb.getId())).collect(Collectors.toList()));

            IgranjeDAO.UkloniIgranje(igranjeIzCmb.getFirst());
            obavjestenjePredstavaUspjesnoUklonjena();
        } else {
            upozorenjeIzaberitePredstavuZaBrisati();
        }
    }

    @FXML
    void dodajIgranjeAction(ActionEvent event) {
        if (!dodajIgranje()) {
            return;
        }
    }

    private boolean dodajIgranje() {

        if (cmbPredstave.getSelectionModel().isEmpty()) {
            upozorenjeIzaberitePredstavu();
            return false;
        }

        if (dpTerminPredstave.getValue() == null) {
            upozorenjeTermin();
            return false;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(dpTerminPredstave.getValue().getYear(), dpTerminPredstave.getValue().getMonthValue() - 1, dpTerminPredstave.getValue().getDayOfMonth());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar kalendarRepertoar = Calendar.getInstance();
        if (dpTerminPredstave.getValue().getYear() != DodajRepertoarController.godinaRepertoara
                || ((dpTerminPredstave.getValue().getMonthValue()) != (DodajRepertoarController.mjesecRepertoara - 1))) {
            upozorenjeTerminPredstave();
            return false;
        }

        sdf.format(new Date(calendar.getInstance().getTimeInMillis()));
        List<Scena> scena = ScenaDAO.scene();

        Igranje novoIgranje = new Igranje(new Date(calendar.getTimeInMillis()), scena.get(0).getIdScene(), (cmbPredstave.getSelectionModel().getSelectedItem() instanceof Predstava) ? ((Predstava) cmbPredstave.getSelectionModel().getSelectedItem()).getId() : null, (cmbPredstave.getSelectionModel().getSelectedItem() instanceof GostujucaPredstava) ? ((GostujucaPredstava) cmbPredstave.getSelectionModel().getSelectedItem()).getId() : null, DodajRepertoarController.repertoar.getId());
        LinkedList<Igranje> svaIgranja = new LinkedList<>();
        svaIgranja = IgranjeDAO.getIgranja(DodajRepertoarController.repertoar.getId());

        if (!svaIgranja.stream().filter(x -> sdf.format(x.getTermin()).equals(sdf.format(novoIgranje.getTermin()))).findAny().isPresent()) {
            IgranjeDAO.dodajIgranje(novoIgranje);
            obavjestenjePredstavaUspjesnoDodata();
            return true;

        } else {
            upozorenjePredstavaSeVecIgraNaTajDan();
            return false;
        }
    }

    @FXML
    void nazadNaDodajRepertoar(ActionEvent event) {
        try {
            Parent adminController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/DodajRepertoar.fxml"));

            Scene dodajRadnikaScene = new Scene(adminController);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(dodajRadnikaScene);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void zavrsiDodavanjeRepertoara(ActionEvent event) {
        try {
            Parent adminController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/PregledSvihRepertoara.fxml"));

            Scene dodajRadnikaScene = new Scene(adminController);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(dodajRadnikaScene);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(DodajIgranjeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void ubaciUCMBPredstave() {
        
        
        cmbPredstave.getItems().addAll(PredstavaDAO.predstave());
        cmbPredstave.getItems().addAll(GostujucaPredstavaDAO.gostujucePredstave());
    }
    
   // private void magija(Igranje ){
   //     HashMap<Predstava,Date> mapa = new HashMap<>();
   //     HashMap<GostujucaPredstava,Date> mapaG = new HashMap<>();
  //  }

    private void ubaciUCMBIgranjaZaRepertoar() {

        PregledSvihRepertoaraController.izabraniRepertoar.getIgranja().stream().forEach(System.out::println);

        final LinkedList<Predstava> predstave = new LinkedList<>();
        final LinkedList<GostujucaPredstava> gostujuce = new LinkedList<>();
        List<Integer> nadjiOBicne = PregledSvihRepertoaraController.izabraniRepertoar.getIgranja().stream().mapToInt(e -> (e.getIdPredstave() != 0 ? e.getIdPredstave() : 0)).boxed().collect(Collectors.toList());
        List<Integer> nadjiGostujuce = PregledSvihRepertoaraController.izabraniRepertoar.getIgranja().stream().mapToInt(e -> (e.getIdGostujucePredstave() != 0 ? e.getIdGostujucePredstave() : 0)).boxed().collect(Collectors.toList());
        Long obicneBroj = nadjiOBicne.stream().count();
        Long gostujuceBroj = nadjiGostujuce.stream().count();
        nadjiOBicne.forEach(e -> {
            Optional<Predstava> op = PredstavaDAO.predstave().stream().filter(p -> p.getId() == e).findFirst();
            if (op.isPresent()) {
                predstave.add(op.get());
            }
        });
        nadjiGostujuce.forEach(e -> {
            Optional<GostujucaPredstava> op = GostujucaPredstavaDAO.gostujucePredstave().stream().filter(p -> p.getId() == e).findFirst();
            if (op.isPresent()) {
                gostujuce.add(op.get());
            }
        });

        cmbIgranjaZaRepertoar.getItems().addAll(predstave);
        cmbIgranjaZaRepertoar.getItems().addAll(gostujuce);

    }

    private void obavjestenjePredstavaUspjesnoDodata() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Uspjesno uklanjanje predstave!");
        alert.setHeaderText(null);
        alert.setContentText("Uspjesno dodana predstave!");
        alert.showAndWait();
    }

    private void obavjestenjePredstavaUspjesnoUklonjena() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Uspjesno uklanjanje predstave!");
        alert.setHeaderText(null);
        alert.setContentText("Uspjesno uklanjanje predstave!");
        alert.showAndWait();
    }

    private void upozorenjePredstavaSeVecIgraNaTajDan() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom dodavanja predstave !");
        alert.setHeaderText(null);
        alert.setContentText("Termin popunjen izaberite drugi!");
        alert.showAndWait();
    }

    private void upozorenjeTerminPredstave() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom dodavanja predstave !");
        alert.setHeaderText(null);
        alert.setContentText("Pogresan termin , prilagodite igranje predstave odgovarajucem repertoaru!");
        alert.showAndWait();
    }

    private void upozorenjeTermin() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom izbora termina !");
        alert.setHeaderText(null);
        alert.setContentText("Izaberite termin");
        alert.showAndWait();
    }

    private void upozorenjeIzaberitePredstavu() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom izbora predstave !");
        alert.setHeaderText(null);
        alert.setContentText("Izaberite predstavu!");
        alert.showAndWait();
    }

    private void upozorenjeIzaberitePredstavuZaBrisati() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom izbora predstave !");
        alert.setHeaderText(null);
        alert.setContentText("Izaberite predstavu za brisanje!");
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ubaciUCMBPredstave();
        lUkloniIgranje.setVisible(false);
        cmbIgranjaZaRepertoar.setVisible(false);
        bUkloni.setVisible(false);
        if (PregledSvihRepertoaraController.izmjenaRepertoara) {
            lUkloniIgranje.setVisible(true);
            cmbIgranjaZaRepertoar.setVisible(true);
            bUkloni.setVisible(true);
            ubaciUCMBIgranjaZaRepertoar();
        }
    }

}
