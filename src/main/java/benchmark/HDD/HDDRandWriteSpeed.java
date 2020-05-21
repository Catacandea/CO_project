package benchmark.HDD;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

import timing.TimeUnit;
import timing.Timer;

public class HDDRandWriteSpeed implements IBenchmark {
	private String PATH;
	private String fileExtension;
	private long fileSize, filesToWrite;
	private double writeSpeed;
	private static final int MIN_BUFFER_SIZE = 1024 * 1; // 1 KB
	private static final int MAX_BUFFER_SIZE = 1024 * 1024 * 32; // 32 MB
	private int bufferSize;
	private int steps;
	private int fileCounter;

	@Override
	public void initialize(Object... parameters) throws IOException {
		String partition = (String) parameters[0];
		fileSize = (Long) parameters[1] * (1024 * 1024);
		filesToWrite = 10;
		fileCounter = 0;
		PATH = partition + ":\\HDDBenchmark\\RAND\\writeRand-";
		/// te ai prins? noua de trebe "D:\\Benchmark\\RAND" atat, fara writeRand- ala,
		/// pentru ca ala e o parte din numele fisieruulu
		fileExtension = ".dat";
		bufferSize = MIN_BUFFER_SIZE;
	}

	@Override
	public void run(Object... parameters) throws IOException {
		int fileIndex = 0;
		String fileName;
		while (bufferSize < MAX_BUFFER_SIZE && fileCounter < filesToWrite) {
			fileName = PATH + fileIndex + fileExtension;
			steps = (int) (fileSize / bufferSize);
			writeRandFile(fileName, bufferSize);
			bufferSize *= 2;
			fileCounter++;
			fileIndex++;
		}
	}

	private void writeRandFile(String fileName, int bufferSize) throws IOException {

		File folderPath = new File(fileName.substring(0, fileName.lastIndexOf(File.separator)));

		if (!folderPath.isDirectory())
			folderPath.mkdirs();

		File fs = new File(fileName);
		RandomAccessFile file = new RandomAccessFile(fileName, "rw");
		int counter = 0;
		byte[] bytes = new byte[bufferSize];
		Timer timer = new Timer();
		Random rand = new Random();
		timer.start();
		while (counter++ < steps) {
			file.seek(rand.nextInt((int) fileSize));
			rand.nextBytes(bytes);
			file.write(bytes);
		}
		TimeUnit tu = new TimeUnit(timer.stop());
		final double time = tu.convert_to_seconds();

		file.close();
		// fs.delete();
		double seconds = time;
		double megabytes = (double) (fileSize) / (1024 * 1024);
		double rate = megabytes / seconds;
		System.out.println(megabytes + "MB were written to " + fileName + " in " + seconds + " seconds");
		System.out.println("rate: " + rate);
		writeSpeed += rate;
	}

	@Override
	public void warmUp(Object... params) throws IOException {
		int myBufferSize = 4 * 1024 * 1024;

		RandomAccessFile file = new RandomAccessFile("tmp.txt", "rw");

		byte[] buffer = new byte[myBufferSize];
		long toWrite = fileSize / myBufferSize;

		Random rand = new Random();
		int i = 0;

		while (i < toWrite) {
			file.seek(rand.nextInt((int) fileSize));
			rand.nextBytes(buffer);
			file.write(buffer);
			i++;
		}
		file.close();

		(new File("tmp.txt")).delete();

	}

	@Override
	public String getResult() {

		NumberFormat nf = new DecimalFormat("#.00");
		double result = writeSpeed / filesToWrite;

		return nf.format(result) + "MB/S";
	}

}
