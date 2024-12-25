class Create extends Element {
    private final double mean=9,deviation=5;
    public Create(String name) {
        super(name);
        tNext = 0;
    }

    @Override
    public void outAct() {
        super.outAct();
        tNext = tCurrent + getDelay(mean, deviation);
        Message message = new Message(tCurrent);
        //System.out.println("Messsage " + message.id + " created at " + message.timeIn);
        if (nextElement != null) {
            nextElement.inAct(message);//buffer
        }

    }
}