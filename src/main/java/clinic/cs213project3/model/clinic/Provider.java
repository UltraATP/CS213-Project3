package clinic.cs213project3.model.clinic;
import clinic.cs213project3.model.enums.Location;

/**
 * The abstract class structure for a Provider object.
 * The providers serving the clinic are the doctors and technicians.
 * @author Aarush Desai, Rohan Vuppunuthula
 */
public abstract class Provider extends Person {

    private Location location;

    /**
     * Constructor creates a new provider for the clinic.
     * @param profile The provider's profile.
     * @param location The provider's place of operation.
     */
    public Provider(Profile profile, Location location) {
        super(profile);
        this.location = location;
    }

    /**
     * Abstract method returns the cost for a single visit with a provider.
     * @return The cost for visiting a provider.
     */
    public abstract int rate();

    /**
     * Getter method returns the provider's location.
     * @return The location.
     */
    public Location getLocation() {
        return location;
    }

}
