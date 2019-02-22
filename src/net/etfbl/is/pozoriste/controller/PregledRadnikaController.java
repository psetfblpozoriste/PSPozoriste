/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.model.dao.mysql.BIletarDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.ConnectionPool;
import net.etfbl.is.pozoriste.model.dao.mysql.RadnikDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.UmjetnikDAO;
import net.etfbl.is.pozoriste.model.dto.Biletar;
import net.etfbl.is.pozoriste.model.dto.Radnik;
import net.etfbl.is.pozoriste.model.dto.Umjetnik;

/**
 * FXML Controller class
 *
 * @author djord
 */
public class PregledRadnikaController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TableView radniciTableView;

    @FXML
    private TableColumn<Radnik, String> imeColumn;

    @FXML
    private TableColumn<Radnik, String> prezimeColumn;

    @FXML
    private TableColumn<Radnik, String> opisPoslaColumn;

    @FXML
    private TableColumn<Radnik, String> jmbColumn;

    @FXML
    private TableColumn<Radnik, Boolean> statusRadnikaColumn;

    @FXML
    private TableColumn<Radnik, String> kontaktColumn;

    @FXML
    private TableColumn<Radnik, String> korisnickoImeColumn;

    @FXML
    private Button bDodaj;

    @FXML
    private Button bIzmjeni;

    @FXML
    private Button bPretrazi;

    @FXML
    private TextField tfPretraga;

    @FXML
    private Button bNazad;

    @FXML
    private TextArea taBiografija;

    public static ObservableList<Radnik> radniciObservaleList = FXCollections.observableArrayList();

    public static Radnik radnikIzPretrage;

    @FXML
    void dodajRadnikaAction(ActionEvent event) {
        try {
            Parent dodajRadnikaController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/DodajRadnika.fxml"));

            Scene dodajRadnikaScene = new Scene(dodajRadnikaController);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(dodajRadnikaScene);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void izmijeniRadnikaAction(ActionEvent event) {

    }

    @FXML
    void pretraziRadnikaAction(ActionEvent event) {
        // if (comboBox.getSelectionModel().isEmpty()) {
        //    upozorenjeComboBox();
        //    return;
        // }
        // if (comboBox.getSelectionModel().getSelectedItem().equals("Zaposleni")) {

        String jmbRegex = "\\d+";
        Pattern pattern = Pattern.compile(jmbRegex);
        if (pattern.matcher(tfPretraga.getText()).matches() && (tfPretraga.getText().length() == 13)) {
            radniciTableView.getColumns().clear();
            final String zaPorednje = tfPretraga.getText();
            Optional<Radnik> radnik = radniciObservaleList.stream().filter(e -> e.getJmb().equals(zaPorednje)).findFirst();
            ObservableList<Radnik> radnikIzPretrageObservableList = null;
            if (radnik.isPresent()) {
                radnikIzPretrageObservableList = FXCollections.observableArrayList();
                radnikIzPretrageObservableList.add(radnik.get());
                System.out.println("AAAAAAAAAAAAAA"+radnikIzPretrageObservableList);
                tabelaRadnika(radnikIzPretrageObservableList);
            }
        } else {
            upozorenjePretraga();
            return;
        }
        // }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //RadnikDAO.ubaciUTabeluRadnik();
        BIletarDAO.ubaciUTabeluRadnik();
        UmjetnikDAO.ubaciUTabeluRadnik();
        ubaciKoloneUTabeluRadnik(radniciObservaleList);
        radniciTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                //radniciTableView.getSelectionModel().clearSelection();
                Radnik radnik = (Radnik) radniciTableView.getSelectionModel().getSelectedItem();
                if (radnik instanceof Umjetnik) {
                    taBiografija.setText(((Umjetnik) radniciTableView.getSelectionModel().getSelectedItem()).getBiografija());
                } else {
                    taBiografija.setText("");
                }
            }
        });

    }

    public void tabelaRadnika(ObservableList radnici) {
        ubaciKoloneUTabeluRadnik(radnici);
        radniciTableView.setItems(radnici);
        
    }

    private void ubaciKoloneUTabeluRadnik(ObservableList radnici) {
        imeColumn = new TableColumn("Ime");
        imeColumn.setCellValueFactory(new PropertyValueFactory<>("ime"));

        prezimeColumn = new TableColumn("Prezime");
        prezimeColumn.setCellValueFactory(new PropertyValueFactory<>("prezime"));

        opisPoslaColumn = new TableColumn("Opis posla");
        opisPoslaColumn.setCellValueFactory(new PropertyValueFactory<>("opisPosla"));

        jmbColumn = new TableColumn("JMB");
        jmbColumn.setCellValueFactory(new PropertyValueFactory<>("jmb"));

        kontaktColumn = new TableColumn("Kontakt");
        kontaktColumn.setCellValueFactory(new PropertyValueFactory<>("kontakt"));

        korisnickoImeColumn = new TableColumn("Korisnicko Ime");
        korisnickoImeColumn.setCellValueFactory(new PropertyValueFactory<>("korisnickoIme"));

        radniciTableView.setItems(radniciObservaleList);
        radniciTableView.getColumns().addAll(imeColumn, prezimeColumn, opisPoslaColumn, jmbColumn, kontaktColumn, korisnickoImeColumn);

        //statusRadnikaColumn = new TableColumn("Status radnika");
        // statusRadnikaColumn.setCellValueFactory(new PropertyValueFactory<>("statusRadnika"));
    }

    @FXML
    void idiNazadNaAdminForm(ActionEvent event) {
        try {
            Parent adminController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/Admin.fxml"));

            Scene dodajRadnikaScene = new Scene(adminController);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(dodajRadnikaScene);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void upozorenjePretraga() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom unosa podataka !");
        alert.setHeaderText(null);
        alert.setContentText("Provjerite polja za pretragu po JMB-u.");
        alert.showAndWait();
    }

}
