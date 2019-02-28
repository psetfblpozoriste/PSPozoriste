package net.etfbl.is.pozoriste.controller;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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

    private static Repertoar repertoarZaPrikaz;

    private VBox vBox = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!"Administrator".equals(LogInController.tipKorisnika)) {
            buttonNazad.setVisible(false);

            Optional<Repertoar> op = RepertoarDAO.repertoars().stream().filter(e -> {
                Calendar kalendar = Calendar.getInstance();
                kalendar.setTime(e.getMjesecIGodina());
                Calendar trenutni = Calendar.getInstance();
                return kalendar.get(Calendar.YEAR) == trenutni.get(Calendar.YEAR) && kalendar.get(Calendar.MONTH) == trenutni.get(Calendar.MONTH);
            }).findAny();
            if (op.isPresent()) {
                repertoarZaPrikaz = op.get();
            } else {
                repertoarZaPrikaz = null;
            }
        }
        buttonNazad.setOnAction(e -> buttonSetAction());

        if (repertoarZaPrikaz != null && !repertoarZaPrikaz.getIgranja().isEmpty()) {
            vBox = new VBox();
            final LinkedList<Predstava> predstave = new LinkedList<>();
            final LinkedList<GostujucaPredstava> gostujuce = new LinkedList<>();
            List<Integer> nadji = repertoarZaPrikaz.getIgranja().stream().mapToInt(e -> (e.getIdPredstave() != 0 ? e.getIdPredstave() : e.getIdGostujucePredstave())).boxed().collect(Collectors.toList());
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
                HBox hBox = new HBox();
                Label vrijeme = new Label();
                vrijeme.setId(i.toString());
                hBox.setId(i.toString());

                final Igranje igranje = repertoarZaPrikaz.getIgranja().get(i);
                String stringZaPrikaz = "";
                if (igranje.getIdPredstave() != 0) {
                    stringZaPrikaz += predstave.stream().filter(e -> e.getId() == igranje.getIdPredstave()).findFirst().get().getNaziv();
                    // String[] empty = new String[135 - stringZaPrikaz.length()];
                    //Arrays.fill(empty, " ");
                    //stringZaPrikaz += Arrays.asList(empty).stream().collect(Collectors.joining(""));
                    // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:hh-mm");
                    // stringZaPrikaz += format.format(igranje.getTermin());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:hh-mm");
                    vrijeme.setText(format.format(igranje.getTermin()));
                }
                if (igranje.getIdGostujucePredstave() != 0) {
                    stringZaPrikaz += gostujuce.stream().filter(e -> e.getId() == igranje.getIdGostujucePredstave()).findFirst().get().getNaziv();
                    //String[] empty = new String[135 - stringZaPrikaz.length()];
                    //Arrays.fill(empty, " ");
                    //stringZaPrikaz += Arrays.asList(empty).stream().collect(Collectors.joining(""));
                    //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:hh-mm");
                    // stringZaPrikaz += format.format(igranje.getTermin());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:hh-mm");
                    vrijeme.setText(format.format(igranje.getTermin()));
                }
                Label nazivLabel = new Label(stringZaPrikaz);
                nazivLabel.setId(i.toString());
                setLabel(nazivLabel, vrijeme);
                hBox.setMinWidth(900);
                hBox.getChildren().add(nazivLabel);
                hBox.getChildren().add(vrijeme);
                if ("Biletar".equals(LogInController.tipKorisnika)) {
                    labelSetAction(nazivLabel, vrijeme);
                }
                vBox.getChildren().add(hBox);
            }
            vBox.setMaxWidth(747);
            scrollPane.vvalueProperty().bind(vBox.heightProperty());
            scrollPane.setContent(vBox);

        } else {
            if ( !repertoarZaPrikaz.getIgranja().isEmpty()) {
                Platform.runLater(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000);
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Za " + (new SimpleDateFormat("MMM").format(Calendar.getInstance().getTime())) + " nije unjet repertoar", ButtonType.OK);
                        alert.setTitle("Upozorenje");
                        alert.setHeaderText("Upozorenje");
                        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(PregledKarataController.class.getResourceAsStream("/net/etfbl/is/pozoriste/resursi/warning.png")));
                        alert.showAndWait();;
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PregledRepertoaraController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
        }
    }

    public static void incijalizacija(Repertoar repertoar) {
        repertoarZaPrikaz = repertoar;
    }

    private void pregledRepertoara(Label label, Label vrijeme) {
        Igranje zeljenoIgranje = null;
        try {
            predstavaSaKojomRadim = label.getText().split(" ")[0];
            // String string = label.getText().split(" ")[Arrays.asList(label.getText().split(" ")).size() - 1];
            String string = vrijeme.getText();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd:hh-mm", Locale.GERMANY);
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
            stage.setTitle("Karte " + "za predstavu " + predstavaSaKojomRadim);
            stage.setResizable(false);
            stage.show();
        } catch (Exception ex) {
            Logger.getLogger(PregledPredstavaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setLabel(Label label, Label vrijeme) {
        if ("Administrator".equals(LogInController.tipKorisnika)) {
            label.addEventFilter(MouseEvent.ANY, MouseEvent::consume);
        }
        label.setMinWidth(665);
        label.setMinHeight(40);
        label.setFont(new Font(16));
        label.setStyle("-fx-font-weight: bold");
        label.setPadding(new Insets(0, 0, 0, 10));

        vrijeme.setMinWidth(230);
        vrijeme.setMinHeight(40);
        vrijeme.setFont(new Font(16));
        vrijeme.setStyle("-fx-font-weight: bold");
        vrijeme.setPadding(new Insets(0, 0, 0, 0));
        if (Integer.parseInt(label.getId()) % 2 == 0) {
            //label.setBackground(new Background(new BackgroundFill(Color.color(144, 100, 100), CornerRadii.EMPTY, Insets.EMPTY)));
            label.setStyle("-fx-background-color: #90c8ff");
        } else {
            //label.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            label.setStyle("-fx-background-color: #e6e6e6");
        }

        if (Integer.parseInt(vrijeme.getId()) % 2 == 0) {
            //label.setBackground(new Background(new BackgroundFill(Color.color(144, 100, 100), CornerRadii.EMPTY, Insets.EMPTY)));
            vrijeme.setStyle("-fx-background-color: #90c8ff");
        } else {
            //label.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            vrijeme.setStyle("-fx-background-color: #e6e6e6");
        }

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

    private void labelSetAction(Label label, Label vrijeme) {
        label.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (((MouseEvent) event).getClickCount() == 2) {
                    Optional<Node> hOpt = vBox.getChildren().stream().filter(e -> e.getId().equals(label.getId())).findFirst();
                    if (hOpt.isPresent()) {
                        pregledRepertoara((Label) ((HBox) hOpt.get()).getChildren().get(0), (Label) ((HBox) hOpt.get()).getChildren().get(1));
                    }
                }
            }
        });
        label.setOnMouseEntered(event -> {
            label.setStyle("-fx-border-color: #005cb7");
            //setLabel(label);
        });
        label.setOnMouseExited(event -> {
            label.setBorder(Border.EMPTY);
            setLabel(label, vrijeme);
        });
    }

}
