package com.benchmark;

import java.io.IOException;

import benchmark.HDD.HDDRandReadSpeed;
import benchmark.HDD.HDDRandWriteSpeed;
import benchmark.HDD.HDDSeqReadSpeed;
import benchmark.HDD.HDDSeqWriteSpeed;
import benchmark.HDD.IBenchmark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

	private static Scene scene;

	@Override
	public void start(Stage stage) throws IOException {
		scene = new Scene(loadFXML("primary"));
		stage.setScene(scene);
		stage.show();
	}

	static void setRoot(String fxml) throws IOException {
		scene.setRoot(loadFXML(fxml));
	}

	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}

	public static void main(String[] args) {
		launch();
		IBenchmark seqWrite = new HDDSeqWriteSpeed();
		IBenchmark seqRead = new HDDSeqReadSpeed();

		try {
			seqWrite.initialize("D", 400L);
			seqWrite.warmUp();
			seqWrite.run();

			System.out.println(seqWrite.getResult());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			seqRead.initialize("D", 400L);
			seqRead.warmUp();
			seqRead.run();

			System.out.println(seqRead.getResult());
		} catch (IOException e) {
			e.printStackTrace();
		}
		IBenchmark randWrite = new HDDRandWriteSpeed();
		IBenchmark randRead = new HDDRandReadSpeed();
		try {
			randWrite.initialize("D", 50L);
			randWrite.warmUp();
			randWrite.run();

			System.out.println(randWrite.getResult());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			randRead.initialize("D", 50L);
			randRead.warmUp();
			randRead.run();

			System.out.println(randRead.getResult());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}