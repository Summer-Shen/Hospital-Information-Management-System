package com.hims.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
@PropertySource({"classpath:application.properties"})
public class TreatmentAreaRepository {
    @Value(value = "${spring.datasource.driver-class-name}")
    private String driver;
    @Value(value = "${spring.datasource.initial_url}")
    private String initial_url;
    @Value(value = "${spring.datasource.username}")
    private String userName;
    @Value(value = "${spring.datasource.password}")
    private String password;
    @Value(value = "${spring,datasource.createDatabase}")
    private String createDatabase;
    @Value(value = "${spring,datasource.useDatabase}")
    private String useDatabase;
    @Value(value = "${spring.datasource.createTreatment_area}")
    private String createTreatment_area;

    @PostConstruct
    public void init() throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(initial_url, userName, password);
        Statement stat = conn.createStatement();
        stat.executeUpdate(createDatabase);
        stat.execute(useDatabase);
        stat.executeUpdate(createTreatment_area);
        stat.close();
        conn.close();
    }
}
