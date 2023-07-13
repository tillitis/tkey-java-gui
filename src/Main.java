/*
 * Copyright (C) 2022, 2023 - Tillitis AB
 * SPDX-License-Identifier: GPL-2.0-only
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.tillitis.*;

public class Main extends Application {

    private static String filePath;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("TKey GUI Client");
        primaryStage.getIcons().add(new Image("file:ant.png"));
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("TKey GUI Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    static void setFilePath(String path){
        filePath = path;
    }

    static void loadApp() throws Exception {
        TkeyClient.loadAppFromFile(filePath);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
