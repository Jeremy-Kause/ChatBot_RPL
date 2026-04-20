package com.demikopi.uiHandler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminUI extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(AdminUI.class.getResource("/com/demikopi/uiHandler/Admin UI/login.fxml"));
        Scene scene = new Scene(loader.load(), 900, 600);
        stage.setTitle("DemiKopi Admin");
        stage.setScene(scene);
        stage.show();
    }
}
