package com.benchmark;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class SecondaryController implements Initializable {

    private PrimaryController pc;

    @FXML
    private Label seqWrite, seqRead, rndWrite, rndRead;

    //@Override
    public void initialize(URL url, ResourceBundle rb) {
//        seqWrite.setText(pc.getSeqWriteResult());
//        seqRead.setText(pc.getSeqReadResult());
//        rndWrite.setText(pc.getRandWriteResult());
//        rndRead.setText(pc.getRandReadResult());

        try{
            File myObj = new File("results.txt");
            Scanner myReader = new Scanner(myObj);
            int i = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(i == 0)
                    seqRead.setText(data);
                else if ( i == 1)
                    seqWrite.setText(data);
                else if ( i == 2)
                    rndWrite.setText(data);
                else if ( i == 3)
                    rndRead.setText(data);
                i++;
            }
            myReader.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void switchBack() throws IOException {
        App.setRoot("primary");
    }

}