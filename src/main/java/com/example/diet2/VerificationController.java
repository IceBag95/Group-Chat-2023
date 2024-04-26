package com.example.diet2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class VerificationController implements Initializable {

    @FXML
    private TextField usernameInput;
    @FXML
    private TextField passwordInput;
    @FXML
    private Label errorLabel;

    // Invalid username or password Please try again...

    FXMLLoader fxmlLoader;
    Stage stage;
    Scene scene;
    String username, password;

    Client client;


    // TODO: CHECKS FOR PASSWORD AND USERNAME

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            client = new Client(new Socket("localhost", 1234));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void assignCredentials(){
        username = usernameInput.getText();
        password = passwordInput.getText();
    }

    public void sendCredentialsToServer(){

        client.writeOnBuffer(username);

        client.writeOnBuffer(password);
    }



    public void SwitchToClient(ActionEvent e){
        errorLabel.setText("");
        assignCredentials();
        if (username.length() >= 6 && password.length() >= 6) {
            //Send username to server for verification and later use
            sendCredentialsToServer();

            boolean acceptUser;
            //TODO: RECIEVE OK TO OPEN CLIENT IF CREDENTIALS ARE OK FROM SERVER

            //Load Client
            loadClientWindow(e);
        }
        else{
            errorLabel.setText("Invalid username or password Please try again...");
            usernameInput.setText("");
            passwordInput.setText("");
        }
    }

    public void loadClientWindow(ActionEvent e){
        fxmlLoader = new FXMLLoader(ClientApp.class.getResource("ClientWindow.fxml"));

        try {
            ClientController clientController = new ClientController(client);
            fxmlLoader.setController(clientController);
            scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(getClass().getResource("ClientWindowStyle.css").toExternalForm());
            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setResizable(false);
            stage.setTitle("Chat");
            stage.setScene(scene);
            stage.setOnCloseRequest(windowEvent -> {
                client.close();
                System.out.println("Application closed successfully...");
            });
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
