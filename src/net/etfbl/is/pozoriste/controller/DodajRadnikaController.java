/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.model.dao.mysql.BIletarDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.ConnectionPool;
import net.etfbl.is.pozoriste.model.dao.mysql.UmjetnikDAO;
import net.etfbl.is.pozoriste.model.dto.Biletar;
import net.etfbl.is.pozoriste.model.dto.Radnik;
import net.etfbl.is.pozoriste.model.dto.Umjetnik;

/**
 * FXML Controller class
 *
 * @author djord
 */
public class DodajRadnikaController implements Initializable {

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
    private ComboBox cmbTipRadnika;

    @FXML
    private Button bOdustani;

    @FXML
    private Button bPotvrdi;

    @FXML
    private Label lIme;

    @FXML
    private Label lPrezime;

    @FXML
    private Label lOpisPosla;

    @FXML
    private Label lJmb;

    @FXML
    private Label lStatusRadnika;

    @FXML
    private Label lKontakt;

    @FXML
    private Label lKorisnickoIme;

    @FXML
    private Label lLozinka;

    @FXML
    private Label lBiografija;

    public void unesiZaposlenog() {
        if (cmbTipRadnika.getValue().equals("Biletar")) {
            if (!tfJmb.getText().isEmpty() && !tfIme.getText().isEmpty() && !tfPrezime.getText().isEmpty() && !tfOpisPosla.getText().isEmpty()
                    && !tfKontakt.getText().isEmpty() && !tfKorisnickoIme.getText().isEmpty() && !tfPassword.getText().isEmpty()
                    && (cmbTipRadnika.getSelectionModel().getSelectedItem() != null)) {

                String jmbRegex = "\\d+";
                Pattern pattern = Pattern.compile(jmbRegex);

                if (!pattern.matcher(tfJmb.getText()).matches() && tfJmb.getText().length() != 13) {
                    upozorenjeNeispravanJMB();
                    return;
                } else {
                    if (!provjeriMaticniBrojUBazi(tfJmb.getText())) {
                        upoorenjeMaticniBroj();
                        return;
                    }
                }

                Biletar biletar = new Biletar();

                biletar.setJmb(tfJmb.getText());
                biletar.setIme(tfIme.getText());
                biletar.setPrezime(tfPrezime.getText());
                biletar.setOpisPosla(tfOpisPosla.getText());
                biletar.setStatusRadnika(true);
                biletar.setKontak(tfKontakt.getText());
                biletar.setKorisnickoIme(tfKorisnickoIme.getText());
                biletar.setHash(tfPassword.getText());
                biletar.setTipKorisnika("Biletar");

                System.out.println("KONTAKT: " + tfKontakt.getText());
                BIletarDAO.dodajBiletara(biletar);

                tfIme.clear();
                tfPrezime.clear();
                tfJmb.clear();
                tfOpisPosla.clear();
                tfStatusRadnika.clear();
                tfKontakt.clear();
                tfKorisnickoIme.clear();
                tfPassword.clear();
            } else {
                upozorenjePoljaSuPrazna();

            }
        }
        if (cmbTipRadnika.getValue().equals("Umjetnik")) {
            System.out.println("USO U UMJETNIK");
            if (!tfJmb.getText().isEmpty() && !tfIme.getText().isEmpty() && !tfPrezime.getText().isEmpty() && !tfOpisPosla.getText().isEmpty()
                    && !tfKontakt.getText().isEmpty() && !taBiografija.getText().isEmpty() && (cmbTipRadnika.getSelectionModel().getSelectedItem() != null)) {

                String jmbRegex = "\\d+";
                Pattern pattern = Pattern.compile(jmbRegex);

                if (!pattern.matcher(tfJmb.getText()).matches() && tfJmb.getText().length() != 13) {
                    upozorenjeNeispravanJMB();
                    return;
                } else {
                    if (!provjeriMaticniBrojUBazi(tfJmb.getText())) {
                        upoorenjeMaticniBroj();
                        return;
                    }
                }

                Umjetnik umjetnik = new Umjetnik();

                umjetnik.setJmb(tfJmb.getText());
                umjetnik.setIme(tfIme.getText());
                umjetnik.setPrezime(tfPrezime.getText());
                umjetnik.setOpisPosla(tfOpisPosla.getText());
                umjetnik.setStatusRadnika(true);
                umjetnik.setKontak(tfKontakt.getText());
                umjetnik.setBiografija(taBiografija.getText());

                UmjetnikDAO.dodajUmjetnika(umjetnik);

                tfIme.clear();
                tfPrezime.clear();
                tfJmb.clear();
                tfOpisPosla.clear();
                tfStatusRadnika.clear();
                tfKontakt.clear();
                taBiografija.clear();
            } else {
                upozorenjePoljaSuPrazna();

            }
        }
    }

