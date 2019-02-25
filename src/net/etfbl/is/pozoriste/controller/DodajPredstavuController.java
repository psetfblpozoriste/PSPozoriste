/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.controller;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    @FXML
    private Button bNazad;

    @FXML
    void dodajAngazmanAction(ActionEvent event) {

    }

    @FXML
    void okAction(ActionEvent event) {

    }

    @FXML
    void nazadNaPregledPredstava(ActionEvent event) {
        Parent dodajZaposlenogView;
        try {
            dodajZaposlenogView = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/PregledPredstava.fxml"));

            Scene dodajZaposlenogScene = new Scene(dodajZaposlenogView);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(dodajZaposlenogScene);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(DodajRadnikaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*   if(domacaPredstava){
         labelPisac.setVisible(false);
         labelReziser.setVisible(false);
         labelGlumci.setVisible(false);
         textFieldPisac.setVisible(false);
         textFieldReziser.setVisible(false);
         textAreaGlumci.setVisible(false);
         }else{
         buttonDodajAngazman.setVisible(false);
         }*/
    }
}
