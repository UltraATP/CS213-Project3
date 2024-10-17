package clinic.cs213project3.model.util;
import clinic.cs213project3.model.clinic.Appointment;

/**
 * A linked list node containing a patient's completed appointment.
 * The nodes make up the linked list of a patient's completed appointments.
 * @author Aarush Desai, Rohan Vuppunuthula
 */
public class Visit {

    private Appointment appointment;
    private Visit next;

    /**
     * Constructor creates a new node, or a single-node linked list.
     * @param apt The appointment, or node data.
     */
    public Visit(Appointment apt) {
        this.appointment = apt; // Data...
        this.next = null; // Pointer...
    }

    /**
     * Setter method sets the node's pointer to the next node.
     * @param next The new node to add to linked-list.
     */
    public void setNext(Visit next) {
        this.next = next;
    }

    /**
     * Getter method to access the node data/appointment.
     * @return The appointment data in the node.
     */
    public Appointment getAppointment() {
        return appointment;
    }

    /**
     * Getter method accesses the node's pointer.
     * Also known as the reference to the next node.
     * @return The next node.
     */
    public Visit getNext() {
        return next;
    }

    /**
     * Returns the visit node data into a string format.
     * String format of a visit is the string format of its appointment data.
     * @return The appointment information as a string.
     */
    public String toString() {
        return appointment.toString();
    }

}