package clinic.cs213project3.model.clinic;
import clinic.cs213project3.model.util.Date;

/**
 * This is the object structure for a new appointment in the clinic list.
 * Each appointment contains a patient, medical provider, date, and timeslot.
 * @author Aarush Desai, Rohan Vuppunuthula
 */
public class Appointment implements Comparable<Appointment> {

    private static final int LESS_THAN = -1;
    private static final int EQUAL = 0;
    private static final int GREATER_THAN = 1;

    protected Date date;
    protected Timeslot timeslot;
    protected Person patient;
    protected Person provider;

    /**
     * The constructor makes a new appointment for the clinic.
     * Parameters store patient information, timings, and medical providers.
     * @param date Date of the appointment.
     * @param timeslot Time of appointment.
     * @param patient The patient.
     * @param provider The medical professional, either a doctor or technician.
     */
    public Appointment(Date date, Timeslot timeslot, Person patient, Person provider) {
        this.date = date;
        this.timeslot = timeslot;
        this.patient = patient;
        this.provider = provider;
    }

    /**
     * Checks if current appointment is equal to another appointment.
     * Appointments are equal if they have same date, timeslot, and patient.
     * @param obj The object to be compared.
     * @return True if appointments are equal, else False.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Appointment) {
            Appointment apt = (Appointment) obj;
            return date.equals(apt.date) &&
                    patient.equals(apt.patient) &&
                    timeslot.equals(apt.timeslot);
        }
        return false;
    }

    /**
     * Defines the ordering of two appointment objects.
     * Ordering is based on the date, timeslots, and patients in that order.
     * @param apt The appointment being compared against.
     * @return -1 if object comes before appointment, 1 if after, and 0 if equal.
     */
    @Override
    public int compareTo(Appointment apt) {
        if (date.compareTo(apt.date) < 0) {
            return LESS_THAN;
        }
        else if (date.compareTo(apt.date) > 0) {
            return GREATER_THAN;
        }
        else if (timeslot.compareTo(apt.timeslot) < 0) {
            return LESS_THAN;
        }
        else if (timeslot.compareTo(apt.timeslot) > 0) {
            return GREATER_THAN;
        }
        else if (patient.compareTo(apt.patient) < 0) {
            return LESS_THAN;
        }
        else if (patient.compareTo(apt.patient) > 0) {
            return GREATER_THAN;
        }
        else {
            return EQUAL;
        }
    }

    /**
     * Converts an appointment object into a string format.
     * String format sequence is the date, timeslot, patient, and provider.
     * @return The string representation of the appointment.
     */
    @Override
    public String toString() {
        return date.toString() + " "
            + timeslot.toString() + " "
            + patient.toString() + " "
            + provider.toString();
    }

    /**
     * Getter method to access appointment's date.
     * @return The date object.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Getter method to access appointment timeslot.
     * @return The timeslot object.
     */
    public Timeslot getTimeslot() {
        return timeslot;
    }

    /**
     * Getter method to access patient from an appointment.
     * @return The patient as a Person object.
     */
    public Person getPatient() {
        return patient;
    }

    /**
     * Getter method to access the provider associated with appointment.
     * @return The provider as a Person object.
     */
    public Person getProvider() {
        return provider;
    }

    /**
     * Setter method sets the provider for the appointment.
     * @param provider The provider we assign to the appointment.
     */
    public void setProvider(Person provider) {
        this.provider = provider;
    }

}