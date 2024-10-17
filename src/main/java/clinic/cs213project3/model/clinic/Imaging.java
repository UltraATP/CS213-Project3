package clinic.cs213project3.model.clinic;
import clinic.cs213project3.model.util.Date;
import clinic.cs213project3.model.enums.Radiology;

/**
 * The structure for an Imaging appointment.
 * Imaging appointments are made for patients meeting technicians.
 * Appointments that are not Imaging objects are for doctors.
 * @author Aarush Desai
 */
public class Imaging extends Appointment {

    private Radiology room;

    /**
     * Constructor creates a new Imaging appointment for the clinic.
     * @param date The appointment date.
     * @param timeslot The appointment timeslot.
     * @param patient The patient.
     * @param provider The technician.
     * @param room The Radiology service as an enum.
     */
    public Imaging(Date date, Timeslot timeslot, Person patient, Person provider, Radiology room) {
        super(date, timeslot, patient, provider);
        this.room = room;
    }

    /**
     * Getter method returns the radiology room.
     * @return The radiology room.
     */
    public Radiology getRoom() {
        return room;
    }

    /**
     * Converts an imaging appointment object into a string format.
     * String format sequence is the date, timeslot, patient, provider, and radiology.
     * @return The string representation of the imaging appointment.
     */
    @Override
    public String toString() {
        return date.toString() + " "
                + timeslot.toString() + " "
                + patient.toString() + " "
                + provider.toString() + "[" + room.name() + "]";
    }

}