/**
 * JAVADOC LOCATION: /InventorySystem/javadoc/index.html
 *
 * FUTURE ENHANCEMENT
 * Have the program write the data to a file and read from that file
 * so that the data does not have to be recreated each time the program runs.
 * Also, add nextPage parameter to all nextPageError methods so the error message has more detail about the problem.
 * Take reused methods from each of the controllers, like nextPage, nextPageError, isInteger, isDouble, etc. and put them in a single new class.
 *
 * RUNTIME ERROR
 * When modifying a product, I had originally just updated the product's associated parts when the user clicked the add button.
 * I then realized that if the user decided to cancel, those changes to the associated parts would still be made to the product.
 * So I created a new product that was a copy of the current product, but I changed it's id to -1 so it would be easy for me to find and delete later.
 * Then I could change that new product's associated parts without touching the associated parts of the current project and if the user canceled,
 * the original product would not have been changed.  Then, I had an issue deleting the product should the user want to save the changes.
 * When I tried writing over the old product with the new product, I would either delete both the old and the new products, or just delete the
 * old product.  Because of my id numbering system, I really needed the new product to be at the same index as the old product.
 * To solve this problem, I decided to make another temporary copy of the new product and not add it to the products observable list so that its scope was limited,
 * then delete the new product I had created from the observable list, and then replace the old product with the temporary copy.
 * Since the temporary copy was not added to the observable list and could only be accessed in that method, I didn't have to worry about deleting it.
 */
package InventorySystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;

public class Main extends Application {

    /**
     *
     * @param primaryStage The primary stage
     */
    @Override
    public void start(Stage primaryStage)  {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../view/MainMenu.fxml"));
            primaryStage.setTitle("Inventory System");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
        catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot start because page does not exist.  Program will now close.");
        }
        catch (IOException e) {
            e.getMessage();
        }
    }

    /**
     * The main method. Creates initial data and launches the program.
     * @param args  Command line arguments
     */
    public static void main(String[] args) {
        InHouse part1 = new InHouse(1, "Brakes", 12.99, 10, 1, 20, 111 );
        Outsourced part2 = new Outsourced(2, "Tire", 14.99, 15, 1, 20, "Tires & Stuff");
        InHouse part3 = new InHouse(3, "Rims", 56.99, 10, 1, 20, 514 );
        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);

        Product product1 = new Product(1, "Bicycle", 499.99, 15, 1, 20);
        Product product2 = new Product(2, "Tricycle", 59.99, 13, 1, 20);
        Inventory.addProduct(product1);
        Inventory.addProduct(product2);

        product1.addAssociatedPart(part1);
        product2.addAssociatedPart(part2);
        product2.addAssociatedPart(part3);

        launch(args);
    }
}