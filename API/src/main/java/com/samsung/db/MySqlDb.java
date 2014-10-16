package com.samsung.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.samsung.data.Config;
import com.samsung.utils.Log;

public class MySqlDb {
	
	private static final String TAG = MySqlDb.class.getSimpleName();
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final int SQL_VALIDATION_TIMEOUT_SECONDS = 30;
    private static Connection connection = null;
    private static volatile MySqlDb instance = null;
    private static boolean pooledConnection = false;
    
    public static MySqlDb getInstance() {
        if (instance == null) {
            synchronized (MySqlDb.class) {
                if (instance == null) {
                    instance = new MySqlDb();
                }
            }
        }
        return instance;
    }    
    
    // private constructor
    private MySqlDb() {
        // To make use of connection pool. DBConnectionPoolManager must be intialized
        // before using MySqlDb
        pooledConnection = DBConnectionPoolManager.getInstance().isInitialized();
   		createConnection();
    }
    
    public static boolean ready() {
    	try {
			return pooledConnection || (getInstance() != null && connection != null && connection.isValid(SQL_VALIDATION_TIMEOUT_SECONDS));
		} catch (SQLException e) {
			Log.w(TAG,e.getMessage());
		}
    	return false;
    }
    
    /**
     * Executes query and the result ResultSet is returned in DBResource.
     * 
     * If database is not ready OR there was any exception executing query, ResultSet will be null.
     * Upon return, the caller can reuse the DBResource across multiple executQuery() call if query remain the same.
     * 
     * Upon return, the ownership of data base resources is assigned to caller and the caller must call closeAll()
     * to release resources.
     * 
     * @param resource
     * @param query
     * @param args
     * @return
     */
    public DBResource executeQuery(DBResource resource, String query, Object... args){
        Log.d(TAG,query);
        final DBResource dbres = resource == null ? new DBResource() : resource;
        dbres.setResultset(null);
        dbres.setWarnings(null);
        if (ready()) {
            try {
                PreparedStatement pstmt = dbres.getPreparedstatement();
                // If we don't have a prepared statement to work with, create one
                // and store reference to it in DBResource for lazy release.
                if (pstmt == null) {
                    // We are delegating the Connection.close to DBResource instance.
                    @SuppressWarnings("resource")
                    Connection con = pooledConnection ? dbres.getPooledConnection() : connection;
                    // If we dont' have a pooled connection to work with, get one from manager
                    // and store reference to it in DBResource for lazy release
                    if (pooledConnection && (con == null)) {
                        con = DBConnectionPoolManager.getInstance().getConnection();
                        dbres.setPooledConnection(con);
                    }
                    if(con != null) {
                    	pstmt = con.prepareStatement(query);
                    	dbres.setPreparedStatement(pstmt);
                    }
                }
                // Bind parameters to prepared statement
                for (int i = 0; i < args.length; ++i) {
                    pstmt.setObject(i + 1, args[i]);
                }
                // Execute, get resultset and store reference to it in DBResource for lazy release.
                dbres.setResultset(pstmt.executeQuery());
                dbres.setWarnings(pstmt.getWarnings());
                Log.d(TAG, "Query result: " + dbres.getResultset().toString());
            } catch (SQLException e) {
                Log.w(TAG, e.getMessage());
            }
        } else {
            Log.w(TAG, "MySQL DB connection is not ready!");
        }
        return dbres;
    }

