package clinic.cs213project3.model.clinic;

/**
 * The general class structure for a Person object.
 * A person can include providers and patients of the clinic.
 * @author Aarush Desai, Rohan Vuppunuthula
 */
public class Person implements Comparable<Person> {

    protected Profile profile;

    /**
     * Constructor creates a new Person object.
     * @param profile The person's profile.
     */
    public Person(Profile profile) {
        this.profile = profile;
    }

    /**
     * Checks if two people are the same.
     * Two people with the same profiles are considered the same.
     * @param obj The object being compared.
     * @return True if profiles are equal, else False.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            Person person = (Person) obj;
            return profile.equals(person.profile);
        }
        return false;
    }

    /**
     * Defines the ordering of two person objects.
     * Ordering is based purely on the person profiles.
     * @param person The person that is being compared against.
     * @return -1 if object is less than person, 1 if greater than, and 0 if equal.
     */
    @Override
    public int compareTo(Person person) {
        return profile.compareTo(person.profile);
    }

    /**
     * Converts the person's information into a string format.
     * Format of the person is exactly the same as its profile.
     * @return The string representation of a person.
     */
    @Override
    public String toString() {
        return profile.toString();
    }

    /**
     * Getter method returns the person's profile.
     * @return The profile.
     */
    public Profile getProfile() {
        return profile;
    }

}