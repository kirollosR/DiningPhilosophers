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

    public void test(int i){
        if((state[(i+numberOfPhilosophers-1)%numberOfPhilosophers] != States.EATING) &&
                (state[(i+1)%numberOfPhilosophers] != States.EATING) && state[i] == States.HUNGRY){
            state[i] = States.EATING;
            cond[i].signal();
        }
    }
    public void pickUp(int i){
        lock.lock();
        try{
            state[i] = States.HUNGRY;
            test(i);
            if(state[i] != States.EATING){
                cond[i].await();
            }
            eat(i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
    public void eat(int i){
//        state[i] = States.EATING;
        chopsticks[(i+numberOfPhilosophers-1) % numberOfPhilosophers].take();
        chopsticks[i].take();
    }
    public void putDown(int i){
        lock.lock();
        try {
            think(i);
            // Tell the right neighbor about the possibility to eat.
            int right = (i + numberOfPhilosophers - 1)%numberOfPhilosophers;
            int left = (i+1)%numberOfPhilosophers;
            test(right);
            test(left);
        }
        finally {
            lock.unlock();
        }
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
