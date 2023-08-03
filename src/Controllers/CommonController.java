package Controllers;/*
 * Copyright (C) 2022, 2023 - Tillitis AB
 * SPDX-License-Identifier: GPL-2.0-only
 */
import com.tillitis.TkeyClient;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import main.Main;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Controller for buttons and behavior shared accross both views.
 */
public class CommonController {
    private static boolean connected = false;
    private static boolean locked = false;

    public CommonController() {}

    /**
     * For connecting to a TKey.
     */
    public boolean commonConnect(TextArea textBox) {
        if (connected && !TkeyClient.getHasCon()) {
            System.out.println(TkeyClient.getHasCon());
            connected = false;
        }
        try {
            if (!connected) {
                TkeyClient.connect();
                textBox.appendText("Connected to the TKey successfully!" + "\n");
                connected = true;
            } else {
                textBox.appendText("Already connected to the Tkey!" + "\n");
            }
        } catch (Exception e) {
            textBox.appendText("Failed to connect!" + "\n");
            TkeyClient.close();
            connected = false;
            System.out.println(TkeyClient.getHasCon());
        }
        Main.connected = connected;
        return connected;
    }

    /**
     * For loading an app
     */
    public void commonLoadApp(boolean hasFile, TextArea textBox, byte[] uss) {
        if (!hasFile) {
            textBox.appendText("No file specified! \n");
        } else if (!connected) {
            textBox.appendText("TKey connection not found! \n");
        } else {
            textBox.appendText("App loading in progress. \n");

            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    Main.loadApp(uss);
                    return true;
                }
            };

            task.setOnSucceeded(event -> {
                if (task.getValue()) {
                    textBox.appendText("App Loaded!" + "\n");
                    connected = true;
                    locked = true;
                } else {
                    textBox.appendText("Failed to load the app. \n");
                }
            });

            task.setOnFailed(event -> {
                Throwable e = task.getException();
                textBox.appendText("Failed to load the app. Error: " + e.getMessage() + "\n");
            });
            new Thread(task).start();
        }
    }
    public boolean getLocked(){
        return locked;
    }
}
