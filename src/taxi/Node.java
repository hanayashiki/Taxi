package taxi;

public class Node implements Comparable<Node> {
    private int i;
    private int j;
    private int flow;
    private int flowSum;
    private int distance;

    private Node precedent = null;

    public Node(int i, int j) {
        this.i = i;
        this.j = j;
        this.flow = 0;
    }

    public Node(int i, int j, int flow) {
        this.i = i;
        this.j = j;
        this.flow = flow;
    }

    public Node(int i, int j, int flow, int flowSum, int distance) {
        this.i = i;
        this.j = j;
        this.flow = flow;
        this.flowSum = flowSum;
        this.distance = distance;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    public int getFlowSum() {
        return flowSum;
    }

    public void setFlowSum(int cost) {
        this.flowSum = cost;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setPrecedent(Node precedent) {
        this.precedent = precedent;
    }

    public Node getPrecedent() {
        return precedent;
    }

    @Override
    public int compareTo(Node that) {
        if (this.distance < that.distance) {
            return -1;
        } else if (this.distance > that.distance) {
            return 1;
        } else {
            return new Integer(this.flowSum).compareTo(that.flowSum);
        }
    }

    @Override
    public String toString() {
        return "(" + i + ", " + j + ")";
    }

    public boolean samePos(Node node) {
        if (node == null) {
            return false;
        } else return this.getI() == node.getI() && this.getJ() == node.getJ();
    }
}

