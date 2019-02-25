/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Tanja
 */
public class DodavanjeAngazmanaController implements Initializable {

    @FXML
    private ComboBox<?> comboBoxVrstaAngazmana;

    @FXML
    private ComboBox<?> comboBoxUmjetnik;

    @FXML
    private DatePicker datePickerDatumOd;

    @FXML
    private DatePicker datePickerDatumDo;

    @FXML
    private TableView<?> tableAngazmani;

    @FXML
    private Button buttonDodaj;

    @FXML
    private Button buttonOK;

    @FXML
    private Button buttonIzmijeni;

    @FXML
    private Label labelVrstaAngazmana;

    @FXML
    private Label labelDatumDo;

    @FXML
    private Label labelDatumOd;

    @FXML
    private Label labelUmjetnik;
    
    @FXML
    private Button buttonDodajVrstuAngazmana;

    @FXML
    void dodajAction(ActionEvent event) {

    }

    @FXML
    void izmijeniAction(ActionEvent event) {

    }

    @FXML
    void dodajVrstuAngazmanaAction(ActionEvent event) {

    }
    
    @FXML
    void okAction(ActionEvent event) {

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
