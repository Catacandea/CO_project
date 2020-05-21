package com.benchmark;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import benchmark.HDD.HDDRandReadSpeed;
import benchmark.HDD.HDDRandWriteSpeed;
import benchmark.HDD.HDDSeqReadSpeed;
import benchmark.HDD.HDDSeqWriteSpeed;
import benchmark.HDD.IBenchmark;
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

	private String seqWriteResult;
	private String seqReadResult;
	private String randWriteResult;
	private String randReadResult;

	public String getSeqWriteResult() {
		return seqWriteResult;
	}

	public String getSeqReadResult() {
		return seqReadResult;
	}

	public String getRandWriteResult() {
		return randWriteResult;
	}

	public String getRandReadResult() {
		return randReadResult;
	}

	private long intSize;
	private String partitionString;

	public long getStringSize() {

		if (stringSize.equals("20 MB")) {
			intSize = 20;
		} else if (stringSize.equals("50 MB")) {
			intSize = 50;
		} else if (stringSize.equals("100 MB")) {
			intSize = 100;
		} else if (stringSize.equals("250 MB")) {
			intSize = 250;
		}

		return intSize;
	}

	public String getStringPartition() {
		if (stringPartition.equals("C:\\")) {
			partitionString = "C";
		} else {
			partitionString = "D";
		}
		return partitionString;
	}

	HDDSeqWriteSpeed hs = new HDDSeqWriteSpeed();

	@FXML
	private ProgressBar progressBar;

	@FXML
	private Label label;

	@FXML
	private ChoiceBox<String> partition, size;

	private Task copyWorker;

	@FXML
	private void Run() {

		progressBar.setProgress(0.0);
		copyWorker = createWorker();

		progressBar.progressProperty().unbind();
		progressBar.progressProperty().bind(copyWorker.progressProperty());
		copyWorker.messageProperty().addListener((observable, oldValue, newValue) -> System.out.println(newValue));
		copyWorker.messageProperty().addListener((observable, oldValue, newValue) -> label.setText(newValue));

		copyWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent workerStateEvent) {
				try {
					AlertBox.display("Finished", "Press the button to see the results");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		new Thread(copyWorker).start();

		stringPartition = partition.getValue();
		stringSize = size.getValue();

		long tempSize = getStringSize();
		String tempPartition = getStringPartition();

		System.out.println(tempPartition);
		System.out.println(tempSize);

		IBenchmark seqWrite = new HDDSeqWriteSpeed();
		IBenchmark seqRead = new HDDSeqReadSpeed();

		try {
			seqWrite.initialize(tempPartition, tempSize);
			seqWrite.warmUp();
			seqWrite.run();

			seqWriteResult = seqWrite.getResult();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			seqRead.initialize(tempPartition, tempSize);
			seqRead.warmUp();
			seqRead.run();

			seqReadResult = seqRead.getResult();
		} catch (IOException e) {
			e.printStackTrace();
		}
		IBenchmark randWrite = new HDDRandWriteSpeed();
		IBenchmark randRead = new HDDRandReadSpeed();
		try {
			randWrite.initialize(tempPartition, tempSize);
			randWrite.warmUp();
			randWrite.run();

			randWriteResult = randWrite.getResult();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			randRead.initialize(tempPartition, tempSize);
			randRead.warmUp();
			randRead.run();

			randReadResult = randRead.getResult();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			FileWriter myWriter = new FileWriter("results.txt");
			myWriter.write(seqReadResult + "\n" + seqWriteResult + "\n" + randReadResult + "\n" + randWriteResult);
			myWriter.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		System.out.println(tempPartition);
		System.out.println(tempSize);

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		partition.getItems().removeAll(partition.getItems());
		partition.getItems().addAll("C:\\", "D:\\");
		partition.getSelectionModel().select("C:\\");

		size.getItems().removeAll(size.getItems());
		size.getItems().addAll("20 MB", "50 MB", "100 MB", "250 MB");
		size.getSelectionModel().select("20 MB");
	}

	public Task createWorker() {

		return new Task() {

			@Override
			protected Object call() throws Exception {
				for (int i = 0; i <= 100; i++) {
					if (i == 0)
						Thread.sleep(1000);
					// Thread.sleep(100);
					Thread.sleep(50);
					updateMessage("Task Completed : " + (i) + "%");
					updateProgress(i, 100);

				}
				return true;
			}
		};
	}

}