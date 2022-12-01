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
        lock = new ReentrantLock(); // can send boolean with true for the fair to make a queue 3ashan law kaza 7ad 3ayz yem3l lock yestano fe queue
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
                state[(i+1)%numberOfPhilosophers] != States.EATING){
            eat(i);
        }else{
            try{
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
            // Tell the left neighbor about the possibility to eat.
            int left = (i - 1 + numberOfPhilosophers)%numberOfPhilosophers;
            int left2 = (i - 2 + numberOfPhilosophers)%numberOfPhilosophers;
            if( (state[left] == States.HUNGRY) &&
                    (state[left2] != States.EATING) ){
                cond[left].signal();
            }
            // Tell the right neighbor about the possibility to eat
            if( (state[(i+1)%numberOfPhilosophers] == States.HUNGRY) &&
                    (state[(i+2)%numberOfPhilosophers] != States.EATING) ){
                cond[(i+1)%numberOfPhilosophers].signal();
            }
        }
        finally {
            lock.unlock();
        }
    }

    public void eat(int i){
        chopsticks[(i+1)%numberOfPhilosophers].take();
        chopsticks[i].take();
        state[i] = States.EATING;
    }

    public void think(int i){
        chopsticks[(i+1)%numberOfPhilosophers].release();
        chopsticks[i].release();
        state[i] = States.THINKING;
    }

    public States philoState(int i){
        return state[i];
    }
}
