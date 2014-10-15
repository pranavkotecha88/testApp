package com.samsung.data;

import junit.framework.TestCase;

import org.junit.Test;

public class ConfigTest extends TestCase {

	protected static void setUpBeforeClass() throws Exception {
	}

	protected static void tearDownAfterClass() throws Exception {
	}

	protected void setUp() throws Exception {
		super.setUp();
		Config.load();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testGetters() {
		assertNotNull(Config.getApacheTomcatJdbcPoolDriverClassName());
		assertNotNull(Config.getApacheTomcatJdbcPoolJdbcInterceptors());
		assertNotNull(Config.getApacheTomcatJdbcPoolUrlScheme());
		assertNotNull(Config.getApacheTomcatJdbcPoolValidationQuery());
		assertNotNull(Config.getMySqlDbHost());
		assertNotNull(Config.getMySqlDbName());
		assertNotNull(Config.getMySqlDbPasswd());
		assertNotNull(Config.getMySqlDbPasswd());
		assertNotNull(Config.getTag());
		assertNotNull(Config.getMySqlDbUser());
		assertNotNull(Config.getApacheTomcatJdbcPoolInitialSize());
		assertNotNull(Config.getApacheTomcatJdbcPoolMaxActive());
		assertNotNull(Config.getApacheTomcatJdbcPoolMaxIdle());
		assertNotNull(Config.getApacheTomcatJdbcPoolMaxWaitMillis());
		assertNotNull(Config.getApacheTomcatJdbcPoolMinEvictableIdleTimeMillis());
		assertNotNull(Config.getApacheTomcatJdbcPoolMinIdle());
		assertNotNull(Config.getApacheTomcatJdbcPoolRemoveAbandonedTimeoutSecs());
		assertNotNull(Config.getApacheTomcatJdbcPoolRemoveAbandonedTimeoutSecs());
		assertNotNull(Config.getApacheTomcatJdbcPoolTimeBetweenEvictionRunsMillis());
		assertNotNull(Config.getApacheTomcatJdbcPoolValidationIntervalMillis());
		assertNotNull(Config.getKeepAliveTimeSecs());
		assertNotNull(Config.getMySqlDbPort());
		assertNotNull(Config.getScheduledThreadPoolSize());
		assertNotNull(Config.getThreadPoolSize());
		
	}

}
