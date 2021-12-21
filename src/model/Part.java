package model;
/**
 * Supplied class model.Part.java
 */

/**
 *
 * @author Rychell Hayes
 */
public abstract class Part {
    private int id; //part id is index +1
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    public Part(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name of the part
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the minimum inventory
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the minimum inventory to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the maximum inventory
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the maximum inventory to set
     */
    public void setMax(int max) {
        this.max = max;
    }

}

