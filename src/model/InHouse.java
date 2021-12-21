package model;

public class InHouse extends Part {
    private int machineId;

    /**
     * The constructor sets part ID, part name, part price,
     * quantity in stock, minimum, maximum and manufacturing company
     * @param id The part ID
     * @param name The part name
     * @param price The price of the part
     * @param stock The quantity in stock
     * @param min The minimum quantity
     * @param max The maximum quantity
     * @param machineId The machine that manufactured part
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId)
    {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Sets the part's machineId.
     * @param machineId The machine id integer to set
     */
    public void setMachineId(int machineId)
    {
        this.machineId = machineId;
    }

    /**
     * Returns the part's machineId.
     * @return The machine id integer
     */
    public int getMachineId()
    {
        return machineId;
    }

}
