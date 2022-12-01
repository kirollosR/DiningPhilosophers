public class Chopstick {
    private boolean used;
    public String name;

    public Chopstick(String name){
        this.name = name;
    }

    public boolean isUsed(){
        return used;
    }

    public void take(){
        System.out.println(name + " is in use");
        this.used = true;
    }

    public void release(){
        System.out.println(name + " is released");
        this.used = false;
    }
}
