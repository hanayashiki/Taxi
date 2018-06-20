package taxi;

import jdk.nashorn.internal.ir.RuntimeNode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static taxi.ConfigParser.Status.Map;

public class Global {
    public static long timeZeroPoint = System.currentTimeMillis();
    public Grid grid;
    public List<Taxi> taxiList;

    public Global(int taxiCount) {
        Adjacency[][] adjacencies = new Adjacency[Grid.GRID_ROW_NUM][Grid.GRID_COL_NUM];
        for (int i = 0; i < Grid.GRID_ROW_NUM - 1; i++) {
            for (int j = 0; j < Grid.GRID_COL_NUM - 1; j++) {
                adjacencies[i][j] = Adjacency.BOTH;
            }
        }
        for (int i = 0; i < Grid.GRID_ROW_NUM - 1; i++) {
            adjacencies[i][Grid.GRID_COL_NUM - 1] = Adjacency.DOWN;
        }
        for (int j = 0; j < Grid.GRID_COL_NUM - 1; j++) {
            adjacencies[Grid.GRID_ROW_NUM - 1][j] = Adjacency.RIGHT;
        }
        adjacencies[Grid.GRID_ROW_NUM - 1][Grid.GRID_COL_NUM - 1] = Adjacency.LEAF;

        grid = new Grid(adjacencies);


        taxiList = new ArrayList<>(taxiCount);
        for (int idx = 0; idx < taxiCount; idx++) {
            taxiList.add(new Taxi(grid, idx, 0, 0)); // TODO: fix this
        }
    }

    public void loadConfig(LoadRequest loadRequest) {
        try {
            synchronized (grid) {
                FileInputStream inputStream = new FileInputStream(loadRequest.getFilePath());
                ConfigParser configParser = new ConfigParser(inputStream, grid);
                configParser.parse(); // grid modified
                for (Taxi taxi : configParser.getTaxiList()) {
                    int index = taxi.getIndex();
                    taxiList.get(index).copyStatus(taxi);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InputException e) {
            System.out.println("invalid input: " + e.getMessage());
        }
    }

    public void startTaxiThreads() {
        for (Taxi taxi : taxiList) {
            taxi.start();
        }
    }

    public static long getRelativeTime() {
        return System.currentTimeMillis() - timeZeroPoint;
    }



}

