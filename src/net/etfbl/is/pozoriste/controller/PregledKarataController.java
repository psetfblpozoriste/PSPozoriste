package net.etfbl.is.pozoriste.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.model.dao.mysql.KartaDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.RepertoarDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.RezervacijaDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.RezervisanoSjedisteDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.SjedisteDAO;
import net.etfbl.is.pozoriste.model.dto.Karta;
import net.etfbl.is.pozoriste.model.dto.Rezervacija;
import net.etfbl.is.pozoriste.model.dto.RezervisanoSjediste;
import net.etfbl.is.pozoriste.model.dto.Scena;

/**
 * FXML Controller class
 *
 * @author Ognjen
 */
public class PregledKarataController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML // fx:id="gridPane"
    private GridPane gridPane; // Value injected by FXMLLoader

    @FXML // fx:id="buttonProdaja"
    private Button buttonProdaja; // Value injected by FXMLLoader

    @FXML // fx:id="comboRezervacije"
    private ComboBox<String> comboRezervacije; // Value injected by FXMLLoader

    @FXML // fx:id="buttonNazad"
    private Button buttonNazad; // Value injected by FXMLLoader

    @FXML // fx:id="buttonRezervisi"
    private Button buttonRezervisi; // Value injected by FXMLLoader

    @FXML // fx:id="buttonObrisiRezervaciju"
    private Button buttonObrisiRezervaciju; // Value injected by FXMLLoader

    @FXML // fx:id="buttonStorniraj"
    private Button buttonStorniraj; // Value injected by FXMLLoader

    @FXML // fx:id="comboBoxKarte"
    private ComboBox<Karta> comboBoxKarte; // Value injected by FXMLLoader

    private final Integer RED = 10;

    private final Integer KOLONA = 10;

    public static Scena scenaZaPrikaz;

    public static Date terminPredstave;

    public static Rezervacija rezervacije;

    public static String naKogaGlasiRezervacija;

    private ArrayList<Button> rezervisanaSjedista = new ArrayList<>();//sjedista za jednu rezervaciju

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        postavi();
        buttonProdaja.setOnAction(e -> prodaj());
        buttonNazad.setOnAction(e -> buttonNazadSetAction());
        buttonObrisiRezervaciju.setOnAction(e -> obrisiRezervacijuButton());
        buttonRezervisi.setOnAction(e -> buttonRezervisi());
        buttonStorniraj.setOnAction(e -> buttonStornirajProdaju());

        comboBoxKarte.getItems().removeAll(comboBoxKarte.getItems());
        comboBoxKarte.getItems().addAll(KartaDAO.karte().stream().filter(e -> e.getIdScene() == scenaZaPrikaz.getIdScene() && e.getTermin().equals(terminPredstave)).collect(Collectors.toList()));
        if (SjedisteDAO.sjedista(scenaZaPrikaz.getIdScene()).isEmpty()) {
            for (int i = 0; i < RED; i++) {
                for (int j = 0; j < KOLONA; j++) {
                    SjedisteDAO.dodavanjeSjedista(scenaZaPrikaz.getIdScene(), (i * KOLONA + j));
                }
            }
        }
        comboRezervacije.getItems().removeAll(comboRezervacije.getItems());
        comboRezervacije.getItems().addAll(RezervacijaDAO.rezervacije(terminPredstave, scenaZaPrikaz.getIdScene()).stream().map(i -> i.getIme()).collect(Collectors.toList()));
        comboRezervacije.getItems().remove("true");
        

    }

    private void buttonNazadSetAction() {
        try {
            Parent pregledRepertoaraController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/PregledRepertoara.fxml"));
            Scene scene = new Scene(pregledRepertoaraController);
            Stage window = (Stage) buttonNazad.getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(PregledKarataController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void postavi() {

        gridPane.setPadding(new Insets(5));
        gridPane.setHgap(13);
        gridPane.setVgap(13);
        gridPane.getChildren().removeAll(gridPane.getChildren());
        Button buttonMatrix[][] = new Button[RED][KOLONA];
        List<RezervisanoSjediste> listRezervisanih = RezervisanoSjedisteDAO.sjedista(terminPredstave, scenaZaPrikaz.getIdScene());
        List<Karta> karteProdate = KartaDAO.karte();
        for (int i = 0; i < RED; i++) {
            for (int j = 0; j < KOLONA; j++) {
                buttonMatrix[i][j] = new Button();
                buttonMatrix[i][j].setDisable(false);
                final Integer brojSjedista = i * KOLONA + j;
                buttonMatrix[i][j].setId(brojSjedista.toString());

                if (karteProdate.stream().filter(k -> k.getBrojSjedista() == brojSjedista && k.getTermin().equals(terminPredstave)).findAny().isPresent()
                        && !listRezervisanih.stream().filter(e -> e.getBrojSjedista().equals(brojSjedista)).findAny().isPresent()) {
                    buttonMatrix[i][j].setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/net/etfbl/is/pozoriste/resursi/red.png"))));
                    buttonMatrix[i][j].setDisable(true);
                }

                if (listRezervisanih.stream().filter(r -> r.getBrojSjedista() == brojSjedista && r.getTermin().equals(terminPredstave) && r.getIdScene() == scenaZaPrikaz.getIdScene())
                        .findAny().isPresent()) {
                    buttonMatrix[i][j].setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/net/etfbl/is/pozoriste/resursi/orange.png"))));
                    buttonMatrix[i][j].setDisable(true);
                }

                if (!listRezervisanih.stream().filter(e -> e.getBrojSjedista().equals(brojSjedista)).findAny().isPresent()
                        && !karteProdate.stream().filter(k -> k.getBrojSjedista() == brojSjedista && k.getTermin().equals(terminPredstave)).findAny().isPresent()) {
                    buttonMatrix[i][j].setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/net/etfbl/is/pozoriste/resursi/Green.png"))));
                } else if (!karteProdate.stream().filter(k -> k.getBrojSjedista() == brojSjedista).findFirst().isPresent()
                        && listRezervisanih.stream().filter(e -> e.getBrojSjedista().equals(brojSjedista)).findAny().isPresent()) {
                    buttonMatrix[i][j].setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/net/etfbl/is/pozoriste/resursi/orange.png"))));
                }
                buttonMatrix[i][j].setId(new Integer(i * KOLONA + j).toString());

                buttonMatrix[i][j].setOnMouseClicked(e -> {
                    if (!RezervisanoSjedisteDAO.sjedista(terminPredstave, scenaZaPrikaz.getIdScene()).stream().
                            filter(r -> r.getBrojSjedista() == brojSjedista && r.getTermin().equals(terminPredstave) && r.getIdScene() == scenaZaPrikaz.getIdScene())
                            .findAny().isPresent() && !rezervisanaSjedista.contains(((Button) e.getSource()))) {
                        rezervisanaSjedista.add((Button) e.getSource());
                        ((Button) e.getSource()).setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/net/etfbl/is/pozoriste/resursi/orange.png"))));
                        return;
                    } else if (rezervisanaSjedista.contains(((Button) e.getSource()))) {
                        ((Button) e.getSource()).setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/net/etfbl/is/pozoriste/resursi/Green.png"))));
                        rezervisanaSjedista.remove(((Button) e.getSource()));
                        return;
                    }
                });
                if (gridPane.getChildren().size() > brojSjedista) {
                    gridPane.getChildren().remove(gridPane.getChildren().get(brojSjedista));
                }
                gridPane.add(buttonMatrix[i][j], j, i);
            }
        }
        comboRezervacije.getItems().removeAll(comboRezervacije.getItems());
        comboRezervacije.getItems().addAll(RezervacijaDAO.rezervacije(terminPredstave, scenaZaPrikaz.getIdScene()).stream().map(i -> i.getIme()).collect(Collectors.toList()));

        comboBoxKarte.getItems().removeAll(comboBoxKarte.getItems());
        comboBoxKarte.getItems().addAll(KartaDAO.karte().stream().filter(e -> e.getIdScene() == scenaZaPrikaz.getIdScene() && e.getTermin().equals(terminPredstave)).collect(Collectors.toList()));
    }

    private void buttonRezervisi() {
        if (!rezervisanaSjedista.isEmpty()) {
            ((Stage) buttonRezervisi.getScene().getWindow()).getScene().getRoot().getChildrenUnmodifiable().forEach(e -> e.setDisable(true));
            DodajRezervacijuController.rezervisanaSjedista = rezervisanaSjedista;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/net/etfbl/is/pozoriste/view/DodajRezervaciju.fxml"));
            DodajRezervacijuController dodajRezervacijuController = null;
            loader.setController(dodajRezervacijuController);
            DodajRezervacijuController.termin = terminPredstave;
            DodajRezervacijuController.idScene = scenaZaPrikaz.getIdScene();
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException ex) {
                Logger.getLogger(PregledKarataController.class.getName()).log(Level.SEVERE, null, ex);
            }
            Stage stage = new Stage();
            Scene scene = new Scene(root);

            stage.setTitle("Dodaj rezervaciju");
            stage.getIcons().add(new Image(PregledKarataController.class.getResourceAsStream("/net/etfbl/is/pozoriste/resursi/drama.png")));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setOnCloseRequest(e -> {
                comboRezervacije.getItems().removeAll(comboRezervacije.getItems());
                comboRezervacije.getItems().addAll(RezervacijaDAO.rezervacije(terminPredstave, scenaZaPrikaz.getIdScene()).stream().map(i -> i.getIme()).collect(Collectors.toList()));
                ((Stage) buttonRezervisi.getScene().getWindow()).getScene().getRoot().getChildrenUnmodifiable().forEach(w -> w.setDisable(false));
                rezervisanaSjedista.removeAll(rezervisanaSjedista);
                postavi();
            });

            stage.setOnHiding(e -> {
                comboRezervacije.getItems().removeAll(comboRezervacije.getItems());
                comboRezervacije.getItems().addAll(RezervacijaDAO.rezervacije(terminPredstave, scenaZaPrikaz.getIdScene()).stream().map(i -> i.getIme()).collect(Collectors.toList()));
                ((Stage) buttonRezervisi.getScene().getWindow()).getScene().getRoot().getChildrenUnmodifiable().forEach(w -> w.setDisable(false));
                rezervisanaSjedista.removeAll(rezervisanaSjedista);
                postavi();
            });
            stage.show();

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Prvo izaberite sjedista koja zelite rezervisati!", ButtonType.OK);
            alert.setTitle("Upozorenje");
            alert.setHeaderText("Upozorenje");
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(PregledKarataController.class.getResourceAsStream("/net/etfbl/is/pozoriste/resursi/warning.png")));
            alert.showAndWait();
            return;
        }
    }

    private void obrisiRezervacijuButton() {
        if (!comboRezervacije.getSelectionModel().isEmpty() && RezervacijaDAO.rezervacije(terminPredstave, scenaZaPrikaz.getIdScene()).stream().filter(e -> e.getIme().equals(comboRezervacije.getSelectionModel().getSelectedItem())).findFirst().isPresent()) {
            final String imeZaBrisati = comboRezervacije.getSelectionModel().getSelectedItem();
            final Rezervacija rez = RezervacijaDAO.rezervacije(terminPredstave, scenaZaPrikaz.getIdScene()).stream().filter(e -> e.getIme().equals(imeZaBrisati)).findFirst().get();
            RezervacijaDAO.obrisiRezervaciju(rez);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Rezervacija obrisana", ButtonType.OK);
            alert.setTitle("Informacija");
            alert.setHeaderText("Informacija");
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(PregledKarataController.class.getResourceAsStream("/net/etfbl/is/pozoriste/resursi/drama.png")));
            alert.showAndWait();
            comboRezervacije.getItems().removeAll(comboRezervacije.getItems());
            comboRezervacije.getItems().addAll(RezervacijaDAO.rezervacije(terminPredstave, scenaZaPrikaz.getIdScene()).stream().map(i -> i.getIme()).collect(Collectors.toList()));
            postavi();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Prvo izaberite rezerevaciju za brisanje", ButtonType.OK);
            alert.setTitle("Upozorenje");
            alert.setHeaderText("Upozorenje");
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(PregledKarataController.class.getResourceAsStream("/net/etfbl/is/pozoriste/resursi/warning.png")));
            alert.showAndWait();
        }
    }

    private void prodaj() {
        if (!comboRezervacije.getSelectionModel().isEmpty() && rezervisanaSjedista.isEmpty()) {
            prodajRezervaciju();
            return;
        }

        if (!comboRezervacije.getSelectionModel().isEmpty() && !rezervisanaSjedista.isEmpty()) {
            final String rez = comboRezervacije.getSelectionModel().getSelectedItem();
            ButtonType obicnaProdaja = new ButtonType("Prodaja novih" + System.lineSeparator() + "karata");
            ButtonType prodajaRezervacije = new ButtonType("Prodaja selektovane" + System.lineSeparator() + "rezervacije");
            ButtonType odustani = new ButtonType("Odustani" + System.lineSeparator());
            Alert alert = new Alert(Alert.AlertType.WARNING, "Prodati", odustani, prodajaRezervacije, obicnaProdaja);
            alert.setTitle("Upozorenje");
            alert.setHeaderText("Selektovana je i rezervacija " + rez + " ,pa molimo izaberite tip prodaje");
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(PregledKarataController.class.getResourceAsStream("/net/etfbl/is/pozoriste/resursi/warning.png")));
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == odustani) {
                rezervisanaSjedista.removeAll(rezervisanaSjedista);
                postavi();
            } else if (result.get() == prodajaRezervacije) {
                prodajRezervaciju();
            } else if (result.get() == obicnaProdaja) {
                rezervisanaSjedista.forEach(e -> {
                    Karta k = new Karta(0, (Integer.valueOf(e.getId())) / 10, Integer.valueOf(e.getId()), terminPredstave, scenaZaPrikaz.getIdScene());
                    KartaDAO.dodajKartu(k);
                });
                rezervisanaSjedista.removeAll(rezervisanaSjedista);
                postavi();
            }

        } else if (comboRezervacije.getSelectionModel().isEmpty() && !rezervisanaSjedista.isEmpty()) {
            rezervisanaSjedista.forEach(e -> {
                Karta k = new Karta(0, (Integer.valueOf(e.getId())) / 10, Integer.valueOf(e.getId()), terminPredstave, scenaZaPrikaz.getIdScene());
                KartaDAO.dodajKartu(k);
            });
            rezervisanaSjedista.removeAll(rezervisanaSjedista);
            postavi();
        }
    }

    private void buttonStornirajProdaju() {
        if (comboBoxKarte.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Prvo izaberite kartu za storniranje", ButtonType.OK);
            alert.setTitle("Upozorenje");
            alert.setHeaderText("Upozorenje");
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(PregledKarataController.class.getResourceAsStream("/net/etfbl/is/pozoriste/resursi/warning.png")));
            alert.showAndWait();
            return;
        }
        if (!comboBoxKarte.getSelectionModel().isEmpty()) {
            KartaDAO.obrisiKartu(comboBoxKarte.getSelectionModel().getSelectedItem().getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Karta uspjesno stornirana", ButtonType.OK);
            alert.setTitle("Informacija");
            alert.setHeaderText("Informacija");
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(PregledKarataController.class.getResourceAsStream("/net/etfbl/is/pozoriste/resursi/drama.png")));
            alert.showAndWait();
            postavi();
        }
    }

    private void prodajRezervaciju() {
        final String zaProdaju = comboRezervacije.getSelectionModel().getSelectedItem();
        final Rezervacija rezervacija = RezervacijaDAO.rezervacije(terminPredstave, scenaZaPrikaz.getIdScene()).stream().filter(e -> e.getIme().equals(zaProdaju)).findFirst().get();
        List<RezervisanoSjediste> sjedista = RezervisanoSjedisteDAO.sjedista(terminPredstave, scenaZaPrikaz.getIdScene()).stream().filter(e -> e.getIdRezervacije() == rezervacija.getId()).collect(Collectors.toList());

        sjedista.forEach(e -> {
            Karta k = new Karta(0, (int) e.getBrojSjedista() / 10, e.getBrojSjedista(), e.getTermin(), e.getIdScene());
            KartaDAO.dodajKartu(k);
        });
        sjedista.forEach(e -> {
            RezervisanoSjedisteDAO.obrisiRezervisanoSjediste(e);
        });
        RezervacijaDAO.obrisiRezervaciju(rezervacija);
        postavi();
    }

}
