package taxi;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class RequestWindow extends Thread {
    public static final int WINDOW_LENGTH = 7500;

    private static int increment = 0;
    private int index;
    private long timeStart;
    private long timeEnd;
    private CustomerRequest customerRequest;
    private Grid grid;

    private List<Taxi> interestedTaxiList = new LinkedList<>();

    public RequestWindow(long timeStart, long timeEnd, CustomerRequest customerRequest) {
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
                    grid.addRequestWindow(this, i, j);
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
                    grid.removeRequestWindow(this, i, j);
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

    @Override
    public void run() {
        addGridRequestWindowSet(grid);
        try {
            sleep(WINDOW_LENGTH);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        removeGridRequestWindowSet(grid);

        Taxi nearestTaxi = null;
        int minDistance = Integer.MAX_VALUE;
        SPF spf = new SPF(this.grid);
        for (Taxi taxi : interestedTaxiList) {
            // TODO: check taxi status
            synchronized (taxi) {
                if (taxi.getTaxiState() != TaxiState.Serving) {
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
        if (nearestTaxi != null) {
            // TODO: output
            nearestTaxi.receiveRequest(this.customerRequest);
        } else {
            // TODO: output
        }
    }
}

class RequestWindowSet {
    private HashMap<Integer, RequestWindow> windowSet;
    public void put(RequestWindow r) {
        windowSet.put(r.getIndex(), r);
    }
    public void remove(RequestWindow r) {
        windowSet.remove(r.getIndex());
    }
    public boolean isEmpty() {
        return windowSet.isEmpty();
    }
}