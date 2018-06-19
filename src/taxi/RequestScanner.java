package taxi;

import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class RequestScanner extends Thread {
    private BlockingQueue<Request> requestQueue;
    private Scanner scanner;

    public RequestScanner(InputStream inputStream, BlockingQueue<Request> requestQueue) {
        this.requestQueue = requestQueue;
        this.scanner = new Scanner(inputStream);
    }

    public void run() {
        while (scanner.hasNextLine()) {
            String inputLine = scanner.nextLine();
            try {
                Request request = RequestParser.Parse(inputLine);
                requestQueue.offer(request);
            } catch (InputException e) {
                System.out.println("invalid: " + e.getMessage());
            }
        }
    }
}
