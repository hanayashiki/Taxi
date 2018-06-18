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
    public static Grid grid;
    public static List<Taxi> taxiList;

    public Global(int taxiCount) {
        Adjacency[][] adjacencies = new Adjacency[Grid.GRID_ROW_NUM][Grid.GRID_COL_NUM];
        for (int i = 0; i < Grid.GRID_ROW_NUM; i++) {
            for (int j = 0; j < Grid.GRID_COL_NUM; j++) {
                adjacencies[i][j] = Adjacency.BOTH;
            }
        }
        grid = new Grid(adjacencies);

        taxiList = new ArrayList<>(taxiCount);
        for (int idx = 0; idx < taxiCount; idx++) {
            taxiList.add(new Taxi(idx, 0, 0)); // TODO: fix this
        }
    }

    public void loadStatus(LoadRequest loadRequest) {
        try {
            FileInputStream inputStream = new FileInputStream(loadRequest.getFilePath());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static long getRelativeTime() {
        return System.currentTimeMillis() - timeZeroPoint;
    }

}

