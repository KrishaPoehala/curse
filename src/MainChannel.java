class MainChannel extends Channel {
    public boolean isInterupted = false;
    public MainChannel(String name,String channelName) {
        super(name,channelName);
    }
    @Override
    public void inAct(Message message) {
        super.inAct(message);
        ++inCount;
    }

    public boolean canTakeMessage(){
        return !isInterupted;
    }

    public Message getInteruptedMessage(){
        return this.buffer.queue.poll();
    }

    int inCount = 0;
    int interuptedMessagesCount = 0;
    public void returnInteruptedMessage(Message interuptedMessage){
        if(interuptedMessage == null){
            return;
        }

        ++interuptedMessagesCount;
        interuptedMessage.isInterupted = true;
        this.buffer.inAct(interuptedMessage);
    }

    public double getInterruptionFrequency(){
        return (double)interuptedMessagesCount / inCount;
    }

    public void activate(double tNext) {
        this.isInterupted = false;
        this.tNext = tNext;
        //when the main channel is activated its queue size has to be updated
        //to reflect current queue's state
        this.queueSize = this.buffer.queue.size();
    }

    public void deactivate(){
        isInterupted = true;
        tNext = Double.MAX_VALUE;
    }
}