package clinic.cs213project3.model.clinic;

/**
 * The general structure of a timeslot for the clinic.
 * Clinic stores twelve timeslots initially.
 * @author Aarush Desai, Rohan Vuppunuthula
 */
public class Timeslot implements Comparable<Timeslot> {

    private static final int NOON = 12;
    private static final int LESS_THAN = -1;
    private static final int EQUAL = 0;
    private static final int GREATER_THAN = 1;

    private int hour;
    private int minute;

    /**
     * Constructor creates a new timeslot.
     * Hours are stored in military time.
     * @param hour The hour in military time.
     * @param minute The minute.
     */
    public Timeslot(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Method checks if two timeslots are equal.
     * Two timeslots are equal if they have the same hour and minute.
     * @param obj The object being compared.
     * @return True if timeslots are equal, else False.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Timeslot) {
            Timeslot timeslot = (Timeslot) obj;
            return hour == timeslot.hour &&
                    minute == timeslot.minute;
        }
        return false;
    }

    /**
     * Defines the ordering of two timeslots.
     * Ordering is defined by hour and minute in that order.
     * @param timeslot The timeslot being compared against.
     * @return -1 if object is before timeslot, 1 if after, and 0 if equal.
     */
    @Override
    public int compareTo(Timeslot timeslot) {
        if (hour < timeslot.hour) {
            return LESS_THAN;
        }
        else if (hour > timeslot.hour) {
            return GREATER_THAN;
        }
        else if (minute < timeslot.minute) {
            return LESS_THAN;
        }
        else if (minute > timeslot.minute) {
            return GREATER_THAN;
        }
        else {
            return EQUAL;
        }
    }

    /**
     * Gives the timeslot formatted into a string.
     * String format sequence is the AM/PM format of the clock time.
     * @return String representation of a timeslot.
     */
    @Override
    public String toString() {
        String period = "AM";
        if (hour >= NOON) {
            period = "PM";
        }
        int formattedHour = hour % NOON;
        if (formattedHour == 0) {
            formattedHour = NOON;
        }
        String formattedMinute = String.format("%02d", minute);
        return formattedHour + ":" + formattedMinute + " " + period;
    }

}
