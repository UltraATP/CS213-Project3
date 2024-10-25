package clinic.cs213project3;
import clinic.cs213project3.model.clinic.Appointment;
import clinic.cs213project3.model.clinic.Doctor;
import clinic.cs213project3.model.clinic.Imaging;
import clinic.cs213project3.model.clinic.Patient;
import clinic.cs213project3.model.clinic.Person;
import clinic.cs213project3.model.clinic.Profile;
import clinic.cs213project3.model.clinic.Provider;
import clinic.cs213project3.model.clinic.Technician;
import clinic.cs213project3.model.clinic.Timeslot;
import clinic.cs213project3.model.util.CircularLinkedList;
import clinic.cs213project3.model.util.Date;
import clinic.cs213project3.model.util.List;
import clinic.cs213project3.model.util.Sort;
import clinic.cs213project3.model.util.Visit;
import clinic.cs213project3.model.enums.Location;
import clinic.cs213project3.model.enums.Radiology;
import clinic.cs213project3.model.enums.Specialty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * The controller class for managing events in the JavaFX UI.
 * @author Aarush Desai, Rohan Vuppunuthula
 */
public class ClinicManagerController {

    private static final int NPI_INDEX = 2;
    private static final int NOT_FOUND = -1;
    private static final int NUM_C_TOKENS = 6;
    private static final int NUM_TOKENS = 7;

    private ObservableList<AppointmentView> appointmentViews = FXCollections.observableArrayList();
    private ObservableList<ProviderView> providerViews = FXCollections.observableArrayList();
    private ObservableList<FundView> fundViews = FXCollections.observableArrayList();
    private List<Appointment> appointmentList;
    private List<Patient> medicalRecord;
    private List<Timeslot> timeslots;
    private List<Provider> providersList;
    private List<Doctor> doctorsList;
    private CircularLinkedList technicians;

    @FXML
    private TextArea logger;
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
    @FXML
    private RadioButton doctorOfficeRadio;
    @FXML
    private RadioButton imagingRoomRadio;
    @FXML
    private RadioButton billingRadio;
    @FXML
    private RadioButton creditRadio;
    @FXML
    private RadioButton allAppointmentsRadio;
    @FXML
    private RadioButton officeAppointmentsRadio;
    @FXML
    private RadioButton imagingAppointmentsRadio;
    @FXML
    private RadioButton dateOrderRadio;
    @FXML
    private RadioButton patientOrderRadio;
    @FXML
    private RadioButton locationOrderRadio;
    @FXML
    private TableView<ProviderView> providerTableView;
    @FXML
    private TableColumn<ProviderView, String> nameColumn;
    @FXML
    private TableColumn<ProviderView, String> typeColumn;
    @FXML
    private TableColumn<ProviderView, String> locationColumn;
    @FXML
    private TableColumn<ProviderView, String> specialtyColumn;
    @FXML
    private TableColumn<ProviderView, String> rateColumn;
    @FXML
    private TableView<AppointmentView> appointmentTableView;
    @FXML
    private TableColumn<AppointmentView, String> dateColumn;
    @FXML
    private TableColumn<AppointmentView, String> timeslotColumn;
    @FXML
    private TableColumn<AppointmentView, String> patientColumn;
    @FXML
    private TableColumn<AppointmentView, String> appointmentLocationColumn;
    @FXML
    private TableColumn<AppointmentView, String> roomColumn;
    @FXML
    private TableColumn<AppointmentView, String> costColumn;
    @FXML
    private TableView<FundView> fundTableView;
    @FXML
    private TableColumn<FundView, String> personColumn;
    @FXML
    private TableColumn<FundView, String> fundColumn;

    /**
     * Method schedules a new appointment in the clinic manager.
     * Event is triggered upon pressing "Schedule Appointment" button.
     */
    @FXML
    private void scheduleAppointment() {
        String command;
        if (doctorOfficeRadio.isSelected()) {
            command = "D";
        }
        else if (imagingRoomRadio.isSelected()) {
            command = "T";
        }
        else {
            printLog("Please choose the type of appointment.");
            return;
        }
        String dateStr = checkForDateInput(dateBoxSchedule);
        if (dateStr == null) {
            printLog("Please enter a date for your appointment.");
            return;
        }
        int slotNumber = timeslotBoxSchedule.getSelectionModel().getSelectedIndex() + 1;
        String firstName = firstNameSchedule.getText();
        if (firstName == null || firstName.isEmpty()) {
            printLog("Please enter your first name.");
            return;
        }
        String lastName = lastNameSchedule.getText();
        if (lastName == null || lastName.isEmpty()) {
            printLog("Please enter your last name.");
            return;
        }
        String dateOfBirthStr = checkForDateInput(dateOfBirthBoxSchedule);
        if (dateOfBirthStr == null) {
            printLog("Please enter your date of birth.");
            return;
        }
        String fullCommand = command + "," + dateStr + "," + slotNumber + "," +
                firstName + "," + lastName + "," + dateOfBirthStr + ",";
        fullCommand = gatherProviderInput(command, fullCommand);
        if (fullCommand == null) {
            return;
        }
        runStringCommand(fullCommand);
    }

