/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.LinkedList;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.model.dto.Igranje;

/**
 * FXML Controller class
 *
 * @author djord
 */
public class DodajIgranjeController implements Initializable {

    @FXML
    private ComboBox cmbPredstave;

    @FXML
    private DatePicker dpTerminPredstave;

    @FXML
    private Button bDodaj;

    @FXML
    private Button bZavrsi;

    @FXML
    private Button bNazad;

    @FXML
    void dodajIgranjeAction(ActionEvent event) {

    }

    /*
     Calendar calendar = Calendar.getInstance();
     calendar.set(datePickerBan.getValue().getYear(), datePickerBan.getValue().getMonthValue() - 1
     , datePickerBan.getValue().getDayOfMonth());
     */
    private boolean dodajIgranje() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(dpTerminPredstave.getValue().getYear(), dpTerminPredstave.getValue().getMonthValue() - 1, dpTerminPredstave.getValue().getDayOfMonth());

        LinkedList<Igranje> novoIgranje = new LinkedList<>();
        // posalji lisu igranja DodajRepertoarController.repertoar.setIgranja();
       // novoIgranje.add(calendar.getTimeInMillis(),);
        return false;
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
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
