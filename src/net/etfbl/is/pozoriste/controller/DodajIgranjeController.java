/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.is.pozoriste.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.model.dao.mysql.ConnectionPool;
import net.etfbl.is.pozoriste.model.dao.mysql.ScenaDAO;
import net.etfbl.is.pozoriste.model.dto.GostujucaPredstava;
import net.etfbl.is.pozoriste.model.dto.Igranje;
import net.etfbl.is.pozoriste.model.dto.Predstava;
import net.etfbl.is.pozoriste.model.dto.Scena;

/**
 * FXML Controller class
 *
 * @author djord
 */
public class DodajIgranjeController implements Initializable {

    @FXML
    private ComboBox cmbPredstave;

    @FXML
    private DatePicker dpTerminPredstave;

    @FXML
    private Button bDodaj;

    @FXML
    private Button bZavrsi;

    @FXML
    private Button bNazad;

    @FXML
    void dodajIgranjeAction(ActionEvent event) {
        dodajIgranje();
    }

    /*
     Calendar calendar = Calendar.getInstance();
     calendar.set(datePickerBan.getValue().getYear(), datePickerBan.getValue().getMonthValue() - 1
     , datePickerBan.getValue().getDayOfMonth());
     */
    private boolean dodajIgranje() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(dpTerminPredstave.getValue().getYear(), dpTerminPredstave.getValue().getMonthValue() - 1, dpTerminPredstave.getValue().getDayOfMonth());
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("KALENDAR PRVI"+sdf.format(new Date(calendar.getTimeInMillis())));
        
        System.out.println("KALENDAR: "+calendar);
        
        LinkedList<Igranje> novoIgranje = new LinkedList<>();
        List<Scena> scena = ScenaDAO.scene();
        System.out.println("SCENA: "+scena);
        System.out.println("REPERTOAR ID: "+DodajRepertoarController.repertoar.getId());
        novoIgranje.add(new Igranje(new Date(calendar.getTimeInMillis()),scena.get(0).getIdScene()
        ,(cmbPredstave.getSelectionModel().getSelectedItem() instanceof Predstava)? ((Predstava)cmbPredstave.getSelectionModel().getSelectedItem()).getId() : null
        ,(cmbPredstave.getSelectionModel().getSelectedItem() instanceof GostujucaPredstava)? ((GostujucaPredstava)cmbPredstave.getSelectionModel().getSelectedItem()).getId() : null
        ,DodajRepertoarController.repertoar.getId()));
        // posalji lisu igranja DodajRepertoarController.repertoar.setIgranja();
       // novoIgranje.add(calendar.getTimeInMillis(),);
        //return false;
        DodajRepertoarController.repertoar.getIgranja().add(new Igranje(new Date(calendar.getTimeInMillis()),scena.get(0).getIdScene()
        ,(cmbPredstave.getSelectionModel().getSelectedItem() instanceof Predstava)? ((Predstava)cmbPredstave.getSelectionModel().getSelectedItem()).getId() : null
        ,(cmbPredstave.getSelectionModel().getSelectedItem() instanceof GostujucaPredstava)? ((GostujucaPredstava)cmbPredstave.getSelectionModel().getSelectedItem()).getId() : null
        ,DodajRepertoarController.repertoar.getId()));      //.setIgranja(novoIgranje);
       
        System.out.println("SVA IGRANJA ZA REPERTOAR: ");
        DodajRepertoarController.repertoar.getIgranja().stream().forEach(System.out::println);
        System.out.println("IGRANJE: ");
        novoIgranje.stream().forEach(System.out::println);
        return true; // false ako je termin vez zauzet :D
    }

    @FXML
    void nazadNaDodajRepertoar(ActionEvent event) {
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
    void zavrsiDodavanjeRepertoara(ActionEvent event) {
        try {
            Parent adminController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/PregledSvihRepertoara.fxml"));

            Scene dodajRadnikaScene = new Scene(adminController);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(dodajRadnikaScene);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(DodajIgranjeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
       private void ubaciUCMBPredstave() {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            connection = ConnectionPool.getInstance().checkOut();
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT naziv,tip from predstava");
            while (rs.next()) {
                cmbPredstave.getItems().add(rs.getString("naziv") + ":" + rs.getString("tip"));
               // mjesta.put(rs.getInt(1), rs.getString(2));
            }
            rs = statement.executeQuery("SELECT naziv,tip from gostujuca_predstava");
            while (rs.next()) {
                cmbPredstave.getItems().add("Gostujuca: "+rs.getString("naziv") + ":" + rs.getString("tip"));
               // mjesta.put(rs.getInt(1), rs.getString(2));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DodajIgranjeController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().checkIn(connection);
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DodajIgranjeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ubaciUCMBPredstave();
    }

}
