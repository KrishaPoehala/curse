class Channel extends Element {

    private int inCount = 0;
    protected boolean isBusy;
    protected double meanLoad;
    protected Message currentMessage;
    public double mean = 7,deviation = 3;

    public Channel(String name) {
        super(name);
        meanLoad = 0.0;
    }

    public boolean isBusy() {
        return isBusy;
    }

    @Override
    public void inAct(Message message) {
        super.inAct(message);
        inCount++;
        currentMessage = message;
    }

    @Override
    public void outAct() {
        super.outAct();
    }

    public void scheduleChannel(){
        tNext = tCurrent + getDelay(mean,deviation);
    }

    @Override
    public void doStatistics(double delta) {
        meanLoad += (isBusy ? 1 : 0) * delta;
    }

    public double getMeanLoad() {
        return meanLoad;
    }
}