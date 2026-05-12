package com.demikopi.uiHandler;

import com.demikopi.sistemUser.ChatEngine;
import com.demikopi.sistemUser.ChatResponse;
import com.demikopi.sistemUser.ChatResponse.ChatBlock;
import com.demikopi.sistemAdmin.AdminSession;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;

public class UserDashboardController {

    private static final String PESAN_PEMBUKA =
            "Halo! Saya bot DemiKopi. Mau cari kopi yang manis, strong, atau mau lihat menu hari ini?";
    private static final String LOGO_PATH = "/com/demikopi/uiHandler/asset/coffee-cup.png";
    private static final String ADMIN_LOGIN_PATH = "/com/demikopi/uiHandler/Admin UI/login.fxml";
    private static final String ADMIN_DASHBOARD_PATH = "/com/demikopi/uiHandler/Admin UI/admin-dashboard.fxml";

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

    @FXML
    private Button adminLoginButton;

    private final ChatEngine chatEngine = new ChatEngine();

    @FXML
    private void initialize() {
        labelStatus.setText("Online | Siap membantu");
        tombolKirim.setDisable(true);
        inputPesan.textProperty().addListener((observable, oldValue, newValue) ->
                tombolKirim.setDisable(newValue == null || newValue.trim().isEmpty()));

        tambahBubbleBot(PESAN_PEMBUKA);
        perbaruiTombolAdmin();
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
    private void handleLoginAdmin() {
        if (AdminSession.isLoggedIn()) {
            bukaHalamanAdmin(ADMIN_DASHBOARD_PATH, "DemiKopi Admin Dashboard", 1000, 650);
        } else {
            bukaHalamanAdmin(ADMIN_LOGIN_PATH, "DemiKopi Admin", 900, 600);
        }
    }

    private void perbaruiTombolAdmin() {
        if (adminLoginButton == null) {
            return;
        }

        adminLoginButton.setText(AdminSession.isLoggedIn() ? "Admin Dashboard" : "Login Admin");
    }

    private void bukaHalamanAdmin(String fxmlPath, String title, double width, double height) {
        URL pageUrl = getClass().getResource(fxmlPath);
        if (pageUrl == null) {
            tambahBubbleBot("Halaman admin belum ditemukan.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(pageUrl);
            Parent root = loader.load();
            Stage stage = (Stage) chatScroll.getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root, width, height));
            stage.centerOnScreen();
        } catch (IOException e) {
            tambahBubbleBot("Maaf, halaman admin tidak bisa dibuka.");
        }
    }

    @FXML
    private void handleTanyaMenu() {
        kirimPesan("Menu apa saja yang tersedia?");
    }

    @FXML
    private void handleTanyaRekomendasi() {
        kirimPesan("Tampilkan rekomendasi best seller");
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
            ChatResponse jawabanBot = chatEngine.getChatResponse(pesanUser);
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
        tambahBubbleBot(ChatResponse.text(pesan));
    }

