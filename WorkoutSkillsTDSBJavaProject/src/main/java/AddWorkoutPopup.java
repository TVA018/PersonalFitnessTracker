//Handles the user interface for the Add Workout popup

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class AddWorkoutPopup extends JPanel {
    App application;
    File directory;

    GridBagConstraints labelConstraints;
    GridBagConstraints inputConstraints;

    JLabel userNameLabel; //label for name of the user
    JTextField userNameInput; //input text field for the name of the user

    JLabel weightLabel;
    JTextField weightInput;
    static double KILOGRAMS_PER_POUNDS = 0.4535924;

    JLabel workoutTypeLabel;
    JButton setWorkoutTypeButton;
    JComboBox<String> workoutTypeDropdown;

    JLabel durationLabel;
    JTextField durationInput;
    static double HOURS_PER_MINUTE = (double) 1/60;

    JLabel intensityLabel;
    JComboBox<String> intensityDropdown;

    JLabel dateLabel;
    JTextField dateInput;

    public AddWorkoutPopup(File directory, App application) {
        this.application = application;
        this.directory = directory;

        //set up formatting
        this.setLayout(new GridBagLayout());
        labelConstraints = new GridBagConstraints();
        inputConstraints = new GridBagConstraints();

        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        labelConstraints.weightx = 0.5;
        labelConstraints.gridx = 0;

        inputConstraints.fill = GridBagConstraints.HORIZONTAL;
        inputConstraints.weightx = 2;
        inputConstraints.gridx = 1;

        //create swing objects
        userNameLabel = new JLabel("User Name: ");
        labelConstraints.gridy = 0;
        this.add(userNameLabel, labelConstraints);

        userNameInput = new JTextField();
        inputConstraints.gridy = 0;
        this.add(userNameInput, inputConstraints);

        weightLabel = new JLabel("Weight (lbs): ");
        labelConstraints.gridy = 1;
        this.add(weightLabel, labelConstraints);

        weightInput = new JTextField();
        inputConstraints.gridy = 1;
        this.add(weightInput, inputConstraints);

        this.CreateWorkoutTypeList();

        durationLabel = new JLabel("Duration (minutes): ");
        labelConstraints.gridy = 3;
        this.add(durationLabel, labelConstraints);

        durationInput = new JTextField();
        inputConstraints.gridy = 3;
        this.add(durationInput, inputConstraints);

        this.CreateWorkoutIntensityList();

        dateLabel = new JLabel("Date (dd-mm-yyyy): ");
        labelConstraints.gridy = 5;
        this.add(dateLabel, labelConstraints);

        dateInput = new JTextField();
        inputConstraints.gridy = 5;
        this.add(dateInput, inputConstraints);
    }

    void Reset(){
        //Resets certain input fields
        workoutTypeDropdown.setSelectedIndex(0);
        durationInput.setText("");
        intensityDropdown.setSelectedIndex(0);
    }

    void SaveLogs(){
        //Check for invalid cases, won't need to check workout type and intensity because they are guaranteed to be valid
        if(
                !isDateValid() || //invalid date
                !isDouble(this.weightInput.getText()) || //weight isn't a valid number
                !isDouble(this.durationInput.getText()) || //duration isn't a valid number
                this.userNameInput.getText().isEmpty() //no username provided
        ){ return; }

        //Write to file
        String logId = String.format("%04d", this.application.numLogs);
        File logFile = new File(this.directory, String.format("Log%s.txt", logId));

        double MET = WorkoutLog.GetMET(this.workoutTypeDropdown.getSelectedItem(), this.intensityDropdown.getSelectedItem());
        double weightKg = Double.parseDouble(weightInput.getText()) * KILOGRAMS_PER_POUNDS;
        double durationHours = Double.parseDouble(this.durationInput.getText()) * HOURS_PER_MINUTE;
        int caloriesBurned = (int) Math.round(MET * weightKg * durationHours);

        try (FileWriter writer = new FileWriter(logFile)) {
            writer.write(String.format("Date: %s\n", this.dateInput.getText()));
            writer.write(String.format("Log Id: %s\n", logId));
            writer.write(String.format("Username: %s\n", this.userNameInput.getText()));
            writer.write(String.format("Weight (lbs): %s\n", this.weightInput.getText()));
            writer.write(String.format("Workout Type: %s\n", this.workoutTypeDropdown.getSelectedItem()));
            writer.write(String.format("Duration: %s minutes\n", this.durationInput.getText()));
            writer.write(String.format("Intensity: %s\n", this.intensityDropdown.getSelectedItem()));
            writer.write(String.format("\n%s CALORIES BURNED\n", caloriesBurned));
            this.application.numLogs++; //Increment log counters
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    boolean isInteger(String stringToCheck){
        try {
            Integer.parseInt(stringToCheck);
            return true;
        } catch (NumberFormatException exception){
            return false;
        }
    }

    boolean isDouble(String stringToCheck){
        try {
            Double.parseDouble(stringToCheck);
            return true;
        } catch (NumberFormatException exception){
            return false;
        }
    }

    boolean isDateValid(){
        String DATE_FORMAT = "dd-MM-yyyy";

        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(dateInput.getText());
            System.out.println("Date is valid");
            return true;
        } catch (ParseException e) {
            System.out.println("Date is invalid");
            return false;
        }
    }

    void CreateWorkoutTypeList(){
        workoutTypeLabel = new JLabel("Workout Type: ");
        labelConstraints.gridy = 2;
        this.add(workoutTypeLabel, labelConstraints);

        WorkoutLog.WorkoutType[] workoutTypes = WorkoutLog.WorkoutType.values();
        String[] workoutTypeStrings = new String[workoutTypes.length];

        for(int i = 0; i < workoutTypes.length; i++){
            workoutTypeStrings[i] = workoutTypes[i].getName();
        }

        workoutTypeDropdown = new JComboBox<>(workoutTypeStrings);
        workoutTypeDropdown.setSelectedIndex(0);
        inputConstraints.gridy = 2;

        this.add(workoutTypeDropdown, inputConstraints);
    }

    void CreateWorkoutIntensityList(){
        intensityLabel = new JLabel("Workout Type: ");
        labelConstraints.gridy = 4;
        this.add(intensityLabel, labelConstraints);

        WorkoutLog.WorkoutIntensity[] workoutIntensities = WorkoutLog.WorkoutIntensity.values();
        String[] workoutIntensityStrings = new String[workoutIntensities.length];

        for(int i = 0; i < workoutIntensities.length; i++){
            workoutIntensityStrings[i] = workoutIntensities[i].getName();
        }

        intensityDropdown = new JComboBox<>(workoutIntensityStrings);
        intensityDropdown.setSelectedIndex(0);
        inputConstraints.gridy = 4;

        this.add(intensityDropdown, inputConstraints);
    }
}
