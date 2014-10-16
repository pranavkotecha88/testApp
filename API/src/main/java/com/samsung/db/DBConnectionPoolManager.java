package com.samsung.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import com.samsung.data.Config;
import com.samsung.utils.Log;

public class DBConnectionPoolManager {
	
    private static final String TAG = DBConnectionPoolManager.class.getSimpleName();
    private static DBConnectionPoolManager singletonInstance = null;

    private DataSource datasource;

    protected DBConnectionPoolManager() {
        
    }

    /**
     * Returns handle to singleton instance
     * 
     * @return
     */
    public static DBConnectionPoolManager getInstance() {
        if (singletonInstance == null) {
            synchronized (TAG) {
                if (singletonInstance == null) {
                    singletonInstance = new DBConnectionPoolManager();
                }
            }
        }
        return singletonInstance;
    }

    /**
     * Initializes db connection pool.
     * 
     */
    public void initialize() {
        Log.i(TAG, "initialize. Enabled:" + Config.isApacheTomcatJdbcPoolEnable());
        if (!Config.isApacheTomcatJdbcPoolEnable()) {
            return;
        }

        final PoolProperties p = new PoolProperties();
        final String dburl = Config.getApacheTomcatJdbcPoolUrlScheme() + "://" + Config.getMySqlDbHost() + ":" + Config.getMySqlDbPort() + "/" + Config.getMySqlDbName() + "?characterEncoding=UTF-8";
        p.setUrl(dburl);
        p.setDriverClassName(Config.getApacheTomcatJdbcPoolDriverClassName());
        p.setUsername(Config.getMySqlDbUser());
        p.setPassword(Config.getMySqlDbPasswd());
        p.setJmxEnabled(Config.isApacheTomcatJdbcPoolJmxEnabled());
        p.setTestWhileIdle(Config.isApacheTomcatJdbcPoolTestWhileIdle());
        p.setTestOnBorrow(Config.isApacheTomcatJdbcPoolTestOnBorrow());
        p.setValidationQuery(Config.getApacheTomcatJdbcPoolValidationQuery());
        p.setTestOnReturn(Config.isApacheTomcatJdbcPoolTestOnReturn());
        p.setValidationInterval(Config.getApacheTomcatJdbcPoolValidationIntervalMillis());
        p.setMinEvictableIdleTimeMillis(Config.getApacheTomcatJdbcPoolMinEvictableIdleTimeMillis());
        p.setTimeBetweenEvictionRunsMillis(Config.getApacheTomcatJdbcPoolTimeBetweenEvictionRunsMillis());
        p.setMaxWait(Config.getApacheTomcatJdbcPoolMaxWaitMillis());
        p.setInitialSize(Config.getApacheTomcatJdbcPoolInitialSize());
        p.setMinIdle(Config.getApacheTomcatJdbcPoolMinIdle());
        p.setMaxIdle(Config.getApacheTomcatJdbcPoolMaxIdle());
        p.setMaxActive(Config.getApacheTomcatJdbcPoolMaxActive());
        p.setLogAbandoned(Config.isApacheTomcatJdbcPoolLogAbandoned());
        p.setRemoveAbandoned(Config.isApacheTomcatJdbcPoolRemoveAbandoned());
        p.setRemoveAbandonedTimeout(Config.getApacheTomcatJdbcPoolRemoveAbandonedTimeoutSecs());
        p.setJdbcInterceptors(Config.getApacheTomcatJdbcPoolJdbcInterceptors());
        datasource = new DataSource();
        datasource.setPoolProperties(p);
    }

    /**
     * Returns whether or not DBConnectionPool is initialized.
     * 
     * @return
     */
    public boolean isInitialized() {
        return datasource != null;
    }

    /**
     * Returns a Connection object from the pool.
     * 
     */
    public Connection getConnection() {
        if (datasource != null) {
            try {
                return datasource.getConnection();
            } catch (SQLException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }
        return null;
    }

    /**
     * Returns a Connection object to the pool.
     * 
     */
    public void putConnection(Connection c) {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }
    }

    /**
     * Shuts down db connection pool.
     * 
     */
    public void shutdown() {
        Log.i(TAG, "shuttting down");
        if (datasource != null) {
            datasource.close();
        }
    }

    public static void main(String[] args) {
        Connection con = null;
        Statement stmt = null;
        ResultSet res = null;
        try {
            Config.load();
            DBConnectionPoolManager.getInstance().initialize();
            con = DBConnectionPoolManager.getInstance().getConnection();
            if (con != null) {
                stmt = con.createStatement();
                res = stmt.executeQuery("select * from content_categories");
                while (res.next()) {
                    Log.i(TAG, res.getString("TITLE"));
                }
                Thread.sleep(10000);
                stmt.close();
                res.close();
            }
            DBConnectionPoolManager.getInstance().putConnection(con);
            con = null;
            DBConnectionPoolManager.getInstance().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(stmt != null) {
            	try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(res != null) {
            	try {
                    res.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
