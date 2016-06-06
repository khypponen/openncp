package epsos.ccd.gnomon.cda.parser.beans;

/**
 * A class encapsulating a generic address.
 *
 * @author Akis Papadopoulos
 *
 */
public class Address {

    // Address usage
    private String use;
    // Street line and number
    private String street;
    // City name
    private String city;
    // Postal code of the address
    private String postal;
    // State name
    private String state;
    // Country ISO code
    private String country;

    /**
     * A constructor initializing an address.
     */
    public Address() {
        // Setting a null use
        this.use = null;

        // Setting a null street line
        this.street = null;

        // Seeting a null city of the address
        this.city = null;

        // Setting a null postal of the address
        this.postal = null;

        // Setting a null state of the address
        this.state = null;

        // Setting a null country
        this.country = null;
    }

    /**
     * A constructor initializing an address.
     *
     * @param street the street line of the address.
     * @param city the city of the address.
     * @param postal the postal code of the address.
     * @param country the country of the address.
     */
    public Address(String street, String city, String postal, String country) {
        // Setting the street line
        this.street = street;

        // Seeting the city of the address
        this.city = city;

        // Setting the postal of the address
        this.postal = postal;

        // Setting the country
        this.country = country;
    }

    /**
     * A method returning the usage of the address.
     *
     * @return the usage of the address.
     */
    public String getUse() {
        return use;
    }

    /**
     * A method setting the usage of the address.
     *
     * @param use the usage to set.
     */
    public void setUse(String use) {
        this.use = use;
    }

    /**
     * A method returning the street of the address.
     *
     * @return the street of the address.
     */
    public String getStreet() {
        return street;
    }

    /**
     * A method setting the street of the address.
     *
     * @param street the street line to set.
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * A method returning the city of the address.
     *
     * @return the city of the address.
     */
    public String getCity() {
        return city;
    }

    /**
     * A method setting the city of the address.
     *
     * @param city the city to set.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * A method returning the postal code of the address.
     *
     * @return the postal code of the address.
     */
    public String getPostal() {
        return postal;
    }

    /**
     * A method setting the postal of the address.
     *
     * @param postal the postal to set.
     */
    public void setPostal(String postal) {
        this.postal = postal;
    }

    /**
     * A method returning the state of the address.
     *
     * @return the state of the address.
     */
    public String getState() {
        return state;
    }

    /**
     * A method setting the state of the address.
     *
     * @param state the state to set.
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * A method returning the country of the address.
     *
     * @return the country of the address.
     */
    public String getCountry() {
        return country;
    }

    /**
     * A method setting the country of the address.
     *
     * @param country the country to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * A parsing method transforms object data into a text-based XML format.
     *
     * @return an XML representation of the object.
     */
    @Override
    public String toString() {
        // Creating a context holder
        StringBuilder context = new StringBuilder();

        // Checking if the address is null flavored
        if (street == null && city == null && postal == null && state == null && country == null) {
            // Adding a null flavored address element
            context.append("<addr nullFlavor=\"NI\"/>");
        } else {
            // Opening the address element
            context.append("<addr");

            // Checking if the usage is provided
            if (use != null) {
                context.append(" ")
                        .append("use=\"")
                        .append(use)
                        .append("\"");
            }

            // Closing the address opening element
            context.append(">");

            // Checking if the street line and number is provided
            if (street != null) {
                // Adding the street line element
                context.append("<streetAddressLine>")
                        .append(street)
                        .append("</streetAddressLine>");
            } else {
                // Adding a null flavored street line element
                context.append("<streetAddressLine nullFlavor=\"UNK\"/>");
            }

            // Checking if the city name is provided
            if (city != null) {
                // Adding the city element
                context.append("<city>")
                        .append(city)
                        .append("</city>");
            } else {
                // Adding a null flavored city element
                context.append("<city nullFlavor=\"UNK\"/>");
            }

            // Checking if the postal code is provided
            if (postal != null) {
                // Adding the postal code element
                context.append("<postalCode>")
                        .append(postal)
                        .append("</postalCode>");
            } else {
                // Adding a null flavored postal code element
                context.append("<postalCode nullFlavor=\"UNK\"/>");
            }

            // Checking if the state name is provided
            if (state != null) {
                // Adding the state element
                context.append("<state>")
                        .append(state)
                        .append("</state>");
            } else {
                // Adding a null flavored state element
                context.append("<state nullFlavor=\"UNK\"/>");
            }

            // Checking if the country code is provided
            if (country != null) {
                // Adding the country element
                context.append("<country>")
                        .append(country)
                        .append("</country>");
            } else {
                // Adding a null flavored country element
                context.append("<country nullFlavor=\"UNK\"/>");
            }

            // Closing the address element
            context.append("</addr>");
        }

        return context.toString();
    }
}
