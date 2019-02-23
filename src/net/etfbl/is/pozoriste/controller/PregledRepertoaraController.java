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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.model.dao.mysql.ScenaDAO;

/**
 * FXML Controller class
 *
 * @author Ognjen
 */
public class PregledRepertoaraController implements Initializable {

    private VBox vBox;

    @FXML // fx:id="scrollPane"
    private ScrollPane scrollPane; // Value injected by FXMLLoader

    @FXML // fx:id="buttonNazad"
    private Button buttonNazad; // Value injected by FXMLLoader

    private static int brojPredstava = 2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!"administrator".equals(LogInController.tipKorisnika)) {
            buttonNazad.setVisible(false);
        }

        buttonNazad.setOnAction(e -> buttonSetAction());

        if (!(brojPredstava == 0)) {
            vBox = new VBox();
            for (int i = 0; i < 20; i++) {
                Label naziv = new Label("Alisa u zemlji cuda termin                                                                               10.3 u 20:00");
                setLabel(naziv);
                naziv.setOnMouseClicked(event -> {
                    if (event.getButton().equals(MouseButton.PRIMARY)) {
                        if (((MouseEvent) event).getClickCount() == 2) {
                            pregledRepertoara();
                        }
                    }
                });

                naziv.setOnMouseEntered(event -> {
                    naziv.setStyle("-fx-border-color: black");
                });

                naziv.setOnMouseExited(event -> {
                    naziv.setBorder(Border.EMPTY);
                    setLabel(naziv);
                });

                vBox.getChildren().add(naziv);
            }
            scrollPane.vvalueProperty().bind(vBox.heightProperty());
            scrollPane.setContent(vBox);
        }
    }

    public static void incijalizacija(int brojRepertoara) {
        brojPredstava = 2;
    }

    private void pregledRepertoara() {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/net/etfbl/is/pozoriste/view/PregledKarata.fxml"));
        PregledKarataController pregledKarataController = null;
        //PregledKarataController.scenaZaPrikaz = ScenaDAO.scene().get(0);
        loader.setController(pregledKarataController);
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
//        stage.setTitle("Karte " + "za scenu " + PregledKarataController.scenaZaPrikaz.getNazivScene());
        stage.getIcons().add(new Image(AdminController.class.getResourceAsStream("/net/etfbl/is/pozoriste/resursi/drama.png")));
        stage.sizeToScene();
        stage.show();
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());

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

}
