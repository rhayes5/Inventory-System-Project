package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;

public class ModifyPartController {

    Stage stage;
    Parent scene;

    @FXML
    private RadioButton inHouseRBtn;

    @FXML
    private ToggleGroup addPartTG;

    @FXML
    private RadioButton outsourcedRBtn;

    @FXML
    private Label machineOrCoLbl;

    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField inventoryTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField maxTextField;

    @FXML
    private TextField minTextField;

    @FXML
    private TextField machineOrCoTextField;

    @FXML
    private Button modifySaveBtn;

    @FXML
    private Button modifyCancelBtn;

    /**
     * Updates the current part and returns to the main menu when the save button is pressed.
     * @param event The save button click event
     * @throws IOException error if text fields inv, min, max, and machineId are not integers or if price is not a double,
     * or displays alert and closes the program if the next window cannot be opened.
     */
    @FXML
    void onActionSaveNewPart(ActionEvent event) throws IOException {
        try {
            int id = Integer.parseInt(idTextField.getText());
            int index = Inventory.getAllParts().indexOf(Inventory.lookupPart(id));
            String name = nameTextField.getText();
            double price = Double.parseDouble(priceTextField.getText());
            int stock = Integer.parseInt(inventoryTextField.getText());
            int min = Integer.parseInt(minTextField.getText());
            int max = Integer.parseInt(maxTextField.getText());

            //make sure stock is between max and min and min is less than max
            if (stock > max || stock < min || min >= max)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Illegal Values Entered");
                if (min >= max)
                {
                    alert.setContentText("Minimum must be less than maximum.");
                }
                else {
                    alert.setContentText("Inventory must be between min and max.");
                }

                alert.showAndWait();
            }
            else {
                if (inHouseRBtn.isSelected()) {
                    int machineId = Integer.parseInt(machineOrCoTextField.getText());
                    InHouse updatedPart = new InHouse(id, name, price, stock, min, max, machineId);
                    Inventory.updatePart(index, updatedPart);
                } else {
                    String companyName = machineOrCoTextField.getText();
                    Outsourced updatedPart = new Outsourced(id, name, price, stock, min, max, companyName);
                    Inventory.updatePart(index, updatedPart);
                }
                sceneChange(event, "/view/MainMenu.fxml");
            }
        }
        catch(NumberFormatException nfe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");

            if (!isInteger(inventoryTextField.getText()))
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
            else if (inHouseRBtn.isSelected() && !isInteger(machineOrCoTextField.getText()))
            {
                alert.setContentText("Please enter an integer value for machine id.");
            }
            else
                alert.setContentText("Please enter valid values for each text field.");

            alert.showAndWait();
        } catch(NullPointerException e) {
            nextPageError();
        }
    }

    /**
     * Checks if input string is an integer.
     * @param search The string to check
     * @return true if the string is an integer or false if not
     */
    public boolean isInteger(String search) {
        try {
            Integer.parseInt(search);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
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
     * Returns to the main menu without updating part when the cancel button is pressed.
     * @param event The cancel button click event
     * @throws IOException Displays error message if next page cannot be loaded and closes program.
     */
    @FXML
    void onActionCancelAddPart(ActionEvent event) throws IOException {
        try {
            sceneChange(event, "/view/MainMenu.fxml");
        } catch (NullPointerException e) {
            nextPageError();
        }
    }

    /**
     * When Outsourced radio button is selected, changes the text on the last label to "Company name"  and clears the text field
     */
    @FXML
    //if Outsourced selected
    void onActionDisplayCompanyName(ActionEvent event)  {
        machineOrCoLbl.setText("Company Name");
        machineOrCoTextField.clear();
    }

    /**
     * When In-house radio button is selected, changes the text on the last label to "Machine ID" and clears the text field
     */
    //if In-house selected
    @FXML
    void onActionDisplayMachineID(ActionEvent event) {
        machineOrCoLbl.setText("Machine ID");
        machineOrCoTextField.clear();
    }

    /**
     * Fills text fields with current part information.
     * @param part The part that will be displayed
     */
    public void getCurrentPartInfo(Part part) {
        //fill text fields
        idTextField.setText(String.valueOf(part.getId()));
        nameTextField.setText(part.getName());
        inventoryTextField.setText(String.valueOf(part.getStock()));
        priceTextField.setText(String.valueOf(part.getPrice()));
        minTextField.setText(String.valueOf(part.getMin()));
        maxTextField.setText(String.valueOf(part.getMax()));

        if (part instanceof InHouse) //if it's an inhouse part, select the inHouse radio button and fill text box with machine id
        {
            inHouseRBtn.setSelected(true);
            machineOrCoTextField.setText(String.valueOf(((InHouse) part).getMachineId()));
        }
        else { //if it's an outsourced part, select the outsourced radio button, fill text box with company name, and change text of label to "Company Name"
            machineOrCoLbl.setText("Company Name");
            outsourcedRBtn.setSelected(true);
            machineOrCoTextField.setText(String.valueOf(((Outsourced) part).getCompanyName()));
        }
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
        catch (NullPointerException e)
        {
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
        alert.setContentText("Error retrieving next page.");
        alert.showAndWait();
    }
}