/*
 * Copyright (c) 2009-2015
 * 	IT-Consulting Stephan Schloepke (http://www.schloepke.de/)
 * 	klemm software consulting Mirko Klemm (http://www.klemm-scs.com/)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jbasics.types.strategy;

import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

@SuppressWarnings("nls")
public class PropertySubstitutionStrategyTest {

	@Test
	public void testQuickAndSimple() {
		PropertySubstitutionStrategy temp = new PropertySubstitutionStrategy();
		System.setProperty("jbasics.hello", "Hello");
		System.setProperty("jbasics.helloWorld", "${jbasics.hello} World!");
		System.getProperties().remove("jbasics.test"); // seems another test uses this too!
		String result = temp.substitute("-- ${jbasics.helloWorld} -- ${jbasics.test:Yo!} --").toString();
		Assert.assertEquals("-- Hello World! -- Yo! --", result);
		Properties props = new Properties();
		props.setProperty("jbasics.test", "Ya!");
		result = temp.substitute("-- ${jbasics.helloWorld} -- ${jbasics.test:Yo!} --", props).toString();
		Assert.assertEquals("-- Hello World! -- Ya! --", result);
	}
}
