package clinic.cs213project3.model.enums;

/**
 * Enum object stores the location of a provider's workplace.
 * @author Aarush Desai, Rohan Vuppunuthula
 */
public enum Location {

    BRIDGEWATER("Somerset", "08807"),
    EDISON("Middlesex", "08817"),
    PISCATAWAY("Middlesex", "08854"),
    PRINCETON("Mercer", "08542"),
    MORRISTOWN("Morris", "07960"),
    CLARK("Union", "07066");

    private final String county;
    private final String zip;

    /**
     * Constructor creates a new location object.
     * @param county The office county.
     * @param zip The zip code.
     */
    Location(String county, String zip) {
        this.county = county;
        this.zip = zip;
    }

    /**
     * Gives the location object formatted into a string.
     * String format sequence is the enum name, county, and zip in that order.
     * @return String representation of a location.
     */
    public String toString() {
        return name() + ", " + county + " " + zip;
    }

    /**
     * Getter method returns the location's county.
     * @return The county's name.
     */
    public String getCounty() {
        return county;
    }

}
