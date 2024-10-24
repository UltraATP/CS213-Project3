package clinic.cs213project3.model.util;
import clinic.cs213project3.model.clinic.Appointment;
import clinic.cs213project3.model.clinic.Imaging;
import clinic.cs213project3.model.clinic.Technician;
import clinic.cs213project3.model.clinic.Timeslot;
import clinic.cs213project3.model.clinic.Provider;
import clinic.cs213project3.model.enums.Location;
import clinic.cs213project3.model.enums.Radiology;

/**
 * The implementation of a circular linked list of Technician objects.
 * This list is used to assign technicians to new clinic appointments.
 * @author Aarush Desai
 */
public class CircularLinkedList {

    /**
     * Class defines the nodes of the circular linked list.
     */
    private static class Node {

        public Technician item;
        public Node next;

        /**
         * Constructor creates a blank node for the list.
         */
        public Node(){
            item = null;
            next = null;
        }

    }

    private Node last;
    private Node tracker;

    /**
     * Constructor creates an empty circular linked list.
     */
    public CircularLinkedList() {
        last = null;
        tracker = null;
    }

    /**
     * Helper method checks if a technician is available on a give date and timeslot.
     * @param appointments The clinic's appointment list.
     * @param technician The technician of interest.
     * @param timeslot The timeslot.
     * @param date The date.
     * @return True if the technician is available, else False.
     */
    private boolean technicianAvailable(List<Appointment> appointments, Technician technician, Timeslot timeslot, Date date) {
        for (Appointment apt : appointments) {
            if (apt instanceof Imaging) {
                Imaging imagingApt = (Imaging) apt;
                // If technician is unavailable at date & timeslot...
                if (imagingApt.getDate().equals(date) &&
                        imagingApt.getTimeslot().equals(timeslot) &&
                        imagingApt.getProvider().equals(technician)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Helper method checks if technician's location is available for an imaging appointment.
     * @param appointments The clinic list of appointments.
     * @param location The technician's location.
     * @param room The radiology room.
     * @param timeslot The appointment's timeslot.
     * @param date The appointment's date.
     * @return True if the location is available, else False.
     */
    private boolean locationAvailable(List<Appointment> appointments, Location location, Radiology room, Timeslot timeslot, Date date) {
        for (Appointment apt : appointments) {
            if (apt instanceof Imaging) {
                Imaging imagingApt = (Imaging) apt;
                // If room at location is being used on same date & timeslot...
                Date imagingDate = imagingApt.getDate();
                Timeslot imagingTimeslot = imagingApt.getTimeslot();
                Location imagingLocation = ((Provider) imagingApt.getProvider()).getLocation();
                Radiology imagingRoom = imagingApt.getRoom();
                if (imagingDate.equals(date) &&
                        imagingTimeslot.equals(timeslot) &&
                        imagingLocation.equals(location) &&
                        imagingRoom.equals(room)) {
                    return false;
                }

            }
        }
        return true;
    }

    /**
     * Checks if the circular linked list is empty.
     * @return True if list is empty, else False.
     */
    public boolean isEmpty() {
        return last == null;
    }

    /**
     * Appends a new technician node to front of the list.
     * @param item The technician object.
     */
    public void addToFront(Technician item) {
        Node newNode = new Node();
        newNode.item = item;
        if (isEmpty()){
            newNode.next = newNode;
            last = newNode;
        }
        else {
            newNode.next = last.next;
            last.next = newNode;
        }
    }

    /**
     * Computes the number of nodes in the circular linked list.
     * @return Integer number of nodes in the list.
     */
    public int size() {
        if (isEmpty()) {
            return 0;
        }
        int count = 0;
        Node PTR = last.next;
        do {
            count++;
            PTR = PTR.next;
        }
        while (PTR != last.next);
        return count;
    }

    /**
     * Initializes the tracker pointing to the front of the list.
     * This tracker is used to assign technicians to new appointments.
     */
    public void createTracker() {
        tracker = last;
    }

    /**
     * Getter method returns the technician in the list the tracker points to.
     * @return The technician object the tracker points to.
     */
    public Technician getTrackerValue() {
        if (tracker != null) {
            return tracker.item;
        }
        return null;
    }

    /**
     * Method updates the tracker node to the next node in the list.
     */
    public void setTrackerNext() {
        if (tracker != null) {
            tracker = tracker.next;
        }
    }

    /**
     * Searches for the next available technician to attend an appointment.
     * @param appointments The clinic's appointment list.
     * @param room The radiology room of the appointment.
     * @param timeslot The appointment's timeslot.
     * @param date The appointment's date.
     * @return The next available technician.
     */
    public Technician findTechnician(List<Appointment> appointments, Radiology room, Timeslot timeslot, Date date) {
        // Start at next technician in the list...
        tracker = tracker.next;
        int listSize = size();
        // Iterate through every technician in the list starting at tracker...
        for (int i = 1; i <= listSize; i++) {
            Technician technician = getTrackerValue();
            // First check if technician is free on given date & timeslot...
            if (technicianAvailable(appointments, technician, timeslot, date)) {
                Location location = technician.getLocation();
                // Next check if radiology room is available at technician's location, timeslot, & date...
                if (locationAvailable(appointments, location, room, timeslot, date)) {
                    return technician;
                }
            }
            if (i != listSize) {
                tracker = tracker.next;
            }
        }
        return null;
    }

}