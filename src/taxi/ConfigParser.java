package taxi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigParser {
    enum Status {
        Header, Map, Flow, Taxi, Request
    }

    ;
    private Status status = Status.Header;

    private static final String headerRegex = "#.*#";
    private static final String mapBeginRegex = "#map";
    private static final String mapEndRegex = "#end_map";
    private static final String flowBeginRegex = "#flow";
    private static final String flowEndRegex = "#end_flow";
    private static final String taxiBeginRegex = "#taxi";
    private static final String taxiEndRegex = "#end_taxi";
    private static final String requestBeginRegex = "#request";
    private static final String requestEndRegex = "#end_request";

    private static final String flowRegex = "\\(\\s*\\+?\\s*([0-9]+)\\s*,\\s*\\+?\\s*([0-9]+)\\s*\\)\\s*,\\s*\\(\\s*\\+?\\s*([0-9]+)\\s*,\\s*\\+?\\s*([0-9]+)\\s*\\)\\s*,\\s*\\+?\\s*([0-9]+)\\s*";
    private static final Pattern flowRegexPattern = Pattern.compile(flowRegex);
    private static final String TaxiStatusRegex = "([0-9]+)\\s*,\\s*([0-9]+)\\s*,\\s*([0-9]+)\\s*,\\s*\\(\\s*\\+?\\s*([0-9]+)\\s*,\\s*\\+?\\s*([0-9]+)\\s*\\)";
    private static final Pattern TaxiRegexPattern = Pattern.compile(TaxiStatusRegex);

    private Scanner scanner;
    private Grid grid;
    private List<Taxi> taxiList;
    private List<Request> requestList;

    public ConfigParser(InputStream inputStream) {
        scanner = new Scanner(inputStream);
        grid = new Grid();
        taxiList = new ArrayList<>(100);
        requestList = new ArrayList<>(100);
    }


    public void parse() throws InputException {
        String newLine = scanner.nextLine().trim();
        while (true) {
            switch (status) {
                case Header:
                    newLine = matchLine(headerRegex, newLine);
                    status = Status.Map;
                    break;
                case Map:
                    newLine = matchLine(mapBeginRegex, newLine);
                    if (!peekLine(mapEndRegex, newLine)) {
                        for (int i = 0; i < Grid.GRID_ROW_NUM; i++) {
                            // TODO: fix this. allowing splitting "01230123"
                            String[] row = newLine.replaceAll("\\s", "").split("\\s*");
                            for (int j = 0; j < Grid.GRID_ROW_NUM; j++) {
                                grid.setGrid(Adjacency.values()[Integer.parseInt(row[j])], i, j);
                            }
                            newLine = scanner.nextLine().trim();
                        }
                    }
                    newLine = matchLine(mapEndRegex, newLine);
                    status = Status.Flow;
                    break;
                case Flow:
                    newLine = matchLine(flowBeginRegex, newLine);
                    while (!peekLine(flowEndRegex, newLine)) {
                        Matcher matcher = flowRegexPattern.matcher(newLine);
                        if (matcher.matches()) {
                            int sourceI = Integer.parseInt(matcher.group(1));
                            int sourceJ = Integer.parseInt(matcher.group(2));
                            int destinationI = Integer.parseInt(matcher.group(3));
                            int destinationJ = Integer.parseInt(matcher.group(4));
                            int flow = Integer.parseInt(matcher.group(5));
                            // TODO: needs modify
                            if (sourceI == destinationI - 1) {
                                grid.setFlowDown(flow, sourceI, sourceJ);
                            } else if (sourceJ == destinationJ - 1) {
                                grid.setFlowRight(flow, sourceI, sourceJ);
                            }
                        } else {
                            throw new InputException(newLine);
                        }
                        newLine = scanner.nextLine().trim();
                    }
                    newLine = matchLine(flowEndRegex, newLine);
                    status = Status.Taxi;
                    break;
                case Taxi:
                    newLine = matchLine(taxiBeginRegex, newLine);
                    while (!peekLine(taxiEndRegex, newLine)) {
                        Matcher matcher = TaxiRegexPattern.matcher(newLine);
                        if (matcher.matches()) {
                            int num = Integer.parseInt(matcher.group(1));
                            int statusIdx = Integer.parseInt(matcher.group(2));
                            int credit = Integer.parseInt(matcher.group(3));
                            int i = Integer.parseInt(matcher.group(4));
                            int j = Integer.parseInt(matcher.group(5));
                            taxiList.add(new Taxi(num, TaxiState.values()[statusIdx], credit, i, j));
                        } else {
                            throw new InputException(newLine);
                        }
                        newLine = scanner.nextLine().trim();
                    }
                    newLine = matchLine(taxiEndRegex, newLine);
                    status = Status.Request;
                    break;
                case Request:
                    newLine = matchLine(requestBeginRegex, newLine);
                    while (!peekLine(requestEndRegex, newLine)) {
                        requestList.add(RequestParser.Parse(newLine));
                        newLine = scanner.nextLine().trim();
                    }
                    newLine = matchLine(requestEndRegex, newLine);
                    return;
            }
        }
    }

    private String matchLine(String regex, String line) throws InputException {
        System.out.println("matching: " + line);
        if (!Pattern.matches(regex, line)) {
            throw new InputException(line);
        }
        if (scanner.hasNextLine()) {
            String next = scanner.nextLine().trim();
            while (next.length() == 0) {
                if (scanner.hasNextLine()) {
                    next = scanner.nextLine().trim();
                } else {
                    return null;
                }
            }
            return next;
        } else {
            return null;
        }
    }

    private Boolean peekLine(String regex, String line) {
        return Pattern.matches(regex, line);
    }

}