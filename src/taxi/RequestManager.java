package taxi;

import jdk.nashorn.internal.ir.RuntimeNode;

import java.util.concurrent.BlockingQueue;

public class RequestManager extends Thread {
    BlockingQueue<Request> requestQueue;
    Global global;
    public RequestManager(Global global, BlockingQueue<Request> requestQueue) {
        this.global = global;
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
                global.loadConfig((LoadRequest) request);
            }
            if (request instanceof EndRequest) {
                // TODO
            }
        }
    }

}
