package net.etfbl.is.pozoriste.controller;

import java.awt.Color;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author Ognjen
 */
public class PregledRepertoaraController implements Initializable {

    @FXML // fx:id="vBox"
    private VBox vBox; // Value injected by FXMLLoader

    private static int brojPredstava = 2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!(brojPredstava == 0)) {
            for (int i = 0; i < brojPredstava; i++) {
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
        }
    }

    public static void incijalizacija(int brojRepertoara) {
        brojPredstava = 2;
    }

    private void pregledRepertoara() {
        System.out.println("ovde pozvati onaj dio za prodaju karata");
    }

    private void setLabel(Label naziv) {
        naziv.setMinWidth(560);
        naziv.setMinHeight(40);
        naziv.setFont(new Font(16));
        naziv.setStyle("-fx-font-weight: bold");
        naziv.setPadding(new Insets(0, 0, 0, 10));
    }

}
