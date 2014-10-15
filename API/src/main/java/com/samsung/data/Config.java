package com.samsung.data;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DefaultConfigurationBuilder;

import com.samsung.utils.Log;

public class Config {
	private static final String TAG = Config.class.getSimpleName();

	// Configuration files location.
	// The file first found in the list gets loaded
	private static final String[] CONFIG_FILE_LIST = {
	    //"/usr/local/etc/recommender/config/config.xml",
	    //System.getProperty("user.home") + File.separatorChar + ".recommender/config/config.xml",
	    "config.xml"
	};

	// Mysql DB
	private static String mySqlDbHost;
    private static String mySqlDbName;
	private static long mySqlDbPort;
	private static String mySqlDbUser;
    private static String mySqlDbPasswd;
	
	private static int threadPoolSize;
    private static int keepAliveTimeSecs;
    private static int scheduledThreadPoolSize;

    private static boolean apacheTomcatJdbcPoolEnable;
    private static String apacheTomcatJdbcPoolUrlScheme;
    private static String apacheTomcatJdbcPoolDriverClassName;
    private static boolean apacheTomcatJdbcPoolJmxEnabled;
    private static boolean apacheTomcatJdbcPoolTestWhileIdle;
    private static boolean apacheTomcatJdbcPoolTestOnBorrow;
    private static String apacheTomcatJdbcPoolValidationQuery;
    private static boolean apacheTomcatJdbcPoolTestOnReturn;
    private static int apacheTomcatJdbcPoolValidationIntervalMillis;
    private static int apacheTomcatJdbcPoolMinEvictableIdleTimeMillis;
    private static int apacheTomcatJdbcPoolTimeBetweenEvictionRunsMillis;
    private static int apacheTomcatJdbcPoolMaxWaitMillis;
    private static int apacheTomcatJdbcPoolInitialSize;
    private static int apacheTomcatJdbcPoolMinIdle;
    private static int apacheTomcatJdbcPoolMaxIdle;
    private static int apacheTomcatJdbcPoolMaxActive;
    private static boolean apacheTomcatJdbcPoolLogAbandoned;
    private static boolean apacheTomcatJdbcPoolRemoveAbandoned;
    private static int apacheTomcatJdbcPoolRemoveAbandonedTimeoutSecs;
    private static String apacheTomcatJdbcPoolJdbcInterceptors;
		
	public static void load() throws Exception {
		Log.i(TAG, "load()");

		DefaultConfigurationBuilder builder = null;
        // Try loading config
        for (String file : CONFIG_FILE_LIST) {
            try {
                builder = getConfigBuilder(file);
                Log.i(TAG, "Loaded config file:" + file);
                // We found the file and loaded it. Now break out of loop.
                break;
            } catch (ConfigurationException ce) {
               Log.i(TAG, "Message:" + ce.getMessage());
            }
        }
		if (builder != null) {
			try {
				Config.init(builder);
			} catch (Exception e) {
				Log.e(TAG, "Error initializing from configuration file");
				throw new Exception("Config initialization error");
			}
		} else {
			Log.e(TAG, "Error configuration file not found");
			throw new Exception("Config initialization error");
		}
	}

    private static DefaultConfigurationBuilder getConfigBuilder(String file) throws ConfigurationException {
        DefaultConfigurationBuilder builder = null;
        builder = new DefaultConfigurationBuilder(file);
        builder.load();
        return builder;
    }

	private static void init(DefaultConfigurationBuilder builder) {
		Log.i(TAG, "init()");
		builder.setThrowExceptionOnMissing(true);
		
		// MySql DB config params
		mySqlDbHost = builder.getString("MySqlDbHost");
	    mySqlDbName = builder.getString("MySqlDbName");
		mySqlDbPort = builder.getLong("MySqlPortName");
		mySqlDbUser = builder.getString("MySqlDbUser");
	    mySqlDbPasswd = builder.getString("MySqlDbPassword");

		threadPoolSize = builder.getInt("ThreadPoolSize");
        keepAliveTimeSecs = builder.getInt("KeepAliveTimeSecs");
        scheduledThreadPoolSize = builder.getInt("ScheduledThreadPoolSize");
        
        apacheTomcatJdbcPoolEnable = builder.getBoolean("ApacheTomcatJdbcPoolEnable", false);
        apacheTomcatJdbcPoolUrlScheme = builder.getString("ApacheTomcatJdbcPoolUrlScheme");
        apacheTomcatJdbcPoolDriverClassName = builder.getString("ApacheTomcatJdbcPoolDriverClassName");
        apacheTomcatJdbcPoolJmxEnabled = builder.getBoolean("ApacheTomcatJdbcPoolJmxEnabled");
        apacheTomcatJdbcPoolTestWhileIdle = builder.getBoolean("ApacheTomcatJdbcPoolTestWhileIdle");
        apacheTomcatJdbcPoolTestOnBorrow = builder.getBoolean("ApacheTomcatJdbcPoolTestOnBorrow");
        apacheTomcatJdbcPoolValidationQuery = builder.getString("ApacheTomcatJdbcPoolValidationQuery");
        apacheTomcatJdbcPoolTestOnReturn = builder.getBoolean("ApacheTomcatJdbcPoolTestOnReturn");
        apacheTomcatJdbcPoolValidationIntervalMillis = builder.getInt("ApacheTomcatJdbcPoolValidationIntervalMillis");
        apacheTomcatJdbcPoolMinEvictableIdleTimeMillis = builder.getInt("ApacheTomcatJdbcPoolMinEvictableIdleTimeMillis");
        apacheTomcatJdbcPoolTimeBetweenEvictionRunsMillis = builder.getInt("ApacheTomcatJdbcPoolTimeBetweenEvictionRunsMillis");
        apacheTomcatJdbcPoolMaxWaitMillis = builder.getInt("ApacheTomcatJdbcPoolMaxWaitMillis");
        apacheTomcatJdbcPoolInitialSize = builder.getInt("ApacheTomcatJdbcPoolInitialSize");
        apacheTomcatJdbcPoolMinIdle = builder.getInt("ApacheTomcatJdbcPoolMinIdle");
        apacheTomcatJdbcPoolMaxIdle = builder.getInt("ApacheTomcatJdbcPoolMaxIdle");
        apacheTomcatJdbcPoolMaxActive = builder.getInt("ApacheTomcatJdbcPoolMaxActive");
        apacheTomcatJdbcPoolLogAbandoned = builder.getBoolean("ApacheTomcatJdbcPoolLogAbandoned");
        apacheTomcatJdbcPoolRemoveAbandoned = builder.getBoolean("ApacheTomcatJdbcPoolRemoveAbandoned");
        apacheTomcatJdbcPoolRemoveAbandonedTimeoutSecs = builder.getInt("ApacheTomcatJdbcPoolRemoveAbandonedTimeoutSecs");
        apacheTomcatJdbcPoolJdbcInterceptors = builder.getString("ApacheTomcatJdbcPoolJdbcInterceptors");

	}

