package timing;

public class Timer implements ITimer {
    private long initial_time = 0;
    private long current_time = 0;
    private long total_time = 0;
    boolean running = false;

    @Override
    public void start() {
        initial_time = 0;
        current_time = 0;
        total_time = 0;
        running = true;
        initial_time = System.nanoTime();
    }

    @Override
    public void resume() {
        running = true;
        initial_time = System.nanoTime();
    }

    @Override
    public long pause() {
        running = false;
        current_time = System.nanoTime();
        total_time = total_time + (current_time - initial_time);
        return current_time - initial_time;
    }

    @Override
    public long stop() {
        current_time = System.nanoTime();
        if (running == true)
            total_time = total_time + (current_time - initial_time);
        return total_time;
    }
}
