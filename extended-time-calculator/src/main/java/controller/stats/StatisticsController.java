package controller.stats;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;

/**
 * Created by will on 11/5/14.
 */
public class StatisticsController {

    @FXML private Tab basicStats;
    @FXML private Tab charts;
    @FXML private Tab statsTable;
    @FXML private Tab testHistory;

    @FXML
    public void initialize() {

        int iconWidth = 25;
        int iconHeight = 25;

        ImageView basicStatsIcon = new ImageView("/images/icons/Gnome-Document-Open-Recent-64.png");
        basicStatsIcon.setFitWidth(iconWidth);
        basicStatsIcon.setFitHeight(iconHeight);
        basicStats.setGraphic(basicStatsIcon);

        ImageView lineChartIcon = new ImageView("/images/icons/Gnome-X-Office-Presentation-64.png");
        lineChartIcon.setFitWidth(iconWidth);
        lineChartIcon.setFitHeight(iconHeight);
        charts.setGraphic(lineChartIcon);

        ImageView detailTableIcon = new ImageView("/images/icons/Gnome-X-Office-Spreadsheet-64.png");
        detailTableIcon.setFitWidth(iconWidth);
        detailTableIcon.setFitHeight(iconHeight);
        statsTable.setGraphic(detailTableIcon);

        ImageView historyTableIcon = new ImageView("/images/icons/Accessories-Dictionary-64.png");
        historyTableIcon.setFitWidth(iconWidth);
        historyTableIcon.setFitHeight(iconHeight);
        testHistory.setGraphic(historyTableIcon);


    }
}
