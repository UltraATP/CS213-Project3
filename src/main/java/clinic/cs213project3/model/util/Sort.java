package clinic.cs213project3.model.util;
import clinic.cs213project3.model.clinic.Appointment;
import clinic.cs213project3.model.clinic.Provider;
import clinic.cs213project3.model.clinic.Person;
import clinic.cs213project3.model.clinic.Timeslot;

/**
 * Class provides methods to sort lists in different ways.
 * The primary sorting used is the Selection Sort algorithm.
 * Sorting is used on the appointment lists for the clinic manager.
 * @author Aarush Desai, Rohan Vuppunuthula
 */
public class Sort {

    private static final int LESS_THAN = -1;
    private static final int EQUIVALENT = 0;
    private static final int GREATER_THAN = 1;

    /**
     * Method compares two appointment objects to find the minimum, given an ordering.
     * Determines the order used to compare appointments in a selection sort.
     * @param minApt The current minimum appointment in selection sort.
     * @param apt The appointment we are comparing with.
     * @param order String to determine what ordering method to use.
     * @return True if apt comes before minApt, False if not or if "order" is an invalid string.
     */
    private static boolean smallerThanMin(Appointment minApt, Appointment apt, String order) {
        if (order.equals("patient")) {
            return compareByPatient(apt, minApt) == LESS_THAN;
        }
        else if (order.equals("location")) {
            return compareByLocation(apt, minApt) == LESS_THAN;
        }
        else if (order.equals("appointment")) {
            return compareByAppointment(apt, minApt) == LESS_THAN;
        }
        else {
            return false;
        }
    }

    /**
     * Compares two appointments by patient, date, and timeslot in that order.
     * Used for the PP command.
     * @param apt1 The first appointment.
     * @param apt2 The second appointment.
     * @return -1 if apt1 comes before apt2, 1 if after, and 0 if the same.
     */
    private static int compareByPatient(Appointment apt1, Appointment apt2) {
        Person patient1 = apt1.getPatient();
        Person patient2 = apt2.getPatient();
        Date date1 = apt1.getDate();
        Date date2 = apt2.getDate();
        Timeslot time1 = apt1.getTimeslot();
        Timeslot time2 = apt2.getTimeslot();
        if (patient1.compareTo(patient2) > 0) {
            return GREATER_THAN;
        }
        else if (patient1.compareTo(patient2) < 0) {
            return LESS_THAN;
        }
        else if (date1.compareTo(date2) > 0) {
            return GREATER_THAN;
        }
        else if (date1.compareTo(date2) < 0) {
            return LESS_THAN;
        }
        else if (time1.compareTo(time2) > 0) {
            return GREATER_THAN;
        }
        else if (time1.compareTo(time2) < 0) {
            return LESS_THAN;
        }
        else {
            return EQUIVALENT;
        }
    }

    /**
     * Compares two appointments by county, date, and timeslot in that order.
     * Used for the PL, PO, and PI commands.
     * @param apt1 The first appointment.
     * @param apt2 The second appointment.
     * @return -1 if apt1 comes before apt2, 1 if after, and 0 if the same.
     */
    private static int compareByLocation(Appointment apt1, Appointment apt2) {
        String county1 = ((Provider) apt1.getProvider()).getLocation().getCounty();
        String county2 = ((Provider) apt2.getProvider()).getLocation().getCounty();
        Date date1 = apt1.getDate();
        Date date2 = apt2.getDate();
        Timeslot time1 = apt1.getTimeslot();
        Timeslot time2 = apt2.getTimeslot();
        if (county1.compareTo(county2) > 0) {
            return GREATER_THAN;
        }
        else if (county1.compareTo(county2) < 0) {
            return LESS_THAN;
        }
        else if (date1.compareTo(date2) > 0) {
            return GREATER_THAN;
        }
        else if (date1.compareTo(date2) < 0) {
            return LESS_THAN;
        }
        else if (time1.compareTo(time2) > 0) {
            return GREATER_THAN;
        }
        else if (time1.compareTo(time2) < 0) {
            return LESS_THAN;
        }
        else {
            return EQUIVALENT;
        }
    }

    /**
     * Compares two appointments by date, timeslot, and provider's name in that order.
     * Used for the PA command.
     * @param apt1 The first appointment.
     * @param apt2 The second appointment.
     * @return -1 if apt1 comes before apt2, 1 if after, and 0 if the same.
     */
    private static int compareByAppointment(Appointment apt1, Appointment apt2) {
        Date date1 = apt1.getDate();
        Date date2 = apt2.getDate();
        Timeslot time1 = apt1.getTimeslot();
        Timeslot time2 = apt2.getTimeslot();
        String providerName1 = apt1.getProvider().getProfile().getLastName().toUpperCase();
        String providerName2 = apt2.getProvider().getProfile().getLastName().toUpperCase();
        if (date1.compareTo(date2) > 0) {
            return GREATER_THAN;
        }
        else if (date1.compareTo(date2) < 0) {
            return LESS_THAN;
        }
        else if (time1.compareTo(time2) > 0) {
            return GREATER_THAN;
        }
        else if (time1.compareTo(time2) < 0) {
            return LESS_THAN;
        }
        else if (providerName1.compareTo(providerName2) > 0) {
            return GREATER_THAN;
        }
        else if (providerName1.compareTo(providerName2) < 0) {
            return LESS_THAN;
        }
        else {
            return EQUIVALENT;
        }
    }

    /**
     * Performs selection sort on a list of appointments, given an ordering.
     * The type of ordering on the appointments can be specified.
     * @param appointments The list of appointments.
     * @param order The type of ordering to sort appointments by.
     */
    public static void sort(List<Appointment> appointments, String order) {
        int size = appointments.size();
        for (int i = 0; i < size - 1; i++) {
            // Find minimum...
            int minIndex = i;
            for (int j = i + 1; j < size; j++) {
                Appointment minApt = appointments.get(minIndex);
                // Choose ordering based on input...
                if (smallerThanMin(minApt, appointments.get(j), order)) {
                    minIndex = j;
                }
            }
            // Swap elements...
            if (minIndex != i) {
                Appointment temp = appointments.get(i);
                appointments.set(i, appointments.get(minIndex));
                appointments.set(minIndex, temp);
            }
        }
    }

    /**
     * Performs selection sort on a list of providers in order of profiles.
     * @param providers The list of providers.
     */
    public static void sort(List<Provider> providers) {
        int size = providers.size();
        for (int i = 0; i < size - 1; i++) {
            // Find minimum...
            int minIndex = i;
            for (int j = i + 1; j < size; j++) {
                Provider minProvider = providers.get(minIndex);
                if (minProvider.compareTo(providers.get(j)) > 0) {
                    minIndex = j;
                }
            }
            // Swap elements...
            if (minIndex != i) {
                Provider temp = providers.get(i);
                providers.set(i, providers.get(minIndex));
                providers.set(minIndex, temp);
            }
        }
    }

}
