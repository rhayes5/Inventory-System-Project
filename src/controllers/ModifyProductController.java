package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyProductController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField searchTextField;

    @FXML
    private TableView<Part> partsTable;

    @FXML
    private TableColumn<Part, Integer> partIdColumn;

    @FXML
    private TableColumn<Part, String> partNameColumn;

    @FXML
    private TableColumn<Part, Integer> invColumn;

    @FXML
    private TableColumn<Part, Double> priceColumn;

    @FXML
    private Button addBtn;

    @FXML
    private TableView<Part> addedPartsTable;

    @FXML
    private TableColumn<Part, Integer> addedPartIdColumn;

    @FXML
    private TableColumn<Part, String> addedPartNameColumn;

    @FXML
    private TableColumn<Part, Integer> addedInvColumn;

    @FXML
    private TableColumn<Part, Double> addedPriceColumn;

    @FXML
    private Button removeAssociatedPartBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField invTextField;

    @FXML
    private TextField maxTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField minTextField;

    /**
     * Adds associated parts to the temporary new product and displays them in the associated parts table
     * @param event The Add button is clicked
     */
    @FXML
    void onActionAddAssociatedPart(ActionEvent event) {
        Product updatedProduct = Inventory.lookupProduct(-1);
        updatedProduct.addAssociatedPart(partsTable.getSelectionModel().getSelectedItem());
    }

    /**
     * Removes associated parts from the temporary new product and removes them from the associated parts table.
     * Displays alert making sure user wants to delete the associated part.
     * @param event The Remove Associated Part button is clicked
     */
    @FXML
    void onActionRemoveAssociatedPart(ActionEvent event) {
        Product updatedProduct = Inventory.getAllProducts().get(Inventory.getAllProducts().size() - 1);
        //make sure the product has associated parts, display error message if not
        if (updatedProduct.getAllAssociatedParts().isEmpty()) {
            Alert noAssociatedPartsAlert = new Alert(Alert.AlertType.ERROR, "There are no associated parts to remove from this product.");
            noAssociatedPartsAlert.showAndWait();
        }
        else
        { //make sure user selected a part in the associated parts table, then make sure they want to delete the associated part
            if (addedPartsTable.getSelectionModel().getSelectedItem() != null)
            {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to remove this associated part?", ButtonType.YES, ButtonType.CANCEL);
                alert.setTitle("Confirm Remove Association");
                Optional<ButtonType> clickButton = alert.showAndWait();
                //if the user clicks yes, delete the associate part and remove from table
                if (clickButton.isPresent() && clickButton.get() == ButtonType.YES)
                {
                    boolean removed = updatedProduct.deleteAssociatedPart(addedPartsTable.getSelectionModel().getSelectedItem());
                    if (removed == true) { //if part was removed
                        Alert alertSuccess = new Alert(Alert.AlertType.CONFIRMATION, "Associated part successfully removed.", ButtonType.OK);
                        alertSuccess.showAndWait();
                    } else { //if part was not removed
                        Alert removeAlert = new Alert(Alert.AlertType.ERROR, "Unable to remove associated part.", ButtonType.OK);
                        removeAlert.showAndWait();
                    }
                }
            }
            else { //if the user did not select a part in the table, display error message
                Alert noSelectionAlert = new Alert(Alert.AlertType.ERROR, "Please select an associated part to remove.", ButtonType.OK);
                noSelectionAlert.showAndWait();
            }
        }
    }

    /**
     * Replaces the old product with the updated product.  Deletes the new product created to temporarily hold the updated information.
     * @param event The Save button is clicked
     * @throws IOException Displays alerts if id, stock, min, and max fields are not integers;
     * price is not a double; min is not less than max or stock is not between min and max;
     * or displays alert and closes the program if the next window cannot be opened.
     */
    @FXML
    void onActionSaveProduct(ActionEvent event) throws IOException {

        try {
            //find original object, copy new one over it, then delete new product
            int id = Integer.parseInt(idField.getText());
            int index = Inventory.getAllProducts().indexOf(Inventory.lookupProduct(id));
            String name = nameField.getText();
            double price = Double.parseDouble(priceTextField.getText());
            int stock = Integer.parseInt(invTextField.getText());
            int min = Integer.parseInt(minTextField.getText());
            int max = Integer.parseInt(maxTextField.getText());

            //create new part but don't add to inventory
            Product newProduct = new Product(id, name, price, stock, min, max);
            for (Part part : Inventory.lookupProduct(-1).getAllAssociatedParts())
            {
                newProduct.addAssociatedPart(part);
            }
            //delete the new Object created to hold the associated Parts
            Inventory.deleteProduct(Inventory.lookupProduct(-1));

            //update the original product
            Inventory.updateProduct(index, newProduct);

            sceneChange(event, "/view/MainMenu.fxml");
        }
        catch(NullPointerException e)
        {
            nextPageError();
        }
        catch(NumberFormatException nfe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");

            if (!isDouble(priceTextField.getText())) {
                alert.setContentText("Please enter an decimal value for price.");
            } else if (!isInteger(invTextField.getText())) {
                alert.setContentText("Please enter an integer value for inventory.");
            } else if (!isInteger(maxTextField.getText())) {
                alert.setContentText("Please enter an integer value for max.");
            } else if (!isInteger(minTextField.getText())) {
                alert.setContentText("Please enter an integer value for min.");
            } else {
                alert.setContentText("Please enter valid values for each text field.");
            }
            alert.showAndWait();
        }
    }

    /**
     * Displays an error message if unable to bring up next window and closes program.
     */
    public void nextPageError()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Error retrieving next page.  Program will now close.");
        alert.showAndWait();
        System.exit(1);
    }

    /**
     * Returns to the main menu without updating the product and deletes the temporary product created to hold updated information.
     * @param event The cancel button was clicked
     * @throws IOException Displays an error message if unable to return to main menu and closes program.
     */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        try {
            Inventory.deleteProduct(Inventory.lookupProduct(-1));
            sceneChange(event, "/view/MainMenu.fxml");
        }
        catch(NullPointerException  e) {
            nextPageError();
        }
    }

    /**
     * When user types a key in the part search field, searches for and displays the part(s) that contain the entered string.
     * If no part matches, displays an empty table. If search field is empty, displays all parts.
     * @param event The key typed event
     */
    @FXML
    void onKeyTypedPartSearch(KeyEvent event) {
        String partSearch = searchTextField.getText();

        if (partSearch != null)
        {
            if (partSearch(partSearch).isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Part not found.");
                alert.showAndWait();
                searchTextField.clear();
            }
            else {
                partsTable.setItems(partSearch(partSearch));
                partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                invColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
                priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            }
        }
    }

    /**
     * Fills the text fields with current values for the product and creates a new temporary product to hold updated associated parts.
     * Calls fillAssociatedPartsTable method to fill associated part table.
     * @param product The product that was selected to be updated
     */
    public void getCurrentProductInfo(Product product) {
        //set text fields with current values
        idField.setText(String.valueOf(product.getId()));
        nameField.setText(product.getName());
        invTextField.setText(String.valueOf(product.getStock()));
        priceTextField.setText(String.valueOf(product.getPrice()));
        minTextField.setText(String.valueOf(product.getMin()));
        maxTextField.setText(String.valueOf(product.getMax()));

        //create new product object to store updates, use id -1 to make it easy to find with product lookup
        Product newProduct = new Product(-1, product.getName(), product.getPrice(), product.getStock(), product.getMin(), product.getMax());
        //copy associated parts to newProduct
        for (Part part : product.getAllAssociatedParts()) {
            newProduct.addAssociatedPart(part);
        }
        //add newProduct to inventory
        Inventory.addProduct(newProduct);

        fillAssociatedPartTable(newProduct);
    }

    /**
     * Fills the associated parts table with product's current associated parts
     * @param newProduct The product being updated
     */
    public void fillAssociatedPartTable(Product newProduct)
    {
        addedPartsTable.setItems(newProduct.getAllAssociatedParts());
        addedPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        addedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addedInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addedPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Loads the next window based on the button pressed.
     * @param event The button press event
     * @param nextPage The address string of the next window to display
     * @throws IOException Alerts user if cannot find nextPage and closes program
     */
    public void sceneChange(ActionEvent event, String nextPage) throws IOException
    {
        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource(nextPage));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NullPointerException e)
        {
            nextPageError();
        }
    }

    /**
     * Searches through the list of parts for names that contain the search string.
     * If the string entered was an integer, the part with the matching id number.
     * If the string entered was not an integer, returns a list of parts whose names contain the searched string.
     * @param search The string entered in the SearchTextField
     * @return
     */
    private ObservableList<Part> partSearch(String search) {
        ObservableList<Part> foundParts = FXCollections.observableArrayList();

        //check if input was an integer
        if (isInteger(search)) {
            int searchedInt = Integer.parseInt(search);
            //search through the part observable list for that part id
            foundParts.add(Inventory.lookupPart(searchedInt));
        }
        //if input was not an integer
        else {
            foundParts = Inventory.lookupPart(search);
        }

        //return the new filtered observable list
        return foundParts;
    }

    /**
     * Checks if input string is an integer.
     * @param search The string to check
     * @return true if the string is an integer or false if not
     */
    public boolean isInteger(String search) {
        try {
            Integer.parseInt(search);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /**
     * Checks if input string is a double.
     * @param search The string to check
     * @return true if the string is an double or false if not
     */
    public boolean isDouble(String search) {
        try {
            Double.parseDouble(search);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Fills the parts table.
     * @param url The URL
     * @param rb The resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //fill upper parts table
        partsTable.setItems(Inventory.getAllParts());
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        invColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

}
