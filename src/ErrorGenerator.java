class ErrorGenerator extends Element {
    private MainChannel mainChannel;
    private final double mean = 200, deviation = 35;

    public ErrorGenerator(String name) {
        super(name);
        tNext = tCurrent + getDelay(mean, deviation);
    }

    public void setMainChannel(MainChannel mainChannel) {
        this.mainChannel = mainChannel;
    }

    @Override
    public void inAct(Message m){
        tNext = tCurrent + getDelay(mean, deviation);
    }

    @Override
    public void outAct() {
        super.outAct();
        mainChannel.isInterupted = true;
        mainChannel.tNext = Double.MAX_VALUE;
        ++errorsGenerated;
        this.nextElement.inAct(null);//backupStarter
        tNext = Double.MAX_VALUE;
    }

    private int errorsGenerated = 0;
    public int getErrorsGenerated() {
        return errorsGenerated;
    }
}

