package net.etfbl.is.pozoriste.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.model.dao.mysql.RezervacijaDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.RezervisanoSjedisteDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.SjedisteDAO;
import net.etfbl.is.pozoriste.model.dto.RezervisanoSjediste;
import net.etfbl.is.pozoriste.model.dto.Scena;
import net.etfbl.is.pozoriste.model.dto.Sjediste;

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

    @FXML // fx:id="buttonRezervacija"
    private Button buttonRezervacija; // Value injected by FXMLLoader

    @FXML // fx:id="buttonProdaja"
    private Button buttonProdaja; // Value injected by FXMLLoader

    @FXML // fx:id="comboRezervacije"
    private ComboBox comboRezervacije; // Value injected by FXMLLoader

    @FXML // fx:id="buttonNazad"
    private Button buttonNazad; // Value injected by FXMLLoader

    private final Integer RED = 10;

    private final Integer KOLONA = 10;

    public static Scena scenaZaPrikaz;

    public static Date terminPredstave;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        postavi();
        buttonNazad.setOnAction(e -> buttonNazadSetAction());
        buttonRezervacija.setOnAction(e -> buttonRezervacija());
        if (SjedisteDAO.sjedista(scenaZaPrikaz.getIdScene()).isEmpty()) {
            for (int i = 0; i < RED; i++) {
                for (int j = 0; j < KOLONA; j++) {
                    SjedisteDAO.dodavanjeSjedista(scenaZaPrikaz.getIdScene(), (i * KOLONA + j) + 1);
                }
            }
        }
        comboRezervacije.getItems().addAll(comboRezervacije.getItems().addAll(RezervacijaDAO.rezervacije(terminPredstave, scenaZaPrikaz.getIdScene()).stream().map(i -> i.getIme()).collect(Collectors.toList())));
    }

    private void buttonRezervacija() {
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
        stage.setOnCloseRequest(e -> comboRezervacije.getItems().addAll(RezervacijaDAO.rezervacije(terminPredstave, scenaZaPrikaz.getIdScene()).stream().map(i -> i.getIme()).collect(Collectors.toList())));
        stage.setOnHiding(e -> comboRezervacije.getItems().addAll(RezervacijaDAO.rezervacije(terminPredstave, scenaZaPrikaz.getIdScene()).stream().map(i -> i.getIme()).collect(Collectors.toList())));
        stage.show();
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

        Button buttonMatrix[][] = new Button[RED][KOLONA];
        for (int i = 0; i < RED; i++) {
            for (int j = 0; j < KOLONA; j++) {
                buttonMatrix[i][j] = new Button();
                buttonMatrix[i][j].setDisable(false);
                buttonMatrix[i][j].setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/net/etfbl/is/pozoriste/resursi/Green.png"))));
                buttonMatrix[i][j].setId(new Integer(i * KOLONA + j).toString());

                buttonMatrix[i][j].setOnMouseClicked(e -> {
                    RezervisanoSjedisteDAO.addRezervisanoSjediste(new RezervisanoSjediste(
                            scenaZaPrikaz.getIdScene(),
                            1,
                            1,
                            terminPredstave));
                    ((Button) e.getSource()).setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/net/etfbl/is/pozoriste/resursi/orange.png"))));

                });
                gridPane.add(buttonMatrix[i][j], i, j);

            }
        }
    }

}
