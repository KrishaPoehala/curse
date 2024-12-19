class BackupStarter extends Element {
    BackupChannel backupChannel;
    private final int mean = 2;

    public BackupStarter(String name) {
        super(name);
        tNext = Double.MAX_VALUE;
    }

    public void setBackupChannel(BackupChannel backupChannel){
        this.backupChannel = backupChannel;
    }

    @Override
    public void inAct(Message m){
        tNext = tCurrent + getDelay(mean,0);
        this.nextElement.inAct(null);//main restorer
    }

    @Override
    public void outAct(){
        this.backupChannel.isAsleep = false;
        this.backupChannel.scheduleChannel();//tcurrent+delay
        tNext = Double.MAX_VALUE;
    }
}