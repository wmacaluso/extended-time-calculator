package model.studenttimerstatistic;

import javafx.scene.control.TableCell;

/**
 * Created by will on 10/24/14.
 */
public class StudentTimerStatisticCell extends TableCell<StudentTimerStatistic, String> {

    private String warningBackgroundColor = "-fx-background-color: #FFFF80;";
    private String almostDoneBackgroundColor = "-fx-background-color: #FFA8A8;";
    private StudentTimerStatistic sts;

    public StudentTimerStatisticCell(){}

    @Override
    protected void updateItem(final String s, boolean b)
    {
        super.updateItem(s, b);

        setText("");

        if(getTableRow() != null)
        {
            if(getTableRow().getItem() != null)
            {
                this.sts = (StudentTimerStatistic)getTableRow().getItem();
                setText(s);
            }
        }
    }
}
