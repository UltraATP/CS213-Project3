package clinic.cs213project3.model.clinic;
import clinic.cs213project3.model.enums.Specialty;
import clinic.cs213project3.model.enums.Location;

/**
 * This is the object structure for a Doctor object.
 * Every doctor object is a type of provider for the clinic.
 * The Doctor class is a subclass of the abstract Provider class.
 * @author Aarush Desai
 */
public class Doctor extends Provider {

    private Specialty specialty;
    private String npi;

    /**
     * Constructor initializes a new doctor for the clinic.
     * @param profile The doctor's profile.
     * @param location The doctor's office location.
     * @param specialty The doctor's specialty.
     * @param npi The doctor's unique numerical ID.
     */
    public Doctor(Profile profile, Location location, Specialty specialty, String npi) {
        super(profile, location);
        this.specialty = specialty;
        this.npi = npi;
    }

    /**
     * Returns the doctor's charge for a single appointment.
     * @return The integer cost for visit with a doctor.
     */
    @Override
    public int rate() {
        return specialty.getCharge();
    }

    /**
     * Convert's a doctor's information into a string format.
     * String format includes the profile, location, specialty, and npi with placed brackets.
     * @return String representation of a doctor object.
     */
    @Override
    public String toString() {
        return "[" + profile.toString() + ", " +
                getLocation().toString() + "][" +
                getSpecialty().toString() + ", #" + npi + "]";
    }

    /**
     * Getter method returns the doctor's specialty.
     * @return The specialty enum.
     */
    public Specialty getSpecialty() {
        return specialty;
    }

    /**
     * Getter method returns the doctor's unique NPI ID.
     * @return The NPI as a string.
     */
    public String getNpi() {
        return npi;
    }

}