    /**
     * Executes update query and the result is returned in DBResource executeUpdateCount.
     * 
     * If database is not ready OR there was any exception executing query, executeUpdateCount will be 0.
     * Upon return, the caller can reuse the DBResource across multiple executeUpdate() call if query remain the same.
     * 
     * Upon return, the ownership of data base resources is assigned to caller and the caller must call closeAll()
     * to release resources.
     * 
     * @param resource
     * @param query
     * @param args
     * @return null
     */
    public DBResource executeUpdate(DBResource resource, String statement, Object... args) {
        final DBResource dbres = resource == null ? new DBResource() : resource;
        dbres.setExecuteUpdateCount(0);
        dbres.setWarnings(null);
        if (ready()) {
            Connection conn = null;
            try {
                Log.d(TAG,"SQL statement :" + statement);
                PreparedStatement pstmt = dbres.getPreparedstatement();
                // If we don't have a prepared statement to work with, create one
                // and store reference to it in DBResource for lazy release.
                if (pstmt == null) {
                    // We are delegating the Connection.close to DBResource instance.
                    @SuppressWarnings("resource")
                    Connection con = pooledConnection ? dbres.getPooledConnection() : connection;
                    // If we dont' have a pooled connection to work with, get one from manager
                    // and store reference to it in DBResource for lazy release
                    if (pooledConnection && (con == null)) {
                        con = DBConnectionPoolManager.getInstance().getConnection();
                        dbres.setPooledConnection(con);
                    }
                    if(con != null) {
                    	pstmt = con.prepareStatement(statement);
                    	dbres.setPreparedStatement(pstmt);
                    }
                }
                // Bind parameters to prepared statement
                for (int i = 0; i < args.length; ++i) {
                    pstmt.setObject(i + 1, args[i]);
                }
                // Execute and store result count in DBResource.
                dbres.setExecuteUpdateCount(pstmt.executeUpdate());
                dbres.setWarnings(pstmt.getWarnings());                
                Log.d(TAG,"Update result: " + dbres.getExecuteUpdateCount());
            } catch (SQLException e) {
                Log.w(TAG, e.getMessage());
            }
        }
        return dbres;
    }

    // Sql update
    public int executeUpdate(String statement, Object... args) {
        PreparedStatement pstmt = null;
        int result = 0;
        if (ready()) {
            Connection conn = null;
            try {
                Log.d(TAG, "SQL statement :" + statement);
                conn = pooledConnection ? DBConnectionPoolManager.getInstance().getConnection() : connection;
                if(conn != null) {
                	pstmt = conn.prepareStatement(statement);
                
                for (int i = 0; i < args.length; ++i) {
                    pstmt.setObject(i + 1, args[i]);
                }
                result = pstmt.executeUpdate();
                Log.d(TAG, "Update result: " + result);
                }
            } catch (SQLException e) {
                Log.w(TAG, e.getMessage());
            } finally {
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (SQLException e) {
                        Log.w(TAG, e.getMessage());
                    }
                }
                // Return connection to pool if pooled connection
                DBConnectionPoolManager.getInstance().putConnection(pooledConnection ? conn : null);
            }
        } else {
            Log.w(TAG, "MySQL DB connection is not ready!");
        }
        return result;
    }

	private void close() {
    	if(connection != null){
	        try {
	        	connection.close();
	        } catch (SQLException sqlException) {
				Log.e(TAG,"Error closing Nachos Contents DB !");
	        }
	    	connection = null;
	    }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }
    
    private void createConnection(){
    	if((connection == null) && (!pooledConnection)){
    		connectToDB();
    	}
    }
    
    private static void connectToDB() {
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(getMySqlDbUrl(),Config.getMySqlDbUser(),Config.getMySqlDbPasswd());
		} catch (SQLException e) {
			Log.e(TAG,"Connection Failed! Check output console");
			Log.w(TAG,e.getMessage());
		} catch (ClassNotFoundException e) {
			Log.e(TAG,"Where is your MySQL JDBC Driver?");
			Log.e(TAG,e.getMessage());
		} catch (Exception e) {
			Log.e(TAG,"Error loading or initializing config..");
			Log.e(TAG,e.getMessage());
		}
	 
		if (connection != null) {
			Log.d(TAG,"Connected !! Take control your database now!");
		} else {
			Log.w(TAG,"Failed to make connection!");
		}
	}
    
    
	private static String getMySqlDbUrl() {
	    return "jdbc:mysql://" + Config.getMySqlDbHost() + ":" + Config.getMySqlDbPort() + "/" + Config.getMySqlDbName() + "?characterEncoding=UTF-8";
	}

}
