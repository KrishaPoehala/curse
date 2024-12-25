
class BackupChannel extends Channel {
    public boolean isAsleep = true;
    public BackupChannel(String name,String channelName) {
        super(name,channelName);
        tNext = Double.MAX_VALUE;
    }

    @Override
    public void inAct(Message message) {
        super.inAct(message);
    }

    public boolean canTakeMessage(){
        return !isAsleep;
    }

    public void activate(double tNext){
        this.isAsleep = false;
        //when the backupchannel is activated its queue size has to be updated
        //to reflect current queue's state
        this.queueSize = this.buffer.queue.size();
        this.tNext = tNext;
    }

    public void deactivate(){
        this.isAsleep = true;
        this.tNext = Double.MAX_VALUE;
    }
}