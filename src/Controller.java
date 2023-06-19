import com.knek.TkeyClient;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.awt.event.ActionEvent;
import java.io.File;

public class Controller {
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Button button5;
    @FXML
    private Button button6;
    @FXML
    private TextArea textBox;


    private String name = "";

    private Boolean connected = false;

    @FXML
    private void button1Clicked() throws Exception {
        if(connected && !TkeyClient.getHasCon()){
            System.out.println(TkeyClient.getHasCon());
            connected = false;
        }
        try{
            if(!connected){
                TkeyClient.connect();
                textBox.appendText("Connected to the TKey successfully!" + "\n");
                connected = true;
            }else{
                textBox.appendText("Already connected to the Tkey!" + "\n");
            }
        }catch (Exception e){
            textBox.appendText("Failed to connect!" + "\n");
            TkeyClient.close();
            connected = false;
            TkeyClient.reconnect();
            System.out.println(TkeyClient.getHasCon());
        }
    }

    @FXML
    private void button2Clicked(){
        try{
            if(!name.equals("")){
                textBox.appendText("Name & Version: " + name + "\n");
            }else{
                name = TkeyClient.getNameVersion();
                textBox.appendText("Name & Version: " + name + "\n");
            }
        }catch (Exception e){
            textBox.appendText("Failed to get name! Have you connected to the TKey first?" + "\n");
        }
    }

    @FXML
    private void button3Clicked() {
        FileChooser fileChooser = new FileChooser();

        // Get the Stage from the ActionEvent source
        Window stage = button3.getScene().getWindow();

        // Show open file dialog
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            String filePath = file.getAbsolutePath();
            Main.setFilePath(filePath);
            textBox.appendText("File found. Click 'Load App' to load it to the Tkey" + "\n");
        }
    }

    @FXML
    private void button4Clicked() {
        textBox.setText("Button 4 clicked!");
    }

    @FXML
    private void button5Clicked() {
        textBox.setText("Button 5 clicked!");
    }

    @FXML
    private void button6Clicked() {
        try{
            Main.loadApp();
        }catch (Exception e){
            textBox.appendText("Loading app failed!" + "\n");
        }
        textBox.setText("Button 6 clicked!");
    }
}
