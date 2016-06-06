package tr.com.srdc.epsos.data.model;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import tr.com.srdc.epsos.data.model.PatientDemographics.Gender;

public class PatientDemographicsTest {
	
	@Test
	public void testEmptyPatientDemographicsGetSetId() {
		final String id1 = "someId1";
		final String id2 = "someId2";

		PatientDemographics pd = new PatientDemographics();
		assertNull( pd.getId() );
		assertNull( pd.getId(313) );

		pd.setId(id1);
		assertEquals( id1, pd.getId() );
		
		pd.setId(313, id2);
		assertEquals(id2, pd.getId(313) );
	}

	@Test
	public void testEmptyPatientDemographicsGetSetId2() {
		final String id1 = "someId1";
		PatientDemographics pd = new PatientDemographics();
		
		pd.setId(512, id1);
		assertEquals(id1, pd.getId(512) );
	}

	@Test
	public void testEmptyPatientDemographicsGetSetHomeCommunity() {
		final String id1 = "someId1";
		final String id2 = "someId2";
		PatientDemographics pd = new PatientDemographics();
		assertNull( pd.getHomeCommunityId() );
		assertNull( pd.getHomeCommunityId(313) );

		pd.setHomeCommunityId(id1);
		assertEquals( id1, pd.getHomeCommunityId() );
		
		pd.setHomeCommunityId(313, id2);
		assertEquals(id2, pd.getHomeCommunityId(313) );
	}

	@Test
	public void testEmptyPatientDemographicsGetSetHomeCommunity2() {
		final String id1 = "someId1";
		PatientDemographics pd = new PatientDemographics();
		
		pd.setHomeCommunityId(512, id1);
		assertEquals(id1, pd.getHomeCommunityId(512) );
	}

	@Test
	public void testEmptyPatientDemographicsGetSetIdGetSetHomeCommunity() {
		final String id1 = "someId1";
		final String id2 = "someId2";

		PatientDemographics pd = new PatientDemographics();
		assertNull( pd.getId() );
		assertNull( pd.getId(313) );

		assertNull( pd.getHomeCommunityId() );
		assertNull( pd.getHomeCommunityId(313) );

		pd.setId(id1);
		assertEquals( id1, pd.getId() );

		pd.setHomeCommunityId(id1);
		assertEquals( id1, pd.getHomeCommunityId() );

		pd.setId(313, id2);
		assertEquals(id2, pd.getId(313) );
		
		pd.setHomeCommunityId(313, id2);
		assertEquals(id2, pd.getHomeCommunityId(313) );
	}

	@Test
	public void testPatientDemographicsGender() {
		PatientDemographics pd = new PatientDemographics();

		List<String> success = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("M");
			add("F");
			add("UN");

			add("Male");
			add("Female");
			add("Undifferentiated");
		}};
		
		for(String s : success) {
			try {
				pd.setAdministrativeGender( Gender.parseGender(s) );
			} catch(ParseException e) {
				fail("ERROR: Unable to parse gender " + s);
			}
		}

		List<String> failure = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("");
			add("Man");
			add("Woman");
			add("Unknown");
		}};

		for(String s : failure) {
			try {
				pd.setAdministrativeGender( Gender.parseGender(s) );
				fail("ERROR: Should not be able to parse gender " + s);
			} catch(ParseException e) {
				// Success
			}
		}
	}
	
	@Test
	public void testPatientDemographicsEmail() {
		PatientDemographics pd = new PatientDemographics();

		List<String> success = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("test@test.te");
			add("test.test@test.te");
			add("test.test.test@test.test");
			add("test.test.test@test.test.te");
			add("test313@test.to");
		}};
		
		for(String s : success) {
			try {
				pd.setEmail( s );
			} catch(ParseException e) {
				fail("ERROR: Unable to parse email " + s);
			}
		}

		List<String> failure = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("");
			add("@test.te");
			add("test@te");
			add("test@");
		}};

		for(String s : failure) {
			try {
				pd.setEmail( s );
				fail("ERROR: Should not be able to parse email " + s);
			} catch(ParseException e) {
				// Success
			}
		}
	}
	
	@Test
	public void testPatientDemographicsPhone() {
		PatientDemographics pd = new PatientDemographics();

		List<String> success = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("+358400123456");
			add("+358 400 123 456");
			add("+358-400-123-456");
			add("+(358) 400 123 456");
		}};
		
		for(String s : success) {
			try {
				pd.setTelephone( s );
			} catch(ParseException e) {
				fail("ERROR: Unable to parse phone " + s);
			}
		}

		List<String> failure = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("");
			add("+358xxx123");
			add("+(358 400 123 456");
		}};

		for(String s : failure) {
			try {
				pd.setTelephone( s );
				fail("ERROR: Should not be able to parse phone " + s);
			} catch(ParseException e) {
				// Success
			}
		}
	}
}
