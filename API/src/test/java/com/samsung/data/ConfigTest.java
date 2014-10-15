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
	}

}
