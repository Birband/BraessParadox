package paradox.braess;

import java.util.ArrayList;
import java.util.List;

public class Queue {
    private final int size;
    private final Node begin;
    private final Node end;
    private final long drive_time;
    private int counter = 0;

    private double last_wait;

    private final List<Entity> queue = new ArrayList<Entity>();

    public Queue() {
        this.size = 0;
        this.begin = null;
        this.end = null;
        this.drive_time = 0;
        this.last_wait = 0;
    }

    public Queue(int size, Node begin, Node end, long drive_time) {
        this.size = size;
        this.begin = begin;
        this.end = end;
        this.drive_time = drive_time;
        this.last_wait = 0;
    }

    public synchronized double access() throws InterruptedException {
        long traffic_start = System.nanoTime();
        boolean in_traffic = false;
        while ( this.counter >= this.size) {
            wait();
            in_traffic = true;
        }
        this.counter++;
        long traffic_end = System.nanoTime();
        this.last_wait = in_traffic ? (double)(traffic_end - traffic_start)/1000 : 0;
        return last_wait;
    }

    public synchronized void leave() {
        this.counter--;
        notifyAll();
    }

    public Node getBegin() {
        return begin;
    }

    public Node getEnd() {
        return end;
    }

    public long getDrive_time() {
        return drive_time;
    }

    public int getCounter() {
        return counter;
    }

    public int getSize() {
        return size;
    }

    public double getLast_wait() {
        return last_wait;
    }

    public void setLast_wait(double last_wait) {
        this.last_wait = last_wait;
    }

}
