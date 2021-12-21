package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Part;
import model.Product;

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Adds a new part to the parts observable list
     * @param newPart New part to be added
     */
    public static void addPart(Part newPart)
    {
        allParts.add(newPart);
    }

    /**
     * Adds a new product to the product observable list
     * @param newProduct New product to be added
     */
    public static void addProduct(Product newProduct)
    {
        allProducts.add(newProduct);
    }

    /**
     * Looks up a part with the given id number. Returns the part if found or null if not found.
     * @param partId The part id to look up
     * @return The part if the part was found or null if not found.
     */
    public static Part lookupPart(int partId)
    {

        for (Part part : getAllParts())
        {
            if (part.getId() == partId)
                return part;
        }
        return null;
    }

    /**
     * Looks up a product with the given id number. Returns the product if found or null if not found.
     * @param productID The product id to look up
     * @return The product if the product was found or null if not found.
     */
    public static Product lookupProduct(int productID)
    {
        for (Product product : getAllProducts())
        {
            if (product.getId() == productID)
                return product;
        }
        return null;
    }

    /**
     * Looks up a part whose name contains the given string.
     * Converts search string and part names to lower case so that the search is not case sensitive.
     * Returns an observable list of parts that contain the string.
     * @param partName The string to search for
     * @return An observable list of parts whose names contain the string
     */
    public static ObservableList<Part> lookupPart(String partName)
    {
        ObservableList<Part> foundParts = FXCollections.observableArrayList();

        partName = partName.toLowerCase();
        for (Part part : Inventory.getAllParts()) {
            if (part.getName().toLowerCase().contains(partName)) {
                foundParts.add(part);
            }
        }
        return foundParts;
    }

    /**
     * Looks up a products whose name contains the given string.
     * Converts search string and products names to lower case so that the search is not case sensitive.
     * Returns an observable list of products that contain the string.
     * @param productName The string to search for
     * @return An observable list of products whose names contain the string
     */
    public static ObservableList<Product> lookupProduct(String productName)
    {
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();

        productName =productName.toLowerCase();
        for (Product product : Inventory.getAllProducts()) {
            if (product.getName().toLowerCase().contains(productName)) {
                foundProducts.add(product);
            }
        }
        return foundProducts;
    }

    /**
     * Updates the part in the observable list.
     * @param index The index of the part to be updated
     * @param selectedPart The updated part that will replace the existing part
     */
    public static void updatePart(int index, Part selectedPart)
    {
        allParts.set(index, selectedPart);
    }

    /**
     * Updates the product in the observable list.
     * @param index The index of the product being updated
     * @param newProduct The updated product that will replace the existing product
     */
    public static void updateProduct(int index, Product newProduct)
    {
        allProducts.set(index, newProduct);
    }

    /**
     * Deletes the part from the observable list.
     * @param selectedPart The part that will be deleted
     * @return Returns true if deleted or false if not deleted
     */
    public static boolean deletePart(Part selectedPart)
    {
        return allParts.remove(selectedPart);
    }

    /**
     * Deletes the product from the observable list.
     * @param selectedProduct The product that will be deleted
     * @return Returns true if deleted or false if not deleted
     */
    public static boolean deleteProduct(Product selectedProduct)
    {
        return allProducts.remove(selectedProduct);
    }

    /**
     * Returns the observable list of all parts
     * @return All parts in the observable list
     */
    public static ObservableList<Part> getAllParts()
    {
        return allParts;
    }

    /**
     * Returns the observable list of all products
     * @return All products in the observable list
     */
    public static ObservableList<Product> getAllProducts()
    {
        return allProducts;
    }

}
