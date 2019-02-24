package net.etfbl.is.pozoriste.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.model.dao.mysql.ConnectionPool;

/**
 *
 * @author djordj
 */
public class AdminController implements Initializable {

    @FXML
    private Button bPregledPredstave;

    @FXML
    private Button bPregledRadnika;

    @FXML
    private Button bPregledRepertoara;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ConnectionPool.getInstance();//da otvori odmah konekciju ka bazi 
    }

    public void PregledRadnikaAction(ActionEvent event) {
        try {
            Parent radnikController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/PregledRadnika.fxml"));

            Scene radnikScene = new Scene(radnikController);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(radnikScene);
            
           /* Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            window.setX(bounds.getMinX());
            window.setY(bounds.getMinY());
            window.setWidth(bounds.getWidth());
            window.setHeight(bounds.getHeight());
            */
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void PregledRepertoaraAction(ActionEvent event) {
        try {
            Parent repertoarController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/PregledRepertoara.fxml"));

            Scene repertoarScene = new Scene(repertoarController);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(repertoarScene);
            window.setResizable(false);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void PregledPredstavaAction(ActionEvent event) {
        try {
            Parent predstavaController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/PregledPredstava.fxml"));

            Scene predstavaScene = new Scene(predstavaController);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(predstavaScene);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
