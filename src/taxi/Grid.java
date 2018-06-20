package taxi;

import java.util.*;

import utils.AdjacencyUtils;
import utils.Copy;

public class Grid {
    public static final int GRID_COL_NUM = 80;
    public static final int GRID_ROW_NUM = 80;
    private Adjacency[][] adjacencyGrid;
    private int[][] rightFlow; // flow of (i, j) to the right side
    private int[][] downFlow;  // flow of (i, j) to the down side
    private RequestWindowSet[][] requestWindowSet;

    public Grid() {
        initGrid();
    }

    private void initGrid() {
        this.adjacencyGrid = new Adjacency[GRID_ROW_NUM][GRID_COL_NUM];
        this.rightFlow = new int[GRID_ROW_NUM][GRID_COL_NUM];
        this.downFlow = new int[GRID_ROW_NUM][GRID_COL_NUM];
        this.requestWindowSet = new RequestWindowSet[GRID_ROW_NUM][GRID_COL_NUM];
        for (int i = 0; i < GRID_ROW_NUM; i++) {
            for (int j = 0; j < GRID_COL_NUM; j++) {
                this.requestWindowSet[i][j] = new RequestWindowSet();
            }
        }
    }

    public Grid(Adjacency[][] grid) {
        initGrid();
        this.adjacencyGrid = grid;
    }

    synchronized public List<Node> getAdjacentNodes(int i, int j) {
        Adjacency adjacency = adjacencyGrid[i][j];
        boolean up = i == 0 ? false : (adjacencyGrid[i-1][j] == Adjacency.DOWN || adjacencyGrid[i-1][j] == Adjacency.BOTH);
        boolean left = j == 0 ? false : (adjacencyGrid[i][j-1] == Adjacency.RIGHT || adjacencyGrid[i][j-1] == Adjacency.BOTH);
        boolean right = adjacencyGrid[i][j] == Adjacency.RIGHT || adjacencyGrid[i][j] == Adjacency.BOTH;
        boolean down = adjacencyGrid[i][j] == Adjacency.DOWN || adjacencyGrid[i][j] == Adjacency.BOTH;
        List<Node> adjacentNodes = new ArrayList<>();
        if (up) {
            adjacentNodes.add(new Node(i - 1, j, downFlow[i - 1][j]));
        }
        if (left) {
            adjacentNodes.add(new Node(i, j - 1, rightFlow[i][j - 1]));
        }
        if (right) {
            adjacentNodes.add(new Node(i, j + 1, rightFlow[i][j]));
        }
        if (down) {
            adjacentNodes.add(new Node(i + 1, j, downFlow[i][j]));
        }
        // System.out.println(adjacentNodes);
        return adjacentNodes;
    }

    synchronized public List<Node> getAdjacentNodes(Node node) {
        // System.out.println(node + ": ");
        return getAdjacentNodes(node.getI(), node.getJ());
    }

    synchronized public int getFlow(int sourceI, int sourceJ, int targetI, int targetJ) {
        // UP
        if (targetI == sourceI - 1 && targetJ == sourceJ) {
            return this.downFlow[targetI][targetJ];
        }
        // left
        if (targetI == sourceI && targetJ == sourceJ - 1) {
            return this.rightFlow[targetI][targetJ];
        }
        // right
        if (targetI == sourceI && targetJ == sourceJ + 1) {
            return this.rightFlow[sourceI][sourceJ];
        }
        // down
        if (targetI == sourceI + 1 && targetJ == sourceJ) {
            return this.downFlow[sourceI][sourceJ];
        }
        throw new RuntimeException("Wrong param");
    }

    synchronized public void setFlow(int flow, int sourceI, int sourceJ, int targetI, int targetJ) {
        // UP
        if (targetI == sourceI - 1 && targetJ == sourceJ) {
            this.downFlow[targetI][targetJ] = flow;
        }
        // left
        else if (targetI == sourceI && targetJ == sourceJ - 1) {
            this.rightFlow[targetI][targetJ] = flow;
        }
        // right
        else if (targetI == sourceI && targetJ == sourceJ + 1) {
            this.rightFlow[sourceI][sourceJ] = flow;
        }
        // down
        else if (targetI == sourceI + 1 && targetJ == sourceJ) {
            this.downFlow[sourceI][sourceJ] = flow;
        } else {
            throw new RuntimeException("Wrong param");
        }
    }

    synchronized public void setFlowRight(int flow, int i, int j) {
        rightFlow[i][j] = flow;
    }

    synchronized public void setFlowDown(int flow, int i, int j) {
        downFlow[i][j] = flow;
    }

    synchronized public void setGrid(Adjacency value, int i, int j) { adjacencyGrid[i][j] = value; }

    synchronized public void connect(int i1, int j1, int i2, int j2) {
        AdjacencyUtils.connect(adjacencyGrid, i1, j1, i2, j2);
    }

    synchronized public void disjoin(int i1, int j1, int i2, int j2) {
        AdjacencyUtils.disjoin(adjacencyGrid, i1, j1, i2, j2);
    }

    synchronized public Adjacency[][] getGridClone() {
        Adjacency [][] adjacencies = new Adjacency[GRID_ROW_NUM][GRID_COL_NUM];
        Copy.copy2d(this.adjacencyGrid, adjacencies);
        return adjacencies;
    }

    synchronized public Adjacency getAdjacency(int i, int j) {
        return adjacencyGrid[i][j];
    }

    synchronized public void addRequestWindow(RequestWindow requestWindow, int i, int j) {
        this.requestWindowSet[i][j].put(requestWindow);
    }

    synchronized public void removeRequestWindow(RequestWindow requestWindow, int i, int j) {
        this.requestWindowSet[i][j].remove(requestWindow);
    }

    synchronized public boolean isEmptyRequestWindow(int i, int j) {
        return this.requestWindowSet[i][j].isEmpty();
    }

    synchronized public List<RequestWindow> getRequestWindows(int i, int j) {
        ArrayList<RequestWindow> requestWindows = new ArrayList<>();
        Iterator<Map.Entry<Integer, RequestWindow>> iter = this.requestWindowSet[i][j].entrySet().iterator();
        while (iter.hasNext()) {
            requestWindows.add(iter.next().getValue());
        }
        return requestWindows;
    }
}
