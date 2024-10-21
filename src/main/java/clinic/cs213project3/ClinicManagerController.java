package clinic.cs213project3;
import clinic.cs213project3.model.clinic.*;
import clinic.cs213project3.model.util.*;
import clinic.cs213project3.model.enums.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * The controller class for managing what is displayed on the JavaFX UI.
 * @author Aarush Desai
 */
public class ClinicManagerController {

    private static final int NPI_INDEX = 2;

    //  Backend instance variables...
    private List<Appointment> appointmentList;
    private List<Patient> medicalRecord;
    private List<Timeslot> timeslots;
    private List<Provider> providersList;
    private List<Doctor> doctorsList;
    private CircularLinkedList technicians;

    // Drop-Downs for timeslots...
    @FXML
    private ChoiceBox<String> timeslotBoxSchedule;
    @FXML
    private ChoiceBox<String> timeslotBoxCancel;
    @FXML
    private ChoiceBox<String> oldTimeslotBox;
    @FXML
    private ChoiceBox<String> newTimeslotBox;
    @FXML
    private ChoiceBox<String> doctorsBox;
    @FXML
    private ChoiceBox<String> techniciansBox;
    // Date pickers...
    @FXML
    private DatePicker dateBoxSchedule;
    @FXML
    private DatePicker dateBoxCancel;
    @FXML
    private DatePicker dateBoxReschedule;
    @FXML
    private DatePicker dateOfBirthBoxSchedule;
    @FXML
    private DatePicker dateOfBirthBoxCancel;
    @FXML
    private DatePicker dateOfBirthBoxReschedule;
    // Text fields...
    @FXML
    private TextField lastNameSchedule;
    @FXML
    private TextField firstNameSchedule;
    @FXML
    private TextField lastNameCancel;
    @FXML
    private TextField firstNameCancel;
    @FXML
    private TextField lastNameReschedule;
    @FXML
    private TextField firstNameReschedule;
    // Radio Buttons...
    @FXML
    private RadioButton doctorOfficeRadio;
    @FXML
    private RadioButton imagingRoomRadio;

    @FXML
    public void initialize() {
        appointmentList = new List<Appointment>();
        medicalRecord = new List<Patient>();
        timeslots = new List<Timeslot>();
        timeslots.add(new Timeslot(9,0));
        timeslots.add(new Timeslot(9,30));
        timeslots.add(new Timeslot(10,0));
        timeslots.add(new Timeslot(10,30));
        timeslots.add(new Timeslot(11,0));
        timeslots.add(new Timeslot(11,30));
        timeslots.add(new Timeslot(14,0));
        timeslots.add(new Timeslot(14,30));
        timeslots.add(new Timeslot(15,0));
        timeslots.add(new Timeslot(15,30));
        timeslots.add(new Timeslot(16,0));
        timeslots.add(new Timeslot(16,30));
        providersList = new List<Provider>();
        doctorsList = new List<Doctor>();
        technicians = new CircularLinkedList();
        File file = new File("providers.txt");
        readInProviders(file);
        // Fill timeslot boxes...
        String[] timeSlotStrings = new String[timeslots.size()];
        for (int i = 0 ; i < timeSlotStrings.length; i++) {
            timeSlotStrings[i] = timeslots.get(i).toString();
        }
        timeslotBoxSchedule.getItems().addAll(timeSlotStrings);
        timeslotBoxCancel.getItems().addAll(timeSlotStrings);
        oldTimeslotBox.getItems().addAll(timeSlotStrings);
        newTimeslotBox.getItems().addAll(timeSlotStrings);
        // Fill in doctor box...
        String[] doctorStrings = new String[doctorsList.size()];
        for (int i = 0 ; i < doctorStrings.length; i++) {
            doctorStrings[i] = doctorsList.get(i).toString();
        }
        doctorsBox.getItems().addAll(doctorStrings);
        // Fill in imaging box...
        Radiology[] rooms = Radiology.values();
        String[] roomStrings = new String[rooms.length];
        for (int i = 0; i < roomStrings.length; i++) {
            roomStrings[i] = rooms[i].toString();
        }
        techniciansBox.getItems().addAll(roomStrings);
        // Set up everything else...
        Sort.sort(providersList);
        technicians.createTracker();
    }

