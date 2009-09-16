/*
 * Copyright (c) 2009 Stephan Schloepke and innoQ Deutschland GmbH
 *
 * Stephan Schloepke: http://www.schloepke.de/
 * innoQ Deutschland GmbH: http://www.innoq.com/
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
package org.jbasics.testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public final class JavaObjectGraphSerializer {

	public static boolean serialize(Object objectGraph, File directory, String outputFileName) {
		if (objectGraph == null || directory == null || outputFileName == null) {
			return false;
		}
		try {
			return serialize(objectGraph, new FileOutputStream(new File(directory, outputFileName)));
		} catch (FileNotFoundException e) {
			return false;
		}
	}

	public static boolean serialize(Object objectGraph, File outputFile) {
		if (objectGraph == null || outputFile == null) {
			return false;
		}
		try {
			return serialize(objectGraph, new FileOutputStream(outputFile));
		} catch (FileNotFoundException e) {
			return false;
		}
	}

	public static boolean serialize(Object objectGraph, OutputStream out) {
		if (objectGraph == null || out == null) {
			return false;
		}
		ObjectOutputStream objectOut = null;
		try {
			objectOut = new ObjectOutputStream(out);
			objectOut.writeObject(objectGraph);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (objectOut != null) {
					objectOut.close();
				}
			} catch (IOException e) {
				// we ignore any close problem
			}
		}
	}

}
