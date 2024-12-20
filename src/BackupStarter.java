class BackupStarter extends Element {
    BackupChannel backupChannel;
    MainChannel mainChannel;
    private final int mean = 2;

    public BackupStarter(String name) {
        super(name);
        tNext = Double.MAX_VALUE;
    }

    public void setChannels(BackupChannel backupChannel,MainChannel mainChannel){
        this.backupChannel = backupChannel;
        this.mainChannel = mainChannel;
    }

    Message interuptedMessage;
    @Override
    public void inAct(Message m){
        tNext = tCurrent + getDelay(mean,0);
        this.interuptedMessage = m;
        this.nextElement.inAct(null);//main restorer
    }

    @Override
    public void outAct(){
        this.backupChannel.isAsleep = false;
        this.backupChannel.tNext = tNext;
        this.mainChannel.returnInteruptedMessage(interuptedMessage);
        tNext = Double.MAX_VALUE;
    }
}