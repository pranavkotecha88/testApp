package com.samsung.utils;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LogTest {
	
	private static final String TAG = LogTest.class.getSimpleName();

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetPriorityTitle() {
		assertThat(Log.getPriorityTitle(Log.VERBOSE)).isNotNull();
		assertThat(Log.getPriorityTitle(Log.DEBUG)).isNotNull();
		assertThat(Log.getPriorityTitle(Log.INFO)).isNotNull();
		assertThat(Log.getPriorityTitle(Log.WARN)).isNotNull();
		assertThat(Log.getPriorityTitle(Log.ERROR)).isNotNull();
		assertThat(Log.getPriorityTitle(Log.ASSERT)).isNotNull();
		assertThat(Log.getPriorityTitle(Integer.MAX_VALUE)).isNull();
	}

	@Test
	public void testGetStackTraceString() {
		final Throwable tr = new Throwable("test throwable");
		assertThat(Log.getStackTraceString(tr)).isNotNull();
	}
}
	