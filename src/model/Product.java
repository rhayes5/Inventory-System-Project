//what if deleteAssociatedPart does not work?
package model;
/**
 * Class model.Product.java
 */

/**
 *
 * @author Rychell Hayes
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Part;

/**
 * This class creates a product object.
 */
public class Product {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * The constructor sets product ID, product name, product price,
     * quantity of product in stock, minimum quantity of product, and maximum quantity of product.
     * @param id The product ID
     * @param name The product name
     * @param price The price of the product
     * @param stock The quantity of product in stock
     * @param min The minimum quantity of product
     * @param max The maximum quantity of product
     */
    public Product(int id, String name, double price, int stock, int min, int max)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Sets the product's id number.
     * @param id  The id number to set
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Sets the product's name.
     * @param name The product name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Sets the product's price.
     * @param price The product's price to set
     */
    public void setPrice(double price)
    {
        this.price = price;
    }

    /**
     * Sets the stock or inventory of the product.
     * @param stock The amount of product in stock
     */
    public void setStock(int stock)
    {
        this.stock = stock;
    }

    /**
     * Sets the minimum amount of product inventory required.
     * @param min The minimum amount of product
     */
    public void setMin(int min)
    {
        this.min = min;
    }
    /**
     * Sets the maximum amount of product inventory required.
     * @param max The maximum amount of product
     */
    public void setMax(int max)
    {
        this.max = max;
    }

    /**
     * Gets the product's id.
     * @return the product's id
     */
    public int getId()
    {
        return id;
    }

    /**
     * Gets the product's name
     * @return the product's name
     */
    public String getName()
    {
        return name;
    }
    /**
     * Gets the product's price
     * @return the product's price
     */
    public double getPrice()
    {
        return price;
    }
    /**
     * Gets the amount of inventory in stock
     * @return the product's stock/inventory
     */
    public int getStock()
    {
        return stock;
    }
    /**
     * Gets the product's minimum required stock
     * @return the product's minimum stock
     */
    public int getMin()
    {
        return min;
    }
    /**
     * Gets the product's maximum required stock
     * @return the product's maximum stock
     */
    public int getMax()
    {
        return max;
    }

    /**
     * Adds associated parts to the product.
     * @param part the part to be associated to product
     */
    public void addAssociatedPart(Part part)
    {
        associatedParts.add(part);
    }

    /**
     * Deletes an associated part from the list of the product's associated parts
     * @param selectedAssociatedPart the associated part that is selected
     * @return true if the selected associated part was removed; false if the product was not removed
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart)
    {
        return associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * Gets the list of the product's associated parts
     * @return the list of associated parts
     */
    public ObservableList<Part> getAllAssociatedParts()
    {
        return associatedParts;
    }

}
