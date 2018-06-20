package utils;

import taxi.Global;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class TestEntry {
    long milliTime;
    String inputString;
    TestEntry(long milliTime, String inputString) {
        this.milliTime = milliTime;
        this.inputString = inputString;
    }
}

public class TestInputter extends Thread {
    ArrayList<TestEntry> testEntryList;
    OutputStream outputStream;
    public static TestInputter testInputter;

    TestInputter(String scriptAddr, OutputStream outputStream) throws FileNotFoundException {
        this.outputStream = outputStream;

        Scanner scanner = new Scanner(new FileInputStream(scriptAddr));
        testEntryList = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String newLine = scanner.nextLine();
            String [] splitted = newLine.split(":", -1);
            String milliTimeString = splitted[0];
            String request = splitted[1];
            testEntryList.add(new TestEntry(Long.parseLong(milliTimeString), request));
        }
        testEntryList.add(new TestEntry(0, "End"));
        scanner.close();
    }
    public static PipedInputStream getStream(String scriptAddr) throws IOException {
        PipedInputStream pipedInputStream = new PipedInputStream();
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        pipedInputStream.connect(pipedOutputStream);
        testInputter = new TestInputter(scriptAddr, pipedOutputStream);
        return pipedInputStream;
    }
    public void run() {
        for (int i = 0; i < testEntryList.size(); i++) {
            while (Global.getRelativeTime() < testEntryList.get(i).milliTime) {
                yield();
            }
            try {
                outputStream.write((testEntryList.get(i).inputString + "\n").getBytes("UTF-8"));
                outputStream.flush();
                Verbose.println("send new request: " +
                        testEntryList.get(i).inputString + " @" + (Global.getRelativeTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
