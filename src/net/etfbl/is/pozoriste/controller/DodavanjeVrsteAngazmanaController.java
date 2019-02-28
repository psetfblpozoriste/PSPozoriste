/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static net.etfbl.is.pozoriste.controller.DodavanjeAngazmanaController.vrste;
import net.etfbl.is.pozoriste.model.dao.mysql.VrstaAngazmanaDAO;
import net.etfbl.is.pozoriste.model.dto.VrstaAngazmana;

/**
 * FXML Controller class
 *
 * @author Tanja
 */
public class DodavanjeVrsteAngazmanaController implements Initializable {

    @FXML
    private Label labelVrstaAngazmana;

    @FXML
    private TextField textFieldNaziv;

    @FXML
    private Button buttonOk;

    
    
    @FXML
    void okAction(ActionEvent event) {
        VrstaAngazmanaDAO.dodajAngazman(textFieldNaziv.getText());
        ((Stage) buttonOk.getScene().getWindow()).close();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
