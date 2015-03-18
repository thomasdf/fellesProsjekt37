package views;

import models.Account;
import models.Group;
import models.Person_Test;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;

//Denne klassen implementerer en checkbox i invite-viewene. 

public class CheckBoxCellFactory_Account implements Callback {
    @Override
    public TableCell call(Object param) {
        CheckBoxTableCell<Account,Boolean> checkBoxCell = new CheckBoxTableCell();
        return checkBoxCell;
    }
}
