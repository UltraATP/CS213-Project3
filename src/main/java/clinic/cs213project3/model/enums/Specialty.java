package clinic.cs213project3.model.enums;

/**
 * Enum object stores a provider's specialty and their prices.
 * @author Aarush Desai, Rohan Vuppunuthula
 */
public enum Specialty {

    FAMILY(250),
    PEDIATRICIAN(300),
    ALLERGIST(350);

    private final int charge;

    /**
     * Constructor creates a new specialty.
     * Specialties represent the occupations of medical professionals in the clinic.
     * @param charge Price for one visit to a specialist.
     */
    Specialty(int charge) {
        this.charge = charge;
    }

    /**
     * Getter method returns the charge for meeting with a specialist.
     * @return The integer charge/expense for meeting a specialist.
     */
    public int getCharge() {
        return charge;
    }

}