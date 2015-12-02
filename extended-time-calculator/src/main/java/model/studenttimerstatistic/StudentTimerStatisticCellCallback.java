package model.studenttimerstatistic;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * Created by will on 10/24/14.
 */
public class StudentTimerStatisticCellCallback implements Callback<TableColumn<StudentTimerStatistic, String>, TableCell<StudentTimerStatistic, String>> {

    @Override
    public TableCell<StudentTimerStatistic, String> call(TableColumn<StudentTimerStatistic, String> studentTimerStringTableColumn)
    {
        return new StudentTimerStatisticCell();
    }

}
