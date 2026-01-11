package com.cricket.util;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class HawkeyeCricketSpeed 
{
//    public static final String HAWKEYE_DIR = CricketUtil.CRICKET_DIRECTORY + "Hawkeye/";
//    public static final String OUTPUT_FILE = CricketUtil.CRICKET_DIRECTORY + CricketUtil.SPEED_DIRECTORY + "AllSpeeds.txt";
    public static final String STATE_FILE  = CricketUtil.CRICKET_DIRECTORY + CricketUtil.SPEED_DIRECTORY + "last_processed.txt";

    public static void readLatestSpeedFiles(String sourceDirectory, String destinationSpeedLogFile) throws IOException {

        Ball lastProcessed = readLastProcessedBall();
        if(lastProcessed == null) {
        	lastProcessed = new Ball(0, 0);
        	writeLastProcessedBall(lastProcessed);
        }
        
        List<File> validFiles = Files.list(Paths.get(sourceDirectory))
                .map(Path::toFile)
                .filter(File::isFile)
                .filter(f -> f.getName().matches("\\d+\\.\\d+"))
                .sorted(Comparator.comparing(HawkeyeCricketSpeed::parseBall))
                .collect(Collectors.toList());
        
        for (File file : validFiles) {

            Ball current = parseBall(file);

            // Skip already processed or equal balls
            if (lastProcessed != null && current.compareTo(lastProcessed) <= 0) {
                continue;
            }

            String speed = readSpeedFromFile(file);
            if (speed == null) continue;

            writeSpeedLog(current, speed, CricketUtil.CRICKET_DIRECTORY + CricketUtil.SPEED_DIRECTORY + destinationSpeedLogFile);
            writeLastProcessedBall(current);

            lastProcessed = current;
        }
    }

    public static String readSpeedFromFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            br.readLine(); // ignore line 1

            String line2 = br.readLine(); // e.g. "1,60.3"
            if (line2 == null || !line2.contains(",")) {
                return null;
            }

            String[] parts = line2.split(",");
            if (parts.length < 2) {
                return null;
            }

            return parts[1].trim(); // returns "60.3"

        } catch (Exception e) {
            return null;
        }
    }


    public static void writeSpeedLog(Ball ball, String speed, String destinationSpeedLogFile) throws IOException 
    {
        String log = String.format("Over=%d,Ball=%d,Speed=%s%n", ball.over, ball.ball, speed);
        Files.write(
                Paths.get(destinationSpeedLogFile),
                log.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }

    public static Ball readLastProcessedBall() {
        try {
            if (!Files.exists(Paths.get(STATE_FILE))) return null;
            String value = Files.readAllLines(Paths.get(STATE_FILE)).get(0);
            return Ball.fromString(value);
        } catch (Exception e) {
            return null;
        }
    }

    public static void writeLastProcessedBall(Ball ball) throws IOException {
        Files.write(
                Paths.get(STATE_FILE),
                ball.toString().getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    public static Ball parseBall(File file) {
        return Ball.fromString(file.getName());
    }

    public static class Ball implements Comparable<Ball> 
    {
        int over;
        int ball;

        Ball(int over, int ball) {
            this.over = over;
            this.ball = ball;
        }

        static Ball fromString(String s) {
            String[] parts = s.split("\\.");
            return new Ball(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }

        @Override
        public int compareTo(Ball other) {
            if (this.over != other.over) {
                return Integer.compare(this.over, other.over);
            }
            return Integer.compare(this.ball, other.ball);
        }

        @Override
        public String toString() {
            return over + "." + ball;
        }
    }
}

