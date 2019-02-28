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
import javafx.scene.control.Alert;
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

    @FXML 
    private TableView<Predstava> tablePredstave; 

    @FXML 
    private ComboBox<String> comboBoxTip; 

    @FXML 
    private Button buttonDodaj; 

    @FXML 
    private TableView<GostujucaPredstava> tableGostujucePredstave; 

    @FXML 
    private TextArea textAreaOpisPredstave; 

    @FXML 
    private TextArea textAreaOpisGostujucePredstave; 

    @FXML 
    private TextArea textAreaGlumciGostujucePredstave; 

    @FXML 
    private Button buttonIzmijeniGostujucuPredstavu; 

    @FXML 
    private Button buttonPregledaj; 

    @FXML 
    private Button buttonIzmijeniPredstavu; 

    @FXML
    private Button buttonNazad;

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
    void nazadAction(ActionEvent event) {
         try {
            Parent dodajRadnikaController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/Admin.fxml"));

            Scene dodajRadnikaScene = new Scene(dodajRadnikaController);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(dodajRadnikaScene);
            window.setResizable(false);
            window.setTitle("Administrator");
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(PregledPredstavaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void dodajAction(ActionEvent event) {
        String temp = comboBoxTip.getValue();
        if ("Predstava".equals(temp)) {
            DodajPredstavuController.setDomacaPredstava(true);
            DodajPredstavuController.setDodavanje(true);
            DodajPredstavuController.setPredstava(null);
        } else {
            DodajPredstavuController.setDomacaPredstava(false);
            DodajPredstavuController.setDodavanje(true);
            DodajPredstavuController.setPredstava(null);
        }
        otvoriDodajPredstavu(event);

    }

    @FXML
    void izmijeniGostujucuPredstavuAction(ActionEvent event) {
        ObservableList<GostujucaPredstava> izabranaVrsta, predstaveObservableList;
        predstaveObservableList = tableGostujucePredstave.getItems();
        izabranaVrsta = tableGostujucePredstave.getSelectionModel().getSelectedItems();
        GostujucaPredstava izabranaPredstava = (GostujucaPredstava) izabranaVrsta.get(0);
        if (izabranaPredstava != null) {
            DodajPredstavuController.setDodavanje(false);
            DodajPredstavuController.setDomacaPredstava(false);
            DodajPredstavuController.setPredstava(izabranaPredstava);
            otvoriDodajPredstavu(event);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText("Nije selektovana predstava!");
            alert.showAndWait();
        }
    }

    @FXML
    void pregledajAction(ActionEvent event) {
        ObservableList<Predstava> izabranaVrsta, predstaveObservableList;
        predstaveObservableList = tablePredstave.getItems();
        izabranaVrsta = tablePredstave.getSelectionModel().getSelectedItems();
        Predstava izabranaPredstava = (Predstava) izabranaVrsta.get(0);
        if (izabranaPredstava != null) {
            DodavanjeAngazmanaController.setPredstava(izabranaPredstava);
            otvoriAngazmane(event);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText("Nije selektovana predstava!");
            alert.showAndWait();
        }
    }

    @FXML
    void izmijeniPredstavuAction(ActionEvent event) {
        ObservableList<Predstava> izabranaVrsta, predstaveObservableList;
        predstaveObservableList = tablePredstave.getItems();
        izabranaVrsta = tablePredstave.getSelectionModel().getSelectedItems();
        Predstava izabranaPredstava = (Predstava) izabranaVrsta.get(0);
        if (izabranaPredstava != null) {
            DodajPredstavuController.setDodavanje(false);
            DodajPredstavuController.setDomacaPredstava(true);
            DodajPredstavuController.setPredstava(izabranaPredstava);
            otvoriDodajPredstavu(event);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText("Nije selektovana predstava!");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tipovi.addAll("Predstava", "Gostujuca predstava");
        comboBoxTip.setItems(tipovi);
        textAreaOpisPredstave.setEditable(false);
        textAreaOpisGostujucePredstave.setEditable(false);
        textAreaGlumciGostujucePredstave.setEditable(false);
        gostujucePredstaveObservableList.clear();
        predstaveObservableList.clear();
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

    private void otvoriDodajPredstavu(ActionEvent event) {
        try {
            Parent dodajRadnikaController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/DodajPredstavu.fxml"));

            Scene dodajRadnikaScene = new Scene(dodajRadnikaController);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(dodajRadnikaScene);
            window.setResizable(false);
            window.setTitle("Predstava");
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(PregledPredstavaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void otvoriAngazmane(ActionEvent event){
        try {
            Parent dodajRadnikaController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/DodavanjeAngazmana.fxml"));

            Scene dodajRadnikaScene = new Scene(dodajRadnikaController);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(dodajRadnikaScene);
            window.setResizable(false);
            window.setTitle("Angazman");
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(PregledPredstavaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
