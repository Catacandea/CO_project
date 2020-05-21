package benchmark.HDD;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

import timing.TimeUnit;
import timing.Timer;

public class HDDSeqWriteSpeed implements IBenchmark {

	private static final int MIN_BUFFER_SIZE = 1024 * 1; // 1 KB
	private static final int MAX_BUFFER_SIZE = 1024 * 1024 * 32; // 32 MB

	private String PATH;
	private String fileExtension;
	private long fileSize, filesToWrite;
	private double writeSpeed;
	private Timer timer = new Timer();

	@Override
	public void initialize(Object... params) {
		String partition = (String) params[0];
		fileSize = (Long) params[1] * (1024 * 1024);
		filesToWrite = 10;
		PATH = partition + ":\\HDDBenchmark\\SEQ\\write-";
		fileExtension = ".dat";
	}

	@Override
	public void warmUp(Object... params) throws IOException {

		int myBufferSize = 4 * 1024 * 1024;

		File file = new File("tmp.txt");
		final FileOutputStream fileStream = new FileOutputStream(file);
		final BufferedOutputStream outputStream = new BufferedOutputStream(fileStream, myBufferSize);

		byte[] buffer = new byte[myBufferSize];
		long toWrite = fileSize / myBufferSize;

		Random rand = new Random();
		int i = 0;

		while (i < toWrite) {

			rand.nextBytes(buffer);
			outputStream.write(buffer);
			i++;
		}

		outputStream.close();
		file.delete();
	}

	@Override
	public void run(Object... params) throws IOException {

		writeSpeed = 0;

		int currentBufferSize = MIN_BUFFER_SIZE;
		int fileIndex = 0;
		int fileCounter = 0;
		String fileName;

		while (currentBufferSize < MAX_BUFFER_SIZE && fileCounter < filesToWrite) {
			fileName = PATH + fileIndex + fileExtension;
			writeInFile(fileName, currentBufferSize);
			currentBufferSize *= 2;
			fileCounter++;
			fileIndex++;
		}
	}

	private void writeInFile(String fileName, int myBufferSize) throws IOException {

		File folderPath = new File(fileName.substring(0, fileName.lastIndexOf(File.separator)));

		if (!folderPath.isDirectory())
			folderPath.mkdirs();

		final File file = new File(fileName);
		final FileOutputStream fileStream = new FileOutputStream(file);
		final BufferedOutputStream outputStream = new BufferedOutputStream(fileStream, myBufferSize);

		byte[] buffer = new byte[myBufferSize];
		long toWrite = fileSize / myBufferSize;

		Random rand = new Random();
		int i = 0;

		timer.start();

		while (i < toWrite) {

			rand.nextBytes(buffer);
			outputStream.write(buffer);
			i++;
		}
		TimeUnit tu = new TimeUnit(timer.stop());
		final double time = tu.convert_to_seconds();

		outputStream.close();
		file.deleteOnExit();

		double seconds = time;
		double megabytes = (double) (fileSize) / (1024 * 1024);
		double rate = megabytes / seconds;
		System.out.println(megabytes + "MB were written to " + fileName + " in " + seconds + " seconds");
		System.out.println("rate: " + rate);
		writeSpeed += rate;
	}

	@Override
	public String getResult() {

		NumberFormat nf = new DecimalFormat("#.00");
		double result = writeSpeed / filesToWrite;

		return nf.format(result) + "MB/S";
	}

}
