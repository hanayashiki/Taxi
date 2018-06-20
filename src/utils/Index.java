package utils;

import taxi.Grid;

public class Index {
    public static boolean checkIndex(int index) {
        return index >= 0 && index < Grid.GRID_ROW_NUM;
    }
    public static boolean checkAdjacency(int i1, int j1, int i2, int j2) {
        return Math.abs(i1 - i2) + Math.abs(j1 - j2) == 1;
    }
}
