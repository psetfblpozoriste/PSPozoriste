package net.etfbl.is.pozoriste.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
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

    @FXML // fx:id="buttonRezervacija"
    private Button buttonRezervacija; // Value injected by FXMLLoader

    @FXML // fx:id="buttonProdaja"
    private Button buttonProdaja; // Value injected by FXMLLoader

    @FXML // fx:id="comboRezervacije"
    private ComboBox comboRezervacije; // Value injected by FXMLLoader

    @FXML // fx:id="buttonFullScrean"
    private Button buttonFullScrean; // Value injected by FXMLLoader
    
    
    public static Scena scenaZaPrikaz=null;
    
    private final Integer RED = 10;
    
    private final Integer KOLONA = 10;
    
    public PregledKarataController(){
      //  RED = scenaZaPrikaz.getBrojRedova();
      //  KOLONA = scenaZaPrikaz.getBrojKolona();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        postavi();
        buttonFullScrean.setOnAction(e -> ((Stage)buttonFullScrean.getScene().getWindow()).setFullScreen(true));
        for (int i = 0; i < RED; i++) {
            for (int j = 0; j < KOLONA; j++) {
//                SjedisteDAO.dodavanjeSjedista(scenaZaPrikaz.getIdScene(), (i * KOLONA + j)+1);
            }
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

                /*buttonMatrix[i][j].setOnMousePressed(new EventHandler<javafx.scene.input.MouseEvent>() {
                    @Override
                    public void handle(javafx.scene.input.MouseEvent event) {
                        System.out.println("rjg;odihgiljgd");
                        
                    }
                });*/
                buttonMatrix[i][j].setOnMouseClicked(e -> {

                    ((Button) e.getSource()).setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/net/etfbl/is/pozoriste/resursi/orange.png"))));

                });
                gridPane.add(buttonMatrix[i][j], i, j);

            }
        }
    }

}
