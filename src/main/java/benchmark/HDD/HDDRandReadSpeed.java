package benchmark.HDD;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

import timing.TimeUnit;
import timing.Timer;

public class HDDRandReadSpeed implements IBenchmark {
	private String PATH;
	private String fileExtension;
	private long fileSize, filesToRead;
	private double readSpeed;
	private static final int MIN_BUFFER_SIZE = 1024 * 1; // 1 KB
	private static final int MAX_BUFFER_SIZE = 1024 * 1024 * 32; // 32 MB
	private int bufferSize;
	private int steps;
	private int fileCounter;
	private String dirPATH;

	@Override
	public void initialize(Object... parameters) throws IOException {
		String partition = (String) parameters[0];
		fileSize = (Long) parameters[1] * (1024 * 1024);
		filesToRead = 10;
		fileCounter = 0;
		PATH = partition + ":\\HDDBenchmark\\RAND\\writeRand-";
		dirPATH = partition + ":\\HDDBenchmark\\RAND";
		fileExtension = ".dat";
		bufferSize = MIN_BUFFER_SIZE;
	}

	@Override
	public void run(Object... parameters) throws IOException {
		readSpeed = 0;
		Timer timer = new Timer();

		final File directory = new File(dirPATH);

		for (File file : directory.listFiles()) {
			RandomAccessFile fr = new RandomAccessFile(file, "rw");
			long steps = fileSize / bufferSize;
			int counter = 0;
			byte[] bytes = new byte[bufferSize];
			Random rand = new Random();
			timer.start();

			while (counter++ < steps) {
				fr.seek(rand.nextInt((int) fileSize));
				fr.read(bytes);
			}
			TimeUnit tu = new TimeUnit(timer.stop());
			final double time = tu.convert_to_seconds();
			fr.close();
			file.delete();
			double seconds = time;
			double megabytes = (double) (fileSize) / (1024 * 1024);
			double rate = megabytes / seconds;
			System.out.println(megabytes + "MB were read to " + file.getName() + " in " + seconds + " seconds");
			System.out.println("rate: " + rate);
			readSpeed += rate;
			bufferSize *= 2;
		}
	}

	@Override
	public void warmUp(Object... params) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getResult() {
		NumberFormat nf = new DecimalFormat("#.00");
		double result = readSpeed / filesToRead;

		return nf.format(result) + "MB/S";
	}
}
