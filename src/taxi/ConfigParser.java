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
    private static final String TaxiStatusRegex = "([0-9]+)\\s*,\\s*([0-9]+)\\s*,\\s*\\+?\\s*([0-9]+)\\s*,\\s*\\(\\s*\\+?\\s*([0-9]+)\\s*,\\s*\\+?\\s*([0-9]+)\\s*\\)";
    private static final Pattern TaxiRegexPattern = Pattern.compile(TaxiStatusRegex);

    private Scanner scanner;
    private Grid grid;

    private List<Taxi> taxiSettingList;
    private List<Request> newRequestList;

    public ConfigParser(InputStream inputStream) {
        scanner = new Scanner(inputStream);
        grid = new Grid();
        taxiSettingList = new ArrayList<>(100);
        newRequestList = new ArrayList<>(100);
    }

    public ConfigParser(InputStream inputStream, Grid grid) {
        scanner = new Scanner(inputStream);
        this.grid = grid;
        taxiSettingList = new ArrayList<>(100);
        newRequestList = new ArrayList<>(100);
    }

    private boolean isValidRow(int i) {
        return i >= 0 && i < Grid.GRID_ROW_NUM;
    }

    private boolean isValidColumn(int j) {
        return j >= 0 && j < Grid.GRID_COL_NUM;
    }

    public void parse() throws InputException {
        String newLine;
        if (scanner.hasNextLine()) {
            newLine = scanner.nextLine().trim();
        } else {
            throw new InputException("empty config loaded. ");
        }
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
                            for (int j = 0; j < Grid.GRID_COL_NUM; j++) {
                                int adjacencyCode = Integer.parseInt(row[j]);
                                grid.setGrid(Adjacency.values()[adjacencyCode], i, j);
                                if (j == Grid.GRID_COL_NUM - 1 &&
                                        (adjacencyCode == Adjacency.RIGHT.ordinal() ||
                                                adjacencyCode == Adjacency.BOTH.ordinal()) ) {
                                    throw new InputException(newLine);
                                }
                                if (i == Grid.GRID_ROW_NUM - 1 &&
                                        (adjacencyCode == Adjacency.DOWN.ordinal() ||
                                                adjacencyCode == Adjacency.BOTH.ordinal()) ) {
                                    throw new InputException(newLine);
                                }
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
                            if (!isValidRow(sourceI) || !isValidColumn(sourceJ) || !isValidRow(destinationI) || !isValidRow(destinationJ)) {
                                throw new InputException(newLine);
                            }
                            try {
                                grid.setFlow(flow, sourceI, sourceJ, destinationI, destinationJ);
                            } catch (Exception e) {
                                throw new InputException(newLine);
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
                            if (num < 0 || num > Taxi.MAX_INDEX) {
                                throw new InputException(newLine);
                            }
                            int statusIdx = Integer.parseInt(matcher.group(2));
                            if (statusIdx != TaxiState.Idle.ordinal()) {
                                throw new InputException(newLine);
                            }
                            int credit = Integer.parseInt(matcher.group(3));
                            // System.out.println("credit: " + credit);
                            int i = Integer.parseInt(matcher.group(4));
                            if (!isValidRow(i)) {
                                throw new InputException(newLine);
                            }
                            int j = Integer.parseInt(matcher.group(5));
                            if (!isValidColumn(j)) {
                                throw new InputException(newLine);
                            }
                            taxiSettingList.add(new Taxi(null, num, TaxiState.values()[statusIdx], credit, i, j));
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
                        newRequestList.add(RequestParser.Parse(newLine));
                        newLine = scanner.nextLine().trim();
                    }
                    newLine = matchLine(requestEndRegex, newLine);
                    return;
            }
        }
    }

    private String matchLine(String regex, String line) throws InputException {
        // System.out.println("matching: " + line);
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

    public Grid getGrid() {
        return grid;
    }

    public List<Taxi> getTaxiList() {
        return taxiSettingList;
    }

}