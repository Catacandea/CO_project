package benchmark.HDD;

import java.io.IOException;

public interface IBenchmark {
	void initialize(Object... parameters) throws IOException;

	void run(Object... parameters) throws IOException; // we can pass any parameter and as many
														// as we want

	void warmUp(Object... params) throws IOException;

	String getResult();
}
