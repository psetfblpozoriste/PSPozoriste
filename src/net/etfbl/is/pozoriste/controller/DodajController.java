/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
/**
 * FXML Controller class
 *
 * @author djord
 */
public class DodajController implements Initializable {

     @FXML
    private TextField tfIme;

    @FXML
    private TextField tfPrezime;

    @FXML
    private TextField tfOpisPosla;

    @FXML
    private TextField tfJmb;

    @FXML
    private TextField tfStatusRadnika;

    @FXML
    private TextField tfKontakt;

    @FXML
    private TextField tfKorisnickoIme;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private TextArea taBiografija;

    @FXML
    private ComboBox<?> cmbTipRadnika;

    @FXML
    private Button bOdustani;

    @FXML
    private Button bPotvrdi;

    @FXML
    void odustaniOdUnosaRadnikaAction(ActionEvent event) {

    }

    @FXML
    void potvrdiUnosRadnikaAction(ActionEvent event) {
        
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
