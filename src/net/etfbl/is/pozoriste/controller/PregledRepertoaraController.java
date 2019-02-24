package net.etfbl.is.pozoriste.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.model.dao.mysql.RepertoarDAO;
import net.etfbl.is.pozoriste.model.dto.Igranje;
import net.etfbl.is.pozoriste.model.dto.Repertoar;

/**
 * FXML Controller class
 *
 * @author Ognjen
 */
public class PregledRepertoaraController implements Initializable {

    private VBox vBox;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="scrollPane"
    private ScrollPane scrollPane; // Value injected by FXMLLoader

    @FXML // fx:id="buttonNazad"
    private Button buttonNazad; // Value injected by FXMLLoader

    private static int brojPredstava = 2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!"Administrator".equals(LogInController.tipKorisnika)) {
            buttonNazad.setVisible(false);
        }
        Repertoar repertoarZaPrikaz = RepertoarDAO.getRepertoar(1);
        buttonNazad.setOnAction(e -> buttonSetAction());

        if (!(brojPredstava == 0)) {
            vBox = new VBox();
            for (int i = 0; i < repertoarZaPrikaz.getIgranja().size(); i++) {
                Igranje igranje = repertoarZaPrikaz.getIgranja().get(i);
                Label naziv = new Label((igranje.getIdPredstave() != null ? igranje.getIdPredstave() : igranje.getIdGostujucePredstave()) + "                  " + igranje.getTermin().toString());
                setLabel(naziv);
                HBox hBox = new HBox(naziv);
                hBoxSetAction(hBox);
                vBox.getChildren().add(hBox);
            }
            scrollPane.vvalueProperty().bind(vBox.heightProperty());
            scrollPane.setContent(vBox);
        }
    }

    public static void incijalizacija(int brojRepertoara) {
        brojPredstava = 2;
    }

    private void pregledRepertoara() {
        try {
            Parent pregledKarataController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/PregledKarata.fxml"));
            Scene scene = new Scene(pregledKarataController);
            Stage stage = (Stage) buttonNazad.getScene().getWindow();
            stage.getIcons().add(new Image(PregledKarataController.class.getResourceAsStream("/net/etfbl/is/pozoriste/resursi/drama.png")));
            stage.setScene(scene);
            //stage.setTitle("Karte " + "za scenu " + PregledKarataController.scenaZaPrikaz.getNazivScene());

            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            //stage.sizeToScene();
            stage.show();
        } catch (Exception ex) {
            Logger.getLogger(PregledPredstavaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setLabel(Label naziv) {
        naziv.setMinWidth(560);
        naziv.setMinHeight(40);
        naziv.setFont(new Font(16));
        naziv.setStyle("-fx-font-weight: bold");
        naziv.setPadding(new Insets(0, 0, 0, 10));
    }

    private void buttonSetAction() {
        try {
            Parent adminController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/Admin.fxml"));
            Scene adminControllerScene = new Scene(adminController);
            Stage window = (Stage) buttonNazad.getScene().getWindow();
            window.setScene(adminControllerScene);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(PregledPredstavaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void hBoxSetAction(HBox hBox) {
        hBox.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (((MouseEvent) event).getClickCount() == 2) {
                    pregledRepertoara();
                }
            }
        });
        hBox.setOnMouseEntered(event -> {
            hBox.setStyle("-fx-border-color: black");
        });
        hBox.setOnMouseExited(event -> {
            hBox.setBorder(Border.EMPTY);
            setLabel((Label) hBox.getChildren().get(0));
        });
    }

}
