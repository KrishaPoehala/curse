import java.util.LinkedList;
import java.util.Queue;

class Buffer extends Element {
    public final Queue<Message> queue;
    private MainChannel mainChannel;
    private BackupChannel backupChannel;
    private double meanQueue;


    public Buffer(String name) {
        super(name);
        queue = new LinkedList<>();
        meanQueue = 0;
        tNext = Double.MAX_VALUE;
    }

    public void setChannels(MainChannel mainChannel, BackupChannel backupChannel) {
        this.mainChannel = mainChannel;
        this.backupChannel = backupChannel;
    }

    public double getMeanQueue() {
        return meanQueue;
    }

    @Override
    public void inAct(Message message) {
        queue.add(message);
        processQueue();
        super.outAct();
    }

    public int lostM = 0;
    public void processQueue() {
        if(this.mainChannel.canTakeMessage()){
            this.mainChannel.inAct(queue.poll());
        }
        else if(this.backupChannel.canTakeMessage()){
            this.backupChannel.inAct(queue.poll());
        }
    }

    @Override
    public void doStatistics(double delta) {
        meanQueue += queue.size() * delta;
    }
}