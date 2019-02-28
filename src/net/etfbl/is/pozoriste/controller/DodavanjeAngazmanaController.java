package net.etfbl.is.pozoriste.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.model.dao.mysql.AngazmanDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.UmjetnikDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.VrstaAngazmanaDAO;
import net.etfbl.is.pozoriste.model.dto.Angazman;
import net.etfbl.is.pozoriste.model.dto.Predstava;
import net.etfbl.is.pozoriste.model.dto.Umjetnik;
import net.etfbl.is.pozoriste.model.dto.VrstaAngazmana;

public class DodavanjeAngazmanaController implements Initializable {

    @FXML
    private ComboBox<String> comboBoxVrstaAngazmana;

    @FXML
    private ComboBox<String> comboBoxUmjetnik;

    @FXML
    private DatePicker datePickerDatumOd;

    @FXML
    private DatePicker datePickerDatumDo;

    @FXML
    private TableView<Angazman> tableAngazmani;

    @FXML
    private Button buttonDodaj;

    @FXML
    private Button buttonOK;

    @FXML
    private Button buttonIzmijeni;

    @FXML
    private Label labelVrstaAngazmana;

    @FXML
    private Label labelDatumDo;

    @FXML
    private Label labelDatumOd;

    @FXML
    private Label labelUmjetnik;

    @FXML
    private Button buttonDodajVrstuAngazmana;

    @FXML
    private TableColumn<Angazman, String> imeColumn;
    @FXML
    private TableColumn<Angazman, String> prezimeColumn;
    @FXML
    private TableColumn<Angazman, String> vrstaAngazmanaColumn;
    @FXML
    private TableColumn<Angazman, Date> datumOdColumn;
    @FXML
    private TableColumn<Angazman, Date> datumDoColumn;

    public static ObservableList<Angazman> angazmani = FXCollections.observableArrayList();
    public static ObservableList<Umjetnik> umjetnici = FXCollections.observableArrayList();
    public static ObservableList<VrstaAngazmana> vrste = FXCollections.observableArrayList();
    private boolean izmjena = false;
    private static Predstava predstava;

    public static void setPredstava(Predstava p) {
        predstava = p;
    }

    private void ubaciKoloneUTabeluAngazmana(ObservableList angazmani) {
        imeColumn = new TableColumn("Ime");
        imeColumn.setCellValueFactory(new PropertyValueFactory<>("ime"));

        prezimeColumn = new TableColumn("Prezime");
        prezimeColumn.setCellValueFactory(new PropertyValueFactory<>("prezime"));

        vrstaAngazmanaColumn = new TableColumn("Vrsta angazmana");
        vrstaAngazmanaColumn.setCellValueFactory(new PropertyValueFactory<>("vrstaAngazmana"));

        datumOdColumn = new TableColumn("Datum od");
        datumOdColumn.setCellValueFactory(new PropertyValueFactory<>("datumOd"));

        datumDoColumn = new TableColumn("Datum do");
        datumDoColumn.setCellValueFactory(new PropertyValueFactory<>("datumDo"));

        tableAngazmani.setItems(angazmani);
        tableAngazmani.getColumns().addAll(imeColumn, prezimeColumn, vrstaAngazmanaColumn, datumOdColumn, datumDoColumn);
    }

