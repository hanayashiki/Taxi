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
    private static final String pathRequestRegex =
            "(Open|Close)\\s*\\(\\s*\\+?\\s*([0-9]+)\\s*,\\s*\\+?\\s*([0-9]+)\\s*\\)\\s*,\\s*\\(\\s*\\+?\\s*([0-9]+)\\s*,\\s*\\+?\\s*([0-9]+)\\s*\\)";
    private static final Pattern customerRequestPattern = Pattern.compile(customerRequestRegex);
    private static final Pattern loadRequestPattern = Pattern.compile(loadRequestRegex);
    private static final Pattern endRequestPattern = Pattern.compile(endRequestRegex);
    private static final Pattern pathRequestPattern = Pattern.compile(pathRequestRegex);


    public static Request Parse(String input) throws InputException {
        String trimmedInput = input.trim();
        Matcher customerRequestMatcher = customerRequestPattern.matcher(trimmedInput);
        Matcher loadRequestMatcher = loadRequestPattern.matcher(trimmedInput);
        Matcher endRequestMatcher = endRequestPattern.matcher(trimmedInput);
        Matcher pathRequestMatcher = pathRequestPattern.matcher(trimmedInput);
        if (customerRequestMatcher.matches()) {
            try {
                int sourceI = Integer.parseInt(customerRequestMatcher.group(1));
                int sourceJ = Integer.parseInt(customerRequestMatcher.group(2));
                int targetI = Integer.parseInt(customerRequestMatcher.group(3));
                int targetJ = Integer.parseInt(customerRequestMatcher.group(4));
                CustomerRequest customerRequest = new CustomerRequest(sourceI, sourceJ, targetI, targetJ);
                customerRequest.setOriginalString(input);
                return customerRequest;
            } catch (IllegalArgumentException e) {
                throw new InputException(input);
            }
        }
        if (loadRequestMatcher.matches()) {
            LoadRequest loadRequest = new LoadRequest(loadRequestMatcher.group(1));
            loadRequest.setOriginalString(input);
            return loadRequest;
        }
        if (endRequestMatcher.matches()) {
            EndRequest endRequest = new EndRequest();
            endRequest.setOriginalString(input);
            return endRequest;
        }
        if (pathRequestMatcher.matches()) {
            try {
                String type = pathRequestMatcher.group(1);
                int sourceI = Integer.parseInt(pathRequestMatcher.group(2));
                int sourceJ = Integer.parseInt(pathRequestMatcher.group(3));
                int targetI = Integer.parseInt(pathRequestMatcher.group(4));
                int targetJ = Integer.parseInt(pathRequestMatcher.group(5));
                PathRequest pathRequest = new PathRequest(type, sourceI, sourceJ, targetI, targetJ);
                pathRequest.setOriginalString(input);
                return pathRequest;
            } catch (IllegalArgumentException e) {
                throw new InputException(input);
            }
        }

        throw new InputException(input);
    }

}
