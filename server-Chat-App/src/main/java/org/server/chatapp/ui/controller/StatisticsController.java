package org.server.chatapp.ui.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.server.chatapp.dao.Database;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class StatisticsController {

    @FXML
    private Label lblOnlineCount;
    @FXML
    private Label lblTotalCount;
    @FXML
    private Rectangle rectOnline;
    @FXML
    private Rectangle rectTotal;
    @FXML
    private PieChart genderChart;
    @FXML
    private BarChart<String, Number> countryChart;

    public void initialize() {
        refreshStatistics();
    }

    public void refreshStatistics() {
        new Thread(() -> {
            try (Connection connection = Database.getDataSource().getConnection()) {

                int totalUsers = 0;
                int onlineUsers = 0;
                String statusSql = "SELECT status, COUNT(*) as count FROM users GROUP BY status";

                try (PreparedStatement ps = connection.prepareStatement(statusSql);
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int count = rs.getInt("count");
                        String statusStr = rs.getString("status").toUpperCase();
                        totalUsers += count;
                        if (!statusStr.equals("OFFLINE")) {
                            onlineUsers += count;
                        }
                    }
                }

                Map<String, Integer> genderMap = new HashMap<>();
                String genderSql = "SELECT gender, COUNT(*) as count FROM users GROUP BY gender";
                try (PreparedStatement ps = connection.prepareStatement(genderSql);
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        genderMap.put(rs.getString("gender"), rs.getInt("count"));
                    }
                }

                Map<String, Integer> countryMap = new HashMap<>();
                String countrySql = "SELECT country, COUNT(*) as count FROM users GROUP BY country ORDER BY count DESC LIMIT 5";
                try (PreparedStatement ps = connection.prepareStatement(countrySql);
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        countryMap.put(rs.getString("country"), rs.getInt("count"));
                    }
                }

                int finalTotal = totalUsers;
                int finalOnline = onlineUsers;
                Platform.runLater(() -> updateDashboardUI(finalTotal, finalOnline, genderMap, countryMap));

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void updateDashboardUI(int total, int online, Map<String, Integer> genderMap, Map<String, Integer> countryMap) {
        lblTotalCount.setText(String.valueOf(total));
        lblOnlineCount.setText(online + " Online");

        if (total > 0) {
            double ratio = (double) online / total;
            rectOnline.setWidth(ratio * rectTotal.getWidth());
        } else {
            rectOnline.setWidth(0);
        }

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        genderMap.forEach((gender, count) -> {
            PieChart.Data data = new PieChart.Data(gender, count);
            data.nodeProperty().addListener((observable, oldNode, newNode) -> {
                if (newNode != null) {
                    Tooltip tooltip = new Tooltip(data.getName() + ": " + (int) data.getPieValue());
                    tooltip.setShowDelay(Duration.millis(10));
                    tooltip.setHideDelay(Duration.millis(10));
                    Tooltip.install(newNode, tooltip);
                }
            });
            pieData.add(data);
        });
        genderChart.setData(pieData);


        countryChart.getData().clear();
        countryChart.setAnimated(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Users per Country");
        countryMap.forEach((country, count) -> {
            XYChart.Data<String, Number> data = new XYChart.Data<>(country, count);
            data.nodeProperty().addListener((observable, oldNode, newNode) -> {
                if (newNode != null) {
                    Tooltip tooltip = new Tooltip(data.getXValue() + ": " + data.getYValue() + " Users");
                    tooltip.setShowDelay(Duration.millis(10));
                    tooltip.setHideDelay(Duration.millis(10));
                    Tooltip.install(newNode, tooltip);
                }
            });
            series.getData().add(data);
        });
        countryChart.getData().add(series);

    }
}
