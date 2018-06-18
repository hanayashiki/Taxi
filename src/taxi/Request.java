package taxi;

public class Request {
    private long time;
    public Request() {
        this.time = Global.getRelativeTime();
    }

    public long getTime() {
        return time;
    }

}