    @FXML
    void dodajAction(ActionEvent event) {
        if (!izmjena) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(datePickerDatumOd.getValue().getYear(), datePickerDatumOd.getValue().getMonthValue() - 1, datePickerDatumOd.getValue().getDayOfMonth());

            if (((comboBoxUmjetnik.getSelectionModel().getSelectedIndex()) < 0) || ((comboBoxVrstaAngazmana.getSelectionModel().getSelectedIndex()) < 0)) {
                upozorenjeSelekcije();
            } else {
                AngazmanDAO.dodajAngazman(predstava.getId(), umjetnici.get(comboBoxUmjetnik.getSelectionModel().getSelectedIndex()).getIdRadnika(),
                        vrste.get(comboBoxVrstaAngazmana.getSelectionModel().getSelectedIndex()).getId(), new Date(calendar.getTimeInMillis()));

                osvjeziTabelu();

            }
        } else {
            Calendar calendar = Calendar.getInstance();
            Calendar calendar1 = Calendar.getInstance();
            calendar.set(datePickerDatumOd.getValue().getYear(), datePickerDatumOd.getValue().getMonthValue() - 1, datePickerDatumOd.getValue().getDayOfMonth());
            calendar1.set(datePickerDatumDo.getValue().getYear(), datePickerDatumDo.getValue().getMonthValue() - 1, datePickerDatumDo.getValue().getDayOfMonth());

            AngazmanDAO.azurirajAngazman(predstava.getId(), umjetnici.get(comboBoxUmjetnik.getSelectionModel().getSelectedIndex()).getIdRadnika(),
                    vrste.get(comboBoxVrstaAngazmana.getSelectionModel().getSelectedIndex()).getId(), new Date(calendar.getTimeInMillis()), new Date(calendar1.getTimeInMillis()));
            osvjeziTabelu();
            izmjena = false;
            datePickerDatumDo.setVisible(false);
            buttonIzmijeni.setVisible(false);
            tableAngazmani.setDisable(false);
            comboBoxUmjetnik.setDisable(false);
            comboBoxVrstaAngazmana.setDisable(false);
            datePickerDatumOd.setDisable(false);
        }
    }

    @FXML
    void izmijeniAction(ActionEvent event) {
        ObservableList<Angazman> izabranaVrsta, angazmaniObservableList;
        angazmaniObservableList = tableAngazmani.getItems();
        izabranaVrsta = tableAngazmani.getSelectionModel().getSelectedItems();
        Angazman izabraniAngazman = (Angazman) izabranaVrsta.get(0);
        if (izabraniAngazman != null) {
            izmjena = true;
            tableAngazmani.setDisable(true);
            comboBoxUmjetnik.setValue(izabraniAngazman.getIme() + " " + izabraniAngazman.getPrezime());
            comboBoxUmjetnik.setDisable(true);
            comboBoxVrstaAngazmana.setValue(izabraniAngazman.getVrstaAngazmana());
            comboBoxVrstaAngazmana.setDisable(true);
            LocalDate l = izabraniAngazman.getDatumOd().toLocalDate();
            datePickerDatumOd.setValue(l);
            datePickerDatumOd.setDisable(true);

            datePickerDatumDo.setVisible(true);
            buttonIzmijeni.setVisible(false);

        } else {
            upozorenjeSelekcijeTabele();
        }

    }

    @FXML
    void dodajVrstuAngazmanaAction(ActionEvent event) {
        ((Stage) tableAngazmani.getScene().getWindow()).getScene().getRoot().getChildrenUnmodifiable().forEach(e -> e.setDisable(true));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/net/etfbl/is/pozoriste/view/DodavanjeVrsteAngazmana.fxml"));
        DodavanjeVrsteAngazmanaController dodajVrstuAngazmana = null;
        loader.setController(dodajVrstuAngazmana);
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(DodavanjeAngazmanaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Dodaj vrstu angazmana");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> {
            ((Stage) tableAngazmani.getScene().getWindow()).getScene().getRoot().getChildrenUnmodifiable().forEach(k -> k.setDisable(false));
            vrste.clear();
            vrste.addAll(VrstaAngazmanaDAO.vrsteAngazmana());
            ObservableList<String> pomocni = FXCollections.observableArrayList();
            vrste.forEach((v) -> {
                pomocni.add(v.getNaziv());
            });
            comboBoxVrstaAngazmana.getItems().removeAll(comboBoxVrstaAngazmana.getItems());
            comboBoxVrstaAngazmana.setItems(pomocni);
        });
        stage.setOnHiding(e -> {
            ((Stage) tableAngazmani.getScene().getWindow()).getScene().getRoot().getChildrenUnmodifiable().forEach(k -> k.setDisable(false));
            vrste.clear();
            vrste.addAll(VrstaAngazmanaDAO.vrsteAngazmana());
            ObservableList<String> pomocni = FXCollections.observableArrayList();
            vrste.forEach((v) -> {
                pomocni.add(v.getNaziv());
            });
            comboBoxVrstaAngazmana.getItems().removeAll(comboBoxVrstaAngazmana.getItems());
            comboBoxVrstaAngazmana.setItems(pomocni);
        });
        stage.show();
    }

    @FXML
    void okAction(ActionEvent event) {
        try {
            Parent predstavaController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/PregledPredstava.fxml"));

            Scene predstavaScene = new Scene(predstavaController);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(predstavaScene);
            window.setResizable(false);
            window.setTitle("Vrsta angazmana");
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(DodavanjeAngazmanaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelDatumDo.setVisible(false);
        datePickerDatumDo.setVisible(false);

        umjetnici.addAll(UmjetnikDAO.umjetnici());
        ObservableList<String> pomocno = FXCollections.observableArrayList();
        umjetnici.stream().map((u) -> u.getIme() + " " + u.getPrezime()).forEachOrdered((pom) -> {
            pomocno.add(pom);
        });
        comboBoxUmjetnik.getItems().removeAll(comboBoxUmjetnik.getItems());
        comboBoxUmjetnik.setItems(pomocno);

        vrste.addAll(VrstaAngazmanaDAO.vrsteAngazmana());
        ObservableList<String> pomocni = FXCollections.observableArrayList();
        vrste.forEach((v) -> {
            pomocni.add(v.getNaziv());
        });
        comboBoxVrstaAngazmana.getItems().removeAll(comboBoxVrstaAngazmana.getItems());
        comboBoxVrstaAngazmana.setItems(pomocni);

        angazmani.clear();
        angazmani.addAll(AngazmanDAO.angazmani(predstava));
        ubaciKoloneUTabeluAngazmana(angazmani);

    }

    private void osvjeziTabelu() {
        angazmani.clear();
        angazmani.addAll(AngazmanDAO.angazmani(predstava));
        tableAngazmani.setItems(angazmani);
    }

    private void upozorenjeSelekcije() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Izaberite umjetnika i vrstu angazmana!");
        alert.showAndWait();
    }

    private void upozorenjeSelekcijeTabele() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Izaberite angazman!");
        alert.showAndWait();
    }

}
