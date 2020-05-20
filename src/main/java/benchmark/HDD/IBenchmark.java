package benchmark.HDD;

public interface IBenchmark {
	void initialize(Object... parameters);

	String run(Object... parameters); // we can pass any parameter and as many as we want

	void clean();

	void cancel();
}
