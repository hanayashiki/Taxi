package test.taxi;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import taxi.Adjacency;
import taxi.ConfigParser;
import taxi.InputException;
import taxi.Taxi;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

/**
 * ConfigParser Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>六月 13, 2018</pre>
 */
public class ConfigParserTest {

    private ConfigParser getConfigParser(String testInputString) {
        return new ConfigParser(new ByteArrayInputStream(testInputString.getBytes()));
    }

    private ConfigParser getConfigParser(InputStream stream) {
        return new ConfigParser(stream);
    }

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: parse()
     */
    @Test
    public void testParseMap() throws Exception {
        //TODO: Test goes here...
        ConfigParser configParser;
        String testInputStringEmpty = "#fuck you leather man#\n" +
                "\n" +
                "#map\n" +
                "#end_map\n" +
                "\n" +
                "#flow\n" +
                "#end_flow\n" +
                "\n" +
                "#taxi\n" +
                "#end_taxi\n" +
                "\n" +
                "#request\n" +
                "#end_request";
        configParser = getConfigParser(testInputStringEmpty);
        configParser.parse();

        FileInputStream fileInputStream1 = new FileInputStream("src/test/taxi/configs/config1.txt");
        configParser = getConfigParser(fileInputStream1);
        configParser.parse();
        Assert.assertEquals(Adjacency.values()[0], configParser.getGrid().getAdjacency(79, 79));
        Assert.assertEquals(Adjacency.values()[1], configParser.getGrid().getAdjacency(79, 78));
    }

    @Test
    public void testParseMapIllegal() throws Exception {
        //TODO: Test goes here...
        ConfigParser configParser;
        int exceptionCount = 0;

        try {
            FileInputStream fileInputStream = new FileInputStream("src/test/taxi/configs/config5.txt");
            configParser = getConfigParser(fileInputStream);
            configParser.parse();
        } catch (InputException e) {
            exceptionCount++;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream("src/test/taxi/configs/config6.txt");
            configParser = getConfigParser(fileInputStream);
            configParser.parse();
        } catch (InputException e) {
            exceptionCount++;
        }


        Assert.assertEquals(2, exceptionCount);
    }

    @Test
    public void testParseFlow() throws Exception {
        //TODO: Test goes here...
        ConfigParser configParser;

        FileInputStream fileInputStream = new FileInputStream("src/test/taxi/configs/config2.txt");
        configParser = getConfigParser(fileInputStream);
        configParser.parse();
        Assert.assertEquals(111, configParser.getGrid().getFlow(1, 1, 1, 2));
        Assert.assertEquals(222, configParser.getGrid().getFlow(1, 1, 1, 0));
        Assert.assertEquals(333, configParser.getGrid().getFlow(1, 1, 0, 1));
        Assert.assertEquals(444, configParser.getGrid().getFlow(1, 1, 2, 1));
    }

    @Test
    public void testParseFlowIllegal() throws Exception {
        ConfigParser configParser;
        int exceptionCount = 0;
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream("src/test/taxi/configs/config3.txt");
            configParser = getConfigParser(fileInputStream);
            configParser.parse();
        } catch (InputException e) {
            exceptionCount++;
        }

        try {
            fileInputStream = new FileInputStream("src/test/taxi/configs/config4.txt");
            configParser = getConfigParser(fileInputStream);
            configParser.parse();
        } catch (InputException e) {
            exceptionCount++;
        }

        Assert.assertEquals(2, exceptionCount);
    }

    @Test
    public void testParseTaxiStatus() throws Exception {
        ConfigParser configParser;
        FileInputStream fileInputStream;
        fileInputStream = new FileInputStream("src/test/taxi/configs/config7.txt");
        configParser = getConfigParser(fileInputStream);
        configParser.parse();
        List<Taxi> taxiList = configParser.getTaxiList();
        Assert.assertEquals(150, taxiList.get(1).getCredit());
    }

    @Test
    public void testParseTaxiStatusIllegal() throws Exception {
        ConfigParser configParser;
        int exceptionCount = 0;
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream("src/test/taxi/configs/config8.txt");
            configParser = getConfigParser(fileInputStream);
            configParser.parse();
        } catch (InputException e) {
            exceptionCount++;
        }
    }
    /**
     * Method: matchLine(String regex, String line)
     */
    @Test
    public void testMatchLine() throws Exception {
        //TODO: Test goes here...
        /*
        try {
           Method method = ConfigParser.getClass().getMethod("matchLine", String.class, String.class);
           method.setAccessible(true);
           method.invoke(<Object>, <Parameters>);
        } catch(NoSuchMethodException e) {
        } catch(IllegalAccessException e) {
        } catch(InvocationTargetException e) {
        }
        */
    }

    /**
     * Method: peekLine(String regex, String line)
     */
    @Test
    public void testPeekLine() throws Exception {
        //TODO: Test goes here...
        /*
        try {
           Method method = ConfigParser.getClass().getMethod("peekLine", String.class, String.class);
           method.setAccessible(true);
           method.invoke(<Object>, <Parameters>);
        } catch(NoSuchMethodException e) {
        } catch(IllegalAccessException e) {
        } catch(InvocationTargetException e) {
        }
        */
    }

} 
