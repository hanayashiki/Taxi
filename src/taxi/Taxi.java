package taxi;

import utils.Verbose;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Thread.sleep;

public class Taxi extends Thread {
    public static final int MAX_INDEX = 99;

    private int index;

    private int i;
    private int j;
    private TaxiState state;
    private int credit;

    private Grid grid;

    private CustomerRequest customerRequest = null;

    private Sleeper sleeper = new Sleeper(500);

    class GetNewRequestException extends RuntimeException {
    }

    public Taxi(Grid grid, int index, int i, int j) {
        this.grid = grid;
        this.index = index;
        this.credit = 0;
        this.state = TaxiState.Idle;
        this.i = i;
        this.j = j;
    }

    public Taxi(Grid grid, int index, TaxiState state, int credit, int i, int j) {
        this.grid = grid;
        this.index = index;
        this.state = state;
        this.credit = credit;
        this.i = i;
        this.j = j;
    }

    public void run() {
        int targetI, targetJ;
        TaxiWalk taxiWalk = null;
        while (true) {
            try {
                switch (state) {
                    case Idle:
                        taxiWalk = new IdleWalk(this.grid, this, 40);
                        while (!taxiWalk.simulate()) {
                            sleeper.sleepN(1);
                            if (this.getCustomerRequest() != null) {
                                Verbose.printlnAt("Taxi " + this.getIndex() + " state: " + state + " -> " + TaxiState.OnTheWay);
                                state = TaxiState.OnTheWay;
                                throw new GetNewRequestException();
                            }
                        }
                        state = TaxiState.Stopped;
                        break;
                    case Serving:
                        synchronized (this) {
                            targetI = this.getCustomerRequest().getDestinationI();
                            targetJ = this.getCustomerRequest().getDestinationJ();
                        }
                        taxiWalk = new SPFWalk(this.grid, this, targetI, targetJ);
                        while (!taxiWalk.simulate()) {
                            sleeper.sleepN(1);
                        }
                        Verbose.printlnAt("Taxi " + this.getIndex() + " state: " + state + " -> " + TaxiState.Stopped);
                        state = TaxiState.Stopped;
                        this.setCustomerRequest(null);
                        break;
                    case OnTheWay:
                        synchronized (this) {
                            targetI = this.getCustomerRequest().getSourceI();
                            targetJ = this.getCustomerRequest().getSourceJ();
                        }
                        taxiWalk = new SPFWalk(this.grid, this, targetI, targetJ);
                        while (!taxiWalk.simulate()) {
                            sleeper.sleepN(1);
                        }
                        sleeper.sleepN(2);
                        Verbose.printlnAt("Taxi " + this.getIndex() + " state: " + state + " -> " + TaxiState.Serving);
                        state = TaxiState.Serving;
                        break;
                    case Stopped:
                        sleeper.sleepN(2);
                        Verbose.printlnAt("Taxi " + this.getIndex() + " state: " + state + " -> " + TaxiState.Idle);
                        state = TaxiState.Idle;
                        break;

                }
            } catch (GetNewRequestException e) {
                continue;
            }
        }
    }

    synchronized public int getIndex() {
        return index;
    }

    synchronized public int getI() {
        return i;
    }

    synchronized public int getJ() {
        return j;
    }

    public void copyStatus(Taxi taxi) {
        this.i = taxi.getI();
        this.j = taxi.getJ();
        this.credit = taxi.getCredit();
        this.state = taxi.getTaxiState();
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }

    synchronized public TaxiState getTaxiState() {
        return state;
    }

    synchronized public int getCredit() {
        return credit;
    }

    synchronized public CustomerRequest getCustomerRequest() {
        return customerRequest;
    }

    synchronized public void setCustomerRequest(CustomerRequest customerRequest) {
        this.customerRequest = customerRequest;
    }

    synchronized public void receiveRequest(CustomerRequest request) {
        this.setCustomerRequest(request);
    }
}