    @FXML
    void potvrdiUnosRadnikaAction(ActionEvent event) {
        if (cmbTipRadnika.getSelectionModel().isEmpty()) {
            upozorenjeComboBox();
            return;
        }
        unesiZaposlenog();
        Parent dodajZaposlenogView;
        try {
            dodajZaposlenogView = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/PregledRadnika.fxml"));

            Scene dodajZaposlenogScene = new Scene(dodajZaposlenogView);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(dodajZaposlenogScene);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(DodajRadnikaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void odustaniOdUnosaRadnikaAction(ActionEvent event) {
        Parent dodajZaposlenogView;
        try {
            dodajZaposlenogView = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/PregledRadnika.fxml"));

            Scene dodajZaposlenogScene = new Scene(dodajZaposlenogView);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(dodajZaposlenogScene);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(DodajRadnikaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initializes the controller class.
     */
    private void sakriPolja() {
        tfIme.setVisible(false);
        tfPrezime.setVisible(false);
        tfOpisPosla.setVisible(false);
        tfJmb.setVisible(false);
        tfStatusRadnika.setVisible(false);
        tfKontakt.setVisible(false);
        tfKorisnickoIme.setVisible(false);
        tfPassword.setVisible(false);
        taBiografija.setVisible(false);

        lIme.setVisible(false);
        lPrezime.setVisible(false);
        lOpisPosla.setVisible(false);
        lJmb.setVisible(false);
        lStatusRadnika.setVisible(false);
        lKontakt.setVisible(false);
        lKorisnickoIme.setVisible(false);
        lLozinka.setVisible(false);
        lBiografija.setVisible(false);
    }

    @FXML
    private void prikazPoljaNaOsnovuTipaRadnikaIzKomboBoksa() {
        sakriPolja();
        System.out.println("LLLLL L L L: " + cmbTipRadnika.getValue().toString());
        if (cmbTipRadnika.getValue().toString().equals("Biletar")) {
            tfIme.setVisible(true);
            tfPrezime.setVisible(true);
            tfOpisPosla.setVisible(true);
            tfJmb.setVisible(true);
            tfStatusRadnika.setVisible(true);
            tfKontakt.setVisible(true);
            tfKorisnickoIme.setVisible(true);
            tfPassword.setVisible(true);

            lIme.setVisible(true);
            lPrezime.setVisible(true);
            lOpisPosla.setVisible(true);
            lJmb.setVisible(true);
            lStatusRadnika.setVisible(true);
            lKontakt.setVisible(true);
            lKorisnickoIme.setVisible(true);
            lLozinka.setVisible(true);
        } else if (cmbTipRadnika.getValue().toString().equals("Administrativni radnik")) {
            tfIme.setVisible(true);
            tfPrezime.setVisible(true);
            tfOpisPosla.setVisible(true);
            tfJmb.setVisible(true);
            tfStatusRadnika.setVisible(true);
            tfKontakt.setVisible(true);

            lIme.setVisible(true);
            lPrezime.setVisible(true);
            lOpisPosla.setVisible(true);
            lJmb.setVisible(true);
            lStatusRadnika.setVisible(true);
            lKontakt.setVisible(true);
        } else if (cmbTipRadnika.getValue().toString().equals("Umjetnik")) {
            tfIme.setVisible(true);
            tfPrezime.setVisible(true);
            tfOpisPosla.setVisible(true);
            tfJmb.setVisible(true);
            tfStatusRadnika.setVisible(true);
            tfKontakt.setVisible(true);
            taBiografija.setVisible(true);

            lIme.setVisible(true);
            lPrezime.setVisible(true);
            lOpisPosla.setVisible(true);
            lJmb.setVisible(true);
            lStatusRadnika.setVisible(true);
            lKontakt.setVisible(true);
            lBiografija.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sakriPolja();
        cmbTipRadnika.getItems().addAll("Biletar", "Umjetnik");

    }

    private boolean provjeriMaticniBrojUBazi(String jmb) {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        String pomJMB = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT jmb from radnik where jmb=" + jmb);
            while (rs.next()) {
                pomJMB = rs.getString(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DodajRadnikaController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DodajRadnikaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return pomJMB == null;
    }

    private void upozorenjeNeispravanJMB() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom unosa JMB !");
        alert.setHeaderText(null);
        alert.setContentText("Provjerite da li JMB ima 13 karaktera.");
        alert.showAndWait();
    }

    private void upoorenjeMaticniBroj() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom unosa podataka !");
        alert.setHeaderText(null);
        alert.setContentText("Uneseni JMB vec postoji u bazi podataka.");
        alert.showAndWait();
    }

    private void upozorenjePoljaSuPrazna() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom unosa podataka !");
        alert.setHeaderText(null);
        alert.setContentText("Polja su prazna.");
        alert.showAndWait();
    }

    private void upozorenjeComboBox() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom odabira u combobox-u !");
        alert.setHeaderText(null);
        alert.setContentText("Izaberite iz padajuceg menija u combobox-u");
        alert.showAndWait();
    }

}
