import java.util.LinkedList;
import java.util.Queue;

abstract class Channel extends Element {
    public boolean isBusy;
    public Queue<Message> queue;
    protected double meanLoad;
    protected Message currentMessage;
    public double mean = 7,deviation = 3;

    public Channel(String name) {
        super(name);
        meanLoad = 0.0;
        queue = new LinkedList<>();
        tNext = tCurrent + getDelay(mean,deviation);
    }

    @Override
    public void inAct(Message message) {
        super.inAct(message);
        this.queue.add(message);
        isBusy = true;
    }

    @Override
    public void outAct() {
        isBusy = false;
        scheduleChannel();
        currentMessage = this.queue.poll();
        if(currentMessage == null){
            return;
        }

        super.outAct();
        this.nextElement.inAct(currentMessage);//dispose
        currentMessage = null;
    }

    public void scheduleChannel(){
        tNext = tCurrent + getDelay(mean,deviation);
    }

    @Override
    public void doStatistics(double delta) {
        meanLoad += (isBusy ? 1 : 0) * delta;
    }

    public double getMeanLoad() {
        return meanLoad;
    }

    public abstract  boolean canTakeMessage();
}