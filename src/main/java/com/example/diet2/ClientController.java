package com.example.diet2;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {


    // FXML Variables
    @FXML
    private VBox messageVBox;
    @FXML
    private Button sendMessage;
    @FXML
    private TextField messageInput;
    @FXML
    private ScrollPane messageScrollPane;
    @FXML
    private Label usernameLabel;


    // Class Variables
    String message;
    String username = "";
    Socket socket;
    BufferedReader br;
    BufferedWriter bw;
    Client client;


    //TODO: METHOD FOR ENCODING AND DECODING TO AND FROM JSON FILES

    // Constructor
    public ClientController(Client client){
        this.client = client;
        socket = client.getSocket();

        try {
            br = client.getBr();
            bw = client.getBw();

            //TODO: CAUSION: JSONSTRING TO STRING CORVERSION
            username = br.readLine();




        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    // Helping Functions
    public void appendOwnMessageOnScreen() {
        System.out.println("Button Pressed");
        String messageToSend = messageInput.getText().trim();
        if (!messageToSend.isEmpty()) {

            //Creating the HBox and applying style
            HBox sentMessageHBox = new HBox();
            sentMessageHBox.getStyleClass().add("clientsOwnMessageHBox");
            sentMessageHBox.setAlignment(Pos.CENTER_RIGHT);
            //sentMessageHBox.getClass().getResource("ClientWindowStyle.css").toExternalForm();

            //creating label with text set to be messageToSend and styling
            Label messageLabel = new Label(messageToSend);
            messageLabel.setWrapText(true);
            messageLabel.getStyleClass().add("clientsOwnMessageLabel");
            sentMessageHBox.getChildren().add(messageLabel);

            //adding message's HBox to scrollPane
            messageVBox.getChildren().add(sentMessageHBox);
            System.out.println(username + ": " + messageToSend);
            broadcastMessage(messageToSend);
            //Emptying input for further use
            messageInput.clear();
        }
    }


    public void appendForeignMessages(){
        //Creating the HBox and applying style
        HBox sentMessageHBox = new HBox();
        sentMessageHBox.getStyleClass().add("clientsOwnMessageHBox");
        sentMessageHBox.setAlignment(Pos.CENTER_LEFT);

        //creating label with text set to be messageToSend and styling
        //TODO: CAUSION JSONSTRING INCOMING. NEED CONVERSION TO STRING AND KEEP ONLY MESSAGE

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-background-color: #dddddd; -fx-text-fill: #000000;");
        messageLabel.getStyleClass().add("clientsOwnMessageLabel");
        sentMessageHBox.getChildren().add(messageLabel);

        //adding message's HBox to scrollPane
        // messageVBox.getChildren().add(sentMessageHBox); TODO: WRONG WAY APPARENTLY
        Platform.runLater(() -> messageVBox.getChildren().add(sentMessageHBox)); //TODO: RIGHT WAY APPARENTLY
    }


    // Logic Functions for GUI
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        usernameLabel.setText(username);
        messageScrollPane.setFocusTraversable(false);
        messageVBox.setFocusTraversable(false);


        messageVBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                messageScrollPane.setVvalue((Double) t1);

            }

        });
        readMessagesFromBuffer();
        sendOwnMessage();


    }

    public void sendOwnMessageWithEnterKey(){
        messageInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                appendOwnMessageOnScreen();
            }
        });

    }

    public void sendOwnMessage() {
        sendMessage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                appendOwnMessageOnScreen();

            }

        });

    }

    public void broadcastMessage(String message){
        try {

            //TODO: CAUSION JSONSTRING INCOMING. NEED CONVERSION TO STRING AND KEEP ONLY MESSAGE
            bw.write(message);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            closeEverything(socket, bw, br);
        }

    }


    public void readMessagesFromBuffer(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()) {
                    try {
                        message = br.readLine();
                        //TODO: REAMKE OBJECT FOR OBJECTMAPPER

                        // append message received to chat
                        appendForeignMessages();
                    } catch (IOException e) {
                        System.out.println("Application is closing");
                        System.out.println("Exception caught: Socket already closed.\nWe wanted that to happen to close the Thread right..??");
                        break;
                    }
                }
            }
        }).start();

    }




    public void closeEverything(Socket socket, BufferedWriter bw, BufferedReader br){
        try {
            if (socket != null) {
                socket.close();
            }
            if (bw != null) {
                bw.close();
            }
            if (br != null) {
                br.close();
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }

    }




}