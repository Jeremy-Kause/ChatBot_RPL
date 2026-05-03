package com.demikopi.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UserUI extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(UserUI.class.getResource("/com/demikopi/uiHandler/USER UI/dashboard.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);
        stage.setTitle("DemiKopi Coffee Assistant");
        stage.setScene(scene);
        stage.show();
    }
}
// Done