abstract class TaxiWalk {
    public abstract boolean simulate(); // \result indicates whether the simulation is finished
}

class IdleWalk extends TaxiWalk {
    // 在 Idle 状态时，出租车如果遇到道路分支，选择流量最小的边行走，如果有多条流量最小的边，可随机选择一条分支边行走。
    private int repeated = 0;
    private int repeatTime;
    private Grid grid;
    private Taxi taxi;

    private Node lastVisited = null;

    public IdleWalk(Grid grid, Taxi taxi, int repeatTime) {
        this.grid = grid;
        this.taxi = taxi;
        this.repeatTime = repeatTime;
    }

    private void visit(Node node) {
        lastVisited = new Node(taxi.getI(), taxi.getJ());
        taxi.setI(node.getI());
        taxi.setJ(node.getJ());
    }

    private void grabRequest(Taxi taxi, Grid grid) {
        synchronized (taxi) {
            int i = taxi.getI();
            int j = taxi.getJ();
            List<RequestWindow> requestWindows = grid.getRequestWindows(i, j);
            for (RequestWindow requestWindow : requestWindows) {
                requestWindow.grabRequest(taxi);
            }
        }
    }

    public boolean simulate() {
        synchronized (taxi) {
            repeated++;
            grabRequest(taxi, grid);
            List<Node> adjacentNodes = grid.getAdjacentNodes(taxi.getI(), taxi.getJ());
            switch (adjacentNodes.size()) {
                case 0:
                    throw new InputException(String.format("No adjacent nodes at (%d, %d)", taxi.getI(), taxi.getJ()));
                case 1:
                    Node node = adjacentNodes.get(0);
                    visit(node);
                    break;
                case 2:
                    for (Node adjacentNode : adjacentNodes) {
                        if (!adjacentNode.samePos(lastVisited)) {
                            visit(adjacentNode);
                        }
                    }
                    break;
                case 3:
                case 4:
                    adjacentNodes.sort(Comparator.comparing(Node::getFlow));
                    visit(adjacentNodes.get(0));
                    break;
            }
            if (repeated == repeatTime) {
                return true;
            }
            return false;
        }
    }
}

class SPFWalk extends TaxiWalk {
    // 在 OnTheWay 和 Serving 状态时，要求出租车按照最短路径行走
    private int targetI;
    private int targetJ;
    private Grid grid;
    private Taxi taxi;
    private SPF spf;

    public SPFWalk(Grid grid, Taxi taxi, int targetI, int targetJ) {
        this.grid = grid;
        this.taxi = taxi;
        this.spf = new SPF(grid);
        this.targetI = targetI;
        this.targetJ = targetJ;
    }

    public boolean simulate() {
        synchronized (taxi) {
            int sourceI = taxi.getI();
            int sourceJ = taxi.getJ();
            Node nextStep = spf.getNextStep(sourceI, sourceJ, targetI, targetJ, "bfs");
            if (nextStep != null) {
                this.taxi.setI(nextStep.getI());
                this.taxi.setJ(nextStep.getJ());
                Verbose.printlnAt("Taxi " + taxi.getIndex() + " from (" + sourceI + ", " + sourceJ + ")" +
                        " to (" + this.taxi.getI() + ", " + this.taxi.getJ() + ")");
                if (this.taxi.getI() == targetI && this.taxi.getJ() == targetJ) {
                    return true;
                }
            } else {
                throw new InputException(String.format("Unreachable route: (%d, %d) to (%d, %d)",
                        sourceI, sourceJ, targetI, targetJ));
            }
        }
        return false;
    }
}

class Sleeper {
    // 按节奏睡眠
    private long lastSleep = 0;
    private long sleepPeriod;

    public Sleeper(long sleepPeriod) {
        this.sleepPeriod = sleepPeriod;
    }

    public void sleepN(long nPeriod) {
        try {
            sleep(sleepPeriod * nPeriod - (lastSleep % sleepPeriod));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lastSleep = Global.getRelativeTime();
    }
}