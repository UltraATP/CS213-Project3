package clinic.cs213project3.model.enums;

/**
 * Enum class defines different imaging services under radiology.
 * Services include cat scans, ultra sounds, and x-rays.
 * @author Aarush Desai, Rohan Vuppunuthula
 */
public enum Radiology {

    CATSCAN(),
    ULTRASOUND(),
    XRAY();

    /**
     * Trivial constructor to create new radiology services.
     */
    Radiology() {}

}
