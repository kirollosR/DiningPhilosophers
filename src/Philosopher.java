public class Philosopher implements Runnable{
    private int timesToEat = 5;
    private int SleepTime = 1000;
    final Monitor monitor;
    private int id;
    public Thread t;

    private Chopstick leftChopstick;
    private Chopstick rightChopstick;

    public Philosopher(int id, Chopstick left, Chopstick right, Monitor m){
        this.id = id;
        this.monitor = m;
        this.leftChopstick = left;
        this.rightChopstick = right;
        t = new Thread(this);
        t.start();
    }

    public void run(){
        int count = 1;
        while(count <= timesToEat){
            monitor.pickUp(id);
            System.out.format("Philosopher %d eating (%d times)\n", id, count);
            // Sleep a bit.
            try {
                Thread.sleep(SleepTime);
            } catch (InterruptedException e) {}
            monitor.putDown(id);
            System.out.format("Philosopher %d thinking (%d times)\n", id, count);
            ++count;
        }
    }
}
