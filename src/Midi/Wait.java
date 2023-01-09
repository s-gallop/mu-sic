package Midi;

public class Wait extends Component{
    private int waitTime;

    public Wait(int waitTime) {
        this.waitTime = waitTime;
    }

    @Override
    public void shift(int numUp) {
        // does not need shift for wait
    }

    @Override
    public void activate() throws InterruptedException {
        Thread.sleep(this.waitTime);
    }
}
