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

   // @FXML
   // private TextField tfOpisPosla;

    @FXML
    private TextField tfJmb;

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
    private ComboBox cmbStatusRadnika;

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
                if(PregledRadnikaController.dodajRadnika){
                if (!provjeriMaticniBrojUBazi(tfJmb.getText())) {
                    upoorenjeMaticniBroj();
                    return false;
                }
                }
            }

            Biletar biletar = new Biletar();
            if (!PregledRadnikaController.dodajRadnika) {
                biletar = (Biletar) PregledRadnikaController.izabraniRadnik;
            }

            biletar.setJmb(tfJmb.getText());
            biletar.setIme(tfIme.getText());
            biletar.setPrezime(tfPrezime.getText());
            String statusRadnika = cmbStatusRadnika.getItems().toString();
            System.out.println("- - - - - - - - - - > > > "+statusRadnika);
            if(statusRadnika.equals("Aktivan")){
                biletar.setStatusRadnika(true);
            } else {
                biletar.setStatusRadnika(false);
            }
            //biletar.setStatusRadnika(true);
            biletar.setKontak(tfKontakt.getText());
            biletar.setKorisnickoIme(tfKorisnickoIme.getText());
            biletar.setHash(tfPassword.getText());
            biletar.setTipRadnika("Biletar");
                System.out.println("Biletar:   "+biletar);
            BIletarDAO.dodajBiletara(biletar);

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
                if(PregledRadnikaController.dodajRadnika){
                if (!provjeriMaticniBrojUBazi(tfJmb.getText())) {
                    upoorenjeMaticniBroj();
                    return false;
                }
                }
            }

            Umjetnik umjetnik = new Umjetnik();
            if (!PregledRadnikaController.dodajRadnika) {
                umjetnik = (Umjetnik) PregledRadnikaController.izabraniRadnik;
            }

            umjetnik.setJmb(tfJmb.getText());
            umjetnik.setIme(tfIme.getText());
            umjetnik.setPrezime(tfPrezime.getText());
            String statusRadnika = cmbStatusRadnika.getItems().toString();
            System.out.println("- - - - - - - - - - > > > "+statusRadnika);
            if(statusRadnika.equals("Aktivan")){
                umjetnik.setStatusRadnika(true);
            } else {
                umjetnik.setStatusRadnika(false);
            }
            //umjetnik.setStatusRadnika(true);
            umjetnik.setKontak(tfKontakt.getText());
            umjetnik.setBiografija(taBiografija.getText());
            System.out.println("UMJETNIK:   "+umjetnik);

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
        } else if (!PregledRadnikaController.dodajRadnika) { 
            Radnik radnik = PregledRadnikaController.izabraniRadnik;
            if (radnik.getTipRadnika().equals("biletar") || radnik.getTipRadnika().equals("Biletar")) {
                dodajBiletara(); 
                return true;
            } else if (radnik.getTipRadnika().equals("Umjetnik")) {
                dodajUmjetnika(); 
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

    /**
     * Initializes the controller class.
     */
    private void sakriPolja() {
        tfIme.setVisible(false);
        tfPrezime.setVisible(false);
        tfJmb.setVisible(false);
        //tfStatusRadnika.setVisible(false);
        tfKontakt.setVisible(false);
        tfKorisnickoIme.setVisible(false);
        tfPassword.setVisible(false);
        taBiografija.setVisible(false);

        lIme.setVisible(false);
        lPrezime.setVisible(false);
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
        if (cmbTipRadnika.getValue().toString().equals("Biletar")) {
            cmbStatusRadnika.setVisible(true);
            tfIme.setVisible(true);
            tfPrezime.setVisible(true);
            tfJmb.setVisible(true);
            //tfStatusRadnika.setVisible(true);
            tfKontakt.setVisible(true);
            tfKorisnickoIme.setVisible(true);
            tfPassword.setVisible(true);

            lIme.setVisible(true);
            lPrezime.setVisible(true);
            lJmb.setVisible(true);
            lStatusRadnika.setVisible(true);
            lKontakt.setVisible(true);
            lKorisnickoIme.setVisible(true);
            lLozinka.setVisible(true);
        } else if (cmbTipRadnika.getValue().toString().equals("Administrativni radnik")) {
            cmbStatusRadnika.setVisible(true);
            tfIme.setVisible(true);
            tfPrezime.setVisible(true);
            tfJmb.setVisible(true);
            //tfStatusRadnika.setVisible(true);
            tfKontakt.setVisible(true);

            lIme.setVisible(true);
            lPrezime.setVisible(true);
            lJmb.setVisible(true);
            lStatusRadnika.setVisible(true);
            lKontakt.setVisible(true);
        } else if (cmbTipRadnika.getValue().toString().equals("Umjetnik")) {
            cmbStatusRadnika.setVisible(true);
            tfIme.setVisible(true);
            tfPrezime.setVisible(true);
            tfJmb.setVisible(true);
           // tfStatusRadnika.setVisible(true);
            tfKontakt.setVisible(true);
            taBiografija.setVisible(true);

            lIme.setVisible(true);
            lPrezime.setVisible(true);
            lJmb.setVisible(true);
            lStatusRadnika.setVisible(true);
            lKontakt.setVisible(true);
            lBiografija.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sakriPolja();
        cmbStatusRadnika.getItems().addAll("Aktivan","Neaktivan");
        cmbTipRadnika.getItems().addAll("Biletar", "Umjetnik");
        cmbStatusRadnika.setVisible(false);
        if (!PregledRadnikaController.dodajRadnika) {
            cmbTipRadnika.setVisible(false);
            System.out.println("- - - - - > " + PregledRadnikaController.tipRadnika);
            if (PregledRadnikaController.tipRadnika.equals("biletar") || PregledRadnikaController.tipRadnika.equals("Biletar")) {
                System.out.println("BILETAR BILETAR");
                cmbStatusRadnika.setVisible(true);
                taBiografija.setVisible(false);
                tfIme.setVisible(true);
                tfPrezime.setVisible(true);
              //  tfOpisPosla.setVisible(true);
                tfJmb.setVisible(true);
               // tfStatusRadnika.setVisible(true);
                tfKontakt.setVisible(true);
                tfKorisnickoIme.setVisible(true);
                tfPassword.setVisible(true);

                lIme.setVisible(true);
                lPrezime.setVisible(true);
              //  lOpisPosla.setVisible(true);
                lJmb.setVisible(true);
                lStatusRadnika.setVisible(true);
                lKontakt.setVisible(true);
                lKorisnickoIme.setVisible(true);
                lLozinka.setVisible(true);
                lBiografija.setVisible(false);

                tfIme.setText(PregledRadnikaController.izabraniRadnik.getIme());
                tfPrezime.setText(PregledRadnikaController.izabraniRadnik.getPrezime());
             //   tfOpisPosla.setText(PregledRadnikaController.izabraniRadnik.getOpisPosla());
                tfJmb.setText(PregledRadnikaController.izabraniRadnik.getJmb());
               // tfStatusRadnika.setText("ovo jos doraditi");
                tfKontakt.setText(PregledRadnikaController.izabraniRadnik.getKontakt());
                tfKorisnickoIme.setText("ovde dodati korisncko ime");
                tfPassword.setText("ovde dodati prezime");

            } else if (PregledRadnikaController.tipRadnika.equals("Umjetnik")) {
                System.out.println("UMJETNIK UMJETNIK");
                cmbStatusRadnika.setVisible(true);
                taBiografija.setVisible(true);
                tfIme.setVisible(true);
                tfPrezime.setVisible(true);
              //  tfOpisPosla.setVisible(true);
                tfJmb.setVisible(true);
               // tfStatusRadnika.setVisible(true);
                tfKontakt.setVisible(true);
                tfKorisnickoIme.setVisible(false);
                tfPassword.setVisible(false);

                lIme.setVisible(true);
                lPrezime.setVisible(true);
              //  lOpisPosla.setVisible(true);
                lJmb.setVisible(true);
                lStatusRadnika.setVisible(true);
                lKontakt.setVisible(true);
                lKorisnickoIme.setVisible(false);
                lLozinka.setVisible(false);
                lBiografija.setVisible(false);

                tfIme.setText(PregledRadnikaController.izabraniRadnik.getIme());
                tfPrezime.setText(PregledRadnikaController.izabraniRadnik.getPrezime());
             //   tfOpisPosla.setText(PregledRadnikaController.izabraniRadnik.getOpisPosla());
                tfJmb.setText(PregledRadnikaController.izabraniRadnik.getJmb());
                //tfStatusRadnika.setText("ovo jos doraditi");
                tfKontakt.setText(PregledRadnikaController.izabraniRadnik.getKontakt());
                taBiografija.setText("DODATI BIOGRAFIJU");
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
