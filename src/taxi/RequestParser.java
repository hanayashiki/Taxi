package taxi;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestParser {
    private static final String customerRequestRegex =
            "\\[\\s*CR\\s*,\\s*\\(\\s*\\+?([0-9]+)\\s*,\\s*\\+?([0-9]+)\\s*\\)\\s*,\\s*\\(\\s*\\+?([0-9]+)\\s*,\\s*\\+?([0-9]+)\\s*\\)\\s*\\]";
    private static final String loadRequestRegex =
            "Load\\s+(.*)";
    private static final String endRequestRegex =
            "End";
    private static final Pattern customerRequestPattern = Pattern.compile(customerRequestRegex);
    private static final Pattern loadRequestPattern = Pattern.compile(loadRequestRegex);
    private static final Pattern endRequestPattern = Pattern.compile(endRequestRegex);


    public static Request Parse(String input) throws InputException {
        String trimmedInput = input.trim();
        Matcher customerRequestMatcher = customerRequestPattern.matcher(trimmedInput);
        Matcher loadRequestMatcher = loadRequestPattern.matcher(trimmedInput);
        Matcher endRequestMatcher = endRequestPattern.matcher(trimmedInput);
        if (customerRequestMatcher.matches()) {
            try {
                int sourceI = Integer.parseInt(customerRequestMatcher.group(1));
                int sourceJ = Integer.parseInt(customerRequestMatcher.group(2));
                int targetI = Integer.parseInt(customerRequestMatcher.group(3));
                int targetJ = Integer.parseInt(customerRequestMatcher.group(4));
                return new CustomerRequest(sourceI, sourceJ, targetI, targetJ);
            } catch (IllegalArgumentException e) {
                throw new InputException(input);
            }
        }
        if (loadRequestMatcher.matches()) {
            return new LoadRequest(loadRequestMatcher.group(1));
        }
        if (endRequestMatcher.matches()) {
            return new EndRequest();
        }

        throw new InputException(input);
    }

}
