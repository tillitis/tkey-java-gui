/*
 * Copyright (C) 2022, 2023 - Tillitis AB
 * SPDX-License-Identifier: GPL-2.0-only
 */
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class ButtonBarController {

    @FXML
    private Button menu1;
    @FXML
    private Button menu2;

    @FXML
    private void menu1Clicked() throws Exception {
        Scene scene = menu1.getScene();
        scene.setRoot(Main.loadFirstView());
    }

    @FXML
    private void menu2Clicked() throws Exception {
        Scene scene = menu2.getScene();
        scene.setRoot(Main.loadSecondView());
    }

}
