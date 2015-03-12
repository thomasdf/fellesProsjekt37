package views;

import models.Person_Test;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;

public class CheckBoxCellFactory implements Callback {
    @Override
    public TableCell call(Object param) {
        CheckBoxTableCell<Person_Test,Boolean> checkBoxCell = new CheckBoxTableCell();
        return checkBoxCell;
    }
}
