class MainChannel extends Channel {
    public boolean isInterupted = false;
    public Buffer buffer;
    public MainChannel(String name) {
        super(name);
        tNext = tCurrent + getDelay(mean,deviation);
    }

    public void setBuffer(Buffer buffer){
        this.buffer = buffer;
    }

    @Override
    public void inAct(Message message) {
        super.inAct(message);
        isBusy = true;
    }

    @Override
    public void outAct(){
        isBusy = false;
        if(currentMessage == null){
            return;
        }

        scheduleChannel();
        if(isBusy && isInterupted){
            returnCurrentMessage();
            return;
        }

        super.outAct();
        this.nextElement.inAct(currentMessage);//despose
    }

    public void returnCurrentMessage(){
        this.currentMessage.isInterupted = true;
        this.buffer.inAct(this.currentMessage);
    }

    public boolean canTakeMessage(){
        return isInterupted == false && isBusy == false;
    }
}