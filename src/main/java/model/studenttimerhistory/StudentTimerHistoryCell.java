package model.studenttimerhistory;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.TableCell;
import model.studenttimer.StudentTimer;

/**
 * Created by will on 11/11/14.
 */
public class StudentTimerHistoryCell extends TableCell<StudentTimer, String> {

    public StudentTimerHistoryCell() {

    }

    @Override
    protected void updateItem(final String s, boolean b) {
        super.updateItem(s, b);

        setText(s);
    }
}
