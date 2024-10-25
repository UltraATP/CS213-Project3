package clinic.cs213project3;

/**
 * The template class used to display billing/credit data onto the table view in the UI.
 * The table view under the "Funds" tab contains two columns, corresponding to each data field.
 * Date fields include person (patient/provider), and fund (billing/credit).
 * @author Aarush Desai
 */
public class FundView {

    private final String person;
    private final String fund;

    /**
     * The constructor for a viewable billing/credit object.
     * @param person Either a patient or provider.
     * @param fund Either the patient's bill, or provider's credit.
     */
    public FundView(String person, String fund) {
        this.person = person;
        this.fund = fund;
    }

    /**
     * Getter method returns the patient/provider.
     * @return The patient/provider person.
     */
    public String getPerson() {
        return person;
    }

    /**
     * Getter method returns the bill/credit.
     * @return The bill/credit.
     */
    public String getFund() {
        return fund;
    }

}