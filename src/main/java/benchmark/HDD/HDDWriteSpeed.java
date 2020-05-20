package benchmark.HDD;

import java.io.IOException;

public class HDDWriteSpeed implements IBenchmark {

	private FileWriter writer;

	@Override
	public void initialize(Object... params) {
		writer = new FileWriter();
	}

	/**
	 * HDDWriteSpeed hws = new HDDWriteSpeed();
	 * 
	 * hws.initialize(); hws.warmUp(); String result = hws.run("fb"/"fs",
	 * true/false, 512, 'D');
	 * 
	 */
	public void warmUp() {
		try {
			writer.streamWriteFixedBuffer("D:\\HDD_Benchmark\\write-", ".dat", 0, 2, 4 * 1024, true);
			writer.streamWriteFixedSize("D:\\HDD_Benchmark\\write-", ".dat", 0, 2, 200 * 1024 * 1024, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * run("fb"/"fs", true/false, 512, 'D');
	 */
	@Override
	public String run(Object... options) {
		String output = null;
		// either "fs" - fixed size, or "fb" - fixed buffer
		String option = (String) options[0];
		// true/false whether the written files should be deleted at the end
		Boolean clean = (Boolean) options[1];
		int size = (int) options[2];
		char partition = (char) options[3];
		String prefix = partition + ":\\HDD_Benchmark\\write-";
		String suffix = ".dat";
		int startIndex = 0;
		int endIndex = 9;
		long fileSize = size * 1024 * 1024;
		int buffersize = 10 * 1024 * 1024; // 4 KB

		try {
			if (option.equals("fs"))
				output = writer.streamWriteFixedSize(prefix, suffix, startIndex, endIndex, fileSize, clean);
			else if (option.equals("fb"))
				output = writer.streamWriteFixedBuffer(prefix, suffix, startIndex, endIndex, buffersize, clean);
			else
				throw new IllegalArgumentException("Argument " + options[0].toString() + " is undefined");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;

	}

	@Override
	public void clean() {
	}

	public String getResult() {
		return null;
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub

	}
}
