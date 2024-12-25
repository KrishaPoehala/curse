import java.util.Random;

public class Element {
    protected String name;
    protected double tNext, tCurrent;
    protected int quantity;
    protected Element nextElement;
    protected double delay;
    protected Random random = new Random();

    public static int nextId = 0;
    private int id;

    public Element(String name) {
        this.name = name;
        tNext = 0.0;
        tCurrent = tNext;
        nextElement = null;
        id = nextId;
        nextId++;
    }

    public double getDelay(double mean, double deviation) {
        double min = mean - deviation;
        double max = mean + deviation;
        return min + (max - min) * random.nextDouble();
    }

    public int getId(){
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setTCurrent(double tCurrent) {
        this.tCurrent = tCurrent;
    }

    public void setNextElement(Element nextElement) {
        this.nextElement = nextElement;
    }

    public void inAct(Message message) {
    }

    public void outAct() {
        quantity++;
    }

    public double getTNext() {
        return tNext;
    }

    public void printInfo() {
        System.out.print(name + ": {" + " Кількість: " + quantity + "; tnext: " + tNext);
        if (name.contains("CREATOR")) {
            System.out.println(" }");
        }
    }

    public String getName() {
        return name;
    }

    public void doStatistics(double delta) {
    }
}