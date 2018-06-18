package test.taxi;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import taxi.*;
import utils.PathDrawer;

import java.util.List;
import java.util.Random;

/**
 * SPF Tester.
 *
 * @author <Authors name>
 * @version 1.0
 */
public class SPFTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getShortestPath(int startI, int startJ, int targetI, int targetJ)
     */
    @Test
    public void testGetShortestPath() throws Exception {
    //TODO: Test goes here...
        PathDrawer pd = new PathDrawer();
        pd.pointTo(1, 1);
        pd.lineTo(5, 5);
        pd.pointTo(3, 1);
        pd.lineTo(3, 5);
        pd.pointTo(5, 5);
        pd.lineTo(10, 5);
        System.out.println(pd.toString());

        Grid grid = new Grid(pd.getAdjacencyGrid());
        SPF spf = new SPF(grid);
        List<Node> path = spf.getShortestPath(1, 1, 10, 5, "bfs");
        System.out.println(path);
        System.out.println(pd.drawPath(path, pd.getAdjacencyGrid()));
    }

    @Test
    public void testGetShortestPath2() throws Exception {
        //TODO: Test goes here...
        PathDrawer pd = new PathDrawer();
        pd.pointTo(1, 1);
        pd.lineTo(3, 1);
        pd.pointTo(4, 1);
        pd.lineTo(10, 1);
        pd.pointTo(1, 1);
        pd.lineTo(1, 5);
        pd.lineTo(5, 5);
        pd.lineTo(5, 1);

        System.out.println(pd.toString());

        Grid grid = new Grid(pd.getAdjacencyGrid());
        SPF spf = new SPF(grid);
        List<Node> path = spf.getShortestPath(1, 1, 10, 1, "bfs");
        System.out.println(path);
        System.out.println(pd.drawPath(path, pd.getAdjacencyGrid()));
    }

    @Test
    public void testGetShortestPath3() throws Exception {
        //TODO: Test goes here...
        PathDrawer pd = new PathDrawer();
        pd.pointTo(0, 0);
        pd.lineTo(10, 5);
        pd.pointTo(5, 0);
        pd.lineTo(5, 5);
        pd.lineTo(10, 5);

        System.out.println(pd.toString());

        Grid grid = new Grid(pd.getAdjacencyGrid());
        grid.setFlowDown(100, 5,5);
        SPF spf = new SPF(grid);
        List<Node> path = spf.getShortestPath(0, 0, 10, 5, "bfs");
        System.out.println(path);
        System.out.println(pd.drawPath(path, pd.getAdjacencyGrid()));
    }

    @Test
    public void testGetShortestPath4() throws Exception {
        //TODO: Test goes here...
        PathDrawer pd = new PathDrawer();
        pd.pointTo(0, 0);
        pd.lineTo(10, 5);
        pd.pointTo(5, 0);
        pd.lineTo(5, 5);
        pd.lineTo(10, 5);
        pd.pointTo(2, 0);
        pd.lineTo(2, 2);
        pd.lineTo(5, 2);

        System.out.println(pd.toString());

        Grid grid = new Grid(pd.getAdjacencyGrid());
        grid.setFlowDown(100, 5,0);
        grid.setFlowDown(100, 2,2);
        SPF spf = new SPF(grid);
        List<Node> path = spf.getShortestPath(0, 0, 10, 5, "bfs");
        System.out.println(path);
        System.out.println(pd.drawPath(path, pd.getAdjacencyGrid()));
    }

    @Test
    public void testGetShortestPathSpeed() throws Exception {
        PathDrawer pd = new PathDrawer();
        for (int i = 0; i < Grid.GRID_ROW_NUM; i++) {
            pd.pointTo(i, 0);
            pd.lineTo(i, Grid.GRID_COL_NUM - 1);
        }
        for (int j = 0; j < Grid.GRID_COL_NUM; j++) {
            pd.pointTo(0, j);
            pd.lineTo(Grid.GRID_COL_NUM - 1, j);
        }

        System.out.println(pd.toString());

        Grid grid = new Grid(pd.getAdjacencyGrid());
        SPF spf = new SPF(grid);

        Random random = new Random();
        for (int i = 0; i <= 78; i++) {
            for (int j = 0; j <= 78; j++) {
                grid.setFlowRight(random.nextInt(10), i, j);
                grid.setFlowDown(random.nextInt(10), i, j);
            }
        }

        long spfStart, spfEnd;
        List<Node> path1;
        List<Node> path2;

        spfStart = System.currentTimeMillis();
        path1 = spf.getShortestPath(0, 0, 79, 79, "bfs");
        spfEnd = System.currentTimeMillis();
        System.out.println(path1);
        System.out.println("Spf bfs time used: " + (spfEnd - spfStart) + "ms");
        System.out.println(pd.drawPath(path1, pd.getAdjacencyGrid()));

        spf = new SPF(grid);
        spfStart = System.currentTimeMillis();
        path2 = spf.getShortestPath(0, 0, 79, 79, "dijkstra");
        spfEnd = System.currentTimeMillis();
        System.out.println(path2);
        System.out.println("Spf dijkstra time used: " + (spfEnd - spfStart) + "ms");
        System.out.println(pd.drawPath(path2, pd.getAdjacencyGrid()));

        Assert.assertEquals(path1.size(), path2.size());
    }

    @Test
    public void testGetShortestPathSpeedRepeat() {
        int repeatCount = 100;

        PathDrawer pd = new PathDrawer();
        for (int i = 0; i < Grid.GRID_ROW_NUM; i++) {
            pd.pointTo(i, 0);
            pd.lineTo(i, Grid.GRID_COL_NUM - 1);
        }
        for (int j = 0; j < Grid.GRID_COL_NUM; j++) {
            pd.pointTo(0, j);
            pd.lineTo(Grid.GRID_COL_NUM - 1, j);
        }

        System.out.println(pd.toString());

        Grid grid = new Grid(pd.getAdjacencyGrid());
        SPF spf = new SPF(grid);

        Random random = new Random();
        for (int i = 0; i <= 78; i++) {
            for (int j = 0; j <= 78; j++) {
                grid.setFlowRight(random.nextInt(10), i, j);
                grid.setFlowDown(random.nextInt(10), i, j);
            }
        }

        long spfStart, spfEnd;

        spfStart = System.currentTimeMillis();
        for (int i = 0; i < repeatCount; i++) {
            spf.getShortestPath(0, 0, 79, 79, "bfs");
        }
        spfEnd = System.currentTimeMillis();
        //System.out.println(path1);
        System.out.println("Spf time used: " + (spfEnd - spfStart) + "ms for " + repeatCount + " rounds.");
        //System.out.println(pd.drawPath(path1, pd.getAdjacencyGrid()));
    }

    /**
     * Method: generatePath(Node node)
     */
    @Test
    public void testGeneratePath() throws Exception {
    //TODO: Test goes here...
    }


} 
