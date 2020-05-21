package benchmark.HDD;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import timing.TimeUnit;
import timing.Timer;

public class HDDSeqReadSpeed implements IBenchmark {

	private static final int MIN_BUFFER_SIZE = 1024 * 1; // 1 KB

	private String PATH;
	private long fileSize;
	private double readSpeed;
	private int filesToRead = 10;

	private Timer timer = new Timer();

	@Override
	public void initialize(Object... params) {
		String partition = (String) params[0];
		fileSize = (Long) params[1] * (1024 * 1024);
		PATH = partition + ":\\HDDBenchmark\\SEQ"; // D:\\HDDBenchmark
	}

	@Override
	public void warmUp(Object... params) {

	}

	@Override
	public void run(Object... params) throws IOException {

		readSpeed = 0;

		int myBufferSize = MIN_BUFFER_SIZE;

		final File directory = new File(PATH);

		for (File file : directory.listFiles()) {

			FileInputStream fr = new FileInputStream(file);

			byte[] buffer = new byte[myBufferSize];
			long toRead = fileSize / myBufferSize;

			int i = 0;

			timer.start();

			while (i < toRead) {

				fr.read(buffer);
				i++;
			}
			TimeUnit tu = new TimeUnit(timer.stop());
			final double time = tu.convert_to_seconds();

			fr.close();
			file.delete();

			double seconds = time;
			double megabytes = (double) (fileSize) / (1024 * 1024);
			double rate = megabytes / seconds;

			System.out.println(megabytes + "MB were read from " + file.getName() + " in " + seconds + " seconds");
			System.out.println("rate: " + rate);

			myBufferSize *= 2;
			readSpeed += rate;
		}
	}

	@Override
	public String getResult() {
		NumberFormat nf = new DecimalFormat("#.00");
		double result = readSpeed / filesToRead;

		return nf.format(result) + "MB/S";
	}
}