    /**
     * Method cancels an existing appointment in the clinic manager.
     * Event is triggered upon pressing "Cancel Appointment" button.
     */
    @FXML
    private void cancelAppointment() {
        String command = "C";
        String dateStr = checkForDateInput(dateBoxCancel);
        if (dateStr == null) {
            printLog("Please enter the date of your appointment.");
            return;
        }
        int slotNumber = timeslotBoxCancel.getSelectionModel().getSelectedIndex() + 1;
        String firstName = firstNameCancel.getText();
        if (firstName == null || firstName.isEmpty()) {
            printLog("Please enter your first name.");
            return;
        }
        String lastName = lastNameCancel.getText();
        if (lastName == null || lastName.isEmpty()) {
            printLog("Please enter your last name.");
            return;
        }
        String dateOfBirthStr = checkForDateInput(dateOfBirthBoxCancel);
        if (dateOfBirthStr == null) {
            printLog("Please enter your date of birth.");
            return;
        }
        String fullCommand = command + "," + dateStr + "," + slotNumber + "," +
                firstName + "," + lastName + "," + dateOfBirthStr;
        runStringCommand(fullCommand);
    }

    /**
     * Method reschedules an existing appointment in the clinic manager.
     * Event is triggered upon pressing "Reschedule Appointment" button.
     */
    @FXML
    private void rescheduleAppointment() {
        String command = "R";
        String dateStr = checkForDateInput(dateBoxReschedule);
        if (dateStr == null) {
            printLog("Please enter the date of your appointment.");
            return;
        }
        int oldSlotNumber = oldTimeslotBox.getSelectionModel().getSelectedIndex() + 1;
        String firstName = firstNameReschedule.getText();
        if (firstName == null || firstName.isEmpty()) {
            printLog("Please enter your first name.");
            return;
        }
        String lastName = lastNameReschedule.getText();
        if (lastName == null || lastName.isEmpty()) {
            printLog("Please enter your last name.");
            return;
        }
        String dateOfBirthStr = checkForDateInput(dateOfBirthBoxReschedule);
        if (dateOfBirthStr == null) {
            printLog("Please enter your date of birth.");
            return;
        }
        int newSlotNumber = newTimeslotBox.getSelectionModel().getSelectedIndex() + 1;
        String fullCommand = command + "," + dateStr + "," + oldSlotNumber + "," +
                firstName + "," + lastName + "," + dateOfBirthStr + "," + newSlotNumber;
        runStringCommand(fullCommand);
    }

    /**
     * Method displays dispersed funds for patients and providers.
     * User can choose to display patient bills or provider credits.
     */
    @FXML
    private void displayFunds() {
        String command;
        if (billingRadio.isSelected()) {
            command = "PS";
            runStringCommand(command);
        }
        else if (creditRadio.isSelected()) {
            command = "PC";
            runStringCommand(command);
        }
        else {
            printLog("Please choose funds to disperse.");
        }
    }

    /**
     * Method displays existing appointments in a specified sorting order.
     * User can choose to sort all appointments by date, patient, or location.
     * User can choose to display only office or imaging appointments sorted by location.
     */
    @FXML
    private void displayAppointments() {
        String command;
        if (officeAppointmentsRadio.isSelected()) {
            command = "PO";
            runStringCommand(command);
        }
        else if (imagingAppointmentsRadio.isSelected()) {
            command = "PI";
            runStringCommand(command);
        }
        else if (allAppointmentsRadio.isSelected()) {
            if (dateOrderRadio.isSelected()) {
                command = "PA";
                runStringCommand(command);
            }
            else if (patientOrderRadio.isSelected()) {
                command = "PP";
                runStringCommand(command);
            }
            else if (locationOrderRadio.isSelected()) {
                command = "PL";
                runStringCommand(command);
            }
            else {
                printLog("Please choose a sorting order.");
            }
        }
        else {
            printLog("Please choose appointments to display.");
        }
    }

    /**
     * Helper method initializes all the backend instance variables.
     */
    @FXML
    private void createBackendVariables() {
        // Make error logger immutable...
        logger.setDisable(true);
        printLog(">>>>> WELCOME TO THE CLINIC MANAGER! <<<<<");
        // Initialize instance variables...
        appointmentList = new List<>();
        medicalRecord = new List<>();
        timeslots = new List<>();
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
        providersList = new List<>();
        doctorsList = new List<>();
        technicians = new CircularLinkedList();
        File file = new File("providers.txt");
        readInProviders(file);
        // Set up everything else...
        Sort.sort(providersList);
        technicians.createTracker();
    }

