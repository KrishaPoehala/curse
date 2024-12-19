
class BackupChannel extends Channel {
    public boolean isAsleep = true;
    public BackupChannel(String name) {
        super(name);
        tNext = Double.MAX_VALUE;
    }

    @Override
    public void inAct(Message message) {
        isBusy = true;
        super.inAct(message);
    }

    @Override
    public void outAct(){
        super.outAct();
        isBusy = false;
        scheduleChannel();
        nextElement.outAct();//despose
    }

    public boolean canTakeMessage(){
        return !isAsleep && !isBusy;
    }
}