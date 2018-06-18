package taxi;

import jdk.nashorn.internal.ir.RuntimeNode;

import java.util.concurrent.BlockingQueue;

public class RequestManager extends Thread {
    BlockingQueue<Request> requestQueue;
    public RequestManager(Grid grid, BlockingQueue<Request> requestQueue) {
        this.requestQueue = requestQueue;
    }
    @Override
    public void run() {
        while (true) {
            Request request = null;
            try {
                request = requestQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (request instanceof CustomerRequest) {
                // TODO
            }
            if (request instanceof LoadRequest) {

            }
            if (request instanceof EndRequest) {
                // TODO
            }
        }
    }

}
