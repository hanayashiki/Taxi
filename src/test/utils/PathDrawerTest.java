package test.utils;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import utils.PathDrawer;

/**
 * PathDrawer Tester.
 *
 * @author <Authors name>
 * @version 1.0
 */
public class PathDrawerTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: pointTo(int i, int j)
     */
    @Test
    public void testPointTo() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: lineTo(int i, int j)
     */
    @Test
    public void testLineTo() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: line(int i1, int j1, int i2, int j2)
     */
    @Test
    public void testLine() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: connect(int i1, int j1, int i2, int j2)
     */
    @Test
    public void testConnect() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: connectRight(int i, int j)
     */
    @Test
    public void testConnectRight() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: connectDown(int i, int j)
     */
    @Test
    public void testConnectDown() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {
        //TODO: Test goes here...
        PathDrawer pd = new PathDrawer();
        pd.pointTo(1, 1);
        pd.lineTo(5, 5);
        pd.pointTo(3, 1);
        pd.lineTo(3, 5);
        pd.pointTo(5, 5);
        pd.lineTo(10, 5);
        System.out.println(pd.toString());
    }

    @Test
    public void testToString2() throws Exception {
        //TODO: Test goes here...
        PathDrawer pd = new PathDrawer();
        pd.pointTo(5, 5);
        pd.lineTo(0, 0);
        System.out.println(pd.toString());
    }

} 
