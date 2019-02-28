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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.model.dao.mysql.AzuriranjeDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.GostujucaPredstavaDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.KreiranjeDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.PredstavaDAO;
import net.etfbl.is.pozoriste.model.dto.Azuriranje;
import net.etfbl.is.pozoriste.model.dto.GostujucaPredstava;
import net.etfbl.is.pozoriste.model.dto.Kreiranje;
import net.etfbl.is.pozoriste.model.dto.Predstava;

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
    private Button buttonPregledajAngazman;

    @FXML
    private Button buttonOK;

    @FXML
    private Button bNazad;

    private static boolean domacaPredstava;
    private static boolean dodavanje;
    private static Object predstava;

    public static void setPredstava(Object p) {
        predstava = p;
    }

    public static void setDomacaPredstava(boolean domacaPr) {
        domacaPredstava = domacaPr;
    }

    public static void setDodavanje(boolean dodaj) {
        dodavanje = dodaj;
    }

    @FXML
    void pregledajAngazmanAction(ActionEvent event) {
        if (!textFieldNaziv.getText().isEmpty() && !textFieldTip.getText().isEmpty() && !textAreaOpis.getText().isEmpty()) {
            Predstava domaca = (Predstava) predstava;
            domaca.setNaziv(textFieldNaziv.getText());
            domaca.setOpis(textAreaOpis.getText());
            domaca.setTip(textFieldTip.getText());
            if (provjeraPredstava()) {
                PredstavaDAO.azurirajPredstavu(domaca);
                Azuriranje azuriranje = new Azuriranje(domaca.getId(), null, null, LogInController.idLogovanog);
                AzuriranjeDAO.dodajAzuriranje(azuriranje);
                DodavanjeAngazmanaController.setPredstava(domaca);
                otvoriAngazmane(event);
            }
        } else {
            upozorenjePoljaSuPrazna();
        }
    }

    @FXML
    void okAction(ActionEvent event) {
        if (dodavanje) {
            if (domacaPredstava) {
                if (!textFieldNaziv.getText().isEmpty() && !textFieldTip.getText().isEmpty() && !textAreaOpis.getText().isEmpty()) {
                    Predstava predstava = new Predstava(textFieldNaziv.getText(), textAreaOpis.getText(), textFieldTip.getText());
                    if (provjeraPredstava()) {
                        PredstavaDAO.dodajPredstavu(predstava);
                        Kreiranje kreiranje = new Kreiranje(predstava.getId(), null, null, LogInController.idLogovanog);
                        KreiranjeDAO.dodajKreiranje(kreiranje);
                        DodavanjeAngazmanaController.setPredstava(predstava);
                        otvoriAngazmane(event);
                    }

                } else {
                    upozorenjePoljaSuPrazna();
                }
            } else {
                if (!textFieldNaziv.getText().isEmpty() && !textFieldTip.getText().isEmpty() && !textAreaOpis.getText().isEmpty() && !textAreaGlumci.getText().isEmpty() && !textFieldPisac.getText().isEmpty() && !textFieldReziser.getText().isEmpty()) {
                    GostujucaPredstava gostujucaPredstava = new GostujucaPredstava(textFieldNaziv.getText(), textAreaOpis.getText(), textFieldTip.getText(), textFieldPisac.getText(), textFieldReziser.getText(), textAreaGlumci.getText());
                    if (provjeraGostujucaPredstava()) {
                        GostujucaPredstavaDAO.dodajGostujucuPredstavu(gostujucaPredstava);
                        Kreiranje kreiranje = new Kreiranje(null, null, gostujucaPredstava.getId(), LogInController.idLogovanog);
                        KreiranjeDAO.dodajKreiranje(kreiranje);
                        nazadNaPregledPredstava(event);
                    }
                } else {
                    upozorenjePoljaSuPrazna();
                }
            }
        } else {

            if (!textFieldNaziv.getText().isEmpty() && !textFieldTip.getText().isEmpty() && !textAreaOpis.getText().isEmpty() && !textAreaGlumci.getText().isEmpty() && !textFieldPisac.getText().isEmpty() && !textFieldReziser.getText().isEmpty()) {
                GostujucaPredstava gostujuca = (GostujucaPredstava) predstava;
                gostujuca.setNaziv(textFieldNaziv.getText());
                gostujuca.setOpis(textAreaOpis.getText());
                gostujuca.setTip(textFieldTip.getText());
                gostujuca.setGlumci(textAreaGlumci.getText());
                gostujuca.setPisac(textFieldPisac.getText());
                gostujuca.setReziser(textFieldReziser.getText());
                if (provjeraGostujucaPredstava()) {
                    GostujucaPredstavaDAO.azurirajGostujucuPredstavu(gostujuca);
                    Azuriranje azuriranje = new Azuriranje(null, null, gostujuca.getId(), LogInController.idLogovanog);
                    AzuriranjeDAO.dodajAzuriranje(azuriranje);
                    nazadNaPregledPredstava(event);
                }
            } else {
                upozorenjePoljaSuPrazna();
            }
        }
    }

    @FXML
    void nazadNaPregledPredstava(ActionEvent event) {
        try {
            Parent predstavaController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/PregledPredstava.fxml"));

            Scene predstavaScene = new Scene(predstavaController);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(predstavaScene);
            window.setResizable(false);
            window.setTitle("Predstave");
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (dodavanje) {
            if (domacaPredstava) {
                labelPisac.setVisible(false);
                labelReziser.setVisible(false);
                labelGlumci.setVisible(false);
                textFieldPisac.setVisible(false);
                textFieldReziser.setVisible(false);
                textAreaGlumci.setVisible(false);
                buttonPregledajAngazman.setVisible(false);
            } else {
                buttonPregledajAngazman.setVisible(false);
            }
        } else {
            if (domacaPredstava) {
                labelPisac.setVisible(false);
                labelReziser.setVisible(false);
                labelGlumci.setVisible(false);
                textFieldPisac.setVisible(false);
                textFieldReziser.setVisible(false);
                textAreaGlumci.setVisible(false);
                buttonOK.setVisible(false);
                Predstava domaca = (Predstava) predstava;
                textFieldNaziv.setText(domaca.getNaziv());
                textFieldTip.setText(domaca.getTip());
                textAreaOpis.setText(domaca.getOpis());

            } else {
                buttonPregledajAngazman.setVisible(false);
                GostujucaPredstava gostujuca = (GostujucaPredstava) predstava;
                textFieldNaziv.setText(gostujuca.getNaziv());
                textFieldTip.setText(gostujuca.getTip());
                textAreaOpis.setText(gostujuca.getOpis());
                textFieldPisac.setText(gostujuca.getPisac());
                textFieldReziser.setText(gostujuca.getReziser());
                textAreaGlumci.setText(gostujuca.getGlumci());
            }
        }
    }

    private void upozorenjePoljaSuPrazna() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prazna polja !");
        alert.setHeaderText(null);
        alert.setContentText("Polja su prazna.");
        alert.showAndWait();
    }

    private void upozorenjeUnosDug(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom unosa podataka !");
        alert.setHeaderText(null);
        alert.setContentText("Unos \"" + s + "\" je predugacak!");
        alert.showAndWait();
    }

    private boolean provjeraPredstava() {
        if (textFieldNaziv.getText().length() > 40) {
            upozorenjeUnosDug("Naziv");
            return false;
        }
        if (textFieldTip.getText().length() > 40) {
            upozorenjeUnosDug("Tip");
            return false;
        }
        return true;
    }

    private boolean provjeraGostujucaPredstava() {
        if (textFieldNaziv.getText().length() > 64) {
            upozorenjeUnosDug("Naziv");
            return false;
        }
        if (textFieldTip.getText().length() > 40) {
            upozorenjeUnosDug("Tip");
            return false;
        }
        if (textFieldPisac.getText().length() > 40) {
            upozorenjeUnosDug("Pisac");
            return false;
        }
        if (textFieldReziser.getText().length() > 40) {
            upozorenjeUnosDug("Reziser");
            return false;
        }
        return true;
    }

    private void otvoriAngazmane(ActionEvent event) {
        try {
            Parent predstavaController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/DodavanjeAngazmana.fxml"));

            Scene predstavaScene = new Scene(predstavaController);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(predstavaScene);
            window.setResizable(false);
            window.setTitle("Angazman");
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