    /**
     * Helper method fills data into all choice boxes in the UI.
     */
    @FXML
    private void fillChoiceBoxes() {
        // Fill timeslot box...
        String[] timeSlotStrings = new String[timeslots.size()];
        for (int i = 0 ; i < timeSlotStrings.length; i++) {
            timeSlotStrings[i] = timeslots.get(i).toString();
        }
        timeslotBoxSchedule.getItems().addAll(timeSlotStrings);
        timeslotBoxCancel.getItems().addAll(timeSlotStrings);
        oldTimeslotBox.getItems().addAll(timeSlotStrings);
        newTimeslotBox.getItems().addAll(timeSlotStrings);
        // Default value set...
        String firstSlot = timeSlotStrings[0];
        timeslotBoxSchedule.setValue(firstSlot);
        timeslotBoxCancel.setValue(firstSlot);
        oldTimeslotBox.setValue(firstSlot);
        newTimeslotBox.setValue(firstSlot);
        // Fill in doctor box...
        String[] doctorStrings = new String[doctorsList.size()];
        for (int i = 0; i < doctorStrings.length; i++) {
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
    }

    /**
     * Helper method creates a listener for the radio buttons in the "Schedule" tab.
     * Either only an office or imaging appointment can be chosen, not both.
     */
    @FXML
    private void createProviderButtonListener() {
        doctorsBox.setDisable(true);
        techniciansBox.setDisable(true);
        ToggleGroup scheduleToggleGroup = doctorOfficeRadio.getToggleGroup();
        scheduleToggleGroup.selectedToggleProperty().addListener((_, _, _) -> {
            if (doctorOfficeRadio.isSelected()) {
                doctorsBox.setDisable(false);
                techniciansBox.setValue(null); // Empty the box...
                techniciansBox.setDisable(true);
            }
            else {
                doctorsBox.setValue(null); // Empty the box...
                doctorsBox.setDisable(true);
                techniciansBox.setDisable(false);
            }
        });
    }

    /**
     * Helper method creates a listener for the radio buttons in the "Appointments" tab.
     * If all appointments are chosen, you can choose to sort them by date, patient, or location.
     * If only office or imaging appointments are chosen, you cannot choose the sorting order.
     */
    @FXML
    private void createSortingListener() {
        ToggleGroup sortingToggleGroup = allAppointmentsRadio.getToggleGroup();
        sortingToggleGroup.selectedToggleProperty().addListener((_, _, _) -> {
            if (officeAppointmentsRadio.isSelected() || imagingAppointmentsRadio.isSelected()) {
                // Deselect them all...
                dateOrderRadio.setSelected(false);
                patientOrderRadio.setSelected(false);
                locationOrderRadio.setSelected(false);
                // Disable them all...
                dateOrderRadio.setDisable(true);
                patientOrderRadio.setDisable(true);
                locationOrderRadio.setDisable(true);
            }
            else {
                dateOrderRadio.setDisable(false);
                patientOrderRadio.setDisable(false);
                locationOrderRadio.setDisable(false);
            }
        });
    }

    /**
     * Method clears the event logger/text area in the UI.
     */
    @FXML
    private void clearEventLog() {
        logger.clear();
    }

    /**
     * Helper method extracts date from a given date picker.
     * Date is formatted with the month, day, and year separated by "/" character.
     * @param dateBox The date picker.
     * @return The date as string, or null if the date picker is empty.
     */
    @FXML
    private String checkForDateInput(DatePicker dateBox) {
        LocalDate selectedDate = dateBox.getValue();
        String dateStr = null;
        if (selectedDate != null) {
            int year = selectedDate.getYear();
            int month = selectedDate.getMonthValue();
            int day = selectedDate.getDayOfMonth();
            dateStr = month + "/" + day + "/" + year;
        }
        return dateStr;
    }

    /**
     * Helper method collects input from the doctor/imaging radio buttons.
     * Method is used to construct the command for scheduling an appointment.
     * @param commandLetter The letter of the command.
     * @param fullCommand The command string to complete.
     * @return The full command with the appropriate token added at the end, else null of radio buttons are empty.
     */
    @FXML
    private String gatherProviderInput(String commandLetter, String fullCommand) {
        if (commandLetter.equals("D")) {
            String doctorString = doctorsBox.getValue();
            if (doctorString == null) {
                printLog("Please choose a doctor's office.");
                return null;
            }
            String npiString = doctorString.replace("]","");
            npiString = npiString.substring(npiString.length() - NPI_INDEX);
            fullCommand = fullCommand + npiString;
            return fullCommand;
        }
        else {
            String roomString = techniciansBox.getValue();
            if (roomString == null) {
                printLog("Please choose an imaging room.");
                return null;
            }
            roomString = roomString.toLowerCase();
            fullCommand = fullCommand + roomString;
            return fullCommand;
        }
    }

    /**
     * Method displays the providers to the UI table view under the "Providers" tab.
     * The table shows the providers' name, type, location, specialty, and rate.
     */
    @FXML
    private void displayTableOfProviders() {
        providerViews.clear(); // Clear existing entries...
        for (Provider provider: providersList) {
            if (provider instanceof Doctor) {
                Doctor doctor = (Doctor) provider;
                String name = doctor.getProfile().getFirstName() + " " + doctor.getProfile().getLastName();
                String type = "DOCTOR";
                String location = doctor.getLocation().toString();
                String specialty = doctor.getSpecialty().toString();
                String rate = "$" + doctor.rate();
                providerViews.add(new ProviderView(name, type, location, specialty, rate));
            }
            else {
                Technician technician = (Technician) provider;
                String name = technician.getProfile().getFirstName() + " " + technician.getProfile().getLastName();
                String type = "TECHNICIAN";
                String location = technician.getLocation().toString();
                String specialty = "N/A";
                String rate = "$" + technician.rate();
                providerViews.add(new ProviderView(name, type, location, specialty, rate));
            }
        }
    }

    /**
     * Helper method parses a date string and returns a Date object.
     * @param dateStr The date in "MM/DD/YYYY" format.
     * @return The Date object or null if string is invalid.
     */
    @FXML
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

    /**
     * Parses a timeslot string and returns a Timeslot object.
     * @param timeslotStr The timeslot number as a string.
     * @return The Timeslot object or null if string is invalid.
     */
    @FXML
    private Timeslot parseTimeslot(String timeslotStr) {
        try {
            int slotNumber = Integer.parseInt(timeslotStr);
            return timeslots.get(slotNumber - 1);
        }
        catch (Exception e) { // Invalid timeslots...
            return null;
        }
    }

    /**
     * Parses a room string and returns a Radiology object.
     * @param roomStr The imaging room as a string.
     * @return The Radiology object or null if string is invalid.
     */
    @FXML
    private Radiology parseRadiology(String roomStr) {
        try {
            String roomName = roomStr.toUpperCase();
            return Radiology.valueOf(roomName);
        }
        catch (Exception e) { // Invalid rooms...
            return null;
        }
    }

    /**
     * Finds the doctor associated with a given npi identification.
     * @param npi The doctor's npi.
     * @return The Doctor object with the npi or null if npi does not exist.
     */
    @FXML
    private Doctor parseDoctor(String npi) {
        for (Provider provider: providersList) {
            if (provider instanceof Doctor) {
                Doctor doctor = (Doctor) provider;
                if (doctor.getNpi().equals(npi)) {
                    return doctor;
                }
            }
        }
        return null;
    }

    /**
     * Finds the provider associated with an appointment in the list.
     * First checks if appointment exists, then if provider is a doctor, and then returns the provider.
     * @param apt The appointment we are searching for.
     * @return The appointment's provider as a Person object, else null if appointment does not exist.
     */
    @FXML
    private Person findProvider(Appointment apt) {
        int index = appointmentList.indexOf(apt);
        if (index != NOT_FOUND) {
            Provider provider = (Provider) appointmentList.get(index).getProvider();
            if (provider instanceof Doctor) {
                return provider;
            }
        }
        return null;
    }

    /**
     * Checks if an appointment can be scheduled based on availability of its provider.
     * Checks the whole list for an appointment with same provider, timeslot, and date.
     * @param apt The appointment we want to schedule.
     * @return True if no appointment in list has same provider, timeslot, and date, else False.
     */
    @FXML
    private boolean isProviderAvailable(Appointment apt) {
        for (int i = 0; i < appointmentList.size(); i++) {
            Appointment currentApt = appointmentList.get(i);
            if (apt.getProvider().equals(currentApt.getProvider()) &&
                    apt.getDate().equals(currentApt.getDate()) &&
                    apt.getTimeslot().equals(currentApt.getTimeslot())) {
                return false;
            }
        }
        return true;
    }

    /**
     * All patients associated with appointments in the list are added to the medical record.
     * Method only runs when PS command is entered.
     */
    @FXML
    private void appendMedicalRecords() {
        // First sort appointments by patient...
        Sort.sort(appointmentList, "patient");
        for (int i = 0; i < appointmentList.size(); i++) {
            // Appointment to add...
            Appointment apt = appointmentList.get(i);
            Patient patient = (Patient) apt.getPatient();
            // Search medical records for the patient...
            int indexOfPatient = medicalRecord.indexOf(patient);
            Visit newVisit = new Visit(apt);
            if (indexOfPatient == NOT_FOUND) {
                patient.addVisit(newVisit);
                medicalRecord.add(patient);
            }
            else {
                Patient foundPatient = medicalRecord.get(indexOfPatient);
                foundPatient.addVisit(newVisit);
            }
        }
    }

    /**
     * Helper method checks if date and timeslot can be used to schedule a new appointment.
     * Method checks if the date is valid, comes after today, is weekday, and within six months of today.
     * Method checks if timeslot is valid by checking if it exists.
     * @param date The date object.
     * @param dateStr The date as a string.
     * @param timeslot The timeslot object.
     * @param timeslotStr The timeslot as a string.
     * @return True if the date and timeslot can be used, else False.
     */
    @FXML
    private boolean isDateAndTimeValid(Date date, String dateStr, Timeslot timeslot, String timeslotStr) {
        if (date == null || !date.isValid()) {
            printLog("Appointment date: " + dateStr +
                    " is not a valid calendar date ");
            return false;
        }
        else if (date.isTodayOrBefore()) {
            printLog("Appointment date: " + dateStr +
                    " is today or a date before today.");
            return false;
        }
        else if (date.isWeekend()) {
            printLog("Appointment date: " + dateStr +
                    " is Saturday or Sunday.");
            return false;
        }
        else if (!date.isWithinSixMonths()) {
            printLog("Appointment date: " + dateStr +
                    " is not within six months.");
            return false;
        }
        else if (timeslot == null) {
            printLog(timeslotStr + " is not a valid time slot.");
            return false;
        }
        return true;
    }

    /**
     * Helper method checks if a date of birth for a patient is valid.
     * Method checks if the date object is valid, and is a date before today.
     * @param dob The date of birth as a date object.
     * @param dobStr The date of birth as a string.
     * @return True if the date of birth is logically valid, else False.
     */
    @FXML
    private boolean isBirthdayValid(Date dob, String dobStr) {
        if (dob == null || !dob.isValid()) {
            printLog("Patient dob: " + dobStr +
                    " is not a valid calendar date ");
            return false;
        }
        else if (dob.isTodayOrAfter()) {
            printLog("Patient dob: " + dobStr +
                    " is today or a date after today.");
            return false;
        }
        return true;
    }

    /**
     * Helper method checks if the appointment a patient wants to schedule exists in the list.
     * @param apt The patient's appointment.
     * @param profile The patient's profile.
     * @return True if the patient's appointment does not exist in list, else False.
     */
    @FXML
    private boolean canAppointmentBeScheduled(Appointment apt, Profile profile) {
        if (appointmentList.contains(apt)) {
            printLog(profile.toString() +
                    " has an existing appointment at the same time slot.");
            return false;
        }
        return true;
    }

    /**
     * Helper method checks if a doctor exists and is available to attend an appointment.
     * @param apt The appointment we are checking the doctor is available for.
     * @param doctor The doctor of interest.
     * @param npi The doctor's unique npi.
     * @param timeslotStr The appointment's timeslot as a string.
     * @return True if doctor exists and is available at the appointment's timeslot, else False.
     */
    @FXML
    private boolean isDoctorValid(Appointment apt, Doctor doctor, String npi, String timeslotStr) {
        // Check if doctor exists...
        if (doctor == null) {
            printLog(npi + " - " +
                    "provider doesn't exist.");
            return false;
        }
        else if (!isProviderAvailable(apt)) {
            printLog(doctor + " " +
                    "is not available at slot " + timeslotStr);
            return false;
        }
        return true;
    }

    /**
     * Reads from a file in a list of providers and appends them to the clinic list.
     * The list of providers includes both doctors and technicians.
     * Any technicians are also appended to the front of the circular linked list.
     * @param file The file object to read from.
     */
    @FXML
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
                    printLog("Not a provider.");
                }
            }
            printLog("\n");
            providerScanner.close();
        }
        catch (FileNotFoundException e) {
            printLog("File not found.");
        }
    }

    /**
     * Helper method appends a doctor to the list of providers.
     * @param tokens The tokenizer used to append doctor.
     */
    @FXML
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
        Doctor doctor = new Doctor(profile, location, specialty, npi);
        doctorsList.add(doctor);
        providersList.add(doctor);
    }

    /**
     * Helper method appends a technician to the list of providers.
     * Method also appends technician to the circular linked list.
     * @param tokens The tokenizer used to append technician.
     */
    @FXML
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
     * Helper method computes the total credit earned by a single given provider.
     * The provider can be either a doctor or a technician.
     * @param provider The provider we compute the credit for.
     * @return The total credit earned by the provider as an integer.
     */
    @FXML
    private int computeProviderCredit(Provider provider) {
        int credit = 0;
        for (Appointment apt: appointmentList) {
            if (apt.getProvider().equals(provider)) {
                credit += ((Provider) apt.getProvider()).rate();
            }
        }
        return credit;
    }

    /**
     * Schedules a new appointment with a doctor to the clinic's list.
     * @param tokenizer The command line data tokens.
     */
    @FXML
    private void dCommand(StringTokenizer tokenizer) {
        int numTokens = tokenizer.countTokens() + 1;
        if (numTokens != NUM_TOKENS) {
            printLog("Missing data tokens.");
            return;
        }
        // Gather command line tokens...
        String dateStr = tokenizer.nextToken();
        String timeslotStr = tokenizer.nextToken();
        String firstName = tokenizer.nextToken();
        String lastName = tokenizer.nextToken();
        String dobStr = tokenizer.nextToken();
        String npi = tokenizer.nextToken();
        // Convert input strings into respective objects...
        Date date = parseDate(dateStr);
        Timeslot timeslot = parseTimeslot(timeslotStr);
        Date dob = parseDate(dobStr);
        Profile profile = new Profile(firstName, lastName, dob);
        Patient patient = new Patient(profile);
        Doctor doctor = parseDoctor(npi);
        // Appointment to add...
        Appointment apt = new Appointment(date, timeslot, patient, doctor);
        if (!isDateAndTimeValid(date, dateStr, timeslot, timeslotStr) ||
                !isBirthdayValid(dob, dobStr) ||
                !canAppointmentBeScheduled(apt, profile) ||
                !isDoctorValid(apt, doctor, npi, timeslotStr)) {
            return;
        }
        appointmentList.add(apt);
        printLog(apt + " booked.");
    }

    /**
     * Schedules a new appointment with a technician to the clinic's list.
     * @param tokenizer The command line data tokens.
     */
    @FXML
    private void tCommand(StringTokenizer tokenizer) {
        int numTokens = tokenizer.countTokens() + 1;
        if (numTokens != NUM_TOKENS) {
            printLog("Missing data tokens.");
            return;
        }
        String dateStr = tokenizer.nextToken();
        String timeslotStr = tokenizer.nextToken();
        String firstName = tokenizer.nextToken();
        String lastName = tokenizer.nextToken();
        String dobStr = tokenizer.nextToken();
        String roomStr = tokenizer.nextToken();
        Date date = parseDate(dateStr);
        Timeslot timeslot = parseTimeslot(timeslotStr);
        Date dob = parseDate(dobStr);
        Profile profile = new Profile(firstName, lastName, dob);
        Patient patient = new Patient(profile);
        Radiology room = parseRadiology(roomStr);
        // Appointment to add...
        Appointment apt = new Imaging(date, timeslot, patient, null, room);
        if (!isDateAndTimeValid(date, dateStr, timeslot, timeslotStr) ||
                !isBirthdayValid(dob, dobStr) ||
                !canAppointmentBeScheduled(apt, profile)) {
            return;
        }
        if (room == null) {
            printLog(roomStr + " - imaging service not provided.");
            return;
        }
        Technician technician = technicians.findTechnician(appointmentList, room, timeslot, date);
        if (technician == null) {
            printLog("Cannot find an available technician at all locations for " +
                    room.name() + " at slot " + timeslotStr + ".");
            return;
        }
        apt.setProvider(technician);
        appointmentList.add(apt);
        printLog(apt + " booked.");
    }

    /**
     * Cancels/deletes an existing appointment from the clinic's list.
     * @param tokenizer The command line data tokens.
     */
    @FXML
    private void cCommand(StringTokenizer tokenizer) {
        int numTokens = tokenizer.countTokens() + 1;
        if (numTokens != NUM_C_TOKENS) {
            printLog("Missing data tokens.");
            return;
        }
        String dateStr = tokenizer.nextToken();
        String timeslotStr = tokenizer.nextToken();
        String firstName = tokenizer.nextToken();
        String lastName = tokenizer.nextToken();
        String dobStr = tokenizer.nextToken();
        // Convert input strings into respective objects...
        Date date = parseDate(dateStr);
        Timeslot timeslot = parseTimeslot(timeslotStr);
        Date dob = parseDate(dobStr);
        Profile profile = new Profile(firstName, lastName, dob);
        Person patient = new Patient(profile);
        // Appointment to remove...
        Appointment apt = new Appointment(date, timeslot, patient, null);
        if (date == null || timeslot == null || dob == null) {
            return;
        }
        if (appointmentList.contains(apt)) {
            appointmentList.remove(apt);
            printLog(date + " " + timeslot +
                    " " + profile + " - appointment has been canceled.");
        }
        else {
            printLog(date + " " + timeslot +
                    " " + profile + " - appointment does not exist.");
        }
    }

    /**
     * Reschedules an existing doctor's appointment on the clinic's list.
     * Clinic does not allow imaging appointments to be rescheduled.
     * @param tokenizer The command line data tokens.
     */
    @FXML
    private void rCommand(StringTokenizer tokenizer) {
        try {
            String dateStr = tokenizer.nextToken();
            String timeslotStr = tokenizer.nextToken();
            String firstName = tokenizer.nextToken();
            String lastName = tokenizer.nextToken();
            String dobStr = tokenizer.nextToken();
            String newTimeslotStr = tokenizer.nextToken();
            Date date = parseDate(dateStr);
            Timeslot oldTimeslot = parseTimeslot(timeslotStr);
            Timeslot newTimeslot = parseTimeslot(newTimeslotStr);
            Profile profile = new Profile(firstName, lastName, parseDate(dobStr));
            Patient patient = new Patient(profile);
            Appointment oldAppointment = new Appointment(date, oldTimeslot, patient, null);
            Provider provider = (Provider) findProvider(oldAppointment);
            Appointment newAppointment = new Appointment(date, newTimeslot, patient, provider);
            if (provider == null) { // Appointment does not exist...
                printLog(date + " " + oldTimeslot +
                        " " + profile + " does not exist.");
                return;
            } else if (newTimeslot == null) { // Checks if new timeslot valid...
                printLog(newTimeslotStr + " is not a valid time slot.");
                return;
            } else if (appointmentList.contains(newAppointment)) {
                printLog(profile + " has an existing appointment at " +
                        date + " " + newTimeslot);
                return;
            } else if (!isProviderAvailable(newAppointment)) {
                printLog(provider + " is not available at slot " + newTimeslotStr);
                return;
            }
            appointmentList.remove(oldAppointment);
            appointmentList.add(newAppointment);
            printLog("Rescheduled to " + newAppointment);
        }
        catch (Exception e) {
            printLog("Missing data tokens.");
        }
    }

    /**
     * Displays all existing appointments sorted by date, time, and provider's name.
     */
    @FXML
    private void paCommand() {
        appointmentViews.clear();
        if (appointmentList.isEmpty()) {
            printLog("Schedule calendar is empty.");
            return;
        }
        Sort.sort(appointmentList, "appointment");
        addAppointmentsToTableView();
    }

    /**
     * Displays all existing appointments sorted by the patient's profile, date, and timeslots.
     */
    @FXML
    private void ppCommand() {
        appointmentViews.clear();
        if (appointmentList.isEmpty()) {
            printLog("Schedule calendar is empty.");
            return;
        }
        Sort.sort(appointmentList, "patient");
        addAppointmentsToTableView();
    }

    /**
     * Displays all existing appointments sorted by county, date, and timeslot.
     */
    @FXML
    private void plCommand() {
        appointmentViews.clear();
        if (appointmentList.isEmpty()) {
            printLog("Schedule calendar is empty.");
            return;
        }
        Sort.sort(appointmentList, "location");
        addAppointmentsToTableView();
    }

    /**
     * Displays all existing office appointments sorted by county, date, and timeslot.
     */
    @FXML
    private void poCommand() {
        appointmentViews.clear();
        if (appointmentList.isEmpty()) {
            printLog("Schedule calendar is empty.");
            return;
        }
        Sort.sort(appointmentList, "location");
        appointmentViews.clear();
        for (Appointment apt : appointmentList) {
            if (!(apt instanceof Imaging)) {
                addAppointmentToTableView(apt);
            }
        }
    }

    /**
     * Displays all existing radiology appointments sorted by county, date, and timeslot.
     */
    @FXML
    private void piCommand() {
        appointmentViews.clear();
        if (appointmentList.isEmpty()) {
            printLog("Schedule calendar is empty.");
            return;
        }
        Sort.sort(appointmentList, "location");
        appointmentViews.clear();
        for (Appointment apt : appointmentList) {
            if (apt instanceof Imaging) {
                addAppointmentToTableView(apt);
            }
        }
    }

    /**
     * Displays the billing information for all patients in the medical record.
     * Computes bill based on number of completed visits for the patient.
     * Method clears out the appointment list once bills are computed and displayed.
     */
    @FXML
    private void psCommand() {
        fundViews.clear();
        appendMedicalRecords();
        if (medicalRecord.isEmpty()) {
            printLog("Schedule calendar is empty.");
            return;
        }
        for (int i = 0; i < medicalRecord.size(); i++) {
            // Add the billing record to table view...
            String person = medicalRecord.get(i).getProfile().toString();
            String fund = "$" + medicalRecord.get(i).charge();
            FundView fundView = new FundView(person, fund);
            fundViews.add(fundView);
        }
        // Empty the list once command finishes...
        appointmentList = new List<>();
        appointmentViews.clear();
    }

    /**
     * Displays the credit information for all providers of the clinic.
     * Computes credit based on the provider's set of completed visits with patients.
     * Method does not clear out the appointment list once credits are computed.
     */
    @FXML
    private void pcCommand() {
        fundViews.clear();
        if (appointmentList.isEmpty()) {
            printLog("Schedule calendar is empty.");
            return;
        }
        // Sort providers by profile...
        Sort.sort(providersList);
        for (int i = 0; i < providersList.size(); i++) {
            Provider provider = providersList.get(i);
            // Add the billing record to table view...
            String fname = provider.getProfile().getFirstName();
            String lname = provider.getProfile().getLastName();
            String dob = provider.getProfile().getDob().toString();
            String providerInfo = fname + " " + lname + " " + dob;
            int credit = computeProviderCredit(provider);
            String fund = "$" + credit;
            FundView fundView = new FundView(providerInfo, fund);
            fundViews.add(fundView);
        }
    }

    /**
     * Helper method runs one of the scheduling commands.
     * Commands include the D, T, C, and R commands.
     * The method prints "Invalid command!" for any invalid commands.
     * @param command The command as a string.
     * @param tokenizer The command's data tokens.
     */
    @FXML
    private void runScheduleCommands(String command, StringTokenizer tokenizer) {
        switch (command) {
            case "D":
                dCommand(tokenizer);
                break;
            case "T":
                tCommand(tokenizer);
                break;
            case "C":
                cCommand(tokenizer);
                break;
            case "R":
                rCommand(tokenizer);
                break;
            default:
                printLog("Invalid command!");
        }
    }

    /**
     * Helper method runs one of the sorting/print commands.
     * Commands include PA, PP, PL, PO, and PI commands.
     * The method prints "Invalid command!" for any invalid commands.
     * @param command The command as a string.
     */
    @FXML
    private void runSortingCommands(String command) {
        switch (command) {
            case "PA":
                paCommand();
                break;
            case "PP":
                ppCommand();
                break;
            case "PL":
                plCommand();
                break;
            case "PO":
                poCommand();
                break;
            case "PI":
                piCommand();
                break;
            default:
                printLog("Invalid command!");
        }
    }

    /**
     * Helper method runs one of the billing/crediting commands.
     * Commands include the PS and PC commands.
     * The method prints "Invalid command!" for any invalid commands.
     * @param command The command as a string.
     */
    @FXML
    private void runBillingCommands(String command) {
        switch (command) {
            case "PS":
                psCommand();
                break;
            case "PC":
                pcCommand();
                break;
            default:
                printLog("Invalid command!");
        }
    }

    /**
     * Helper method runs one of the valid commands using the data tokens.
     * The method prints "Invalid command!" for any invalid commands.
     * @param command The command as a string.
     * @param tokenizer The command's data tokens.
     */
    @FXML
    private void runCommand(String command, StringTokenizer tokenizer) {
        // Running scheduling commands...
        if (command.equals("D") || command.equals("T") ||
                command.equals("C") || command.equals("R")) {
            runScheduleCommands(command, tokenizer);
        }
        // Running sorting commands...
        else if (command.equals("PA") || command.equals("PP") ||
                command.equals("PL") || command.equals("PO") || command.equals("PI")) {
            runSortingCommands(command);
        }
        // Running billing commands...
        else if (command.equals("PS") || command.equals("PC")) {
            runBillingCommands(command);
        }
        else {
            printLog("Invalid command!");
        }
    }

    /**
     * Helper method converts a string command into a tokenizer and executes the command.
     * @param fullCommand The command as a string.
     */
    @FXML
    private void runStringCommand(String fullCommand) {
        StringTokenizer tokenizer = new StringTokenizer(fullCommand, ",");
        String command = tokenizer.nextToken();
        runCommand(command, tokenizer);
    }

    /**
     * Prints text to the UI text area for logging events.
     * @param text The text to print.
     */
    @FXML
    private void printLog(String text) {
       logger.appendText(text + "\n");
    }

    /**
     * Method adds all existing appointments to the Table View under the "Appointments" tab.
     */
    @FXML
    private void addAppointmentsToTableView() {
        for (Appointment apt : appointmentList) {
            addAppointmentToTableView(apt);
        }
    }

    /**
     * Method adds a given appointment to the Table View of appointments.
     * @param apt The appointment being added.
     */
    @FXML
    private void addAppointmentToTableView(Appointment apt) {
        String date = apt.getDate().toString();
        String timeslot = apt.getTimeslot().toString();
        String patient = apt.getPatient().toString();
        String location;
        String room;
        String cost;
        if (apt instanceof Imaging) {
            location = ((Technician) apt.getProvider()).getLocation().toString();
            room = ((Imaging) apt).getRoom().toString();
            cost = "$" + ((Technician) apt.getProvider()).rate();
        }
        else {
            location = ((Doctor) apt.getProvider()).getLocation().toString();
            room = "N/A";
            cost = "$" + ((Doctor) apt.getProvider()).rate();
        }
        AppointmentView appointmentView = new AppointmentView(date, timeslot, patient, location, room, cost);
        appointmentViews.add(appointmentView);
    }

    /**
     * Method is run upon execution of the UI software.
     * Method initializes backend variables, and enables event listeners.
     */
    @FXML
    public void initialize() {
        createBackendVariables();
        fillChoiceBoxes();
        createProviderButtonListener();
        createSortingListener();
        // Create the providers table view...
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        specialtyColumn.setCellValueFactory(new PropertyValueFactory<>("specialty"));
        rateColumn.setCellValueFactory(new PropertyValueFactory<>("rate"));
        providerTableView.setItems(providerViews);
        displayTableOfProviders();
        // Create the appointments table view...
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeslotColumn.setCellValueFactory(new PropertyValueFactory<>("timeslot"));
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("room"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        appointmentTableView.setItems(appointmentViews);
        appointmentViews.clear();
        // Create the billing table view...
        personColumn.setCellValueFactory(new PropertyValueFactory<>("person"));
        fundColumn.setCellValueFactory(new PropertyValueFactory<>("fund"));
        fundTableView.setItems(fundViews);
        fundViews.clear();
    }

}