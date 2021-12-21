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

import java.io.IOException;

public class AddPartController {

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
    private Button saveBtn;

    @FXML
    private Button cancelBtn;

    /**
     * Creates a new part and returns to the main menu when the save button is pressed.
     * @param event The save button click event
     */
    @FXML
    void onActionSaveNewPart(ActionEvent event) {
        try {
            int index = Inventory.getAllParts().size() - 1;
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
            else { //if correct values entered, continue to add part
                if (inHouseRBtn.isSelected()) //create inHouse part if inHouse radio button is selected
                {
                    int machineId = Integer.parseInt(machineOrCoTextField.getText());
                    InHouse part = new InHouse(id, name, price, stock, min, max, machineId);
                    Inventory.addPart(part);
                } else //create outsourced part if outsourced radio button is selected
                {
                    String companyName = machineOrCoTextField.getText();
                    Outsourced part = new Outsourced(id, name, price, stock, min, max, companyName);
                    Inventory.addPart(part);
                }
                //returns to main menu
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
        } catch (IOException ioe) {
            ioe.getMessage();
        }
    }

    /**
     * Checks to see if an input value is a integer
     * @param textInput The input from the text field being checked
     * @return true if the value is an integer, otherwise false
     */
    public boolean isInteger(String textInput) {
        try {
            Integer.parseInt(textInput);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Checks to see if an input value is a double
     * @param textInput The input from the text field being checked
     * @return true if the value is a double, otherwise false
     */
    public boolean isDouble(String textInput) {
        try {
            Double.parseDouble(textInput);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Returns to the main menu and does not create a part when the cancel button is pressed.
     * @param event The cancel button click event
     * @throws IOException Displays error message if next page cannot be loaded and closes program.
     */
    @FXML
    void onActionCancelAddPart(ActionEvent event) {
        try
        {
            sceneChange(event, "/view/MainMenu.fxml");
        }
        catch(NullPointerException e)
        {
            nextPageError();
        }
        catch (IOException e)
        {
            e.getMessage();
        }
    }

    /**
     * When Outsourced radio button is selected, changes the text on the last label to "Company name"
     * @param event The Outsource radio button being selected
     */
    @FXML
    void onActionDisplayCompanyName(ActionEvent event)  {
        machineOrCoLbl.setText("Company Name");
    }

    /**
     * When In-house radio button is selected, changes the text on the last label to "Machine ID"
     * @param event The In-house radio button being selected
     */
    @FXML
    void onActionDisplayMachineID(ActionEvent event) {
        machineOrCoLbl.setText("Machine ID");

    }

    /**
     * Loads the next window based on the button pressed.
     * @param event The button press event
     * @param nextPage The address string of the next window to display
     * @throws IOException Alerts user if cannot find nextPage and closes program
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
     */
    public void nextPageError()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Error retrieving next page.");
        alert.showAndWait();
    }
}