package com.samsung.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.samsung.data.Config;

public class MySqlDbTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private static final String TAG = MySqlDbTest.class.getSimpleName();
	private static final String USERS_TABLE = "users";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");	
	
	@Before
	public void setUp() throws Exception {
		Config.load();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInstance() {
		assertNotNull(MySqlDb.getInstance());
		assertSame(MySqlDb.getInstance(), MySqlDb.getInstance());
	}

	@Test
	public void testExecuteQuery() {
		MySqlDb cs = MySqlDb.getInstance();
		
		// Delete existing test contents, if any
		String s = "DELETE FROM " + USERS_TABLE + " WHERE NAME = 'xyz'";
		cs.executeUpdate(s);

		// Insert tests
		s = "INSERT INTO " + USERS_TABLE +" (CREATED_AT, NAME, ADDRESS, EMAIL_ID, PHONE_NUMBER) "
				+ "VALUES ('"+sdf.format(new Date())+"','xyz', '135 rio','xyz@abc.com','1234567890')";
		assertEquals(1,cs.executeUpdate(s));
		s = "INSERT INTO " + USERS_TABLE +" (CREATED_AT, NAME, ADDRESS, EMAIL_ID, PHONE_NUMBER) "
				+ "VALUES ('"+sdf.format(new Date())+"','xyz', '3465 agate','abc@xyz.com','0123456789')";
		assertEquals(1,cs.executeUpdate(s));
		s = "INSERT INTO " + USERS_TABLE +" (CREATED_AT, NAME, ADDRESS, EMAIL_ID, PHONE_NUMBER) "
				+ "VALUES ('"+sdf.format(new Date())+"','xyz', '133 Dell','def@hijk.com','2345678')";
		assertEquals(1,cs.executeUpdate(s));

		// Query tests
		final DBResource dbres = MySqlDb.getInstance().executeQuery(null, "SELECT * FROM " + USERS_TABLE +" WHERE NAME = 'xyz' AND PHONE_NUMBER='1234567890'");
		try {
			final ResultSet rs = dbres.getResultset();
			while (rs.next()) {
				assertEquals(rs.getString("ADDRESS"), "135 rio");
				assertEquals(rs.getString("EMAIL_ID"), "xyz@abc.com");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbres.closeAll();
		}
	}
	
	@Test
	public void testUpdateQuery() {
		final String NAME = "xyz";
		
		MySqlDb cs = MySqlDb.getInstance();
		
		// Delete existing test contents, if any
		String query = "DELETE FROM " + USERS_TABLE +" WHERE NAME='" + NAME + "'";
		cs.executeUpdate(query);

		// Insert entry with 0 user views and Id=123
		query = "INSERT INTO " + USERS_TABLE +" (CREATED_AT, NAME, ADDRESS, EMAIL_ID, PHONE_NUMBER) "
				+ "VALUES ('"+sdf.format(new Date())+"','xyz', '135 rio','xyz@abc.com','1234567890')";
		cs.executeUpdate(query);
		
		// part 1: double check it has 0 USER_VIEWS
		int phone_number = -1;
		query = "SELECT PHONE_NUMBER FROM " + USERS_TABLE + " WHERE NAME ='" + NAME + "' AND EMAIL_ID='xyz@abc.com'";
		final DBResource dbres1 = cs.executeQuery(null, query);
		try {
			final ResultSet rs = dbres1.getResultset();
			// only expect 1 entry from query
			while (rs.next()) {
				phone_number = rs.getInt("PHONE_NUMBER");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		assertEquals(1234567890, phone_number);
		dbres1.closeAll();
	}
}
