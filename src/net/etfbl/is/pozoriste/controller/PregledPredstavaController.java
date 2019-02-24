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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import net.etfbl.is.pozoriste.model.dao.mysql.GostujucaPredstavaDAO;
import net.etfbl.is.pozoriste.model.dto.GostujucaPredstava;
import net.etfbl.is.pozoriste.model.dto.Predstava;

/**
 * FXML Controller class
 *
 * @author djord
 */
public class PregledPredstavaController implements Initializable {

    @FXML // fx:id="tablePredstave"
    private TableView<Predstava> tablePredstave; // Value injected by FXMLLoader

    @FXML // fx:id="comboBoxTip"
    private ComboBox<String> comboBoxTip; // Value injected by FXMLLoader

    @FXML // fx:id="buttonDodaj"
    private Button buttonDodaj; // Value injected by FXMLLoader

    @FXML // fx:id="tableGostujucePredstave"
    private TableView<GostujucaPredstava> tableGostujucePredstave; // Value injected by FXMLLoader
    
    @FXML // fx:id="textAreaOpisPredstave"
    private TextArea textAreaOpisPredstave; // Value injected by FXMLLoader

    @FXML // fx:id="textAreaOpisGostujucePredstave"
    private TextArea textAreaOpisGostujucePredstave; // Value injected by FXMLLoader

    @FXML
    private TableColumn<Predstava, String> nazivPColumn;
    @FXML
    private TableColumn<Predstava, String> tipPColumn;
    @FXML
    private TableColumn<GostujucaPredstava, String> nazivGpColumn;
    @FXML
    private TableColumn<GostujucaPredstava, String> tipGpColumn;
    @FXML
    private TableColumn<GostujucaPredstava, String> pisacColumn;
    @FXML
    private TableColumn<GostujucaPredstava, String> reziserColumn;
    @FXML
    private TableColumn<Predstava, String> glumciColumn;
    
    public static ObservableList<Predstava> predstaveObservableList = FXCollections.observableArrayList();
    public static ObservableList<GostujucaPredstava> gostujucePredstaveObservableList = FXCollections.observableArrayList();
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gostujucePredstaveObservableList.addAll(GostujucaPredstavaDAO.gostujucePredstave());
    }    
    
    
    
    private void ubaciKoloneUTabeluRadnik(ObservableList radnici) {
        nazivGpColumn = new TableColumn("Naziv");
        nazivGpColumn.setCellValueFactory(new PropertyValueFactory<>("naziv"));

        tipGpColumn = new TableColumn("Tip");
        tipGpColumn.setCellValueFactory(new PropertyValueFactory<>("tip"));

        pisacColumn = new TableColumn("Pisac");
        pisacColumn.setCellValueFactory(new PropertyValueFactory<>("pisac"));

        reziserColumn = new TableColumn("Reziser");
        reziserColumn.setCellValueFactory(new PropertyValueFactory<>("reziser"));

        /*korisnickoImeColumn = new TableColumn("Korisnicko Ime");
        korisnickoImeColumn.setCellValueFactory(new PropertyValueFactory<>("korisnickoIme"));
        
        zanimanjeColumn = new TableColumn("Zanimanje");
        zanimanjeColumn.setCellValueFactory(new PropertyValueFactory<>("tipRadnika"));
        
        statusRadnikaColumn = new TableColumn("Status radnika");
        statusRadnikaColumn.setCellValueFactory(new PropertyValueFactory<>("statusRadnika"));

        radniciTableView.setItems(radniciObservableList);
        radniciTableView.getColumns().addAll(jmbColumn,imeColumn, prezimeColumn,zanimanjeColumn,kontaktColumn, korisnickoImeColumn,statusRadnikaColumn);
    */
    }
}