	public static String getTag() {
		return TAG;
	}

	public static String[] getConfigFileList() {
		return CONFIG_FILE_LIST;
	}

	public static String getMySqlDbHost() {
		return mySqlDbHost;
	}

	public static String getMySqlDbName() {
		return mySqlDbName;
	}

	public static long getMySqlDbPort() {
		return mySqlDbPort;
	}

	public static String getMySqlDbUser() {
		return mySqlDbUser;
	}

	public static String getMySqlDbPasswd() {
		return mySqlDbPasswd;
	}

	public static int getThreadPoolSize() {
		return threadPoolSize;
	}

	public static int getKeepAliveTimeSecs() {
		return keepAliveTimeSecs;
	}

	public static int getScheduledThreadPoolSize() {
		return scheduledThreadPoolSize;
	}

	public static boolean isApacheTomcatJdbcPoolEnable() {
		return apacheTomcatJdbcPoolEnable;
	}

	public static String getApacheTomcatJdbcPoolUrlScheme() {
		return apacheTomcatJdbcPoolUrlScheme;
	}

	public static String getApacheTomcatJdbcPoolDriverClassName() {
		return apacheTomcatJdbcPoolDriverClassName;
	}

	public static boolean isApacheTomcatJdbcPoolJmxEnabled() {
		return apacheTomcatJdbcPoolJmxEnabled;
	}

	public static boolean isApacheTomcatJdbcPoolTestWhileIdle() {
		return apacheTomcatJdbcPoolTestWhileIdle;
	}

	public static boolean isApacheTomcatJdbcPoolTestOnBorrow() {
		return apacheTomcatJdbcPoolTestOnBorrow;
	}

	public static String getApacheTomcatJdbcPoolValidationQuery() {
		return apacheTomcatJdbcPoolValidationQuery;
	}

	public static boolean isApacheTomcatJdbcPoolTestOnReturn() {
		return apacheTomcatJdbcPoolTestOnReturn;
	}

	public static int getApacheTomcatJdbcPoolValidationIntervalMillis() {
		return apacheTomcatJdbcPoolValidationIntervalMillis;
	}

	public static int getApacheTomcatJdbcPoolMinEvictableIdleTimeMillis() {
		return apacheTomcatJdbcPoolMinEvictableIdleTimeMillis;
	}

	public static int getApacheTomcatJdbcPoolTimeBetweenEvictionRunsMillis() {
		return apacheTomcatJdbcPoolTimeBetweenEvictionRunsMillis;
	}

	public static int getApacheTomcatJdbcPoolMaxWaitMillis() {
		return apacheTomcatJdbcPoolMaxWaitMillis;
	}

	public static int getApacheTomcatJdbcPoolInitialSize() {
		return apacheTomcatJdbcPoolInitialSize;
	}

	public static int getApacheTomcatJdbcPoolMinIdle() {
		return apacheTomcatJdbcPoolMinIdle;
	}

	public static int getApacheTomcatJdbcPoolMaxIdle() {
		return apacheTomcatJdbcPoolMaxIdle;
	}

	public static int getApacheTomcatJdbcPoolMaxActive() {
		return apacheTomcatJdbcPoolMaxActive;
	}

	public static boolean isApacheTomcatJdbcPoolLogAbandoned() {
		return apacheTomcatJdbcPoolLogAbandoned;
	}

	public static boolean isApacheTomcatJdbcPoolRemoveAbandoned() {
		return apacheTomcatJdbcPoolRemoveAbandoned;
	}

	public static int getApacheTomcatJdbcPoolRemoveAbandonedTimeoutSecs() {
		return apacheTomcatJdbcPoolRemoveAbandonedTimeoutSecs;
	}

	public static String getApacheTomcatJdbcPoolJdbcInterceptors() {
		return apacheTomcatJdbcPoolJdbcInterceptors;
	}
}