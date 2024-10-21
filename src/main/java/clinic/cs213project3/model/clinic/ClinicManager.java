
//////////////////////////////////////////////
////          DELETE THIS CLASS!!!        ////
//////////////////////////////////////////////

package clinic.cs213project3.model.clinic;

import clinic.cs213project3.model.enums.Specialty;
import clinic.cs213project3.model.enums.Radiology;
import clinic.cs213project3.model.enums.Location;
import clinic.cs213project3.model.util.CircularLinkedList;
import clinic.cs213project3.model.util.Date;
import clinic.cs213project3.model.util.List;
import clinic.cs213project3.model.util.Sort;
import clinic.cs213project3.model.util.Visit;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * The user interface in which the clinic can manage appointments.
 * Clients manage their appointments by entering commands in the terminal.
 * @author Aarush Desai, Rohan Vuppunuthula
 */
public class ClinicManager {

    private static final int NOT_FOUND = -1;
    private static final int NUM_C_TOKENS = 6;
    private static final int NUM_TOKENS = 7;

    private boolean terminate;
    private List<Appointment> appointmentList;
    private List<Patient> medicalRecord;
    private List<Timeslot> timeslots;
    private List<Provider> providersList;
    private CircularLinkedList technicians;

    /**
     * Constructor initialized the user interface.
     * Appointment list and medical records are initially empty.
     * Initially 12 possible timeslots are appended to the clinic list.
     */
    public ClinicManager() {
        terminate = false;
        appointmentList = new List<Appointment>();
        medicalRecord = new List<Patient>();
        timeslots = new List<Timeslot>();
        // Initially 12 timeslots...
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
        technicians = new CircularLinkedList();
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

    /**
     * Parses a timeslot string and returns a Timeslot object.
     * @param timeslotStr The timeslot number as a string.
     * @return The Timeslot object or null if string is invalid.
     */
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
    private boolean isDateAndTimeValid(Date date, String dateStr, Timeslot timeslot, String timeslotStr) {
        if (date == null || !date.isValid()) {
            System.out.println("Appointment date: " + dateStr +
                    " is not a valid calendar date ");
            return false;
        }
        else if (date.isTodayOrBefore()) {
            System.out.println("Appointment date: " + dateStr +
                    " is today or a date before today.");
            return false;
        }
        else if (date.isWeekend()) {
            System.out.println("Appointment date: " + dateStr +
                    " is Saturday or Sunday.");
            return false;
        }
        else if (!date.isWithinSixMonths()) {
            System.out.println("Appointment date: " + dateStr +
                    " is not within six months.");
            return false;
        }
        else if (timeslot == null) {
            System.out.println(timeslotStr + " is not a valid time slot.");
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
    private boolean isBirthdayValid(Date dob, String dobStr) {
        if (dob == null || !dob.isValid()) {
            System.out.println("Patient dob: " + dobStr +
                    " is not a valid calendar date ");
            return false;
        }
        else if (dob.isTodayOrAfter()) {
            System.out.println("Patient dob: " + dobStr +
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
    private boolean canAppointmentBeScheduled(Appointment apt, Profile profile) {
        if (appointmentList.contains(apt)) {
            System.out.println(profile.toString() +
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
    private boolean isDoctorValid(Appointment apt, Doctor doctor, String npi, String timeslotStr) {
        // Check if doctor exists...
        if (doctor == null) {
            System.out.println(npi + " - " +
                    "provider doesn't exist.");
            return false;
        }
        else if (!isProviderAvailable(apt)) {
            System.out.println(doctor.toString() + " " +
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
     * Helper method computes the total credit earned by a single given provider.
     * The provider can be either a doctor or a technician.
     * @param provider The provider we compute the credit for.
     * @return The total credit earned by the provider as an integer.
     */
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
    private void dCommand(StringTokenizer tokenizer) {
        int numTokens = tokenizer.countTokens() + 1;
        if (numTokens != NUM_TOKENS) {
            System.out.println("Missing data tokens.");
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
        System.out.println(apt.toString() + " booked.");
    }

    /**
     * Schedules a new appointment with a technician to the clinic's list.
     * @param tokenizer The command line data tokens.
     */
    private void tCommand(StringTokenizer tokenizer) {
        int numTokens = tokenizer.countTokens() + 1;
        if (numTokens != NUM_TOKENS) {
            System.out.println("Missing data tokens.");
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
            System.out.println(roomStr + " - imaging service not provided.");
            return;
        }
        Technician technician = technicians.findTechnician(appointmentList, room, timeslot, date);
        if (technician == null) {
            System.out.println("Cannot find an available technician at all locations for " +
                    room.name() + " at slot " + timeslotStr + ".");
            return;
        }
        apt.setProvider(technician);
        appointmentList.add(apt);
        System.out.println(apt.toString() + " booked.");
    }

    /**
     * Cancels/deletes an existing appointment from the clinic's list.
     * @param tokenizer The command line data tokens.
     */
    private void cCommand(StringTokenizer tokenizer) {
        int numTokens = tokenizer.countTokens() + 1;
        if (numTokens != NUM_C_TOKENS) {
            System.out.println("Missing data tokens.");
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
            System.out.println(date.toString() + " " + timeslot.toString() +
                    " " + profile.toString() + " - appointment has been canceled.");
        }
        else {
            System.out.println(date.toString() + " " + timeslot.toString() +
                    " " + profile.toString() + " - appointment does not exist.");
        }
    }

    /**
     * Reschedules an existing doctor's appointment on the clinic's list.
     * Clinic does not allow imaging appointments to be rescheduled.
     * @param tokenizer The command line data tokens.
     */
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
                System.out.println(date.toString() + " " + oldTimeslot.toString() +
                        " " + profile.toString() + " does not exist.");
                return;
            } else if (newTimeslot == null) { // Checks if new timeslot valid...
                System.out.println(newTimeslotStr + " is not a valid time slot.");
                return;
            } else if (appointmentList.contains(newAppointment)) {
                System.out.println(profile.toString() + " has an existing appointment at " +
                        date.toString() + " " + newTimeslot.toString());
                return;
            } else if (!isProviderAvailable(newAppointment)) {
                System.out.println(provider.toString() + " is not available at slot " + newTimeslotStr);
                return;
            }
            appointmentList.remove(oldAppointment);
            appointmentList.add(newAppointment);
            System.out.println("Rescheduled to " + newAppointment.toString());
        }
        catch (Exception e) {
            System.out.println("Missing data tokens.");
        }
    }

    /**
     * Displays all existing appointments sorted by date, time, and provider's name.
     */
    private void paCommand() {
        if (appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
            return;
        }
        System.out.println("** List of appointments, ordered by date/time/provider.");
        Sort.sort(appointmentList, "appointment");
        print(appointmentList);
        System.out.println("** end of list **");
    }

    /**
     * Displays all existing appointments sorted by the patient's profile, date, and timeslots.
     */
    private void ppCommand() {
        if (appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
            return;
        }
        System.out.println("** List of appointments, ordered by patient/date/time.");
        Sort.sort(appointmentList, "patient");
        print(appointmentList);
        System.out.println("** end of list **");
    }

    /**
     * Displays all existing appointments sorted by county, date, and timeslot.
     */
    private void plCommand() {
        if (appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
            return;
        }
        System.out.println("** List of appointments, ordered by county/date/time.");
        Sort.sort(appointmentList, "location");
        print(appointmentList);
        System.out.println("** end of list **");
    }

    /**
     * Displays all existing office appointments sorted by county, date, and timeslot.
     */
    private void poCommand() {
        if (appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
            return;
        }
        Sort.sort(appointmentList, "location");
        System.out.println("** List of office appointments ordered by county/date/time.");
        for (Appointment apt : appointmentList) {
            if (!(apt instanceof Imaging)) {
                System.out.println(apt);
            }
        }
        System.out.println("** end of list **");
    }

    /**
     * Displays all existing radiology appointments sorted by county, date, and timeslot.
     */
    private void piCommand() {
        if (appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
            return;
        }
        Sort.sort(appointmentList, "location");
        System.out.println("** List of radiology appointments ordered by county/date/time.");
        for (Appointment apt : appointmentList) {
            if (apt instanceof Imaging) {
                Imaging imagingApt = (Imaging) apt;
                System.out.println(imagingApt);
            }
        }
        System.out.println("** end of list **");

    }

    /**
     * Displays the billing information for all patients in the medical record.
     * Computes bill based on number of completed visits for the patient.
     * Method clears out the appointment list once bills are computed and displayed.
     */
    private void psCommand() {
        appendMedicalRecords();
        if (medicalRecord.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
            return;
        }
        System.out.println("** Billing statement ordered by patient. **");
        for (int i = 0; i < medicalRecord.size(); i++) {
            int patientNum = i + 1;
            String patientInfo = medicalRecord.get(i).billingInfo();
            System.out.println("(" + patientNum + ")" + " " + patientInfo);
        }
        System.out.println("** end of list **");
        // Empty the list once command finishes...
        appointmentList = new List<Appointment>();
    }

    /**
     * Displays the credit information for all providers of the clinic.
     * Computes credit based on the provider's set of completed visits with patients.
     * Method does not clear out the appointment list once credits are computed.
     */
    private void pcCommand() {
        if (appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
            return;
        }
        // Sort providers by profile...
        Sort.sort(providersList);
        System.out.println("** Credit amount ordered by provider. **");
        for (int i = 0; i < providersList.size(); i++) {
            int providerNum = i + 1;
            Provider provider = providersList.get(i);
            int credit = computeProviderCredit(provider);
            DecimalFormat deci = new DecimalFormat("#,###.00");
            String creditStr = deci.format(credit);
            String fname = provider.getProfile().getFirstName();
            String lname = provider.getProfile().getLastName();
            String dob = provider.getProfile().getDob().toString();
            String providerInfo = fname + " " + lname + " " + dob;
            System.out.println("(" + providerNum + ") " +
                    providerInfo + " [credit amount: $" +
                    creditStr + "]");
        }
        System.out.println("** end of list **");
    }

    /**
     * Command terminates the software runtime.
     */
    private void qCommand() {
        terminate = true;
    }

    /**
     * Helper method runs one of the scheduling commands.
     * Commands include the D, T, C, and R commands.
     * The method prints "Invalid command!" for any invalid commands.
     * @param command The command as a string.
     * @param tokenizer The command's data tokens.
     */
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
                System.out.println("Invalid command!");
        }
    }

    /**
     * Helper method runs one of the sorting/print commands.
     * Commands include PA, PP, PL, PO, and PI commands.
     * The method prints "Invalid command!" for any invalid commands.
     * @param command The command as a string.
     */
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
                System.out.println("Invalid command!");
        }
    }

    /**
     * Helper method runs one of the billing/crediting commands.
     * Commands include the PS and PC commands.
     * The method prints "Invalid command!" for any invalid commands.
     * @param command The command as a string.
     */
    private void runBillingCommands(String command) {
        switch (command) {
            case "PS":
                psCommand();
                break;
            case "PC":
                pcCommand();
                break;
            default:
                System.out.println("Invalid command!");
        }
    }

    /**
     * Helper method runs one of the valid commands using the data tokens.
     * The method prints "Invalid command!" for any invalid commands.
     * @param command The command as a string.
     * @param tokenizer The command's data tokens.
     */
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
        // Terminating program...
        else if (command.equals("Q")) {
            qCommand();
        }
        else {
            System.out.println("Invalid command!");
        }
    }

    /**
     * Prints all elements of the list in a column.
     * @param list The generic list.
     */
    private void print(List list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    /**
     * Executes the terminal user interface for the scheduler.
     * Method waits for users to enter commands with their data tokens.
     * Multiple commands can be pasted and executed all at once.
     */
    public void run() {
        File file = new File("providers.txt");
        readInProviders(file);
        System.out.println("Providers loaded to the list.");
        // Sort providers by profile and print them...
        Sort.sort(providersList);
        print(providersList);
        System.out.println("\nRotation list for the technicians.");
        // Create technician tracker for linked list...
        technicians.createTracker();
        technicians.printChain();
        System.out.println("\nClinic Manager is running...");
        Scanner scanner = new Scanner(System.in);
        while (!terminate) {
            String commandLine = scanner.nextLine().trim();
            if (commandLine.isEmpty()) {
                continue;
            }
            StringTokenizer tokenizer = new StringTokenizer(commandLine, ",");
            String command = tokenizer.nextToken();
            runCommand(command, tokenizer);
        }
        scanner.close();
        System.out.println("Clinic Manager terminated.");
    }

}