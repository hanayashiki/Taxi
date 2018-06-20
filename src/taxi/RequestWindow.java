package taxi;

import utils.Verbose;
import utils.Index;

import java.util.*;

public class RequestWindow extends Thread {
    public static final int WINDOW_LENGTH = 7500;

    private static int increment = 0;
    private int index;
    private long timeStart;
    private long timeEnd;
    private CustomerRequest customerRequest;
    private Grid grid;

    private List<Taxi> interestedTaxiList = new LinkedList<>();

    public RequestWindow(Grid grid, long timeStart, long timeEnd, CustomerRequest customerRequest) {
        this.grid = grid;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.customerRequest = customerRequest;
        this.index = increment++;
    }

    private void addGridRequestWindowSet(Grid grid) {
        int centerI = customerRequest.getSourceI();
        int centerJ = customerRequest.getSourceJ();
        // notifying 4 * 4 square
        synchronized (grid) {
            for (int i = centerI - 2; i <= centerI + 2; i++) {
                for (int j = centerJ - 2; j <= centerJ + 2; j++) {
                    if (Index.checkIndex(i) && Index.checkIndex(j)) {
                        grid.addRequestWindow(this, i, j);
                    }
                }
            }
        }
    }

    private void removeGridRequestWindowSet(Grid grid) {
        int centerI = customerRequest.getSourceI();
        int centerJ = customerRequest.getSourceJ();
        // cleaning 4 * 4 square
        synchronized (grid) {
            for (int i = centerI - 2; i <= centerI + 2; i++) {
                for (int j = centerJ - 2; j <= centerJ + 2; j++) {
                    if (Index.checkIndex(i) && Index.checkIndex(j)) {
                        grid.removeRequestWindow(this, i, j);
                    }
                }
            }
        }
    }

    public long getTimeStart() {
        return timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public CustomerRequest getCustomerRequest() {
        return customerRequest;
    }

    public int getIndex() {
        return index;
    }

    synchronized void grabRequest(Taxi taxi) {
        interestedTaxiList.add(taxi);
    }

    @Override
    public void run() {
        addGridRequestWindowSet(grid);
        try {
            sleep(WINDOW_LENGTH);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        removeGridRequestWindowSet(grid);

        long time1 = System.currentTimeMillis();

        Taxi nearestTaxi = null;
        int minDistance = Integer.MAX_VALUE;
        SPF spf = new SPF(this.grid);
        for (Taxi taxi : interestedTaxiList) {
            synchronized (taxi) {
                if (taxi.getTaxiState() != TaxiState.Idle) {
                    continue;
                }
                int taxiPathLength = spf.getShortestPath(taxi.getI(), taxi.getJ(),
                        customerRequest.getSourceI(), customerRequest.getSourceJ(), "bfs").size();
                if (taxiPathLength < minDistance ||
                        (taxiPathLength == minDistance && nearestTaxi.getCredit() > taxi.getCredit())) {
                    nearestTaxi = taxi;
                    minDistance = taxiPathLength;
                }
            }
        }

        long time2 = System.currentTimeMillis();

        if (nearestTaxi != null) {
            // TODO: output
            Verbose.printlnAt("Deciding who to fuck "
                    +  this.customerRequest.getOriginalString() + " used " + (time2 - time1) + "ms. ");
            nearestTaxi.receiveRequest(this.customerRequest);
            Verbose.printlnAt(String.format("Taxi %d gets CustomerRequest %s",
                    nearestTaxi.getIndex(), this.customerRequest.getOriginalString()));
        } else {
            Verbose.printlnAt("No one answers " + customerRequest.getOriginalString());
            // TODO: output
        }
    }
}

class RequestWindowSet {
    private HashMap<Integer, RequestWindow> windowSet;

    public RequestWindowSet() {
        this.windowSet = new HashMap<>();
    }

    public void put(RequestWindow r) {
        windowSet.put(r.getIndex(), r);
    }
    public void remove(RequestWindow r) {
        windowSet.remove(r.getIndex());
    }
    public boolean isEmpty() {
        return windowSet.isEmpty();
    }
    public Set<Map.Entry<Integer, RequestWindow>> entrySet() {
        return windowSet.entrySet();
    }
}