package com.demikopi.uiHandler;

import com.demikopi.sistemUser.ChatEngine;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class UserDashboardController {

    private static final String PESAN_PEMBUKA =
            "Halo! Saya bot DemiKopi. Mau cari kopi yang manis, strong, atau mau lihat menu hari ini?";
    private static final String LOGO_PATH = "/com/demikopi/uiHandler/asset/coffee-cup.png";

    @FXML
    private ScrollPane chatScroll;

    @FXML
    private VBox chatStream;

    @FXML
    private Label labelStatus;

    @FXML
    private TextField inputPesan;

    @FXML
    private Button tombolKirim;

    private final ChatEngine chatEngine = new ChatEngine();

    @FXML
    private void initialize() {
        labelStatus.setText("Online | Siap membantu");
        tombolKirim.setDisable(true);
        inputPesan.textProperty().addListener((observable, oldValue, newValue) ->
                tombolKirim.setDisable(newValue == null || newValue.trim().isEmpty()));

        tambahBubbleBot(PESAN_PEMBUKA);
        Platform.runLater(inputPesan::requestFocus);
    }

    @FXML
    private void handleKirimPesan() {
        kirimPesan(inputPesan.getText());
    }

    @FXML
    private void handleClearChat() {
        chatStream.getChildren().clear();
        tambahBubbleBot(PESAN_PEMBUKA);
        inputPesan.clear();
    }

    @FXML
    private void handleTanyaMenu() {
        kirimPesan("Menu apa saja yang tersedia?");
    }

    @FXML
    private void handleTanyaRekomendasi() {
        kirimPesan("Rekomendasi menu bestseller");
    }

    @FXML
    private void handleTanyaJamBuka() {
        kirimPesan("Jam buka DemiKopi");
    }

    @FXML
    private void handleTanyaLokasi() {
        kirimPesan("Lokasi DemiKopi");
    }

    @FXML
    private void handleTanyaFasilitas() {
        kirimPesan("Fasilitas apa saja yang tersedia?");
    }

    private void kirimPesan(String pesanUser) {
        if (pesanUser == null || pesanUser.trim().isEmpty()) {
            return;
        }

        pesanUser = pesanUser.trim();
        inputPesan.clear();

        tambahBubbleUser(pesanUser);

        try {
            String jawabanBot = chatEngine.getResponse(pesanUser);
            tambahBubbleBot(jawabanBot);
        } catch (RuntimeException e) {
            tambahBubbleBot("Maaf, terjadi kendala saat memproses pesan. Silakan coba lagi sebentar.");
        }
    }

    private void tambahBubbleUser(String pesan) {
        Label bubble = buatLabelBubble(pesan, "bubble-user");

        HBox barisChat = new HBox(bubble);
        barisChat.setAlignment(Pos.BOTTOM_RIGHT);

        chatStream.getChildren().add(barisChat);
        scrollKeBawah();
    }

    private void tambahBubbleBot(String pesan) {
        StackPane avatarBot = buatAvatarBot();

        Label bubble = buatLabelBubble(pesan, "bubble-bot");

        HBox barisChat = new HBox(10, avatarBot, bubble);
        barisChat.setAlignment(Pos.BOTTOM_LEFT);

        chatStream.getChildren().add(barisChat);
        scrollKeBawah();
    }

    private StackPane buatAvatarBot() {
        StackPane avatar = new StackPane();
        avatar.setMinSize(30, 30);
        avatar.setPrefSize(30, 30);
        avatar.setMaxSize(30, 30);
        avatar.getStyleClass().add("logo-circle");

        if (getClass().getResource(LOGO_PATH) == null) {
            Label fallbackLogo = new Label("DK");
            fallbackLogo.getStyleClass().add("logo-text");
            avatar.getChildren().add(fallbackLogo);
            return avatar;
        }

        ImageView logo = new ImageView(new Image(getClass().getResource(LOGO_PATH).toExternalForm()));
        logo.setFitWidth(20);
        logo.setFitHeight(20);
        logo.setPreserveRatio(true);
        logo.setEffect(new ColorAdjust(0, -1, 1, 0));

        avatar.getChildren().add(logo);
        return avatar;
    }

    private Label buatLabelBubble(String pesan, String styleClass) {
        Label bubble = new Label(pesan);
        bubble.setWrapText(true);
        bubble.maxWidthProperty().bind(chatScroll.widthProperty().multiply(0.72));
        bubble.getStyleClass().add(styleClass);
        return bubble;
    }

    private void scrollKeBawah() {
        Platform.runLater(() -> chatScroll.setVvalue(1.0));
    }
}
