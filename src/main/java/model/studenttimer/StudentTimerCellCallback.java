//package com.peanuttech.app;
package model.studenttimer;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * Created with IntelliJ IDEA.
 * User: will
 * Date: 10/22/13
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudentTimerCellCallback implements Callback<TableColumn<StudentTimer, String>, TableCell<StudentTimer, String>>
{
    @Override
    public TableCell<StudentTimer, String> call(TableColumn<StudentTimer, String> studentTimerStringTableColumn)
    {
        return new StudentTimerCell();
    }
}
