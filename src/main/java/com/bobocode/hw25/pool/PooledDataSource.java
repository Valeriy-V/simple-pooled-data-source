package com.bobocode.hw25.pool;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PooledDataSource extends PGSimpleDataSource {

    private Queue<Connection> pool = new ConcurrentLinkedQueue<>();

    public PooledDataSource(String url) throws SQLException {
        this.setURL(url);
        init();
    }

    public PooledDataSource(String url, String user, String pass) throws SQLException {
        this.setURL(url);
        this.setUser(user);
        this.setPassword(pass);
        init();
    }

    private void init() throws SQLException {
        for (int i = 0; i < 10; i++) {
            Connection connection = super.getConnection();
            ConnectionProxy connectionProxy = new ConnectionProxy(connection, pool);
            pool.offer(connectionProxy);
        }
    }

    @Override
    public Connection getConnection() {
        return pool.poll();
    }

}
