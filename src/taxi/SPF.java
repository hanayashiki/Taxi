package taxi;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class SPF {
    private Grid grid;
    public SPF(Grid grid) {
        this.grid = grid;
    }
    public List<Node> getShortestPath(int startI, int startJ, int targetI, int targetJ) {
        boolean [][] visited = new boolean[Grid.GRID_ROW_NUM][Grid.GRID_COL_NUM];

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        synchronized (grid) {
            priorityQueue.add(new Node(startI, startJ, 0, 0, 0));
            while (!priorityQueue.isEmpty()) {
                Node node = priorityQueue.poll();
                if (!visited[node.getI()][node.getJ()]) {
                    visited[node.getI()][node.getJ()] = true;
                } else {
                    continue;
                }

                if (node.getI() == targetI && node.getJ() == targetJ) {
                    return generatePath(node);
                }

                List<Node> adjacentNodes = grid.getAdjacentNodes(node);
                for (Node adjacentNode : adjacentNodes) {
                    if (!visited[adjacentNode.getI()][adjacentNode.getJ()]) {
                        adjacentNode.setDistance(node.getDistance() + adjacentNode.getFlow());
                        adjacentNode.setPrecedent(node);
                        priorityQueue.add(adjacentNode);
                    }
                }
            }
        }

        return null;
    }
    public List<Node> generatePath(Node node) {
        LinkedList<Node> path = new LinkedList<>();
        while (node != null) {
            path.addFirst(node);
            node = node.getPrecedent();
        }
        return path;
    }

}
