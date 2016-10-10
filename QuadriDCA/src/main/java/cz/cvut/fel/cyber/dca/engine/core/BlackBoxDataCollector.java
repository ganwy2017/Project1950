package cz.cvut.fel.cyber.dca.engine.core;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jan on 08.10.2016.
 */
public class BlackBoxDataCollector{

    private static String EXPERIMENT_FILENAME = "experimentRecord";
    private static String POSITION_FILENAME = "positionRecord";
    private static String BOUNDARY_FILENAME = "boundaryRecord";
    private static String CONNECTION_FILENAME = "connectionRecord";
    private static java.nio.file.Path outputPositionFile = Paths.get("FlightRecords/" + POSITION_FILENAME + ".txt");
    private static java.nio.file.Path outputBoundaryFile = Paths.get("FlightRecords/" + BOUNDARY_FILENAME + ".txt");
    private static java.nio.file.Path outputExperimentFile = Paths.get("FlightRecords/" + EXPERIMENT_FILENAME + ".txt");
    private static java.nio.file.Path outputConnectionFile = Paths.get("FlightRecords/" + CONNECTION_FILENAME + ".txt");

    private static int iteration = 0;
    private static List<String> positionRecords = new ArrayList<>(RobotGroup.getMembers().size());
    private static List<String> boundaryRecords = new ArrayList<>(RobotGroup.getMembers().size());
    private static List<String> connectionRecords = new ArrayList<>(RobotGroup.getMembers().size());

    
    public static void logUnit(Quadracopter quadracopter){
        positionRecords.add(quadracopter.getId(), quadracopter.log());
        boundaryRecords.add(quadracopter.getId(), quadracopter.isBoundary() ? "1" : "0");
        String conns = "";
        for(int i = 0; i < RobotGroup.getMembers().size(); i++){
            if((RobotGroup.getMembers().get(i).getId()==quadracopter.getId())||(!quadracopter.getNeighbors().contains(RobotGroup.getMembers().get(i)))){
                conns += "0";
            }else conns += "1";
            if(i!=RobotGroup.getMembers().size()-1)conns += " ";
        }
        connectionRecords.add(quadracopter.getId(), conns);
    }

    public static void logAll(){
        RobotGroup.getMembers().stream().forEach(BlackBoxDataCollector::logUnit);
    }

    public static List<String> createExperimentInfoLog(){
        List<String> info = new ArrayList<>();
        info.add(Integer.toString(RobotGroup.getMembers().size()));
        info.add(Integer.toString(RobotGroup.getLeaders().size()));
        return info;
    }

    public static void writeRecord(){
        if(iteration == 0){
            try {
                Files.write(outputExperimentFile, createExperimentInfoLog(), Charset.forName("UTF-8"),StandardOpenOption.CREATE);
            } catch (IOException e) {
                System.out.println("Could not create file.");
                e.printStackTrace();
            }
            try {
                Files.write(outputPositionFile, positionRecords, Charset.forName("UTF-8"),StandardOpenOption.CREATE_NEW);
            } catch (IOException e) {
                System.out.println("Could not write record.");
                e.printStackTrace();
            }

            try {
                Files.write(outputBoundaryFile, boundaryRecords, Charset.forName("UTF-8"),StandardOpenOption.CREATE_NEW);
            } catch (IOException e) {
                System.out.println("Could not write record.");
                e.printStackTrace();
            }
            try {
                Files.write(outputConnectionFile, connectionRecords, Charset.forName("UTF-8"),StandardOpenOption.CREATE_NEW);
            } catch (IOException e) {
                System.out.println("Could not write record.");
                e.printStackTrace();
            }

        }

        try {
            Files.write(outputPositionFile, positionRecords, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Could not write record.");
            e.printStackTrace();
        }

        try {
            Files.write(outputBoundaryFile, boundaryRecords, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Could not write record.");
            e.printStackTrace();
        }

        try {
            Files.write(outputConnectionFile, connectionRecords, Charset.forName("UTF-8"),StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Could not write record.");
            e.printStackTrace();
        }

        iteration++;
    }

}
