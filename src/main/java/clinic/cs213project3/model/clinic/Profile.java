package clinic.cs213project3.model.clinic;
import clinic.cs213project3.model.util.Date;

/**
 * The object structure for a patient's profile.
 * @author Aarush Desai, Rohan Vuppunuthula
 */
public class Profile implements Comparable<Profile> {

    private static final int LESS_THAN = -1;
    private static final int EQUAL = 0;
    private static final int GREATER_THAN = 1;

    private String fname;
    private String lname;
    private Date dob;

    /**
     * Constructor creates a new profile object for a patient.
     * @param fname Patient's first name.
     * @param lname Patient's last name.
     * @param dob Patient's date of birth.
     */
    public Profile(String fname, String lname, Date dob) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
    }

    /**
     * Checks if two profiles are equivalent.
     * Profiles match if they share the same last, first names, and birthdays.
     * Last and first names are not case-sensitive.
     * @param obj The object being compared.
     * @return True if profiles are equal, else False.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Profile) {
            Profile profile = (Profile) obj;
            return fname.equalsIgnoreCase(profile.fname) &&
                    lname.equalsIgnoreCase(profile.lname) &&
                    dob.equals(profile.dob);
        }
        return false;
    }

    /**
     * Converts the profile information into a string format.
     * Format includes first name, last name, and birthday, separated by spaces.
     * @return The string representation of a profile.
     */
    @Override
    public String toString() {
        return fname + " "  + lname + " " + dob.toString();
    }

    /**
     * Defines the ordering of two profile objects.
     * Ordering is based on patients' last, first names, and birthdays.
     * Last and first names are not case-sensitive.
     * @param profile The profile that is being compared against.
     * @return -1 if object is less than profile, 1 if greater than, and 0 if equal.
     */
    @Override
    public int compareTo(Profile profile) {
        if (lname.compareToIgnoreCase(profile.lname) < 0) {
            return LESS_THAN;
        }
        else if (lname.compareToIgnoreCase(profile.lname) > 0) {
            return GREATER_THAN;
        }
        else if (fname.compareToIgnoreCase(profile.fname) < 0) {
            return LESS_THAN;
        }
        else if (fname.compareToIgnoreCase(profile.fname) > 0) {
            return GREATER_THAN;
        }
        else if (dob.compareTo(profile.dob) < 0) {
            return LESS_THAN;
        }
        else if (dob.compareTo(profile.dob) > 0) {
            return GREATER_THAN;
        }
        else {
            return EQUAL;
        }
    }

    /**
     * Getter method returns the first name of the profile.
     * @return First name of the profile object.
     */
    public String getFirstName() {
        return fname;
    }

    /**
     * Getter method returns the last name of the profile.
     * @return Last name of the profile object.
     */
    public String getLastName() {
        return lname;
    }

    /**
     * Getter method returns the date of birth of profile.
     * @return Date of birth of the profile object.
     */
    public Date getDob() {
        return dob;
    }

}