package clinic.cs213project3;

/**
 * The template class used to display appointment data onto the table view in the UI.
 * The table view under the "Appointments" tab contains six columns, corresponding to each data field.
 * Date fields include date, timeslot, patient, location, room, and cost.
 * @author Aarush Desai
 */
public class AppointmentView {

    private final String date;
    private final String timeslot;
    private final String patient;
    private final String location;
    private final String room;
    private final String cost;

    /**
     * The constructor for a viewable appointment object.
     * @param date The appointment date.
     * @param timeslot The appointment timeslot.
     * @param patient The patient.
     * @param location The location.
     * @param room The radiology room for imaging appointments.
     * @param cost The cost for visit.
     */
    public AppointmentView(String date, String timeslot, String patient, String location, String room, String cost) {
        this.date = date;
        this.timeslot = timeslot;
        this.patient = patient;
        this.location = location;
        this.room = room;
        this.cost = cost;
    }

    /**
     * Getter method returns the date of the appointment.
     * @return The date.
     */
    public String getDate() {
        return date;
    }

    /**
     * Getter method returns the timeslot of the appointment.
     * @return The timeslot.
     */
    public String getTimeslot() {
        return timeslot;
    }

    /**
     * Getter method returns the patient.
     * @return The patient.
     */
    public String getPatient() {
        return patient;
    }

    /**
     * Getter method returns the appointment location.
     * @return The location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Getter method returns the room for imaging appointment.
     * @return The room.
     */
    public String getRoom() {
        return room;
    }

    /**
     * Getter method returns the cost for the visit.
     * @return The cost for the visit.
     */
    public String getCost() {
        return cost;
    }

}
