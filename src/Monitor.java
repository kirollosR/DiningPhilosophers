import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private int numberOfPhilosophers;
    final Lock lock;
    private enum States {HUNGRY, THINKING, EATING}
    private States[] state;
    final Condition[] cond;
    private Chopstick[] chopsticks;

    public Monitor(int numberOfPhilosophers, Chopstick[] c){
        this.numberOfPhilosophers = numberOfPhilosophers;
        lock = new ReentrantLock(true); // can send boolean with true for the fair to make a queue 3ashan law kaza 7ad 3ayz yem3l lock yestano fe queue
        state = new States[numberOfPhilosophers];
        cond = new Condition[numberOfPhilosophers];
        chopsticks = new Chopstick[numberOfPhilosophers];

        for(int i = 0; i < numberOfPhilosophers; i++){
            state[i] = States.THINKING;
            cond[i] = lock.newCondition();
        }

        this.chopsticks = c;
    }

    public void pickUp(int i){
        lock.lock();
        try{
            state[i] = States.HUNGRY;
            if((state[(i+numberOfPhilosophers-1)%numberOfPhilosophers] != States.EATING) &&
               (state[(i+1)%numberOfPhilosophers] != States.EATING)){
//            if((!chopsticks[i].isUsed()) && (!chopsticks[(i + numberOfPhilosophers - 1) % numberOfPhilosophers].isUsed())){
                eat(i);
            }else{
                try{
    //                System.out.format("***Philosopher %d trying to eat\n", i);
                    cond[i].await();
                    eat(i);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }finally {
            lock.unlock();
        }
    }

    public void putDown(int i){
        lock.lock();
        try {
            think(i);
            // Tell the right neighbor about the possibility to eat.
            int right = (i + numberOfPhilosophers - 1)%numberOfPhilosophers;
            int right2 = (i + numberOfPhilosophers - 2)%numberOfPhilosophers;
            if( (state[right] == States.HUNGRY) && (state[right2] != States.EATING) ){
                cond[right].signal();
            }
            // Tell the left neighbor about the possibility to eat
            if( (state[(i+1)%numberOfPhilosophers] == States.HUNGRY) && (state[(i+2)%numberOfPhilosophers] != States.EATING) ){
                cond[(i+1)%numberOfPhilosophers].signal();
            }
        }
        finally {
            lock.unlock();
        }
    }

    public void eat(int i){
        state[i] = States.EATING;
        chopsticks[(i+numberOfPhilosophers-1) % numberOfPhilosophers].take();
        chopsticks[i].take();
    }

    public void think(int i){
        state[i] = States.THINKING;
        chopsticks[(i+numberOfPhilosophers-1) % numberOfPhilosophers].release();
        chopsticks[i].release();
    }

    public States philoState(int i){
        return state[i];
    }
}
