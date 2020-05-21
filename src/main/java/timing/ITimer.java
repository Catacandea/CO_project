package timing;

public interface ITimer {
    void start();
    void resume();
    long pause();
    long stop();
}
