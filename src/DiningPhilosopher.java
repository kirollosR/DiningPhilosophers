public class DiningPhilosopher {
    public static void main(String[] args) throws InterruptedException {
        Chopstick[] chopistics = new Chopstick[5];
        for(int i = 0; i < chopistics.length;i++){
            chopistics[i] = new Chopstick("C"+i);
        }
        int numOfPhilosophers = 5;

        Monitor monitor = new Monitor(numOfPhilosophers,chopistics);
        Philosopher [] p = new Philosopher[numOfPhilosophers];

        for(int i = 0; i < numOfPhilosophers; i++)
            p[i] = new Philosopher(i,chopistics[(i+numOfPhilosophers-1)%numOfPhilosophers],chopistics[(i+1)%numOfPhilosophers],monitor);

        for(int i = 0; i < numOfPhilosophers; i++)
            try {
                p[i].t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        System.out.println("");
        System.out.println("Dinner is over!");

    }
}
