/*
 * Copyright (C) 2022, 2023 - Tillitis AB
 * SPDX-License-Identifier: GPL-2.0-only
 */
package main;
import Controllers.*;
import com.tillitis.TkeyClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    private static String filePath;
    public static boolean connected;
    public static CommonController commonController;
    @Override
    public void start(Stage primaryStage) throws Exception {
        commonController = new CommonController();
        Parent root = FXMLLoader.load(getClass().getResource("/Views/start.fxml"));
        primaryStage.setTitle("TKey GUI Client");
        primaryStage.getIcons().add(new Image("file:ant.png"));
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("TKey GUI Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void setFilePath(String path){
        filePath = path;
    }

    public static void loadApp(byte[] uss) throws Exception {
        TkeyClient.loadAppFromFile(filePath,uss);
    }

    public static Parent loadFirstView() throws Exception {
        return FXMLLoader.load(Main.class.getResource("/Views/start.fxml"));
    }

    public static Parent loadSecondView() throws Exception {
        return FXMLLoader.load(Main.class.getResource("/Views/signer.fxml"));
    }

    public static void main(String[] args) {
        launch(args);
    }

}
