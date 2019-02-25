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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author Tanja
 */
public class DodajPredstavuController implements Initializable {

    
    @FXML
    private Label labelNaziv;

    @FXML
    private Label labelOpis;

    @FXML
    private Label labelTip;

    @FXML
    private Label labelPisac;

    @FXML
    private Label labelReziser;

    @FXML
    private Label labelGlumci;

    @FXML
    private TextField textFieldNaziv;

    @FXML
    private TextArea textAreaOpis;

    @FXML
    private TextField textFieldTip;

    @FXML
    private TextField textFieldPisac;

    @FXML
    private TextField textFieldReziser;

    @FXML
    private TextArea textAreaGlumci;

    @FXML
    private Button buttonDodajAngazman;

    @FXML
    private Button buttonOK;
    
    
    private static boolean domacaPredstava;
    
    public static void setDomacaPredstava(boolean domacaPr){
        domacaPredstava=domacaPr;
    }

    @FXML
    void dodajAngazmanAction(ActionEvent event) {

    }

    @FXML
    void okAction(ActionEvent event) {

    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         if(domacaPredstava){
            labelPisac.setVisible(false);
            labelReziser.setVisible(false);
            labelGlumci.setVisible(false);
            textFieldPisac.setVisible(false);
            textFieldReziser.setVisible(false);
            textAreaGlumci.setVisible(false);
        }else{
            buttonDodajAngazman.setVisible(false);
        }
    }
}
