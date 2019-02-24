package net.etfbl.is.pozoriste.controller;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.etfbl.is.pozoriste.model.dao.mysql.GostujucaPredstavaDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.PredstavaDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.RepertoarDAO;
import net.etfbl.is.pozoriste.model.dao.mysql.ScenaDAO;
import net.etfbl.is.pozoriste.model.dto.GostujucaPredstava;
import net.etfbl.is.pozoriste.model.dto.Igranje;
import net.etfbl.is.pozoriste.model.dto.Predstava;
import net.etfbl.is.pozoriste.model.dto.Repertoar;
import net.etfbl.is.pozoriste.model.dto.Scena;

/**
 * FXML Controller class
 *
 * @author Ognjen
 */
public class PregledRepertoaraController implements Initializable {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="scrollPane"
    private ScrollPane scrollPane; // Value injected by FXMLLoader

    @FXML // fx:id="buttonNazad"
    private Button buttonNazad; // Value injected by FXMLLoader

    private static int brojPredstava = 2;

    public static String predstavaSaKojomRadim = "";

    public static Scena scena;

    private Repertoar repertoarZaPrikaz;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!"Administrator".equals(LogInController.tipKorisnika)) {
            buttonNazad.setVisible(false);
        }
        repertoarZaPrikaz = RepertoarDAO.getRepertoar(1);//ovde zvati repertoar
        buttonNazad.setOnAction(e -> buttonSetAction());

        if (!(brojPredstava == 0)) {
            VBox vBox = new VBox();
            final LinkedList<Predstava> predstave = new LinkedList<>();
            final LinkedList<GostujucaPredstava> gostujuce = new LinkedList<>();
            List<Integer> nadji = repertoarZaPrikaz.getIgranja().stream().mapToInt(e -> (e.getIdPredstave() != null ? e.getIdPredstave() : e.getIdGostujucePredstave())).boxed().collect(Collectors.toList());
            PredstavaDAO.predstave().stream().forEach(e -> {
                if (nadji.stream().filter(p -> p == e.getId()).findFirst().isPresent()) {
                    predstave.add(e);
                }
            });
            GostujucaPredstavaDAO.gostujucePredstave().stream().forEach(e -> {
                if (nadji.stream().filter(p -> p == e.getId()).findFirst().isPresent()) {
                    gostujuce.add(e);
                }
            });
            repertoarZaPrikaz.getIgranja().sort(Comparator.comparing(e -> e.getTermin()));

            for (Integer i = 0; i < repertoarZaPrikaz.getIgranja().size(); i++) {
                final Igranje igranje = repertoarZaPrikaz.getIgranja().get(i);
                String stringZaPrikaz = "";
                if (igranje.getIdPredstave() != 0) {
                    stringZaPrikaz += predstave.stream().filter(e -> e.getId() == igranje.getIdPredstave()).findFirst().get().getNaziv();
                    String[] empty = new String[115 - stringZaPrikaz.length()];
                    Arrays.fill(empty, " ");
                    stringZaPrikaz += Arrays.asList(empty).stream().collect(Collectors.joining(""));
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:hh-mm");
                    stringZaPrikaz += format.format(igranje.getTermin());
                }
                if (igranje.getIdGostujucePredstave() != 0) {
                    stringZaPrikaz += gostujuce.stream().filter(e -> e.getId() == igranje.getIdGostujucePredstave()).findFirst().get().getNaziv();
                    String[] empty = new String[115 - stringZaPrikaz.length()];
                    Arrays.fill(empty, " ");
                    stringZaPrikaz += Arrays.asList(empty).stream().collect(Collectors.joining(""));
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:hh-mm");
                    stringZaPrikaz += format.format(igranje.getTermin());
                }
                Label nazivLabel = new Label(stringZaPrikaz);
                setLabel(nazivLabel);
                nazivLabel.setId(i.toString());
                if ("Biletar".equals(LogInController.tipKorisnika)) {
                    labelSetAction(nazivLabel);
                }
                vBox.getChildren().add(nazivLabel);
            }
            scrollPane.vvalueProperty().bind(vBox.heightProperty());
            scrollPane.setContent(vBox);
            if ("Administrator".equals(LogInController.tipKorisnika)) {
                scrollPane.addEventFilter(MouseEvent.ANY, MouseEvent::consume);
            }
        }
    }

    public static void incijalizacija(int brojRepertoara) {
        brojPredstava = 2;
    }

    private void pregledRepertoara(Label label) {
        Igranje zeljenoIgranje = null;
        try {
            predstavaSaKojomRadim = label.getText().split(" ")[0];
            String string = label.getText().split(" ")[Arrays.asList(label.getText().split(" ")).size()-1];
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd:hh-mm",Locale.GERMANY);
            Date date = format.parse(string);
            zeljenoIgranje = repertoarZaPrikaz.getIgranja().stream().filter(e -> e.getTermin().equals(date)).findFirst().get();
        } catch (Exception e) {
            Logger.getLogger(PregledPredstavaController.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            final Igranje temp = zeljenoIgranje;
            PregledKarataController.scenaZaPrikaz = new Scena(temp.getIdScene(), ScenaDAO.scene().stream().filter(e -> e.getIdScene().equals(temp.getIdScene())).findAny().get().getNazivScene());
            PregledKarataController.terminPredstave = temp.getTermin();
            Parent pregledKarataController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/PregledKarata.fxml"));
            Scene scene = new Scene(pregledKarataController);
            Stage stage = (Stage) buttonNazad.getScene().getWindow();
            stage.getIcons().add(new Image(PregledKarataController.class.getResourceAsStream("/net/etfbl/is/pozoriste/resursi/drama.png")));
            stage.setScene(scene);
            stage.setTitle("Karte " + "za scenu " + predstavaSaKojomRadim);
            stage.setResizable(false);
            stage.show();
        } catch (Exception ex) {
            Logger.getLogger(PregledPredstavaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setLabel(Label label) {
        label.setMinWidth(scrollPane.getWidth() - 2);
        label.setMinHeight(40);
        label.setFont(new Font(16));
        label.setStyle("-fx-font-weight: bold");
        label.setPadding(new Insets(0, 0, 0, 10));
    }

    private void buttonSetAction() {
        try {
            Parent adminController = FXMLLoader.load(getClass().getResource("/net/etfbl/is/pozoriste/view/Admin.fxml"));
            Scene adminControllerScene = new Scene(adminController);
            Stage window = (Stage) buttonNazad.getScene().getWindow();
            window.setScene(adminControllerScene);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(PregledRepertoaraController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void labelSetAction(Label label) {
        label.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (((MouseEvent) event).getClickCount() == 2) {
                    pregledRepertoara(label);
                }
            }
        });
        label.setOnMouseEntered(event -> {
            label.setStyle("-fx-border-color: black");
            //setLabel(label);
        });
        label.setOnMouseExited(event -> {
            label.setBorder(Border.EMPTY);
            setLabel(label);
        });
    }

}
