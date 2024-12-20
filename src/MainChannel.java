class MainChannel extends Channel {
    public boolean isInterupted = false;
    private Buffer buffer;
    public MainChannel(String name) {
        super(name);
    }

    public  void setBuffer(Buffer b){
        this.buffer = b;
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
        return this.queue.poll();
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
}