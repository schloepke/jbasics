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
