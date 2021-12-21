package model;

public class Outsourced extends Part {
    private String companyName;

    /**
     * The constructor sets part ID, part name, part price,
     * quantity in stock, minimum, maximum and manufacturing company
     * @param id The part ID
     * @param name The part name
     * @param price The price of the part
     * @param stock The quantity in stock
     * @param min The minimum quantity
     * @param max The maximum quantity
     * @param companyName The manufacturing company
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName)
    {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }
    /**
     * Sets the part's companyName.
     * @param companyName The company name string to set
     */
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    /**
     * Returns the part's company name.
     * @return The company name string
     */
    public String getCompanyName()
    {
        return companyName;
    }
}
