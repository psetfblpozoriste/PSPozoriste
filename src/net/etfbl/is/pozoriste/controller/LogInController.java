package net.etfbl.is.pozoriste.controller;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.model.dao.mysql.ConnectionPool;
import net.etfbl.is.pozoriste.model.dao.mysql.ConnectionPool;

/**
 * FXML Controller class
 *
 * @author djord
 */


public class LogInController implements Initializable {

    @FXML // fx:id="tfKorisnickoIme"
    private TextField tfKorisnickoIme; // Value injected by FXMLLoader

    @FXML // fx:id="tfLozinka"
    private TextField tfLozinka; // Value injected by FXMLLoader

    @FXML // fx:id="bPotvrda"
    private Button bPotvrda; // Value injected by FXMLLoader

    public static String tipKorisnika = "";
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //ovde incijalizovati login formu i onda nekom metodom , ili kako vec ako prodje login forma ovde napraviti metodu koja ce uzitati PozoristeController
        //bar po meni

        //bPotvrda.setOnAction(e -> provjeraAutentifikacije(tfKorisnickoIme.getText(), tfLozinka.getText()));
    }

    private boolean provjeraAutentifikacije(String username, String password) {
        System.out.println("PASSS: "+password);
        String passwordHash = hashSHA256(password);
        System.out.println("1313PASS: "+passwordHash);
        //if(postojiUBazi(username, passwordHash)){
        //    return true;
       // } else return false;
        String userType = postojiUBazi(username, passwordHash);
        System.out.println("USER TYPE: "+userType);
        if ("".equals(userType)) {
            return false;
        }
        System.out.println("POSALO!!!");
        return true; 
    }

    private String postojiUBazi(String username, String passwordHash) {
        boolean postoji = false;
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBB");
        try {
            connection = ConnectionPool.getInstance().checkOut();
            callableStatement = connection.prepareCall("{call provjeraLogovanja(?,?,?)}");
            callableStatement.setString(1, username);
            callableStatement.setString(2, passwordHash);
            callableStatement.registerOutParameter(3, Types.BOOLEAN);
            callableStatement.executeQuery();
                postoji = callableStatement.getBoolean(3);
                System.out.println("KOPSAJAOG: "+postoji);
                if(postoji){
                    System.out.println("POSTOJ!!!");
                    callableStatement = connection.prepareCall("{call provjeraLozinkeIKorisnickogImena(?,?)}");
                    callableStatement.setString(1, username);
                    callableStatement.setString(2, passwordHash);
                    
                    resultSet = callableStatement.executeQuery();
                    if (resultSet.next()) {
                       tipKorisnika = resultSet.getString("tipKorisnika"); 
                    }
                } else {
                   upozorenjeLogovanje();

                }
                return tipKorisnika;
        } catch (SQLException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionPool.getInstance().checkIn(connection);
        }
        return "";
    }

    private String hashSHA256(String value) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] encodedhash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
        String hash = bytesToHex(encodedhash);
        return hash;
    }

    private String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @FXML
    void potvrdaAction(ActionEvent event) throws IOException {
        if(provjeraAutentifikacije(tfKorisnickoIme.getText(), tfLozinka.getText())) {
            if ("Administrator".equals(tipKorisnika)) {
                try {
                    Parent pozoristeController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/Admin.fxml"));

                    Scene pozScene = new Scene(pozoristeController);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(pozScene);
                    window.show();
                } catch (IOException ex) {
                    Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else if("Biletar".equals(tipKorisnika)){
                                try {
                    Parent pozoristeController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/PregledRepertoara.fxml"));
                    PregledRepertoaraController.incijalizacija(0);
                    Scene pozScene = new Scene(pozoristeController);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(pozScene);
                    window.show();
                } catch (IOException ex) {
                    Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        } else {
            upozorenjeKorisnik();
        }
    }
    private void upozorenjeLogovanje() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom unosa korisnickog imena ili lozinke!");
        alert.setHeaderText(null);
        alert.setContentText("Pogresno korisnicko ime ili lozinka!");
        alert.showAndWait();
        return;
    }
    
        private void upozorenjeKorisnik() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom unosa korisnickog imena!");
        alert.setHeaderText(null);
        alert.setContentText("Pogresno korisnicko ime!");
        alert.showAndWait();
    }
    

    private void upozorenjeLozinka() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greska prilikom unosa lozinke!");
        alert.setHeaderText(null);
        alert.setContentText("Netacna lozinka");
        alert.showAndWait();
    }

}
