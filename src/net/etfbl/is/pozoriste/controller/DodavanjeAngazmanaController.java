/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.model.dao.mysql.AngazmanDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.UmjetnikDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.VrstaAngazmanaDAO;
import net.etfbl.is.pozoriste.model.dto.Angazman;
import net.etfbl.is.pozoriste.model.dto.Predstava;
import net.etfbl.is.pozoriste.model.dto.Umjetnik;
import net.etfbl.is.pozoriste.model.dto.VrstaAngazmana;

/**
 * FXML Controller class
 *
 * @author Tanja
 */
public class DodavanjeAngazmanaController implements Initializable {

    @FXML
    private ComboBox<String> comboBoxVrstaAngazmana;

    @FXML
    private ComboBox<String> comboBoxUmjetnik;

    @FXML
    private DatePicker datePickerDatumOd;

    @FXML
    private DatePicker datePickerDatumDo;

    @FXML
    private TableView<Angazman> tableAngazmani;

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
    private TableColumn<Angazman, String> imeColumn;
    @FXML
    private TableColumn<Angazman, String> prezimeColumn;
    @FXML
    private TableColumn<Angazman, String> vrstaAngazmanaColumn;
    @FXML
    private TableColumn<Angazman, Date> datumOdColumn;
    @FXML
    private TableColumn<Angazman, Date> datumDoColumn;

    
    public static ObservableList<Angazman> angazmani = FXCollections.observableArrayList();
    public static ObservableList<Umjetnik> umjetnici = FXCollections.observableArrayList();
    public static ObservableList<VrstaAngazmana> vrste = FXCollections.observableArrayList();
    private boolean izmjena=false;
    private static Predstava predstava;
    public static void setPredstava(Predstava p){
        predstava=p;
    }
    
    private void ubaciKoloneUTabeluAngazmana(ObservableList angazmani) {
        imeColumn = new TableColumn("Ime");
        imeColumn.setCellValueFactory(new PropertyValueFactory<>("ime"));

        prezimeColumn = new TableColumn("Prezime");
        prezimeColumn.setCellValueFactory(new PropertyValueFactory<>("prezime"));

        vrstaAngazmanaColumn = new TableColumn("Vrsta angazmana");
        vrstaAngazmanaColumn.setCellValueFactory(new PropertyValueFactory<>("vrstaAngazmana"));

        datumOdColumn = new TableColumn("Datum od");
        datumOdColumn.setCellValueFactory(new PropertyValueFactory<>("datumOd"));
        
        datumDoColumn = new TableColumn("Datum do");
        datumDoColumn.setCellValueFactory(new PropertyValueFactory<>("datumDo"));

        
        tableAngazmani.setItems(angazmani);
        tableAngazmani.getColumns().addAll(imeColumn,prezimeColumn,vrstaAngazmanaColumn,datumOdColumn,datumDoColumn);
    }
    
    @FXML
    void dodajAction(ActionEvent event) {

    }

    @FXML
    void izmijeniAction(ActionEvent event) {
        izmjena=true;
        
        labelDatumDo.setVisible(true);
        datePickerDatumDo.setVisible(true);
        
        labelUmjetnik.setVisible(false);
        comboBoxUmjetnik.setVisible(false);
        labelVrstaAngazmana.setVisible(false);
        comboBoxVrstaAngazmana.setVisible(false);
        labelDatumOd.setVisible(false);
        datePickerDatumOd.setVisible(false);
        
    }

    @FXML
    void dodajVrstuAngazmanaAction(ActionEvent event) {
        
    }
    
    @FXML
    void okAction(ActionEvent event) {
        try {
            Parent predstavaController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/PregledPredstava.fxml"));

            Scene predstavaScene = new Scene(predstavaController);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(predstavaScene);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(DodavanjeAngazmanaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelDatumDo.setVisible(false);
        datePickerDatumDo.setVisible(false);
        
        umjetnici.addAll(UmjetnikDAO.umjetnici());
        ObservableList<String> pomocno = FXCollections.observableArrayList();
        for(Umjetnik u:umjetnici){
            String pom=u.getIme()+" "+u.getPrezime();
            pomocno.add(pom);
        }
        comboBoxUmjetnik.getItems().removeAll(comboBoxUmjetnik.getItems());
        comboBoxUmjetnik.setItems(pomocno);
        
        vrste.addAll(VrstaAngazmanaDAO.vrsteAngazmana());
        ObservableList<String> pomocni = FXCollections.observableArrayList();
        for(VrstaAngazmana v:vrste){
            pomocni.add(v.getNaziv());
        }
        comboBoxVrstaAngazmana.getItems().removeAll(comboBoxVrstaAngazmana.getItems());
        comboBoxVrstaAngazmana.setItems(pomocni);
        
        angazmani.addAll(AngazmanDAO.angazmani(predstava));
        ubaciKoloneUTabeluAngazmana(angazmani);
        
    }    
    
}
