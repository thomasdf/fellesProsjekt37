package views;


import models.Group;

import javafx.scene.control.TableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;

//Denne klassen implementerer en checkbox i invite-viewene. 

public class CheckBoxCellFactory_Group implements Callback {
    @Override
    public TableCell call(Object param) {
        CheckBoxTableCell<Group,Boolean> checkBoxCell = new CheckBoxTableCell();
        return checkBoxCell;
    }
}
