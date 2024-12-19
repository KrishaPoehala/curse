class MainRestorer extends Element {
    private ErrorGenerator errorGenerator;
    private MainChannel mainChannel;
    private BackupChannel backupChannel;

    private int restores = 0;
    private final int mean=70,deviation = 7;

    public MainRestorer(String name) {
        super(name);
        tNext = Double.MAX_VALUE;
    }

    public void setMainChannel(MainChannel mainChannel){
        this.mainChannel = mainChannel;
    }
    public void setBackupChannel(BackupChannel backupChannel){
        this.backupChannel = backupChannel;
    }
    public void setErrorGenerator(ErrorGenerator errorGenerator){
        this.errorGenerator = errorGenerator;
    }

    @Override
    public void inAct(Message m){
        tNext = tCurrent + getDelay(mean,deviation);
    }

    @Override
    public void outAct() {
        super.outAct();
        restores++;
        this.backupChannel.isAsleep = true;
        this.backupChannel.tNext = Double.MAX_VALUE;
        this.mainChannel.isInterupted = false;
        this.mainChannel.scheduleChannel();
        this.errorGenerator.inAct(null);
        this.tNext = Double.MAX_VALUE;
    }

    public int getRestores() {
        return restores;
    }
}
