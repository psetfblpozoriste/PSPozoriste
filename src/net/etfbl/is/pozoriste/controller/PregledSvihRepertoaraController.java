/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.model.dao.mysql.BIletarDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.RepertoarDAO;
import net.etfbl.is.pozoriste.model.dto.Radnik;
import net.etfbl.is.pozoriste.model.dto.Repertoar;

/**
 * FXML Controller class
 *
 * @author djord
 */
public class PregledSvihRepertoaraController implements Initializable {

    @FXML
    private Button bNazad;

    @FXML
    private TableColumn<Radnik, Date> datumColumn;

    public static ObservableList<Repertoar> repertoariObservableList = FXCollections.observableArrayList();

    @FXML
    private Button bDodajRepertoar;

    @FXML
    private TableView sviRepertoariTableView;

    private void ubaciKoloneUTabeluRadnik(ObservableList repertoari) {
        datumColumn = new TableColumn("Pregled svih repertoara");
        datumColumn.setCellValueFactory(new PropertyValueFactory<>("mjesecIGodina"));

        sviRepertoariTableView.setItems(repertoariObservableList);
        sviRepertoariTableView.getColumns().addAll(datumColumn);
    }

    @FXML
    void dodajRepertoaraAction(ActionEvent event) {
        try {
            Parent adminController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/DodajRepertoar.fxml"));

            Scene dodajRadnikaScene = new Scene(adminController);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(dodajRadnikaScene);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void nazadNaAdminFormu(ActionEvent event) {
        try {
            Parent adminController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/Admin.fxml"));

            Scene dodajRadnikaScene = new Scene(adminController);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(dodajRadnikaScene);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        repertoariObservableList.removeAll(repertoariObservableList);
        repertoariObservableList.addAll(RepertoarDAO.repertoars());
        repertoariObservableList.stream().forEach(System.out::println);
        ubaciKoloneUTabeluRadnik(repertoariObservableList);
        datumColumn.prefWidthProperty().bind(sviRepertoariTableView.widthProperty().divide(1));
    }

}
