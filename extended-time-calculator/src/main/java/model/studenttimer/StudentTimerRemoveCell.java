//package com.peanuttech.app;
package model.studenttimer;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import model.studenttimer.StudentTimer;

/**
 * Created with IntelliJ IDEA.
 * User: will
 * Date: 10/22/13
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudentTimerRemoveCell extends TableCell<StudentTimer, Button> {
    private String warningBackgroundColor = "-fx-background-color: #FFFF80;";
    private String almostDoneBackgroundColor = "-fx-background-color: #FFA8A8;";

    public StudentTimerRemoveCell() {

    }

    @Override
    protected void updateItem(final Button s, boolean b) {
        super.updateItem(s, b);
        setGraphic(s);

        if (!b) {
            attachListener();
            setBackgroundColor();
        } else {
            setStyle("");
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

    private void setBackgroundColor() {

        if (getTableRow() != null) {
            if (getTableRow().getItem() != null) {
                StudentTimer st = (StudentTimer) getTableRow().getItem();

                if (st.minutesLeft > 15) {

                } else if (st.minutesLeft > 5) {
                    setStyle(warningBackgroundColor);
                } else {
                    setStyle(almostDoneBackgroundColor);
                }
            }
        }
    }
}
