package test.taxi;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import taxi.CustomerRequest;
import taxi.Request;
import taxi.RequestParser;

/**
 * RequestParser Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>ÁùÔÂ 20, 2018</pre>
 */
public class RequestParserTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: Parse(String input)
     */
    @Test
    public void testParse() throws Exception {
    //TODO: Test goes here...
        Request request = RequestParser.Parse("[CR, (1, 1), (79, 79)]");
        Assert.assertTrue(request instanceof CustomerRequest);
    }


} 
