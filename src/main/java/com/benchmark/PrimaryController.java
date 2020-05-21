package com.benchmark;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class PrimaryController implements Initializable {

    private String stringSize, stringPartition;

    private int intSize;
    private String partitionString;

    public int getStringSize() {

        if (stringSize.equals("500 MB")) {
            intSize = 500;
        } else if (stringSize.equals("1 GB")){
            intSize = 1024;
        }else if(stringSize.equals("5 GB")){
            intSize = 5120;
        }else{
            intSize = 10240;
        }

        return intSize;
    }

    public String getStringPartition() {
        if(stringPartition.equals("C:\\")){
            partitionString = "C";
        }else{
            partitionString = "D";
        }
        return partitionString;
    }

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label label;

    @FXML
    private ChoiceBox <String> partition, size;

    private Task copyWorker;


    @FXML
    private void Run() {

        progressBar.setProgress(0.0);
        copyWorker = createWorker();

        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(copyWorker.progressProperty());
        copyWorker.messageProperty().addListener((observable, oldValue, newValue) -> System.out.println(newValue));
        copyWorker.messageProperty().addListener((observable, oldValue, newValue) -> label.setText(newValue));

        new Thread(copyWorker).start();

        stringPartition = partition.getValue();
        stringSize = size.getValue();

        System.out.println(stringPartition);
        System.out.println(stringSize);

        copyWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                AlertBox.display("Cfff", "bnnn");
            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partition.getItems().removeAll(partition.getItems());
        partition.getItems().addAll("C:\\","D:\\");
        partition.getSelectionModel().select("C:\\");

        size.getItems().removeAll(size.getItems());
        size.getItems().addAll("500 MB","1 GB","5 GB","10 GB");
        size.getSelectionModel().select("500 MB");
    }


    public Task createWorker() {

        return new Task() {

            @Override
            protected Object call() throws Exception {
                for (int i = 0; i <= 100; i++) {
                   if(i == 0)
                       Thread.sleep(1000);
                       // Thread.sleep(100);
                       Thread.sleep(10);
                       updateMessage("Task Completed : " + (i) + "%");
                       updateProgress(i, 100);

                }
                return true;
            }
        };
    }

    @FXML
    private Label sizeLabel, partitionLabel;

    public void SeeResults()
    {
        stringPartition = partition.getValue();
        stringSize = size.getValue();

        sizeLabel.setText(getStringPartition());
        partitionLabel.setText( Integer.toString(getStringSize()) );

        System.out.println(getStringPartition());
        System.out.println(getStringSize());
        //AlertBox.display("Cf","bn");
    }


//    @FXML
//    public void switchSec() throws IOException{
        //App.setRoot("secondary");
//        System.out.println(getStringPartition());
//        System.out.println(getStringSize());
//
//        sizeLabel.setText(getStringPartition());
//        partitionLabel.setText( Integer.toString(getStringSize()) );
   // }
}