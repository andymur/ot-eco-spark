package com.andymur.pg.influxdb.repository;

public class Config {
    private final String url;
    private final String databaseName;
    private final String userName;
    private final String password;
    private final String retentionPolicy;

    public Config(String url, String dbName, String userName, String password) {
        this(url, dbName, userName, password, "default");
    }

    public Config(String url, String dbName, String retentionPolicy) {
        this(url, dbName, "", "", retentionPolicy);
    }

    public Config(String url, String dbName, String userName, String password, String retentionPolicy) {
        this.url = url;
        databaseName = dbName;
        this.userName = userName;
        this.password = password;
        this.retentionPolicy = retentionPolicy;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getUrl() {
        return url;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getRetentionPolicy() {
        return retentionPolicy;
    }
}
