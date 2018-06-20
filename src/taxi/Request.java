package taxi;

public class Request {
    private long time;

    private String originalString;
    public Request() {
        this.time = Global.getRelativeTime();
    }

    public long getTime() {
        return time;
    }

    public String getOriginalString() {
        return originalString;
    }

    public void setOriginalString(String originalString) {
        this.originalString = originalString;
    }

}
