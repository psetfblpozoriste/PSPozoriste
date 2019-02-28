package net.etfbl.is.pozoriste.main;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.controller.LogInController;

/**
 *
 * @author Grupa6
 */
public class Pozoriste extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Handler handler = null;
       /* try {
            handler = new FileHandler("./error.log");
            Logger.getLogger("").addHandler(handler);
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(Pozoriste.class.getName()).log(Level.SEVERE, null, ex);
        }
        */

        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/net/etfbl/is/pozoriste/view/LogIn.fxml"));
        LogInController logInController1 = null;
        loader.setController(logInController1);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Login");
        stage.getIcons().add(new Image(Pozoriste.class.getResourceAsStream("/net/etfbl/is/pozoriste/resursi/drama.png")));
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
