import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Queue;

abstract class Channel extends Element {
    public boolean isBusy;
    private double meanQueue;
    public String channelName;
    //public static Queue<Message> queue;
    public int queueSize;
    protected double meanLoad;
    protected Message currentMessage;
    protected Buffer buffer;
    public void setBuffer(Buffer b){
        this.buffer = b;
    }
    public double mean = 7, deviation = 3;

    public Channel(String name, String channelName) {
        super(name);
        this.channelName = channelName;
        meanLoad = 0.0;
        //queue = new LinkedList<>();
        tNext = tCurrent + getDelay(mean, deviation);
        queueSize = 0;
    }

    DecimalFormat df = new DecimalFormat("0.00");
    @Override
    public void inAct(Message message) {
        super.inAct(message);
        queueSize++;
        isBusy = true;
    }

    @Override
    public void outAct() {
        isBusy = false;
        scheduleChannel();
        currentMessage = this.buffer.queue.poll();
        if (currentMessage == null) {
            //System.out.println("Current message is null ---------------------------");
            return;
        }

        super.outAct();
        currentMessage.channel = channelName;
        currentMessage.timeOut = tCurrent;
        this.nextElement.inAct(currentMessage);//dispose
        currentMessage = null;
        if (queueSize > 0) {
            queueSize--;
        }
    }

    public void scheduleChannel() {
        tNext = tCurrent + getDelay(mean, deviation);
    }

    @Override
    public void doStatistics(double delta) {
        meanLoad += (isBusy ? 1 : 0) * delta;
        meanQueue += queueSize * delta;
    }

    public double getMeanLoad() {
        return meanLoad;
    }

    public double getMeanQueue() {
        return meanQueue;
    }

    public abstract boolean canTakeMessage();
}