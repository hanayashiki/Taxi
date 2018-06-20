package taxi;

import jdk.nashorn.internal.ir.RuntimeNode;
import utils.PathDrawer;
import utils.Verbose;

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
                Verbose.printlnAt("CustomerRequest " + request.getOriginalString() + " received");
                RequestWindow requestWindow = new RequestWindow(global.grid, Global.getRelativeTime(),
                        Global.getRelativeTime() + RequestWindow.WINDOW_LENGTH, (CustomerRequest) request);
                requestWindow.start();
            }
            if (request instanceof LoadRequest) {
                Verbose.printlnAt("LoadRequest " + request.getOriginalString() + " received");
                global.loadConfig((LoadRequest) request);
            }
            if (request instanceof EndRequest) {
                // TODO
            }
            if (request instanceof PathRequest) {
                PathRequest pathRequest = (PathRequest) request;
                int i1 = pathRequest.getSourceI();
                int j1 = pathRequest.getSourceJ();
                int i2 = pathRequest.getDestinationI();
                int j2 = pathRequest.getDestinationJ();
                if (pathRequest.getType().equals("Open")) {
                    global.grid.connect(i1, j1, i2, j2);
                }
                else if (pathRequest.getType().equals("Close")) {
                    global.grid.disjoin(i1, j1, i2, j2);
                }
                Verbose.println(PathDrawer.gridToString(global.grid.getGridClone()));
            }
        }
    }

}
