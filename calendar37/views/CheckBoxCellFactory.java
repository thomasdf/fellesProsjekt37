package views;

import models.Person_Test;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;

//Denne klassen implementerer en checkbox i invite-viewene. 

public class CheckBoxCellFactory implements Callback {
    @Override
    public TableCell call(Object param) {
        CheckBoxTableCell<Person_Test,Boolean> checkBoxCell = new CheckBoxTableCell();
        return checkBoxCell;
    }
}
