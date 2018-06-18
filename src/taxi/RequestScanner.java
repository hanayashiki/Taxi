package taxi;

import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class RequestScanner extends Thread {
    BlockingQueue<Request> requestQueue;
    Scanner scanner;

    public RequestScanner(InputStream inputStream, BlockingQueue<Request> requestQueue) {
        this.requestQueue = requestQueue;
        this.scanner = new Scanner(inputStream);
    }

    public void run() {
        while (scanner.hasNextLine()) {
            String inputLine = scanner.nextLine();

        }
    }
}