    @FXML
    private void scheduleAppointment() {
        // Command...
        String command = "D";
        if (imagingRoomRadio.isSelected()) { command = "T";}
        // Date...
        LocalDate selectedDate = dateBoxSchedule.getValue();
        String dateStr = null;
        if (selectedDate != null) {
            int year = selectedDate.getYear();
            int month = selectedDate.getMonthValue();
            int day = selectedDate.getDayOfMonth();
            dateStr = month + "/" + day + "/" + year;
        }
        else { return;}
        // Timeslot Number...
        int slotNumber = timeslotBoxSchedule.getSelectionModel().getSelectedIndex() + 1;
        // First Name...
        String firstName = firstNameSchedule.getText();
        if (firstName == null) { return;}
        // Last Name...
        String lastName = lastNameSchedule.getText();
        if (lastName == null) { return;}
        // Date of Birth...
        LocalDate selectedDateOfBirth = dateOfBirthBoxSchedule.getValue();
        String dateOfBirthStr = null;
        if (selectedDateOfBirth != null) {
            int year = selectedDateOfBirth.getYear();
            int month = selectedDateOfBirth.getMonthValue();
            int day = selectedDateOfBirth.getDayOfMonth();
            dateOfBirthStr = month + "/" + day + "/" + year;
        }
        else { return;}
        // Execute proper command...
        if (command.equals("D")) {
            // Get doctor's NPI number...
            String doctorString = doctorsBox.getValue();
            if (doctorString == null) { return;}
            String npiString = doctorString.replace("]","");
            npiString = npiString.substring(npiString.length() - NPI_INDEX);
            String fullCommand = command + "," + dateStr + "," + slotNumber + "," +
                                firstName + "," + lastName + "," + dateOfBirthStr + "," + npiString;
            dCommand(fullCommand);
        }
        else {
            // Get the imaging room...
            String roomString = techniciansBox.getValue();
            if (roomString == null) { return;}
            roomString = roomString.toLowerCase();
            String fullCommand = command + "," + dateStr + "," + slotNumber + "," +
                    firstName + "," + lastName + "," + dateOfBirthStr + "," + roomString;
            tCommand(fullCommand);
        }
    }

    @FXML
    private void dCommand(String command) { System.out.println(command);}
    @FXML
    private void tCommand(String command) { System.out.println(command);}

    @FXML
    private void cancelAppointment() {}
    @FXML
    private void rescheduleAppointment() {}

    ///////////////////////////////////////////////////////////////////////////////////////
    //                                  CLINIC MANAGER                                   //
    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     * Reads from a file in a list of providers and appends them to the clinic list.
     * The list of providers includes both doctors and technicians.
     * Any technicians are also appended to the front of the circular linked list.
     * @param file The file object to read from.
     */
    private void readInProviders(File file) {
        try {
            Scanner providerScanner = new Scanner(file);
            while (providerScanner.hasNextLine()) {
                String line = providerScanner.nextLine();
                StringTokenizer providerTokens = new StringTokenizer(line, " ");
                String providerType = providerTokens.nextToken();
                if (providerType.equals("T")) {
                    addTechnicians(providerTokens);
                }
                else if (providerType.equals("D")) {
                    addDoctors(providerTokens);
                }
                else {
                    System.out.println("Not a provider.");
                }
            }
            System.out.println();
            providerScanner.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    /**
     * Helper method appends a doctor to the list of providers.
     * @param tokens The tokenizer used to append doctor.
     */
    private void addDoctors(StringTokenizer tokens) {
        String firstName = tokens.nextToken();
        String lastName = tokens.nextToken();
        String dateStr = tokens.nextToken();
        String locationStr = tokens.nextToken();
        String specialtyStr = tokens.nextToken();
        String npi = tokens.nextToken();
        // Parse the parameters...
        Date date = parseDate(dateStr);
        Profile profile = new Profile(firstName, lastName, date);
        Location location = Location.valueOf(locationStr.toUpperCase());
        Specialty specialty = Specialty.valueOf(specialtyStr);
        Provider doctor = new Doctor(profile, location, specialty, npi);
        doctorsList.add((Doctor) doctor);
        providersList.add(doctor);
    }

    /**
     * Helper method appends a technician to the list of providers.
     * Method also appends technician to the circular linked list.
     * @param tokens The tokenizer used to append technician.
     */
    private void addTechnicians(StringTokenizer tokens) {
        String firstName = tokens.nextToken();
        String lastName = tokens.nextToken();
        String dateStr = tokens.nextToken();
        String locationStr = tokens.nextToken();
        String ratePerVisitStr = tokens.nextToken();
        // Parse the parameters...
        Date date = parseDate(dateStr);
        Profile profile = new Profile(firstName, lastName, date);
        Location location = Location.valueOf(locationStr.toUpperCase());
        int ratePerVisit = Integer.parseInt(ratePerVisitStr);
        Technician technician = new Technician(profile, location, ratePerVisit);
        providersList.add(technician);
        technicians.addToFront(technician);
    }

    /**
     * Helper method parses a date string and returns a Date object.
     * @param dateStr The date in "MM/DD/YYYY" format.
     * @return The Date object or null if string is invalid.
     */
    private Date parseDate(String dateStr) {
        try {
            // Date has three integer parts...
            String[] parts = dateStr.split("/");
            int month = Integer.parseInt(parts[0]) - 1; // Month is 0-indexed...
            int day = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            return new Date(year, month, day);
        }
        catch (Exception e) { // Invalid date string...
            return null;
        }
    }

}