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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import static net.etfbl.is.pozoriste.controller.PregledRadnikaController.dodajRadnika;
import net.etfbl.is.pozoriste.model.dao.mysql.GostujucaPredstavaDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.PredstavaDAO;
import net.etfbl.is.pozoriste.model.dto.GostujucaPredstava;
import net.etfbl.is.pozoriste.model.dto.Predstava;

/**
 * FXML Controller class
 *
 * @author djord
 */
public class PregledPredstavaController implements Initializable {

    @FXML // fx:id="tablePredstave"
    private TableView<Predstava> tablePredstave; // Value injected by FXMLLoader

    @FXML // fx:id="comboBoxTip"
    private ComboBox<String> comboBoxTip; // Value injected by FXMLLoader

    @FXML // fx:id="buttonDodaj"
    private Button buttonDodaj; // Value injected by FXMLLoader

    @FXML // fx:id="tableGostujucePredstave"
    private TableView<GostujucaPredstava> tableGostujucePredstave; // Value injected by FXMLLoader

    @FXML // fx:id="textAreaOpisPredstave"
    private TextArea textAreaOpisPredstave; // Value injected by FXMLLoader

    @FXML // fx:id="textAreaOpisGostujucePredstave"
    private TextArea textAreaOpisGostujucePredstave; // Value injected by FXMLLoader

    @FXML // fx:id="textAreaGlumciGostujucePredstave"
    private TextArea textAreaGlumciGostujucePredstave; // Value injected by FXMLLoader

    @FXML // fx:id="buttonIzmijeniGostujucuPredstavu"
    private Button buttonIzmijeniGostujucuPredstavu; // Value injected by FXMLLoader

    @FXML // fx:id="buttonPregledaj"
    private Button buttonPregledaj; // Value injected by FXMLLoader

    @FXML // fx:id="buttonIzmijeniPredstavu"
    private Button buttonIzmijeniPredstavu; // Value injected by FXMLLoader

    @FXML
    private TableColumn<Predstava, String> nazivColumn;
    @FXML
    private TableColumn<Predstava, String> tipColumn;
    @FXML
    private TableColumn<GostujucaPredstava, String> nazivGpColumn;
    @FXML
    private TableColumn<GostujucaPredstava, String> tipGpColumn;
    @FXML
    private TableColumn<GostujucaPredstava, String> pisacColumn;
    @FXML
    private TableColumn<GostujucaPredstava, String> reziserColumn;

    public static ObservableList<Predstava> predstaveObservableList = FXCollections.observableArrayList();
    public static ObservableList<GostujucaPredstava> gostujucePredstaveObservableList = FXCollections.observableArrayList();
    public static ObservableList<String> tipovi = FXCollections.observableArrayList();

    @FXML
    void dodajAction(ActionEvent event) {
        try {
            Parent dodajRadnikaController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/DodajPredstavu.fxml"));

            Scene dodajRadnikaScene = new Scene(dodajRadnikaController);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(dodajRadnikaScene);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }

        /* try {
         Parent dodajPredstavuController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/DodajPredstavu.fxml"));
         Scene dodajPredstavuScene = new Scene(dodajPredstavuController);
         Stage window;
         window = (Stage) ((Node) event.getSource()).getScene().getWindow();
         String temp=comboBoxTip.getValue();
         System.out.println("temp: "+temp);
         if("Predstava".equals(temp)){
         DodajPredstavuController.setDomacaPredstava(true);
         }else{
         DodajPredstavuController.setDomacaPredstava(false);
         }
         window.setScene(dodajPredstavuScene);
         window.show();
         } catch (IOException ex) {
         Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
         }catch(Exception ex){
         Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
         }*/
    }

    @FXML
    void izmijeniGostujucuPredstavuAction(ActionEvent event) {

    }

    @FXML
    void pregledajAction(ActionEvent event) {

    }

    @FXML
    void izmijeniPredstavuAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tipovi.addAll("Predstava", "Gostujuca predstava");
        comboBoxTip.setItems(tipovi);
        textAreaOpisPredstave.setEditable(false);
        textAreaOpisGostujucePredstave.setEditable(false);
        textAreaGlumciGostujucePredstave.setEditable(false);
        gostujucePredstaveObservableList.addAll(GostujucaPredstavaDAO.gostujucePredstave());
        predstaveObservableList.addAll(PredstavaDAO.predstave());
        ubaciKoloneUTabeluGostujucaPredstava(gostujucePredstaveObservableList);
        ubaciKoloneUTabeluPredstava(predstaveObservableList);
        tableGostujucePredstave.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                GostujucaPredstava gp = (GostujucaPredstava) tableGostujucePredstave.getSelectionModel().getSelectedItem();
                textAreaOpisGostujucePredstave.setText(((GostujucaPredstava) tableGostujucePredstave.getSelectionModel().getSelectedItem()).getOpis());
                textAreaGlumciGostujucePredstave.setText("Glumci:   " + ((GostujucaPredstava) tableGostujucePredstave.getSelectionModel().getSelectedItem()).getGlumci());
            }
        });
        tablePredstave.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Predstava p = (Predstava) tablePredstave.getSelectionModel().getSelectedItem();
                textAreaOpisPredstave.setText(((Predstava) tablePredstave.getSelectionModel().getSelectedItem()).getOpis());

            }
        });
    }

    private void ubaciKoloneUTabeluGostujucaPredstava(ObservableList gostujucePredstaveObservableList) {
        nazivGpColumn = new TableColumn("Naziv");
        nazivGpColumn.setCellValueFactory(new PropertyValueFactory<>("naziv"));

        tipGpColumn = new TableColumn("Tip");
        tipGpColumn.setCellValueFactory(new PropertyValueFactory<>("tip"));

        pisacColumn = new TableColumn("Pisac");
        pisacColumn.setCellValueFactory(new PropertyValueFactory<>("pisac"));

        reziserColumn = new TableColumn("Reziser");
        reziserColumn.setCellValueFactory(new PropertyValueFactory<>("reziser"));

        tableGostujucePredstave.setItems(gostujucePredstaveObservableList);
        tableGostujucePredstave.getColumns().addAll(nazivGpColumn, tipGpColumn, pisacColumn, reziserColumn);

    }

    private void ubaciKoloneUTabeluPredstava(ObservableList predstaveObservableList) {
        nazivColumn = new TableColumn("Naziv");
        nazivColumn.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        nazivColumn.setPrefWidth(210);

        tipColumn = new TableColumn("Tip");
        tipColumn.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tipColumn.setPrefWidth(210);

        tablePredstave.setItems(predstaveObservableList);
        tablePredstave.getColumns().addAll(nazivColumn, tipColumn);

    }
}
