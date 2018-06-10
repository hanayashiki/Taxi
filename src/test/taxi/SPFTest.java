package test.taxi;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import taxi.*;
import utils.PathDrawer;

import java.util.List;

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
        List<Node> path = spf.getShortestPath(1, 1, 10, 5);
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
        List<Node> path = spf.getShortestPath(1, 1, 10, 1);
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
        List<Node> path = spf.getShortestPath(0, 0, 10, 5);
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
        grid.setFlowRight(100, 5,4);
        SPF spf = new SPF(grid);
        List<Node> path = spf.getShortestPath(0, 0, 10, 5);
        System.out.println(path);
        System.out.println(pd.drawPath(path, pd.getAdjacencyGrid()));
    }

    /**
     * Method: generatePath(Node node)
     */
    @Test
    public void testGeneratePath() throws Exception {
    //TODO: Test goes here...
    }


} 
