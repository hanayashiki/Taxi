package taxi;

import java.util.*;

public class SPF {
    private Grid grid;
    public SPF(Grid grid) {
        this.grid = grid;
    }
    public List<Node> getShortestPath(int startI, int startJ, int targetI, int targetJ, String algorithm) {
        synchronized (grid) {
            if (algorithm.equals("dijkstra")) {
                return dijkstra(startI, startJ, targetI, targetJ);
            } else if (algorithm.equals("bfs")) {
                return bfs(startI, startJ, targetI, targetJ);
            } else {
                throw new RuntimeException("Invalid algorithm name");
            }
        }
    }
    public Node getNextStep(int startI, int startJ, int targetI, int targetJ, String algorithm) {
        // TODO: optimize
        List<Node> path = getShortestPath(startI, startJ, targetI, targetJ, algorithm);
        if (path.size() > 1) {
            return path.get(1);
        } else if (path.size() == 1) {
            throw new RuntimeException(String.format("Already arrived at (%d, %d)", targetI, targetJ));
        } else {
            return null;
        }
    }
    public List<Node> generatePath(Node node) {
        LinkedList<Node> path = new LinkedList<>();
        while (node != null) {
            path.addFirst(node);
            node = node.getPrecedent();
        }
        return path;
    }
    private List<Node> dijkstra(int startI, int startJ, int targetI, int targetJ) {
        boolean [][] visited = new boolean[Grid.GRID_ROW_NUM][Grid.GRID_COL_NUM];

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(6400);
        int round = 0;
        int longRound = 0;
        synchronized (grid) {
            priorityQueue.add(new Node(startI, startJ, 0, 0, 0));
            while (!priorityQueue.isEmpty()) {
                round++;
                Node node = priorityQueue.poll();
                if (!visited[node.getI()][node.getJ()]) {
                    visited[node.getI()][node.getJ()] = true;
                } else {
                    continue;
                }
                longRound++;
                if (node.getI() == targetI && node.getJ() == targetJ) {
                    System.out.println("SPF while repeated " + round + " times.");
                    System.out.println("SPF long while repeated " + longRound + " times.");
                    return generatePath(node);
                }

                List<Node> adjacentNodes = grid.getAdjacentNodes(node);
                for (Node adjacentNode : adjacentNodes) {
                    if (!visited[adjacentNode.getI()][adjacentNode.getJ()]) {
                        adjacentNode.setDistance(node.getDistance() + 1);
                        adjacentNode.setFlowSum(node.getFlowSum() + adjacentNode.getFlow());
                        adjacentNode.setPrecedent(node);
                        priorityQueue.add(adjacentNode);
                    }
                }
            }
        }

        return null;
    }

    private List<Node> bfs(int startI, int startJ, int targetI, int targetJ) {
        boolean [][] visited = new boolean[Grid.GRID_ROW_NUM][Grid.GRID_COL_NUM];
        int [][] metrics = new int[Grid.GRID_ROW_NUM][Grid.GRID_COL_NUM];
        int [][] distance = new int[Grid.GRID_ROW_NUM][Grid.GRID_COL_NUM];
        int [][] path = new int[Grid.GRID_ROW_NUM][Grid.GRID_COL_NUM];

        for (int i = 0; i < metrics.length; i++) {
            for (int j = 0; j < metrics[i].length; j++) {
                metrics[i][j] = Integer.MAX_VALUE;
                distance[i][j] = Integer.MAX_VALUE;
            }
        }

        Queue<Node> queue = new ArrayDeque<>(6400);
        synchronized (grid) {
            queue.add(new Node(startI, startJ, 0, 0, 0));
            metrics[startI][startJ] = 0;
            distance[startI][startJ] = 0;
            visited[startI][startJ] = true;
            path[startI][startJ] = -1;
            while (!queue.isEmpty()) {
                Node node = queue.poll();
                //System.out.println("Visiting: " + node.toString());
                if (node.getI() == targetI && node.getJ() == targetJ) {
                    int i = targetI, j = targetJ;
                    LinkedList<Node> pathNodes = new LinkedList<>();
                    while (path[i][j] != -1) {
                        pathNodes.addFirst(new Node(i, j));
                        int pathValue = path[i][j];
                        i = pathValue / Grid.GRID_COL_NUM;
                        j = pathValue % Grid.GRID_COL_NUM;
                    }
                    pathNodes.addFirst(new Node(i, j));
                    return pathNodes;
                }
                List<Node> adjacentNodes = grid.getAdjacentNodes(node);
                for (Node adjacentNode : adjacentNodes) {
                    int newFlow = adjacentNode.getFlow() + metrics[node.getI()][node.getJ()];
                    int newDistance = 1 + distance[node.getI()][node.getJ()];
                    if (newFlow < metrics[adjacentNode.getI()][adjacentNode.getJ()] &&
                            newDistance <= distance[adjacentNode.getI()][adjacentNode.getJ()]) {
                        metrics[adjacentNode.getI()][adjacentNode.getJ()] = newFlow;
                        distance[adjacentNode.getI()][adjacentNode.getJ()] = newDistance;
                        path[adjacentNode.getI()][adjacentNode.getJ()] = Grid.GRID_COL_NUM * node.getI() + node.getJ();
                        // System.out.println(String.format("(%d, %d) is set path to (%d, %d)", adjacentNode.getI(), adjacentNode.getJ(), node.getI(), node.getJ()));
//                        if (Math.abs(adjacentNode.getI() - node.getI())  + Math.abs(adjacentNode.getJ() - node.getJ()) > 1) {
//                            throw new RuntimeException();
//                        }
                    }
                    if (!visited[adjacentNode.getI()][adjacentNode.getJ()]) {
                        visited[adjacentNode.getI()][adjacentNode.getJ()] = true;
                        //System.out.println("Not visited: " + adjacentNode);
                        queue.add(adjacentNode);
                    }
                }
            }
        }
        return null;
    }
}
