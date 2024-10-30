package clinic.cs213project3.view;

/**
 * The template class used to display provider data onto the table view in the UI.
 * The table view under the "Providers" tab contains five columns, corresponding to each data field.
 * Date fields include name, type, location, specialty, and rate.
 * @author Aarush Desai
 */
public class ProviderView {

    private final String name;
    private final String type;
    private final String location;
    private final String specialty;
    private final String rate;

    /**
     * The constructor for a viewable provider object.
     * @param name The provider's name.
     * @param type The type of provider.
     * @param location The provider's location.
     * @param specialty The provider's specialty.
     * @param rate The provider's rate.
     */
    public ProviderView(String name, String type, String location, String specialty, String rate) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.specialty = specialty;
        this.rate = rate;
    }

    /**
     * Getter method returns the name.
     * @return The provider's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method returns the type.
     * @return The type of provider.
     */
    public String getType() {
        return type;
    }

    /**
     * Getter method returns the location.
     * @return The provider's location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Getter method returns the specialty.
     * @return The provider's specialty.
     */
    public String getSpecialty() {
        return specialty;
    }

    /**
     * Getter method returns the rate.
     * @return The provider's rate.
     */
    public String getRate() {
        return rate;
    }

}