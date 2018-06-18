package taxi;

public class Taxi extends Thread {
    private int index;
    private int i;
    private int j;
    private TaxiState state;
    private int credit;

    public Taxi(int index, int i, int j) {
        this.index = index;
        this.credit = 0;
        this.state = TaxiState.Idle;
        this.i = i;
        this.j = j;
    }

    public Taxi(int index, TaxiState state, int credit, int i, int j) {
        this.index = index;
        this.state = TaxiState.Idle;
        this.credit = 0;
        this.i = i;
        this.j = j;
    }

    public void run() {

    }

}

class IdleWalk {
    // 在 Idle 状态时，出租车如果遇到道路分支，选择流量最小的边行走，如果有多条流量最小的边，可随机选择一条分支边行走。
}

class SPFWalk {
    // 在 OnTheWay 和 Serving 状态时，要求出租车按照最短路径行走
}