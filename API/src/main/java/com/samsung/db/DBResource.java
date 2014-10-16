package com.samsung.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;

import com.samsung.utils.Log;

public class DBResource {

    private static final String TAG = DBResource.class.getSimpleName();

    private Connection pooledConnection;
    private PreparedStatement preparedStatement;
    private ResultSet resultset;
    private int executeUpdateCount;
    private SQLWarning warnings;

    public DBResource() {
        this(null, null, null, 0, null);
    }

    public DBResource(Connection con, PreparedStatement ps, ResultSet rs, int count, SQLWarning warnings) {
        this.pooledConnection = con;
        this.preparedStatement = ps;
        this.resultset = rs;
        this.executeUpdateCount = count;
        this.warnings = warnings;
    }

    /**
     * @return the pooledConnection
     */
    public Connection getPooledConnection() {
        return pooledConnection;
    }

    /**
     * @param pooledConnection the pooledConnection to set
     */
    public void setPooledConnection(Connection pooledConnection) {
        this.pooledConnection = pooledConnection;
    }

    /**
     * Returns the PreparedStatement
     * 
     * @return the preparedstatement
     */
    public PreparedStatement getPreparedstatement() {
        return preparedStatement;
    }

    /**
     * Sets the PreparedStatement
     * 
     * @param preparedstatement to set
     */
    public void setPreparedStatement(PreparedStatement preparedstatement) {
        this.preparedStatement = preparedstatement;
    }

    /**
     * Returns the ResultSet
     * 
     * @return the resultset
     */
    public ResultSet getResultset() {
        return resultset;
    }

    /**
     * Sets the ResultSet
     * 
     * @param resultset to set
     */
    public void setResultset(ResultSet resultset) {
        this.resultset = resultset;
    }

    /**
     * @return the executeUpdateCount
     */
    public int getExecuteUpdateCount() {
        return executeUpdateCount;
    }

    /**
     * @param executeUpdateCount the executeUpdateCount to set
     */
    public void setExecuteUpdateCount(int executeUpdateCount) {
        this.executeUpdateCount = executeUpdateCount;
    }

    /**
     * Closes Connection resources and sets references to null.
     * 
     */
    public void closePooledConnection() {
        DBConnectionPoolManager.getInstance().putConnection(pooledConnection);
        pooledConnection = null;
    }

    /**
     * Closes PreparedStatement resources and sets references to null.
     */
    public void closePreparedStatement() {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
                preparedStatement = null;
            } catch (SQLException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }
    }

    /**
     * Closes ResultSet and sets references to null.
     */
    public void closeResultSet() {
        if (resultset != null) {
            try {
                resultset.close();
                resultset = null;
            } catch (SQLException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }
    }

    /**
     * Closes all database resource references.
     */
    public void closeAll() {
        closeResultSet();
        closePreparedStatement();
        closePooledConnection();
    }

	/**
	 * Gets the warnings.
	 *
	 * @return the warnings
	 */
	public SQLWarning getWarnings() {
		return warnings;
	}

	/**
	 * Sets the warnings.
	 *
	 * @param warnings the new warnings
	 */
	public void setWarnings(SQLWarning warnings) {
		this.warnings = warnings;
	}

}
