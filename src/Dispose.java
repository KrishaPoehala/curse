import java.util.LinkedList;
import java.util.Queue;

class Dispose extends Element {
    private Queue<Message> processedMessages;
    public Dispose(String name) {
        super(name);
        processedMessages = new LinkedList<>();
        tNext = Double.MAX_VALUE;
    }

    @Override
    public void inAct(Message m){
        super.outAct();
        processedMessages.add(m);
    }

    public double getMeanTimeInSystem(){
        return processedMessages
                .stream()
                .mapToDouble(m -> m.timeOut - m.timeIn)
                .average()
                .orElse(0.0);
    }
}