/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.controller;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.model.dao.mysql.RezervacijaDAO;
import net.etfbl.is.pozoriste.model.dto.Rezervacija;

/**
 * FXML Controller class
 *
 * @author Ognjen
 */
public class DodajRezervacijuController implements Initializable {

    @FXML // fx:id="textFiled"
    private TextField textFiled; // Value injected by FXMLLoader

    @FXML // fx:id="buttonDodaj"
    private Button buttonDodaj; // Value injected by FXMLLoader

    @FXML // fx:id="buttonOdustani"
    private Button buttonOdustani; // Value injected by FXMLLoader

    public static Date termin;

    public static Integer idScene;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonOdustani.setOnAction(e -> ((Stage) buttonOdustani.getScene().getWindow()).close());
        buttonDodaj.setOnAction(e -> dodajButton());
    }

    private void dodajButton() {
        String ime = textFiled.getText();
        if (RezervacijaDAO.addRezervacija(new Rezervacija(0, ime, termin, idScene))) {
            ((Stage) buttonDodaj.getScene().getWindow()).close();
        }

    }

}
