package test.taxi;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import taxi.ConfigParser;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
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
    public void testParse() throws Exception {
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
        // TODO: some asserts
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
