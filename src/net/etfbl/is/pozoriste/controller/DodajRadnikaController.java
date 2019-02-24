/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.CallableStatement;
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
import static net.etfbl.is.pozoriste.controller.LogInController.tipKorisnika;
import net.etfbl.is.pozoriste.model.dao.mysql.AdministratorDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.BIletarDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.ConnectionPool;
import net.etfbl.is.pozoriste.model.dao.mysql.UmjetnikDAO;
import net.etfbl.is.pozoriste.model.dto.AdministrativniRadnik;
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
    private TextField tfJmb;

    @FXML
    private TextField tfKontakt;

    @FXML
    private TextField tfKorisnickoIme;
    @FXML
    private TextField tfNovaLozinka;

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
    @FXML
    private Label lNovaLozinka;
    @FXML
    private ComboBox cmbStatusRadnika;

    private boolean dodajAdministrativnogRadnika() {
        if (PregledRadnikaController.dodajRadnika) {
            if (cmbTipRadnika.getSelectionModel().getSelectedItem() == null) {
                upozorenjeComboBox();
                return false;
            }
        }
        if (!tfJmb.getText().isEmpty() && !tfIme.getText().isEmpty() && !tfPrezime.getText().isEmpty()
                && !tfKontakt.getText().isEmpty() && !tfKorisnickoIme.getText().isEmpty() && !tfPassword.getText().isEmpty()) {

            String jmbRegex = "\\d+";
            Pattern pattern = Pattern.compile(jmbRegex);

            if (!pattern.matcher(tfJmb.getText()).matches() && tfJmb.getText().length() != 13) {
                upozorenjeNeispravanJMB();
                return false;
            } else {
                if (PregledRadnikaController.dodajRadnika) {
                    if (!provjeriMaticniBrojUBazi(tfJmb.getText())) {
                        upoorenjeMaticniBroj();
                        return false;
                    }
                }
            }
            if (tfIme.getText().length() > 40) {
                upozorenjePredugacakUnos();
                return false;
            }

            if (tfPrezime.getText().length() > 40) {
                upozorenjePredugacakUnos();
                return false;
            }

            if (tfKorisnickoIme.getText().length() > 20) {
                upozorenjePredugacakUnos();
                return false;
            }

            AdministrativniRadnik admin = new AdministrativniRadnik();
            if (!PregledRadnikaController.dodajRadnika) {
                admin = (AdministrativniRadnik) PregledRadnikaController.izabraniRadnik;
                admin.setIdRadnika(((AdministrativniRadnik) PregledRadnikaController.izabraniRadnik).getIdRadnika());
            }

            admin.setJmb(tfJmb.getText());
            admin.setIme(tfIme.getText());
            admin.setPrezime(tfPrezime.getText());
            String statusRadnika = "";
            if (PregledRadnikaController.dodajRadnika) {
                statusRadnika = "Aktivan";
            } else {
                statusRadnika = cmbStatusRadnika.getSelectionModel().getSelectedItem().toString();
            }
            if (statusRadnika.equals("Aktivan")) {
                admin.setStatusRadnika(true);
            } else {
                admin.setStatusRadnika(false);
            }
            admin.setKontak(tfKontakt.getText());
            if (postojiUBaziKorisnickoIme(tfKorisnickoIme.getText())) {
                admin.setKorisnickoIme(tfKorisnickoIme.getText());
            } else {
                return false;
            }
            admin.setHash(tfPassword.getText());
            admin.setTipRadnika("Administrator");
            if (PregledRadnikaController.dodajRadnika) {
                AdministratorDAO.dodajAdministrativnogRadnika(admin);
            } else {
                AdministratorDAO.izmjeniAdministratora(admin);
            }

        } else {
            upozorenjePoljaSuPrazna();
            return false;
        }

        return true;
    }

    private boolean dodajBiletara() {
        if (PregledRadnikaController.dodajRadnika) {
            if (cmbTipRadnika.getSelectionModel().getSelectedItem() == null) {
                upozorenjeComboBox();
                return false;
            }
        }
        if (!tfJmb.getText().isEmpty() && !tfIme.getText().isEmpty() && !tfPrezime.getText().isEmpty()
                && !tfKontakt.getText().isEmpty() && !tfKorisnickoIme.getText().isEmpty() && !tfPassword.getText().isEmpty()) {

            String jmbRegex = "\\d+";
            Pattern pattern = Pattern.compile(jmbRegex);

            if (!pattern.matcher(tfJmb.getText()).matches() && tfJmb.getText().length() != 13) {
                upozorenjeNeispravanJMB();
                return false;
            } else {
                if (PregledRadnikaController.dodajRadnika) {
                    if (!provjeriMaticniBrojUBazi(tfJmb.getText())) {
                        upoorenjeMaticniBroj();
                        return false;
                    }
                }
            }

            if (tfIme.getText().length() > 40) {
                upozorenjePredugacakUnos();
                return false;
            }

            if (tfPrezime.getText().length() > 40) {
                upozorenjePredugacakUnos();
                return false;
            }

            if (tfKorisnickoIme.getText().length() > 20) {
                upozorenjePredugacakUnos();
                return false;
            }

            String brojTelefona = "\\d+";
            pattern = Pattern.compile(brojTelefona);
            if (!pattern.matcher(tfKontakt.getText()).matches()) {
                upozorenjeBrjTelefona();
                return false;
            }

            Biletar biletar = new Biletar();
            if (!PregledRadnikaController.dodajRadnika) {
                biletar = (Biletar) PregledRadnikaController.izabraniRadnik;
                biletar.setIdRadnika(((Biletar) PregledRadnikaController.izabraniRadnik).getIdRadnika());
            }

            biletar.setJmb(tfJmb.getText());
            biletar.setIme(tfIme.getText());
            biletar.setPrezime(tfPrezime.getText());
            String statusRadnika = "";
            if (PregledRadnikaController.dodajRadnika) {
                statusRadnika = "Aktivan";
            } else {
                statusRadnika = cmbStatusRadnika.getSelectionModel().getSelectedItem().toString();
            }
            if (statusRadnika.equals("Aktivan")) {
                biletar.setStatusRadnika(true);
            } else {
                biletar.setStatusRadnika(false);
            }
            biletar.setKontak(tfKontakt.getText());
            if (postojiUBaziKorisnickoIme(tfKorisnickoIme.getText())) {
                biletar.setKorisnickoIme(tfKorisnickoIme.getText());
            } else {
                return false;
            }
            String staraLozinka = tfPassword.getText();
            if(!PregledRadnikaController.dodajRadnika){
            if(!postojiUBaziLozinka(biletar.hashSHA256(staraLozinka))){
                upozorenjeLozinka();
            } else {
                biletar.setHash(tfNovaLozinka.getText());
            }
            }
            else{
            biletar.setHash(tfPassword.getText());
            }
            biletar.setTipRadnika("Biletar");
            if (PregledRadnikaController.dodajRadnika) {
                BIletarDAO.dodajBiletara(biletar);
            } else {
                BIletarDAO.izmjeniBiletara(biletar);
            }

        } else {
            upozorenjePoljaSuPrazna();
            return false;
        }

        return true;
    }

    private boolean dodajUmjetnika() {
        if (PregledRadnikaController.dodajRadnika) {
            if (cmbTipRadnika.getSelectionModel().getSelectedItem() == null) {
                upozorenjeComboBox();
                return false;
            }
        }
        if (!tfJmb.getText().isEmpty() && !tfIme.getText().isEmpty() && !tfPrezime.getText().isEmpty()
                && !tfKontakt.getText().isEmpty() && !taBiografija.getText().isEmpty()) {

            String jmbRegex = "\\d+";
            Pattern pattern = Pattern.compile(jmbRegex);

            if (!pattern.matcher(tfJmb.getText()).matches() && tfJmb.getText().length() != 13) {
                upozorenjeNeispravanJMB();
                return false;
            } else {
                if (PregledRadnikaController.dodajRadnika) {
                    if (!provjeriMaticniBrojUBazi(tfJmb.getText())) {
                        upoorenjeMaticniBroj();
                        return false;
                    }
                }
            }

            if (tfIme.getText().length() > 40) {
                upozorenjePredugacakUnos();
                return false;
            }

            if (tfPrezime.getText().length() > 40) {
                upozorenjePredugacakUnos();
                return false;
            }

            Umjetnik umjetnik = new Umjetnik();
            if (!PregledRadnikaController.dodajRadnika) {
                umjetnik = (Umjetnik) PregledRadnikaController.izabraniRadnik;
            }

            umjetnik.setJmb(tfJmb.getText());
            umjetnik.setIme(tfIme.getText());
            umjetnik.setPrezime(tfPrezime.getText());
            String statusRadnika = "";
            if (PregledRadnikaController.dodajRadnika) {
                statusRadnika = "Aktivan";
            } else {
                statusRadnika = cmbStatusRadnika.getSelectionModel().getSelectedItem().toString();
            }
            if (statusRadnika.equals("Aktivan")) {
                umjetnik.setStatusRadnika(true);
            } else {
                umjetnik.setStatusRadnika(false);
            }
            umjetnik.setKontak(tfKontakt.getText());
            umjetnik.setBiografija(taBiografija.getText());

            if (PregledRadnikaController.dodajRadnika) {
                UmjetnikDAO.dodajUmjetnika(umjetnik);
            } else {
                UmjetnikDAO.izmjeniUmjetnika(umjetnik);
            }
        } else {
            upozorenjePoljaSuPrazna();
            return false;
        }
        return true;
    }

    public boolean unesiZaposlenog() {
        if (PregledRadnikaController.dodajRadnika) {
            if (cmbTipRadnika.getValue().equals("Biletar")) {
                if (dodajBiletara()) {
                    return true;
                }
            }
            if (cmbTipRadnika.getValue().equals("Umjetnik")) {
                if (dodajUmjetnika()) {
                    return true;
                }
            }
            if (cmbTipRadnika.getValue().equals("Administrativni radnik")) {
                if (dodajAdministrativnogRadnika()) {
                    return true;
                }
            }

        } else if (!PregledRadnikaController.dodajRadnika) {
            Radnik radnik = PregledRadnikaController.izabraniRadnik;
            if (radnik.getTipRadnika().equals("Biletar")) {
                dodajBiletara();
                return true;
            } else if (radnik.getTipRadnika().equals("Umjetnik")) {
                dodajUmjetnika();
                return true;
            } else if (radnik.getTipRadnika().equals("Administrator")) {
                dodajAdministrativnogRadnika();
                return true;
            }
        }
        return false;
    }

    @FXML
    void potvrdiUnosRadnikaAction(ActionEvent event) {
        if (PregledRadnikaController.dodajRadnika) {
            if (cmbTipRadnika.getSelectionModel().isEmpty()) {
                upozorenjeComboBox();
                return;
            }
        }
        if (unesiZaposlenog()) {
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
        } else {
            return;
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

    private void sakriPolja() {
        tfIme.setVisible(false);
        tfPrezime.setVisible(false);
        tfJmb.setVisible(false);
        tfKontakt.setVisible(false);
        tfKorisnickoIme.setVisible(false);
        tfPassword.setVisible(false);
        taBiografija.setVisible(false);
        tfNovaLozinka.setVisible(false);

        lIme.setVisible(false);
        lPrezime.setVisible(false);
        lJmb.setVisible(false);
        lStatusRadnika.setVisible(false);
        lKontakt.setVisible(false);
        lKorisnickoIme.setVisible(false);
        lLozinka.setVisible(false);
        lBiografija.setVisible(false);
        lNovaLozinka.setVisible(false);
    }

    private void prikazRadnikaKojiKoristiSistem() {
        if(PregledRadnikaController.dodajRadnika){
        cmbStatusRadnika.setVisible(false);
        } else {
           cmbStatusRadnika.setVisible(true); 
        }
        tfIme.setVisible(true);
        tfPrezime.setVisible(true);
        tfJmb.setVisible(true);
        tfKontakt.setVisible(true);
        tfKorisnickoIme.setVisible(true);
        tfPassword.setVisible(true);
        if(PregledRadnikaController.dodajRadnika){
         tfNovaLozinka.setVisible(false);
        } else {
        tfNovaLozinka.setVisible(true);
        }
        
        lIme.setVisible(true);
        lPrezime.setVisible(true);
        lJmb.setVisible(true);
        if(PregledRadnikaController.dodajRadnika){
        lStatusRadnika.setVisible(false);
        } else {
          lStatusRadnika.setVisible(true);  
        }
        lKontakt.setVisible(true);
        lKorisnickoIme.setVisible(true);
        lLozinka.setVisible(true);
        if(!PregledRadnikaController.dodajRadnika){
        lLozinka.setText("Stara lozinka");
        }
        if(PregledRadnikaController.dodajRadnika){
            lNovaLozinka.setVisible(false);
        } else {
            lNovaLozinka.setVisible(true);
        }
    }

    private void prikazUmjetnika() {
        if(PregledRadnikaController.dodajRadnika){
        cmbStatusRadnika.setVisible(false);
        } else {
          cmbStatusRadnika.setVisible(true);  
        }
        tfIme.setVisible(true);
        tfPrezime.setVisible(true);
        tfJmb.setVisible(true);
        tfKontakt.setVisible(true);
        taBiografija.setVisible(true);
        
        lIme.setVisible(true);
        lPrezime.setVisible(true);
        lJmb.setVisible(true);
        if(PregledRadnikaController.dodajRadnika){
        lStatusRadnika.setVisible(false);
        } else {
            lStatusRadnika.setVisible(true);
        }
        lKontakt.setVisible(true);
        lBiografija.setVisible(true);
    }

    @FXML
    private void prikazPoljaNaOsnovuTipaRadnikaIzKomboBoksa() {
        sakriPolja();
        if (cmbTipRadnika.getValue().toString().equals("Biletar")) {
            prikazRadnikaKojiKoristiSistem();
        } else if (cmbTipRadnika.getValue().toString().equals("Administrativni radnik")) {
            prikazRadnikaKojiKoristiSistem();
        } else if (cmbTipRadnika.getValue().toString().equals("Umjetnik")) {
            prikazUmjetnika();
        }
    }

    private void postavljanjeTextFieldovaZaUmjetnika() {
        tfIme.setText(PregledRadnikaController.izabraniRadnik.getIme());
        tfPrezime.setText(PregledRadnikaController.izabraniRadnik.getPrezime());
        tfJmb.setText(PregledRadnikaController.izabraniRadnik.getJmb());
        tfKontakt.setText(PregledRadnikaController.izabraniRadnik.getKontakt());
        taBiografija.setText(((Umjetnik) PregledRadnikaController.izabraniRadnik).getBiografija());
        tfJmb.setEditable(false);
    }

    private void postavljanjeTextFieldovaZaRadnikaKojiKoristiSistem() {
        tfIme.setText(PregledRadnikaController.izabraniRadnik.getIme());
        tfPrezime.setText(PregledRadnikaController.izabraniRadnik.getPrezime());
        tfJmb.setText(PregledRadnikaController.izabraniRadnik.getJmb());
        tfKontakt.setText(PregledRadnikaController.izabraniRadnik.getKontakt());
        if (PregledRadnikaController.izabraniRadnik instanceof Biletar) {
            tfKorisnickoIme.setText(((Biletar) PregledRadnikaController.izabraniRadnik).getKorisnickoIme());
        } else {
            tfKorisnickoIme.setText(((AdministrativniRadnik) PregledRadnikaController.izabraniRadnik).getKorisnickoIme());
        }
        tfPassword.setText("");
        tfJmb.setEditable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sakriPolja();
        cmbStatusRadnika.getItems().addAll("Aktivan", "Neaktivan");
        cmbTipRadnika.getItems().addAll("Biletar", "Umjetnik", "Administrativni radnik");
        cmbStatusRadnika.setVisible(false);
        cmbStatusRadnika.getSelectionModel().selectFirst();
        if (!PregledRadnikaController.dodajRadnika) {
            cmbTipRadnika.setVisible(false);
            if (PregledRadnikaController.tipRadnika.equals("Biletar")) {
                if (PregledRadnikaController.izabraniRadnik.isStatusRadnika()) {
                    cmbStatusRadnika.getSelectionModel().selectFirst();
                } else {
                    cmbStatusRadnika.getSelectionModel().selectLast();
                }
                cmbStatusRadnika.setVisible(true);
                prikazRadnikaKojiKoristiSistem();
                postavljanjeTextFieldovaZaRadnikaKojiKoristiSistem();

            } else if (PregledRadnikaController.tipRadnika.equals("Umjetnik")) {
                if (PregledRadnikaController.izabraniRadnik.isStatusRadnika()) {
                    cmbStatusRadnika.getSelectionModel().selectFirst();
                } else {
                    cmbStatusRadnika.getSelectionModel().selectLast();
                }
                cmbStatusRadnika.setVisible(true);
                prikazUmjetnika();
                postavljanjeTextFieldovaZaUmjetnika();

            } else if (PregledRadnikaController.tipRadnika.equals("Administrator")) {
                if (PregledRadnikaController.izabraniRadnik.isStatusRadnika()) {
                    cmbStatusRadnika.getSelectionModel().selectFirst();
                } else {
                    cmbStatusRadnika.getSelectionModel().selectLast();
                }
                cmbStatusRadnika.setVisible(true);
                prikazRadnikaKojiKoristiSistem();
                postavljanjeTextFieldovaZaRadnikaKojiKoristiSistem();
                tfJmb.setEditable(false);
            }
        }
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

    private boolean postojiUBaziKorisnickoIme(String korisnickoIme) {
        boolean postoji = false;
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call provjeraKorisnickogImena(?,?)}");
            callableStatement.setString(1, korisnickoIme);

            callableStatement.executeQuery();
            postoji = callableStatement.getBoolean(2);
            if (postoji) {
                upozorenjeKorisnickoIme();
                return false;
            } else {
                return true;

            }
        } catch (SQLException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionPool.getInstance().checkIn(connection);
        }
        return false;
    }
    
        private boolean postojiUBaziLozinka(String lozinka) {
        boolean postoji = false;
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call provjeraLozinke(?,?)}");
            callableStatement.setString(1, lozinka);

            callableStatement.executeQuery();
            postoji = callableStatement.getBoolean(2);
            if (postoji) {
                upozorenjeKorisnickoIme();
                return false;
            } else {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionPool.getInstance().checkIn(connection);
        }
        return false;
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

    private void upozorenjePredugacakUnos() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom unosa podataka !");
        alert.setHeaderText(null);
        alert.setContentText("Predugacak unos!");
        alert.showAndWait();
    }

    private void upozorenjeBrjTelefona() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom unosa podataka !");
        alert.setHeaderText(null);
        alert.setContentText("Provjerite broj telefona!");
        alert.showAndWait();
    }

    private void upozorenjeKorisnickoIme() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom unosa korisnickog imena!");
        alert.setHeaderText(null);
        alert.setContentText("Korisnicko ime vec postoji u bazi!");
        alert.showAndWait();
        return;
    }
    
        private void upozorenjeLozinka() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom unosa lozinka!");
        alert.setHeaderText(null);
        alert.setContentText("Lozinka vec postoji u bazi!");
        alert.showAndWait();
        return;
    }

}
