package taxi;

import utils.TestInputter;
import utils.PathDrawer;
import utils.Verbose;

import java.io.IOException;
import java.io.PipedInputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MainTest {
    public static void normalRun() {
        try {
            BlockingQueue<Request> requestBlockingQueue = new ArrayBlockingQueue<Request>(1000);
            PipedInputStream pipedInputStream = TestInputter.getStream("src/resource/script2.txt");
            Global global = new Global(4);
            RequestScanner requestScanner = new RequestScanner(pipedInputStream, requestBlockingQueue);
            RequestManager requestManager = new RequestManager(global, requestBlockingQueue);

            Verbose.println(PathDrawer.gridToString(global.grid.getGridClone()));

            requestScanner.start();
            requestManager.start();
            TestInputter.testInputter.start();
            global.startTaxiThreads();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String [] args) {
        normalRun();
    }
}
