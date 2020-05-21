package com.benchmark;

import javafx.fxml.FXMLLoader;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import com.benchmark.App;

import java.io.IOException;

public class AlertBox {

    public static void display(String title, String message) throws IOException
    {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label1 =  new Label();
        label1.setText(message);

        Button closeButton = new Button("Ok");
        closeButton.setOnAction(e-> {
            window.close();
                    try {
                        App.setRoot("secondary");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
        );

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1,closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}