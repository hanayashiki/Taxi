package utils;

import taxi.Adjacency;

import static taxi.Adjacency.*;

public class AdjacencyUtils {
    public static void connect(Adjacency[][] adjacencies, int i1, int j1, int i2, int j2) {
        // 2 at top
        if (!Index.checkAdjacency(i1, j1, i2, j2)) {
            throw new IllegalArgumentException();
        }
        if (i1 - 1 == i2) {
            connectDown(adjacencies, i2, j2);
        }
        // 2 at left
        if (j2 == j1 - 1) {
            connectRight(adjacencies, i2, j2);
        }
        // 2 at right
        if (j2 == j1 + 1) {
            connectRight(adjacencies, i1, j1);
        }
        // 2 at down
        if (i1 + 1 == i2) {
            connectDown(adjacencies, i1, j1);
        }
    }

    public static void disjoin(Adjacency[][] adjacencies, int i1, int j1, int i2, int j2) {
        // 2 at top
        if (!Index.checkAdjacency(i1, j1, i2, j2)) {
            throw new IllegalArgumentException();
        }
        if (i1 - 1 == i2) {
            disjoinDown(adjacencies, i2, j2);
        }
        // 2 at left
        if (j2 == j1 - 1) {
            disjoinRight(adjacencies, i2, j2);
        }
        // 2 at right
        if (j2 == j1 + 1) {
            disjoinRight(adjacencies, i1, j1);
        }
        // 2 at down
        if (i1 + 1 == i2) {
            disjoinDown(adjacencies, i1, j1);
        }
    }

    public static void connectRight(Adjacency[][] adjacencies, int i, int j) {
        switch (adjacencies[i][j]) {
            case LEAF:
                adjacencies[i][j] = taxi.Adjacency.RIGHT;
                break;
            case DOWN:
                adjacencies[i][j] = taxi.Adjacency.BOTH;
                break;
        }
    }

    public static void disjoinRight(Adjacency[][] adjacencies, int i, int j) {
        switch (adjacencies[i][j]) {
            case BOTH:
                adjacencies[i][j] = taxi.Adjacency.DOWN;
                break;
            case RIGHT:
                adjacencies[i][j] = taxi.Adjacency.LEAF;
                break;
        }
    }

    public static void connectDown(Adjacency[][] adjacencies, int i, int j) {
        switch (adjacencies[i][j]) {
            case LEAF:
                adjacencies[i][j] = taxi.Adjacency.DOWN;
                break;
            case RIGHT:
                adjacencies[i][j] = taxi.Adjacency.BOTH;
                break;
        }
    }

    public static void disjoinDown(Adjacency[][] adjacencies, int i, int j) {
        switch (adjacencies[i][j]) {
            case BOTH:
                adjacencies[i][j] = taxi.Adjacency.RIGHT;
                break;
            case DOWN:
                adjacencies[i][j] = taxi.Adjacency.LEAF;
                break;
        }
    }
}
