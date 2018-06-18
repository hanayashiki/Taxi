package taxi;

public class LoadRequest extends Request {
    private String filePath;

    public LoadRequest(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}