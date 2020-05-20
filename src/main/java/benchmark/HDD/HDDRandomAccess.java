package benchmark.HDD;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

import timing.Timer;

public class HDDRandomAccess implements IBenchmark {

	private String PATH = ":\\HDD_Benchmark\\file.dat";
	private String result;

	@Override
	public void initialize(Object... params) {
		PATH = (char) params[1] + PATH;
		File tempFile = new File(PATH);
		RandomAccessFile rafFile;
		long fileSizeInBytes = Long.parseLong(String.valueOf(params[0]));

		// Create a temporary file with random content to be used for
		// reading and writing
		try {
			rafFile = new RandomAccessFile(tempFile, "rw");
			Random rand = new Random();
			int bufferSize = 4 * 1024;
			long toWrite = fileSizeInBytes / bufferSize;
			byte[] buffer = new byte[bufferSize];
			long counter = 0;

			while (counter++ < toWrite) {
				rand.nextBytes(buffer);
				rafFile.write(buffer);
			}
			rafFile.close();
			// tempFile.deleteOnExit();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public void warmUp() throws IOException {
		new RandomAccess().randomWriteFixedSize(PATH, 4 * 1024, 10000);
		new RandomAccess().randomReadFixedSize(PATH, 4 * 1024, 10000);
		new RandomAccess().randomWriteFixedTime(PATH, 4 * 1024, 5000);
		new RandomAccess().randomReadFixedTime(PATH, 4 * 1024, 5000);
	}

	@Override
	public String run(Object... options) {
		// ex. {"r", "fs", 4*1024}
		Object[] param = options;
		// used by the fixed size option
		final int steps = 100000;
		// used by the fixed time option
		final int runtime = 10000; // ms

		try {
			// read benchmark
			if (String.valueOf(param[0]).toLowerCase().equals("r")) {
				// buffer size given as parameter
				int bufferSize = Integer.parseInt(String.valueOf(param[2]));

				// read a fixed size and measure time
				if (String.valueOf(param[1]).toLowerCase().equals("fs")) {

					long timeMs = new RandomAccess().randomReadFixedSize(PATH, bufferSize, steps);
					result = steps + " random reads in " + timeMs + " ms [" + (steps * bufferSize / 1024 / 1024)
							+ " MB, " + 1.0 * (steps * bufferSize / 1024 / 1024) / timeMs * 1000 + "MB/s]";
				}
				// read for a fixed time amount and measure time
				else if (String.valueOf(param[1]).toLowerCase().equals("ft")) {

					int ios = new RandomAccess().randomReadFixedTime(PATH, bufferSize, runtime);
					result = ios + " I/Os per second [" + (ios * bufferSize / 1024 / 1024) + " MB, "
							+ 1.0 * (ios * bufferSize / 1024 / 1024) / runtime * 1000 + "MB/s]";
				}

			}
			// write benchmark
			else if (String.valueOf(param[0]).toLowerCase().equals("w")) {
				int bufferSize = Integer.parseInt(String.valueOf(param[2]));
				if (String.valueOf(param[1]).toLowerCase().equals("fs")) {

					long timeMs = new RandomAccess().randomWriteFixedSize(PATH, bufferSize, steps);
					result = steps + " random writes in " + timeMs + " ms [" + (steps * bufferSize / 1024 / 1024)
							+ " MB, " + 1.0 * (steps * bufferSize / 1024 / 1024) / timeMs * 1000 + "MB/s]";
				}
				// read for a fixed time amount and measure time
				else if (String.valueOf(param[1]).toLowerCase().equals("ft")) {

					int ios = new RandomAccess().randomWriteFixedTime(PATH, bufferSize, runtime);
					result = ios + " I/Os per second [" + (ios * bufferSize / 1024 / 1024) + " MB, "
							+ 1.0 * (ios * bufferSize / 1024 / 1024) / runtime * 1000 + "MB/s]";
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void clean() {
		new File(PATH).deleteOnExit();
	}

	@Override
	public void cancel() {
	}

	class RandomAccess {
		private Random random;

		RandomAccess() {
			random = new Random();
		}

		/**
		 * Reads data from random positions into a fixed size buffer from a given file
		 * using RandomAccessFile
		 * 
		 * @param filePath   Full path to file on disk
		 * @param bufferSize Size of byte buffer to read at each step
		 * @param toRead     Number of steps to repeat random read
		 * @return Amount of time needed to finish given reads in milliseconds
		 * @throws IOException
		 */
		public long randomReadFixedSize(String filePath, int bufferSize, int toRead) throws IOException {
			// file to read from
			RandomAccessFile file = new RandomAccessFile(filePath, "rw");
			// size of file
			int fileSize = (int) (file.getChannel().size() % Integer.MAX_VALUE);
			// counter for number of reads
			int counter = 0;
			// buffer for reading
			byte[] bytes = new byte[bufferSize];
			// timer
			Timer timer = new Timer();

			Random rand = new Random();
			timer.start();
			while (counter++ < toRead) {
				// go to random spot in file
				file.seek(rand.nextInt(fileSize));
				// read the bytes into buffer
				file.read(bytes);
			}

			file.close();
			return timer.stop() / 1000000; // ns to ms!
		}

		public long randomWriteFixedSize(String filePath, int bufferSize, int toWrite) throws IOException {
			// file to read from
			RandomAccessFile file = new RandomAccessFile(filePath, "rw");
			// size of file
			int fileSize = (int) (file.getChannel().size() % Integer.MAX_VALUE);
			// counter for number of reads
			int counter = 0;
			// buffer for reading
			byte[] bytes = new byte[bufferSize];
			// timer
			Timer timer = new Timer();
			Random rand = new Random();
			timer.start();

			while (counter++ < toWrite) {
				// go to random spot in file
				file.seek(rand.nextInt(fileSize));
				// read the bytes into buffer
				rand.nextBytes(bytes);
				file.write(bytes);
			}

			file.close();
			return timer.stop() / 1000000; // ns to ms!

		}

		/**
		 * Reads data from random positions into a fixed size buffer from a given file
		 * using RandomAccessFile for one second, or any other given time
		 * 
		 * @param filePath   Full path to file on disk
		 * @param bufferSize Size of byte buffer to read at each step
		 * @param millis     Total time to read from file
		 * @return Number of reads in the given amount of time
		 * @throws IOException
		 */
		public int randomReadFixedTime(String filePath, int bufferSize, int millis) throws IOException {
			// file to read from
			RandomAccessFile file = new RandomAccessFile(filePath, "rw");
			// size of file
			int fileSize = (int) (file.getChannel().size() % Integer.MAX_VALUE);
			// counter for number of reads
			int counter = 0;
			// buffer for reading
			byte[] bytes = new byte[bufferSize];
			Random rand = new Random();
			long now = System.nanoTime();
			// read for a fixed amount of time

			while (System.nanoTime() - now < millis) {
				// go to random spot in file
				file.seek(rand.nextInt(fileSize));
				// read the bytes into buffer
				file.read(bytes);
				counter++;
			}

			file.close();
			return counter;
		}

		public int randomWriteFixedTime(String filePath, int bufferSize, int millis) throws IOException {
			RandomAccessFile file = new RandomAccessFile(filePath, "rw");
			// size of file
			int fileSize = (int) (file.getChannel().size() % Integer.MAX_VALUE);
			// counter for number of writes
			int counter = 0;
			byte[] bytes = new byte[bufferSize];
			Random rand = new Random();
			long now = System.nanoTime();
			// read for a fixed amount of time

			while (System.nanoTime() - now < millis) {
				// go to random spot in file
				file.seek(rand.nextInt(fileSize));
				// read the bytes into buffer
				rand.nextBytes(bytes);
				file.write(bytes);
				counter++;
			}

			file.close();
			return counter;
		}

	}

}
