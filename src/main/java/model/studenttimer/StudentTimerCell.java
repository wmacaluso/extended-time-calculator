//package com.peanuttech.app;
package model.studenttimer;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.TableCell;
import model.studenttimer.StudentTimer;

/**
 * Created with IntelliJ IDEA.
 * User: will
 * Date: 10/22/13
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudentTimerCell extends TableCell<StudentTimer, String> {
    private String warningBackgroundColor = "-fx-background-color: #FFFF80;";
    private String almostDoneBackgroundColor = "-fx-background-color: #FFA8A8;";

    public StudentTimerCell() {

    }

    private void setBackgroundColor() {

        if (getTableRow() != null) {
            if (getTableRow().getItem() != null) {
                StudentTimer st = (StudentTimer) getTableRow().getItem();

                if (st.minutesLeft > 5 && st.minutesLeft < 15) {
                    setStyle(warningBackgroundColor);
                } else if(st.minutesLeft <= 5){
                    setStyle(almostDoneBackgroundColor);
                }
            }
        }
    }

    private void attachListener(){

        if (getTableRow() != null) {
            if (getTableRow().getItem() != null) {
                StudentTimer st = (StudentTimer) getTableRow().getItem();

                InvalidationListener listener = new InvalidationListener() {

                @Override
                public void invalidated(Observable observable) {
                    setBackgroundColor();
                }
            };

            st.TimeRemainingProperty().removeListener(listener);
            st.TimeRemainingProperty().addListener(listener);

            }
        }
    }

    @Override
    protected void updateItem(final String s, boolean b) {
        super.updateItem(s, b);

        setText(s);

        if (!b) {
            attachListener();
            setBackgroundColor();
        } else {
            setStyle("");
        }
    }
}
