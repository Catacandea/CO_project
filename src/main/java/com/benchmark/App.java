package com.benchmark;

import java.io.IOException;

import benchmark.HDD.HDDRandomAccess;
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
		// launch();

		/*
		 * HDDWriteSpeed hws = new HDDWriteSpeed(); hws.initialize(); hws.warmUp();
		 * 
		 * System.out.println(hws.run("fs", false, 1000, 'C'));
		 * 
		 */ HDDRandomAccess hra = new HDDRandomAccess();

		hra.initialize(1024 * 1024, 'D');
		try {
			hra.warmUp();
			System.out.println(hra.run("r", "fs", 40 * 1024));
			System.out.println(hra.run("r", "ft", 40 * 1024));
			System.out.println(hra.run("w", "fs", 40 * 1024));
			System.out.println(hra.run("w", "ft", 40 * 1024));
			hra.clean();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}