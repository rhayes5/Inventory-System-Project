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
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TableView<Part> partsTable;

    @FXML
    private TableColumn<Part, Integer> partIDColumn;

    @FXML
    private TableColumn<Part, String> partNameColumn;

    @FXML
    private TableColumn<Part, Integer> partInvLevelColumn;

    @FXML
    private TableColumn<Part, Double> partPriceColumn;

    @FXML
    private TextField partSearchField;

    @FXML
    private Button addPartBtn;

    @FXML
    private Button modifyPartBtn;

    @FXML
    private Button deletePartBtn;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, Integer> productIdColumn;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, Integer> productInvLevelColumn;

    @FXML
    private TableColumn<Product, Double> productPriceColumn;

    @FXML
    private TextField productSearchField;

    @FXML
    private Button addProductBtn;

    @FXML
    private Button modifyProductBtn;

    @FXML
    private Button deleteProductBtn;

    @FXML
    private Button exitBtn;

    /**
     * When user types a key in the part search field, searches for and displays the part(s) that contain the entered string.
     * If no part matches, displays an empty table. If search field is empty, displays all parts.
     * @param event The key typed event
     */
    @FXML
    void onKeyTypedPartSearch(KeyEvent event) {
        String partSearch = partSearchField.getText();
        //if the search field is not empty
        if (partSearch != null)
        {
            if (partSearch(partSearch).isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Part not found.");
                alert.showAndWait();
                partSearchField.clear();
            }
            else
            {
                partsTable.setItems(partSearch(partSearch));
                partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                partInvLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
                partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            }
        }
    }

    /**
     * Searches through the list of parts for names that contain the search string.
     * If the string entered was an integer, the part with the matching id number.
     * If the string entered was not an integer, returns a list of parts whose names contain the searched string.
     * @param search the string the user entered to search
     * @return returns an observable list of parts whose name contains the search string or whose id matches the entered integer
     */
    private ObservableList<Part> partSearch(String search) {
        ObservableList<Part> foundParts = FXCollections.observableArrayList();

        //check if input was an integer
        if (isInteger(search))
        {
            int searchedInt = Integer.parseInt(search);
            //search through the part observable list for that part id
            foundParts.add(Inventory.lookupPart(searchedInt));
        }
        //if input was not an integer
        else
        {
            foundParts =  Inventory.lookupPart(search);
        }

        //return the new filtered observable list
        return foundParts;
    }

    /**
     * When user types a key in the product search field, searches for and displays the product(s) that contain the entered string.
     * If no product matches, displays an empty table. If search field is empty, displays all products.
     * @param event The key typed event
     */
    @FXML
    void onKeyTypedProductSearch(KeyEvent event) {
        String productSearch = productSearchField.getText();

        if (productSearch != null)
        {
            if (productSearch(productSearch).isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Product not found.");
                alert.showAndWait();
                productSearchField.clear();
            }
            else {
                productTable.setItems(productSearch(productSearch));
                productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                productInvLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
                productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            }
        }
    }

    /**
     * Searches through the list of products for names that contain the search string.
     * If the string entered was an integer, the product with the matching id number.
     * If the string entered was not an integer, returns a list of products whose names contain the searched string.
     * @param search The string the user entered in the productSearchField
     * @return Returns an observable list of products whose name contains the search string or whose id matches the entered integer
     */
    private ObservableList<Product> productSearch(String search) {
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();

        //check if input is an integer
        if (isInteger(search))
        {
            int searchedInt = Integer.parseInt(search);

            //search through the part observable list for that part id and add it to foundProducts
            foundProducts.add(Inventory.lookupProduct(searchedInt));
        }
        else //if input was not an integer
        {
            foundProducts =  Inventory.lookupProduct(search);
        }
        return foundProducts;
    }

    /**
     * Checks if input string is an integer.
     * @param search The string to check
     * @return Returns true if the string is an integer or false if not
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
     * Opens AddPart window when user clicks 'Add' under Part
     * @param event Add button under parts clicked
     * @throws IOException throws exception if null pointer
     */
    @FXML
    void onActionAddPart(ActionEvent event) throws IOException {
        try {
            sceneChange(event, "/view/AddPart.fxml");
        } catch(NullPointerException e) {
            nextPageError();
        }
    }

    /**
     * Opens ModifyPart window when user clicks 'Modify' under Part
     * @param event Modify button under parts clicked
     * @throws IOException throws exception if null pointer
     */
    @FXML
    void onActionModifyPart(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyPart.fxml"));
            loader.load();

            ModifyPartController mpc = loader.getController();
            mpc.getCurrentPartInfo(partsTable.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NullPointerException e)
        {
            Alert alertConfirm = new Alert(Alert.AlertType.WARNING, "Please select the part you would like to modify.", ButtonType.OK);
            alertConfirm.setTitle("Select Part to Modify");
            alertConfirm.showAndWait();
        }
    }

    /**
     * Deletes the selected part.  Sends an error alert if no part selected.
     * Sends warning alert to make sure user wanted to delete the part.
     * Sends confirmation alert to inform user that part was deleted.
     * @param event Delete button clicked event under parts
     */
    @FXML
    void onActionDeletePart(ActionEvent event) {

        //make sure user selected a part
        if (partsTable.getSelectionModel().getSelectedItem() != null)
            {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete this part?", ButtonType.YES, ButtonType.CANCEL);
            alert.setTitle("Confirm Delete");
            Optional<ButtonType> clickButton = alert.showAndWait();

            if (clickButton.isPresent() && clickButton.get() == ButtonType.YES) {
                boolean deleted = Inventory.deletePart(partsTable.getSelectionModel().getSelectedItem());
                if (deleted == false) {
                    Alert deleteAlert = new Alert(Alert.AlertType.ERROR, "Could not delete selected part.");
                    alert.showAndWait();
                } else {
                    Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION, "Part was successfully deleted.", ButtonType.OK);
                    alertConfirm.setTitle("Deleted");
                    alertConfirm.showAndWait();
                }
            }
        }
        else
        {
            Alert selectionAlert = new Alert(Alert.AlertType.ERROR, "Please select a part to delete.", ButtonType.OK);
            selectionAlert.showAndWait();
        }
    }

    /**
     * Opens AddProduct window when user clicks 'Add' under Product
     * @param event Add button under product clicked
     * @throws IOException throws exception if null pointer
     */
    @FXML
    void onActionAddProduct(ActionEvent event) throws IOException {
        try {
            sceneChange(event, "/view/AddProduct.fxml");
        }
        catch(NullPointerException npe)
        {
            nextPageError();
        }
    }

    /**
     * Opens ModifyProduct window when user clicks 'Modify' under Product
     * @param event Modify button under products clicked
     */
    @FXML
    void onActionModifyProduct(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyProduct.fxml"));
            loader.load();

            ModifyProductController MPC = loader.getController();
            MPC.getCurrentProductInfo(productTable.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NullPointerException npe)
        {
            Alert alertConfirm = new Alert(Alert.AlertType.WARNING, "Please select the product you would like to modify.", ButtonType.OK);
            alertConfirm.setTitle("Select Product");
            alertConfirm.showAndWait();
        } catch (IOException e)
        {
            e.getMessage();
        }
    }

    /**
     * Deletes the selected product. Sends an error alert if no product selected.
     * Sends warning alert to make sure user wants to delete the product.
     * Sends alert to tell user to remove attached parts before deleting if any exist.
     * Sends confirmation alert to inform user that product was deleted.
     * @param event Delete button clicked event under products
     */
    @FXML
    void onActionDeleteProduct(ActionEvent event) {
        //make sure user selected a product
        if (productTable.getSelectionModel().getSelectedItem() != null) {
            //make sure user wants to delete product
            Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete this product?", ButtonType.YES, ButtonType.CANCEL);
            alert.setTitle("Confirm Delete");
            Optional<ButtonType> clickButton = alert.showAndWait();

            //when user clicks yes button, move forward with delete, else stop
            if (clickButton.isPresent() && clickButton.get() == ButtonType.YES) {
                int id = productTable.getSelectionModel().getSelectedItem().getId();
                //makes sure there are no associated parts so it can be deleted
                if (Inventory.lookupProduct(id).getAllAssociatedParts().isEmpty()) {
                    boolean deleted = Inventory.deleteProduct(productTable.getSelectionModel().getSelectedItem());

                    if (deleted == false) {
                        Alert deleteAlert = new Alert(Alert.AlertType.ERROR, "Could not delete selected product.");
                        alert.showAndWait();
                    } else {
                        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION, "Product was successfully deleted.", ButtonType.OK);
                        alertConfirm.setTitle("Deleted");
                        alertConfirm.showAndWait();
                    }
                }
                //if there are associated parts, don't delete and show error message
                else {
                    Alert alertRemovePart = new Alert(Alert.AlertType.ERROR,
                            "Cannot delete the product while it has an associated part attached.  " +
                                    "Please modify the product to remove the associated part and then delete.", ButtonType.OK);
                    alertRemovePart.showAndWait();
                }
            }
        }
        else
        {
            Alert selectionAlert = new Alert(Alert.AlertType.ERROR, "Please select a product to delete.", ButtonType.OK);
            selectionAlert.showAndWait();
        }
    }

    /**
     * Exits the program. Sends alert making sure the user wants to exit.
     * @param event Exit button click event
     */
    @FXML
    void onActionExit(ActionEvent event) {Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to exit?", ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle("Confirm Exit");
        Optional<ButtonType> clickButton = alert.showAndWait();

        if (clickButton.isPresent() && clickButton.get() == ButtonType.YES) {
            System.exit(0);
        }
    }

    /**
     * Loads the next window based on the button pressed.
     * @param event The button press event
     * @param nextPage The address string of the next window to display
     * @throws IOException Alerts user if cannot find nextPage and closes program
     */
    public void sceneChange(ActionEvent event, String nextPage)  throws IOException {
        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource(nextPage));
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NullPointerException e) {
            nextPageError();
        }
    }

    /**
     * Displays the error message if unable to load the next window.
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
     * Fills the product and parts tables when initialized
     * @param url The location used
     * @param rb The resources used
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //fill parts table
        partsTable.setItems(Inventory.getAllParts());
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTable.setItems(Inventory.getAllProducts());
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
