/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.controller;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import static net.etfbl.is.pozoriste.controller.PregledKarataController.scenaZaPrikaz;
import static net.etfbl.is.pozoriste.controller.PregledKarataController.terminPredstave;
import net.etfbl.is.pozoriste.model.dao.mysql.RezervacijaDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.RezervisanoSjedisteDAO;
import net.etfbl.is.pozoriste.model.dto.Rezervacija;
import net.etfbl.is.pozoriste.model.dto.RezervisanoSjediste;

/**
 * FXML Controller class
 *
 * @author Ognjen
 */
public class DodajRezervacijuController implements Initializable {

    @FXML // fx:id="textFiled"
    private TextField textFiled; // Value injected by FXMLLoader

    @FXML // fx:id="buttonDodaj"
    private Button buttonDodaj; // Value injected by FXMLLoader

    @FXML // fx:id="buttonOdustani"
    private Button buttonOdustani; // Value injected by FXMLLoader

    public static Date termin;

    public static Integer idScene;

    public static ArrayList<Button> rezervisanaSjedista;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonOdustani.setOnAction(e -> ((Stage) buttonOdustani.getScene().getWindow()).close());
        buttonDodaj.setOnAction(e -> dodajButton());
    }

    private void dodajButton() {
        String ime = textFiled.getText();
        PregledKarataController.naKogaGlasiRezervacija = textFiled.getText();
        PregledKarataController.rezervacije = RezervacijaDAO.addRezervacija(new Rezervacija(0, ime, termin, idScene));
        final String imeRezervacije = PregledKarataController.naKogaGlasiRezervacija;
        Rezervacija unosRezervacija = null;

        if (!RezervacijaDAO.rezervacije(terminPredstave, scenaZaPrikaz.getIdScene()).stream().filter(e -> e.getIme().equals(imeRezervacije)).findAny().isPresent()) {
            unosRezervacija = RezervacijaDAO.addRezervacija(new Rezervacija(0, imeRezervacije, terminPredstave, scenaZaPrikaz.getIdScene()));
        } else {
            unosRezervacija = RezervacijaDAO.rezervacije(terminPredstave, scenaZaPrikaz.getIdScene()).stream().filter(e -> e.getIme().equals(imeRezervacije)).findAny().get();
        }
        final Rezervacija temp = unosRezervacija;
        rezervisanaSjedista.forEach(e -> {
            RezervisanoSjedisteDAO.addRezervisanoSjediste(new RezervisanoSjediste(scenaZaPrikaz.getIdScene(), Integer.parseInt(e.getId()), temp.getId(), temp.getTermin()));
            e.setDisable(true);
        });
        rezervisanaSjedista.removeAll(rezervisanaSjedista);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Rezervisano na ime " + imeRezervacije, ButtonType.OK);
        alert.setTitle("Informacija");
        alert.setHeaderText("Informacija");
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(PregledKarataController.class.getResourceAsStream("/net/etfbl/is/pozoriste/resursi/drama.png")));
        alert.showAndWait();

        ((Stage) buttonDodaj.getScene().getWindow()).close();
    }

}
