package com.bobocode.hw25;


import com.bobocode.hw25.pool.PooledDataSource;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DemoApp {
    public static void main(String[] args) throws SQLException {
        DataSource ds = createPooledDataSource();
        double total = 0.0;
        long start = System.nanoTime();
        for (int i = 0; i < 10; i++) {
            try (Connection conn = ds.getConnection()) {
                conn.setAutoCommit(false);
                try (Statement ps = conn.createStatement()) {
                    ResultSet rs = ps.executeQuery("select random() from products");
                    rs.next();
                    total += rs.getDouble(1);
                }
                conn.rollback();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("time: " + (System.nanoTime() - start)/1000_000 + " ms");
        System.out.println();

    }

    public static DataSource createDataSource() {
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setURL("jdbc:postgresql://localhost:5432/postgres");
        pgSimpleDataSource.setUser("ju22user");
        pgSimpleDataSource.setPassword("ju22pass");

        return pgSimpleDataSource;
    }

    public static DataSource createPooledDataSource() throws SQLException {
        return new PooledDataSource("jdbc:postgresql://localhost:5432/postgres");
    }

    public static DataSource createLocalDataSource() {
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setURL("jdbc:postgresql://localhost:5432/postgres");

        return pgSimpleDataSource;
    }
}
