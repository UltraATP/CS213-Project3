package clinic.cs213project3.model.clinic;
import clinic.cs213project3.model.enums.Location;
import java.text.DecimalFormat;

/**
 * This is the object structure for a Technician object.
 * Every technician object is a type of provider for the clinic.
 * The Technician class is a subclass of the abstract Provider class.
 * @author Aarush Desai
 */
public class Technician extends Provider {

    private int ratePerVisit;

    /**
     * Constructor creates a new technician for the clinic.
     * @param profile The technician's profile.
     * @param location The technician's location of operation.
     * @param ratePerVisit The technician's cost for one visit.
     */
    public Technician(Profile profile, Location location, int ratePerVisit) {
        super(profile, location);
        this.ratePerVisit = ratePerVisit;
    }

    /**
     * Returns the technician's charge for a single appointment.
     * @return The integer cost for visit with a technician.
     */
    @Override
    public int rate() {
        return ratePerVisit;
    }

    /**
     * Convert's the technician's information into a string format.
     * String format includes the profile, location, and rate with placed brackets.
     * @return String representation of a technician object.
     */
    @Override
    public String toString() {
        DecimalFormat deci = new DecimalFormat("#,###.00");
        String rate = deci.format(ratePerVisit);
        return "[" + profile.toString() + ", " +
                getLocation().toString() + "][rate: $" +
                rate + "]";
    }

}
