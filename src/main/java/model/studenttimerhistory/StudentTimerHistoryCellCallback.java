package model.studenttimerhistory;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import model.studenttimer.StudentTimer;
import model.studenttimer.StudentTimerCell;

/**
 * Created by will on 11/11/14.
 */
public class StudentTimerHistoryCellCallback implements Callback<TableColumn<StudentTimer, String>, TableCell<StudentTimer, String>>
{
    @Override
    public TableCell<StudentTimer, String> call(TableColumn<StudentTimer, String> studentTimerStringTableColumn)
    {
        return new StudentTimerHistoryCell();
    }
}