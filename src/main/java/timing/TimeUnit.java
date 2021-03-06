package timing;

public class TimeUnit {
	private double time_ns = 0;

	public TimeUnit(long time_ns) {
		this.time_ns = time_ns;
	}

	public long convert_to_microseconds() {
		return (long) (time_ns / Math.pow(10, -6));
	}

	public long convert_to_miliseconds() {
		return (long) (time_ns / Math.pow(10, -3));
	}

	public double convert_to_seconds() {
		return time_ns / Math.pow(10, 9);
	}

}
