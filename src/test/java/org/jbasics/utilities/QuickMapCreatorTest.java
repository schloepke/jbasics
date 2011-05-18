package org.jbasics.utilities;

import java.util.Map;

import org.jbasics.testing.Java14LoggingTestCase;
import org.jbasics.utilities.QuickMapCreator;
import org.junit.Test;

import junit.framework.Assert;


public class QuickMapCreatorTest extends Java14LoggingTestCase {

	@Test
	public void testMap() {
		QuickMapCreator<String, String> mapCreator = new QuickMapCreator<String, String>("Givenname", "Surname", "Age");
		Map<String, String> john = mapCreator.orderedMap("John", "Doe", "37");
		Map<String, String> marie = mapCreator.orderedMap("Marie", "Donnovan");
		this.logger.info("Map for John = " + john);
		this.logger.info("Map for Marie = " + marie);
		Assert.assertEquals("John", john.get("Givenname"));
		Assert.assertEquals("Doe", john.get("Surname"));
		Assert.assertEquals("37", john.get("Age"));
		Assert.assertEquals("Marie", marie.get("Givenname"));
		Assert.assertEquals("Donnovan", marie.get("Surname"));
		Assert.assertEquals(null, marie.get("Age"));
	}

}