    private void tambahBubbleBot(ChatResponse response) {
        StackPane avatarBot = buatAvatarBot();

        Node bubble = buatBubbleBot(response);

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

    private VBox buatBubbleBot(ChatResponse response) {
        VBox bubble = new VBox(10);
        bubble.maxWidthProperty().bind(chatScroll.widthProperty().multiply(0.72));
        bubble.getStyleClass().add("bubble-bot");

        Node textContent = buatKontenTeksBot(response);
        if (textContent != null) {
            bubble.getChildren().add(textContent);
        }

        Node gambar = buatAreaGambar(response);
        if (gambar != null) {
            bubble.getChildren().add(gambar);
        }

        return bubble;
    }

    private Node buatKontenTeksBot(ChatResponse response) {
        VBox content = new VBox(8);
        content.prefWidthProperty().bind(chatScroll.widthProperty().multiply(0.66));
        content.maxWidthProperty().bind(chatScroll.widthProperty().multiply(0.66));
        content.getStyleClass().add("bot-formatted-content");

        if (response.hasBlocks()) {
            for (ChatBlock block : response.getBlocks()) {
                Node node = buatNodeBlock(block);
                if (node != null) {
                    content.getChildren().add(node);
                }
            }
        } else if (response.getText() != null && !response.getText().isBlank()) {
            content.getChildren().add(buatLabelTeksBot(response.getText(), "bubble-bot-text"));
        }

        return content.getChildren().isEmpty() ? null : content;
    }

    private Node buatNodeBlock(ChatBlock block) {
        if (block == null) {
            return null;
        }

        switch (block.getType()) {
            case TITLE:
                return buatLabelTeksBot(block.getTitle(), "chat-block-title");
            case SECTION:
                return buatLabelSection(block.getTitle());
            case PARAGRAPH:
                return buatLabelTeksBot(block.getTitle(), "chat-block-paragraph");
            case NOTE:
                return buatLabelTeksBot(block.getTitle(), "chat-block-note");
            case DETAIL_ROW:
                return buatDetailRow(block);
            case SCHEDULE_ROW:
                return buatScheduleRow(block);
            case NUMBERED_ITEM:
                return buatNumberedItem(block);
            case NUMBERED_DETAIL_ITEM:
                return buatNumberedDetailItem(block);
            case LIST_ITEM:
                return buatListItem(block);
            default:
                return null;
        }
    }

    private Label buatLabelSection(String pesan) {
        Label label = new Label(pesan == null ? "" : pesan);
        label.setWrapText(true);
        label.setTextOverrun(OverrunStyle.CLIP);
        label.setMinHeight(Region.USE_PREF_SIZE);
        label.setMaxWidth(Double.MAX_VALUE);
        label.getStyleClass().add("chat-block-section");
        return label;
    }

    private Node buatDetailRow(ChatBlock block) {
        Label label = new Label(block.getTitle());
        label.setMinWidth(76);
        label.setMaxWidth(92);
        label.getStyleClass().add("chat-detail-label");

        Label value = buatLabelTeksBot(block.getValue(), "chat-detail-value");
        HBox.setHgrow(value, Priority.ALWAYS);

        HBox row = new HBox(10, label, value);
        row.setAlignment(Pos.TOP_LEFT);
        row.getStyleClass().add("chat-detail-row");
        return row;
    }

    private Node buatScheduleRow(ChatBlock block) {
        Label day = new Label(block.getTitle());
        day.setWrapText(true);
        day.setMinWidth(118);
        day.setMaxWidth(170);
        day.setMinHeight(Region.USE_PREF_SIZE);
        day.getStyleClass().add("chat-schedule-day");

        Label time = new Label(block.getValue());
        time.setWrapText(true);
        time.setMinHeight(Region.USE_PREF_SIZE);
        time.getStyleClass().add("chat-schedule-time");
        HBox.setHgrow(time, Priority.ALWAYS);

        HBox row = new HBox(12, day, time);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setMaxWidth(Double.MAX_VALUE);
        row.getStyleClass().add("chat-schedule-row");
        return row;
    }

    private Node buatNumberedItem(ChatBlock block) {
        Label title = buatLabelTeksBot(block.getTitle(), "chat-numbered-title");
        Label value = buatLabelTeksBot(block.getValue(), "chat-numbered-value");

        HBox row = new HBox(10, title, value);
        row.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(title, Priority.ALWAYS);
        row.getStyleClass().add("chat-numbered-item");
        return row;
    }

    private Node buatNumberedDetailItem(ChatBlock block) {
        Label title = buatLabelTeksBot(block.getTitle(), "chat-numbered-detail-title");
        Label value = buatLabelTeksBot(block.getValue(), "chat-numbered-detail-value");

        VBox item = new VBox(3, title);
        if (block.getValue() != null && !block.getValue().isBlank()) {
            item.getChildren().add(value);
        }
        item.getStyleClass().add("chat-numbered-detail-item");
        return item;
    }

    private Node buatListItem(ChatBlock block) {
        Label marker = new Label("-");
        marker.setMinSize(18, 18);
        marker.setPrefSize(18, 18);
        marker.setMaxSize(18, 18);
        marker.setAlignment(Pos.CENTER);
        marker.getStyleClass().add("chat-list-marker");

        Label title = buatLabelTeksBot(block.getTitle(), "chat-list-title");
        Label value = buatLabelTeksBot(block.getValue(), "chat-list-value");

        VBox text = new VBox(2, title);
        if (block.getValue() != null && !block.getValue().isBlank()) {
            text.getChildren().add(value);
        }
        HBox.setHgrow(text, Priority.ALWAYS);

        HBox item = new HBox(8, marker, text);
        item.setAlignment(Pos.TOP_LEFT);
        item.getStyleClass().add("chat-list-item");
        return item;
    }

    private Label buatLabelTeksBot(String pesan, String styleClass) {
        Label label = new Label(pesan == null ? "" : pesan);
        label.setWrapText(true);
        label.setTextOverrun(OverrunStyle.CLIP);
        label.setMinHeight(Region.USE_PREF_SIZE);
        label.maxWidthProperty().bind(chatScroll.widthProperty().multiply(0.62));
        label.getStyleClass().add(styleClass);
        return label;
    }

    private Node buatAreaGambar(ChatResponse response) {
        if (!response.hasImage()) {
            return null;
        }

        List<ChatResponse.ChatImage> images = response.getImages();
        if (images.size() == 1 && images.get(0).getTitle().isBlank()) {
            return buatGambarMenu(images.get(0).getImagePath());
        }

        FlowPane galeri = new FlowPane(10, 10);
        galeri.setPrefWrapLength(500);
        galeri.getStyleClass().add("recommendation-gallery");

        for (ChatResponse.ChatImage image : images) {
            Node kartu = buatKartuRekomendasi(image);
            if (kartu != null) {
                galeri.getChildren().add(kartu);
            }
        }

        return galeri.getChildren().isEmpty() ? null : galeri;
    }

    private Node buatGambarMenu(String imagePath) {
        String imageSource = resolveSumberGambar(imagePath);
        if (imageSource == null) {
            return null;
        }

        ImageView imageView = new ImageView(new Image(imageSource));
        imageView.setFitWidth(220);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        StackPane frame = new StackPane(imageView);
        frame.setMinSize(230, 150);
        frame.setPrefSize(230, 150);
        frame.setMaxWidth(230);
        frame.getStyleClass().add("menu-image-frame");
        return frame;
    }

    private Node buatKartuRekomendasi(ChatResponse.ChatImage chatImage) {
        if (chatImage == null) {
            return null;
        }

        String imageSource = resolveSumberGambar(chatImage.getImagePath());
        if (imageSource == null) {
            return null;
        }

        ImageView imageView = new ImageView(new Image(imageSource));
        imageView.setFitWidth(132);
        imageView.setFitHeight(86);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        StackPane imageFrame = new StackPane(imageView);
        imageFrame.setMinSize(140, 92);
        imageFrame.setPrefSize(140, 92);
        imageFrame.setMaxSize(140, 92);
        imageFrame.getStyleClass().add("recommendation-image-frame");

        Label title = new Label(chatImage.getTitle());
        title.setWrapText(true);
        title.setTextOverrun(OverrunStyle.CLIP);
        title.setMinHeight(Region.USE_PREF_SIZE);
        title.setMaxWidth(140);
        title.getStyleClass().add("recommendation-title");

        Label subtitle = new Label(chatImage.getSubtitle());
        subtitle.setWrapText(true);
        subtitle.setMaxWidth(140);
        subtitle.getStyleClass().add("recommendation-subtitle");

        Button detailButton = new Button("Lihat Detail");
        detailButton.setMaxWidth(Double.MAX_VALUE);
        detailButton.getStyleClass().add("recommendation-detail-button");
        detailButton.setOnAction(event -> tampilkanDetailMenu(chatImage.getTitle()));

        VBox card = new VBox(6, imageFrame, title, subtitle, detailButton);
        card.setPrefWidth(156);
        card.setMaxWidth(156);
        card.getStyleClass().add("recommendation-card");
        return card;
    }

    private void tampilkanDetailMenu(String namaMenu) {
        if (namaMenu == null || namaMenu.isBlank()) {
            return;
        }

        kirimPesan("Detail " + namaMenu);
    }

    private String resolveSumberGambar(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            return null;
        }

        for (String candidate : buildKandidatGambar(imagePath.trim())) {
            if (isUrlGambar(candidate)) {
                return candidate;
            }

            String resourcePath = normalisasiResourcePath(candidate);
            if (resourcePath != null && getClass().getResource(resourcePath) != null) {
                return getClass().getResource(resourcePath).toExternalForm();
            }

            String filePath = resolveFilePath(candidate);
            if (filePath != null) {
                return filePath;
            }
        }

        return getClass().getResource(LOGO_PATH) == null
                ? null
                : getClass().getResource(LOGO_PATH).toExternalForm();
    }

