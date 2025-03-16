import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Handles user interface

public class App {
    final static Pattern USERNAME_PATTERN = Pattern.compile("Username: ");
    final static Pattern ID_PATTERN = Pattern.compile("Log Id: ");
    final static Pattern DATE_PATTERN = Pattern.compile("Date: ");

    JFrame window;
    JPanel panel;
    JFileChooser fileChooser;
    AddWorkoutPopup addWorkoutPopup;

    //buttons
    JButton addWorkoutButton;
    JButton removeWorkoutButton;
    JButton findByUsernameButton;
    JButton showLogsByDateButton;
    JButton helpButton;

    //Logs info
    File logsDirectory;
    int numLogs = 0;

    public App(){
        this.CreateWindow();
        window.setVisible(true);
    }

    void CreateWindow(){
        //Creates and configures the swing objects
        window = new JFrame();
        window.setSize(900, 500);
        window.setTitle("Workout Log Manager");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setDialogTitle("Pick the Folder to store Workout Log files");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        boolean directoryFound = this.GetLogsDirectory();

        if(!directoryFound){ //Close the program if the user didn't select a directory
            window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
            return;
        }

        logsDirectory = fileChooser.getSelectedFile();

        panel = new JPanel();
        window.add(panel);

        addWorkoutPopup = new AddWorkoutPopup(logsDirectory, this);

        this.CreateHelpButton();
        this.CreateAddButton();
        this.CreateRemoveButton();
        this.CreateFindByUsernameButton();
        this.CreateShowByDateButton();
    }

    boolean GetLogsDirectory(){
        //Retrieves the directory for Log files
        return fileChooser.showOpenDialog(window) == JFileChooser.APPROVE_OPTION;
    }

    void CreateHelpButton(){
        helpButton = new JButton("HELP");
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(window,
                        "INSTRUCTIONS\n" +
                                "Press the Add Workout button to create a new workout log file.\n" +
                                "Press the Remove Workout button to remove a workout log based on its id.\n" +
                                "Press the Find Log by Username button to find the first log with the associated username.\n" +
                                "Press the Show Logs Sorted by Date button to display the every log's data sorted by date."
                );
            }
        });
        panel.add(helpButton);
    }

    void CreateAddButton(){
        //creates and configures the Add Workout button
        addWorkoutButton = new JButton("Add Workout");
        addWorkoutButton.addActionListener(new ActionListener() { //On click
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(window, addWorkoutPopup, "Add workout", JOptionPane.OK_CANCEL_OPTION);

                if(result == JOptionPane.CANCEL_OPTION){ return; }
                addWorkoutPopup.SaveLogs();
                addWorkoutPopup.Reset();
            }
        });

        panel.add(addWorkoutButton);
    }

    void CreateRemoveButton(){
        //creates and configures the Remove Workout button
        removeWorkoutButton = new JButton("Remove Workout");
        App app = this;
        removeWorkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idToRemove = JOptionPane.showInputDialog(window, "Id of Log to remove: ", "0000");
                File logFile = new File(app.logsDirectory, String.format("Log%s.txt", idToRemove));

                if(logFile.exists()){
                    logFile.delete();
                }
            }
        });

        panel.add(removeWorkoutButton);
    }

    void CreateFindByUsernameButton(){
        findByUsernameButton = new JButton("Find Log by Username");
        App app = this;
        findByUsernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = JOptionPane.showInputDialog(window, "Enter the username: ");
                if(username.isEmpty()){ return; } //Cancel
                File[] logFiles = app.logsDirectory.listFiles();

                for(File logFile : logFiles){ //Loop through all log files
                    try(BufferedReader reader = new BufferedReader(new FileReader(logFile))){
                        //track lines
                        String line = reader.readLine();
                        StringBuilder fileData = new StringBuilder();
                        String fileUsername = ""; //the username of the file
                        String fileId = ""; //the id of the file

                        while (line != null){
                            fileData.append(String.format("%s\n", line));

                            Matcher USERNAME_MATCHER = USERNAME_PATTERN.matcher(line);
                            Matcher ID_MATCHER = ID_PATTERN.matcher(line);

                            if(USERNAME_MATCHER.find()){ //This line describes the username
                                fileUsername = line.substring(10);
                            } else if(ID_MATCHER.find()) {  //This line describes the file id
                                fileId = line.substring(8);
                            }

                            line = reader.readLine();
                        }

                        if(fileUsername.equals(username) && //Match found
                        !fileId.isEmpty()){ //error catching
                            JOptionPane.showMessageDialog(window, fileData);
                            //Desktop.getDesktop().edit(logFile); //Open in notepad
                            break; //Stop looking for more files
                        }
                    } catch (IOException exception){
                        exception.printStackTrace();
                    }
                }
            }
        });

        panel.add(findByUsernameButton);
    }

    void CreateShowByDateButton(){
        showLogsByDateButton = new JButton("Show Logs Sorted by Date");
        App app = this;
        showLogsByDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File[] logFiles = app.logsDirectory.listFiles();
                assert logFiles != null;
                DateSortedFile[] sortedFiles = new DateSortedFile[logFiles.length];
                int i = 0;

                for(File logFile : logFiles){ //Loop through all log files
                    try(BufferedReader reader = new BufferedReader(new FileReader(logFile))){
                        //track lines
                        String line = reader.readLine();
                        StringBuilder fileData = new StringBuilder();
                        String fileDate = ""; //the date of the file
                        String fileId = ""; //the id of the file

                        while (line != null){
                            fileData.append(String.format("%s\n", line));

                            Matcher DATE_MATCHER = DATE_PATTERN.matcher(line);
                            Matcher ID_MATCHER = ID_PATTERN.matcher(line);

                            if(DATE_MATCHER.find()){ //This line describes the username
                                fileDate = line.substring(6);
                            } else if(ID_MATCHER.find()) {  //This line describes the file id
                                fileId = line.substring(8);
                            }

                            line = reader.readLine();
                        }

                        sortedFiles[i] = new DateSortedFile(fileDate, fileId, fileData.toString());
                        i++;
                    } catch (IOException exception){
                        exception.printStackTrace();
                    }
                }

                Arrays.sort(sortedFiles, new Comparator<DateSortedFile>() {
                    @Override
                    public int compare(DateSortedFile o1, DateSortedFile o2) {
                        return Integer.compare(o2.dateValue, o1.dateValue);
                    }
                });

                StringBuilder combinedLogs = new StringBuilder();

                for(DateSortedFile file : sortedFiles){
                    combinedLogs.append(String.format("%s%s\n",
                            file.fileData,
                            new String(new char[10]).replace("\0", "-")));
                }

                JOptionPane.showMessageDialog(window, combinedLogs);
            }
        });

        panel.add(showLogsByDateButton);
    }
}
