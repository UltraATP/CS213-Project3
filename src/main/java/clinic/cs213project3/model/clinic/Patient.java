package clinic.cs213project3.model.clinic;
import clinic.cs213project3.model.enums.Specialty;
import clinic.cs213project3.model.util.Visit;
import java.text.DecimalFormat;

/**
 * The class structure for a patient attending a clinic.
 * Every patient has a profile, and a linked-list of their clinic visits.
 * @author Aarush Desai, Rohan Vuppunuthula
 */
public class Patient extends Person {

    private Visit visit;

    /**
     * Constructor makes a patient object with a profile and empty list of visits.
     * @param profile The patient's profile.
     */
    public Patient(Profile profile) {
        super(profile);
    }

    /**
     * Computes the total bill for a patient's set of visits.
     * Iterates over linked-list of visits along with totaling charges.
     * @return The integer amount due by the patient.
     */
    public int charge() {
        int totalCharge = 0;
        Visit currentVisit = visit;
        // Traverse the linked list of visits...
        while (currentVisit != null) {
            // Get the node's data...
            Appointment appointment = currentVisit.getAppointment();
            if (appointment != null) {
                Person provider = appointment.getProvider();
                // If provider is a doctor...
                if (provider instanceof Doctor) {
                    Doctor doctor = (Doctor) provider;
                    Specialty specialty = doctor.getSpecialty();
                    if (specialty != null) {
                        totalCharge += specialty.getCharge();
                    }
                }
                // If provider is a technician...
                else if (provider instanceof Technician) {
                    Technician technician = (Technician) provider;
                    totalCharge += technician.rate();
                }
            }
            currentVisit = currentVisit.getNext();
        }
        return totalCharge;
    }

    /**
     * The String representation of a patient's billing.
     * String format sequence is the profile string, and patient's charge, in that order.
     * @return String with patient's profile and total charges.
     */
    public String billingInfo() {
        DecimalFormat deci = new DecimalFormat("#,###.00");
        String charge = deci.format(charge());
        return profile.toString() + " [due: $" +
                charge + "]";
    }

    /**
     * Adds a new visit node to front of the patient's linked list of visits.
     * @param newVisit The new node/visit to add.
     */
    public void addVisit(Visit newVisit) {
        if (visit == null) { // If list is empty...
            visit = newVisit;
            return;
        }
        newVisit.setNext(visit);
        visit = newVisit;
    }

}
