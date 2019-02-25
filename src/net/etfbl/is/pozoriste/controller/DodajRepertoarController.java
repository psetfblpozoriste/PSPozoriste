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

   // @FXML
    //private DatePicker dpTerminRepertoara;
    
     @FXML
    private ComboBox<Integer> cmbGodina;

    @FXML
    private ComboBox<Integer> cmbMjesec;

    @FXML
    private Button bNazad;

    private boolean dodajRepertoar() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
        Calendar calendar = Calendar.getInstance();
        //calendar.set(dpTerminRepertoara.getValue().getYear(), dpTerminRepertoara.getValue().getMonthValue() - 1, dpTerminRepertoara.getValue().getDayOfMonth());
       // calendar.set(cmbGodina.getSelectionModel().getSelectedItem(), cmbDan.getSelectionModel().getSelectedItem(), 1);
        // sdf.format(calendar.getTimeInMillis());
        Repertoar repertoar = null;
        try {
            repertoar = new Repertoar(0,
    new java.sql.Date(sdf.parse(cmbGodina.getSelectionModel().getSelectedItem().toString()+"-"+cmbMjesec.getSelectionModel().getSelectedItem().toString()+"-1").getTime()));
        } catch (ParseException ex) {
            Logger.getLogger(DodajRepertoarController.class.getName()).log(Level.SEVERE, null, ex);
        }
       //  new java.sql.Date(d.getTime());
        System.out.println("REPERTOAR: " + repertoar);
        final Repertoar simo = repertoar;
        if (!PregledSvihRepertoaraController.repertoariObservableList.stream().filter(e -> e.getMjesecIGodina().getYear() == simo.getMjesecIGodina().getYear() && e.getMjesecIGodina().getMonth() == simo.getMjesecIGodina().getMonth() ).findAny().isPresent()) {
            RepertoarDAO.dodajRepertoar(repertoar);
            return true;
        } else {
            upozorenjeRepertoar();
            return false;
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
       for (Integer year = 1950; year <= Calendar.getInstance().get(Calendar.YEAR); year++) {
            //cmbGodina.getItems().add(year.intValue());
           cmbGodina.getItems().add(year);
        }
        for (Integer month = 1; month <= 12; month++) {
            //cmbMjesac.getItems().add(month.intValue());  }
            cmbMjesec.getItems().add(month);}
    }

    private void upozorenjeRepertoar() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom dodavanja repertoara !");
        alert.setHeaderText(null);
        alert.setContentText("Repertoar postoji u bazi");
        alert.showAndWait();
    }

}
