package views;

import models.Account;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;

//Denne klassen implementerer en checkbox i invite-viewene. 

@SuppressWarnings("rawtypes")
public class CheckBoxCellFactory_Account implements Callback {
    @Override
    public TableCell call(Object param) {
        CheckBoxTableCell<Account, Boolean> checkBoxCell = new CheckBoxTableCell<Account, Boolean>();
        return checkBoxCell;
    }
}
