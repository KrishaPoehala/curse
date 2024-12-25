class ErrorGenerator extends Element {
    private MainChannel mainChannel;
    private double k;
    private final double mean = 200, deviation = 35;

    public ErrorGenerator(String name,double k) {
        super(name);
        this.k = k;
        tNext = tCurrent + getDelay(mean + k, deviation);
    }

    public void setMainChannel(MainChannel mainChannel) {
        this.mainChannel = mainChannel;
    }

    @Override
    public void inAct(Message m){
        tNext = tCurrent + getDelay(mean + k, deviation);
    }

    @Override
    public void outAct() {
        super.outAct();
        this.mainChannel.deactivate();
        Message interuptedMessage = null;
        if(mainChannel.isBusy){
            interuptedMessage = mainChannel.getInteruptedMessage();
        }

        ++errorsGenerated;
        this.nextElement.inAct(interuptedMessage);//backupStarter
        tNext = Double.MAX_VALUE;
    }

    private int errorsGenerated = 0;
    public int getErrorsGenerated() {
        return errorsGenerated;
    }
}