    private String[] buildKandidatGambar(String imagePath) {
        if (imagePath.endsWith(".png")) {
            String basePath = imagePath.substring(0, imagePath.length() - 4);
            return new String[]{imagePath, basePath + ".jpg", basePath + ".jpeg"};
        }
        return new String[]{imagePath};
    }

    private String normalisasiResourcePath(String imagePath) {
        if (imagePath.contains("\\") || isUrlGambar(imagePath)) {
            return null;
        }
        if (imagePath.startsWith("/")) {
            return imagePath;
        }
        if (imagePath.startsWith("com/")) {
            return "/" + imagePath;
        }
        if (imagePath.startsWith("asset/")) {
            return "/com/demikopi/uiHandler/" + imagePath;
        }
        return imagePath;
    }

    private boolean isUrlGambar(String imagePath) {
        return imagePath.startsWith("http://")
                || imagePath.startsWith("https://")
                || imagePath.startsWith("file:");
    }

    private String resolveFilePath(String imagePath) {
        try {
            Path path = Path.of(imagePath);
            if (!path.isAbsolute()) {
                path = Path.of("").toAbsolutePath().resolve(path);
            }

            if (Files.isRegularFile(path)) {
                return path.toUri().toString();
            }
        } catch (InvalidPathException e) {
            return null;
        }
        return null;
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
