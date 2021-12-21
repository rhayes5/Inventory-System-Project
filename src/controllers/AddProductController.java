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

public class AddProductController implements Initializable {

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
     * When the add button is clicked, the selected part is added to the associated part list and the associated part table is updated.
     * @param event The add button being clicked
     */
    @FXML
    void onActionAddAssociatedPart(ActionEvent event) {
        int id = Integer.parseInt(idField.getText());
        Product updatedProduct = Inventory.lookupProduct(id);
        updatedProduct.addAssociatedPart(partsTable.getSelectionModel().getSelectedItem());
        fillAssociatedPartTable(updatedProduct);
    }

    /**
     * When the Removed Associated Part button is clicked, the selected part is removed from the associated part list and the associated part table is updated.
     * @param event The Remove Associated Part button being clicked
     */
    @FXML
    void onActionRemoveAssociatedPart(ActionEvent event) {
        int id = Integer.parseInt(idField.getText());
        Product updatedProduct = Inventory.lookupProduct(id);
        //make sure the product has associated parts, display error message if not
        if (updatedProduct.getAllAssociatedParts().isEmpty())
        {
            Alert noAssociatedPartsAlert = new Alert(Alert.AlertType.ERROR, "There are no associated parts to remove from this product.");
            noAssociatedPartsAlert.showAndWait();
        }
        else {
            //make sure user selected a part in the associated parts table, then make sure they want to delete the associated part
            if (addedPartsTable.getSelectionModel().getSelectedItem() != null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to remove this associated part?", ButtonType.YES, ButtonType.CANCEL);
                alert.setTitle("Confirm Remove Association");
                Optional<ButtonType> clickButton = alert.showAndWait();
                //if the user clicks yes, delete the associate part and remove from table
                if (clickButton.isPresent() && clickButton.get() == ButtonType.YES)
                {
                    boolean removed = updatedProduct.deleteAssociatedPart(addedPartsTable.getSelectionModel().getSelectedItem());
                    if (removed == true) {//if part was deleted
                        Alert alertSuccess = new Alert(Alert.AlertType.CONFIRMATION, "Associated part successfully removed.", ButtonType.OK);
                        alertSuccess.showAndWait();
                    } else { //if part was not deleted
                        Alert removeAlert = new Alert(Alert.AlertType.ERROR, "Unable to remove associated part.", ButtonType.OK);
                        removeAlert.showAndWait();
                    }
                }
            } else { //if the user did not select a part in the table, display error message
                Alert noSelectionAlert = new Alert(Alert.AlertType.ERROR, "Please select an associated part to remove.", ButtonType.OK);
                noSelectionAlert.showAndWait();
            }
        }
    }

    /**
     * The product is updated with the user entered information when the save button is clicked. The user is returned to the main menu.
     * @param event The save button is pressed
     */
    @FXML
    void onActionSaveProduct(ActionEvent event) {

        int id = Integer.parseInt(idField.getText());
        Product newProduct = Inventory.lookupProduct(id);
        //update new product with entered information
        try {
            newProduct.setName(nameField.getText());
            newProduct.setPrice(Double.parseDouble(priceTextField.getText()));
            newProduct.setStock(Integer.parseInt(invTextField.getText()));
            newProduct.setMin(Integer.parseInt(minTextField.getText()));
            newProduct.setMax(Integer.parseInt(maxTextField.getText()));

            sceneChange(event, "/view/MainMenu.fxml");
        }
        catch(NumberFormatException nfe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");

            if (!isInteger(invTextField.getText()))
            {
                alert.setContentText("Please enter an integer value for inventory.");
            }
            else if (!isDouble(priceTextField.getText()))
            {
                alert.setContentText("Please enter an decimal value for price.");
            }
            else if (!isInteger(maxTextField.getText()))
            {
                alert.setContentText("Please enter an integer value for max.");
            }
            else if (!isInteger(minTextField.getText()))
            {
                alert.setContentText("Please enter an integer value for min.");
            }
            else
                alert.setContentText("Please enter valid values for each text field.");

            alert.showAndWait();
        }
        catch(NullPointerException e)
        {
            nextPageError("/view/MainMenu.fxml");
        } catch (IOException ioe)
        {
            ioe.getMessage();
        }
        }

    /**
     * The new product is deleted and the user is returned to the main menu.
     * @param event The cancel button is pressed
     * @throws IOException Displays an error message if unable to find next window and closes program.
     */
    @FXML
    void onActionCancel(ActionEvent event) {
        String nextPage = "/view/MainMenu.fxml";
        try {
            Inventory.deleteProduct(Inventory.lookupProduct(Integer.parseInt(idField.getText())));
            sceneChange(event, nextPage);

        } catch (NullPointerException e) { nextPageError(nextPage);
        } catch (IOException ioe)
        {
            ioe.getMessage();
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
     * Fills the associated parts table with the product's associated parts.
     * @param product The product
     */
    public void fillAssociatedPartTable(Product product)
    {
        addedPartsTable.setItems(product.getAllAssociatedParts());
        addedPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        addedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addedInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addedPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Changes the scene when a button is pressed
     * @param event The button press event
     * @param nextPage The next page that will be displayed
     * @throws IOException Displays an error message if unable to find next window and closes program.
     */
    public void sceneChange(ActionEvent event, String nextPage) throws IOException
    {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource(nextPage));
            stage.setScene(new Scene(scene));
            stage.show();
    }

    /**
     * Displays an error message if unable to bring up next window and closes the program.
     * @param nextPage The next window to open
     */
    public void nextPageError(String nextPage)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Error retrieving next page " + nextPage +".  Program will now close.");
        alert.showAndWait();
        System.exit(1);
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
            foundParts =  Inventory.lookupPart(search);
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
     * Creates a new product,fills in the ID text field with the new product's auto-generated product number, and fills the parts table.
     * @param url The URL
     * @param rb The resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        int index = Inventory.getAllProducts().size() - 1;
        int id;
        if (index == -1)
        {
            id = 1;
        }
        else
        {
            int lastId = Inventory.getAllProducts().get(index).getId();
            id = lastId + 1;
        }
        String name = null;
        double price = 0.0;
        int stock = 0;
        int min = 0;
        int max = 0;
        Product newProduct = new Product(id, name, price, stock, min, max);
        //fill the id text field for the new product
        idField.setText(String.valueOf(id));

        Inventory.addProduct(newProduct);

        //fill upper parts table
        partsTable.setItems(Inventory.getAllParts());
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        invColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
