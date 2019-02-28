/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.model.dao.mysql.RepertoarDAO;
import net.etfbl.is.pozoriste.model.dto.Repertoar;
import java.util.Calendar;
import net.etfbl.is.pozoriste.model.dao.mysql.AzuriranjeDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.KreiranjeDAO;
import net.etfbl.is.pozoriste.model.dto.Azuriranje;
import net.etfbl.is.pozoriste.model.dto.Kreiranje;

/**
 * FXML Controller class
 *
 * @author djord
 */
public class DodajRepertoarController implements Initializable {

    @FXML
    private Label lTerminRepertoara;

    @FXML
    private Button bDodajIgranje;

    @FXML
    private ComboBox<Integer> cmbGodina;

    @FXML
    private ComboBox<Integer> cmbMjesec;

    @FXML
    private Button bNazad;

    public static Repertoar repertoar = null;
    public static Integer mjesecRepertoara;
    public static Integer godinaRepertoara;

    private boolean dodajRepertoar() {

        if (cmbGodina.getSelectionModel().isEmpty() || cmbMjesec.getSelectionModel().isEmpty()) {
            upozorenjeTermin();
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            Integer mjesec = (cmbMjesec.getSelectionModel().getSelectedItem() + 1);
            Integer godina = cmbGodina.getSelectionModel().getSelectedItem();
            mjesecRepertoara = mjesec;
            godinaRepertoara = godina;

            if (!PregledSvihRepertoaraController.izmjenaRepertoara) {
                repertoar = new Repertoar(0,
                        new java.sql.Date(sdf.parse(cmbGodina.getSelectionModel().getSelectedItem().toString()
                                        + "-" + Integer.valueOf(cmbMjesec.getSelectionModel().getSelectedItem()).toString() + "-1").getTime()));
            } else {
                repertoar = new Repertoar();
                repertoar.setId(PregledSvihRepertoaraController.izabraniRepertoar.getId());
                repertoar.setMjesecIGodina(new java.sql.Date(sdf.parse(cmbGodina.getSelectionModel().getSelectedItem().toString()
                        + "-" + Integer.valueOf(cmbMjesec.getSelectionModel().getSelectedItem()).toString() + "-1").getTime()));
                repertoar.setIgranja(PregledSvihRepertoaraController.izabraniRepertoar.getIgranja());
                System.out.println("IZMJENJENI REPERTOAR: " + repertoar);
            }
        } catch (ParseException ex) {
            Logger.getLogger(DodajRepertoarController.class.getName()).log(Level.SEVERE, null, ex);
        }

        final Repertoar simo = repertoar;

        if (!PregledSvihRepertoaraController.izmjenaRepertoara) {
            if (!PregledSvihRepertoaraController.repertoariObservableList.stream().filter(x -> sdf.format(x.getMjesecIGodina())
                    .equals(sdf.format(simo.getMjesecIGodina()))).findAny().isPresent()) {
                //if (!PregledSvihRepertoaraController.izmjenaRepertoara) {
                RepertoarDAO.dodajRepertoar(repertoar);
                Kreiranje kreiranje = new Kreiranje(null, repertoar.getId(), null, LogInController.idLogovanog);
                
                KreiranjeDAO.dodajKreiranje(kreiranje);
                return true;
                // } //else {
                //  RepertoarDAO.izmjeniRepertoar(repertoar);
                //  return true;
                //  }
            } //else 
            else {
                upozorenjeRepertoar();
                return false;
            }
        } else {
            //  if (sdf.format(simo.getMjesecIGodina()).equals(sdf.format(PregledSvihRepertoaraController.izabraniRepertoar.getMjesecIGodina()))) {
            RepertoarDAO.izmjeniRepertoar(repertoar);
            Azuriranje azuriranje = new Azuriranje(null, repertoar.getId(), null, LogInController.idLogovanog);
            AzuriranjeDAO.dodajAzuriranje(azuriranje);
            return true;
           // } else if (!PregledSvihRepertoaraController.repertoariObservableList.stream().filter(x -> sdf.format(x.getMjesecIGodina())
            //         .equals(sdf.format(simo.getMjesecIGodina()))).findAny().isPresent()) {
            //     RepertoarDAO.izmjeniRepertoar(repertoar);
            //     return true;
            //  } else {
            //     upozorenjeRepertoar();
            //    return false;
            //   }

        }
    }

    @FXML
    void dodajIgranjeAction(ActionEvent event) {

        if (dodajRepertoar()) {

            try {
                Parent adminController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/DodajIgranje.fxml"));

                Scene dodajRadnikaScene = new Scene(adminController);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(dodajRadnikaScene);
                window.show();
            } catch (IOException ex) {
                Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            return;
        }
    }

    @FXML
    void nazadaNaPregledSvihRepertoara(ActionEvent event) {
        try {
            Parent adminController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/PregledSvihRepertoara.fxml"));

            Scene dodajRadnikaScene = new Scene(adminController);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(dodajRadnikaScene);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //for (Integer year = 1950; year <= Calendar.getInstance().get(Calendar.YEAR); year++) {
        // cmbGodina.getItems().add(year);
        cmbGodina.getItems().add(Calendar.getInstance().get(Calendar.YEAR));
        //}
        // for (Integer month = 1; month <= 12; month++) {
        for (Integer mjesec = (Calendar.getInstance().get(Calendar.MONTH) + 1); mjesec <= 12; mjesec++) {
            cmbMjesec.getItems().add(mjesec);
        }
        if (PregledSvihRepertoaraController.izmjenaRepertoara) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            String datum = sdf.format(PregledSvihRepertoaraController.izabraniRepertoar.getMjesecIGodina());
            String godinaIzabranogRepertoara = datum.split("-")[0];
            String mjesecIzabranogRepertoara = datum.split("-")[1];

            cmbGodina.getSelectionModel().select(Integer.valueOf(godinaIzabranogRepertoara));
            cmbMjesec.getSelectionModel().select(Integer.valueOf(mjesecIzabranogRepertoara));
            cmbMjesec.getItems().clear();
            cmbMjesec.getItems().add(Integer.valueOf(mjesecIzabranogRepertoara));
            cmbMjesec.getSelectionModel().select(Integer.valueOf(mjesecIzabranogRepertoara));

        }

    }

    private void upozorenjeRepertoar() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom dodavanja repertoara !");
        alert.setHeaderText(null);
        alert.setContentText("Repertoar postoji u bazi");
        alert.showAndWait();
    }

    private void upozorenjeTermin() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom dodavanja repertoara !");
        alert.setHeaderText(null);
        alert.setContentText("Izaberite termin");
        alert.showAndWait();
    }

}
