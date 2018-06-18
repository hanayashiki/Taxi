package taxi;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    public static final int GRID_COL_NUM = 80;
    public static final int GRID_ROW_NUM = 80;
    private Adjacency[][] adjacencyGrid;
    private int[][] rightFlow; // flow of (i, j) to the right side
    private int[][] downFlow;  // flow of (i, j) to the down side

    public Grid() {
        this.adjacencyGrid = new Adjacency[GRID_ROW_NUM][GRID_COL_NUM];
        this.rightFlow = new int[GRID_ROW_NUM][GRID_COL_NUM];
        this.downFlow = new int[GRID_ROW_NUM][GRID_COL_NUM];
    }

    public Grid(Adjacency[][] grid) {
        this.adjacencyGrid = grid;
        this.rightFlow = new int[GRID_ROW_NUM][GRID_COL_NUM];
        this.downFlow = new int[GRID_ROW_NUM][GRID_COL_NUM];
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

    synchronized public Adjacency getAdjacency(int i, int j) {
        return adjacencyGrid[i][j];
    }
}
