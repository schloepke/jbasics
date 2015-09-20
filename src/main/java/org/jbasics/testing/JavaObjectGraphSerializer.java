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
package org.jbasics.testing;

import org.jbasics.checker.ContractCheck;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple Helper to serialize java object graphs which are {@link Serializable}. <p> In order to use the helper you need
 * to instantiate it. The helper itself is fully thread safe and can easily be used as a singleton. However the logging
 * used needs to be thread safe as well since the helper does use a private attribute of the logger instance. Since Java
 * Logging is used there should be no problem involved. </p> <p> The easiest way to use the helper is to create one use
 * it and destroy it right away (unless you serialize more than one graph at a time). </p> <p> Example:<br> {@code new
 * JavaObjectGraphSerializer().serialize(instance, new File(filename))} </p>
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
public final class JavaObjectGraphSerializer {
	private static final Level LOG_LEVEL = Level.FINE;
	private final Logger logger = Logger.getLogger(JavaObjectGraphSerializer.class.getName());

	/**
	 * Serialize the given object graph to the file name in the directory. If any of the supplied arguments is null no
	 * serialization takes place and false is returned. If serialization completed successfully true is returned.
	 *
	 * @param objectGraph The object graph
	 * @param directory   The directory to serialize the file to
	 * @param fileName    The filename to create in the directory
	 *
	 * @return True if the serialization was successful otherwise false
	 */
	public boolean serialize(Object objectGraph, File directory, String fileName) {
		if (objectGraph == null || directory == null || fileName == null) {
			if (this.logger.isLoggable(LOG_LEVEL)) {
				Logger.getLogger(JavaObjectGraphSerializer.class.getName()).log(LOG_LEVEL,
						"Skiping serialization since either object, directory or file name is null");
			}
			return false;
		}
		try {
			return serialize(objectGraph, new FileOutputStream(new File(directory, fileName)));
		} catch (FileNotFoundException e) {
			if (this.logger.isLoggable(LOG_LEVEL)) {
				Logger.getLogger(JavaObjectGraphSerializer.class.getName()).log(LOG_LEVEL, "Could not serialize due to exception", e);
			}
			return false;
		}
	}

	public boolean serialize(Object objectGraph, OutputStream out) {
		if (objectGraph == null || out == null) {
			if (this.logger.isLoggable(LOG_LEVEL)) {
				Logger.getLogger(JavaObjectGraphSerializer.class.getName()).log(LOG_LEVEL,
						"Skiping serialization since either object output stream is null");
			}
			return false;
		}
		ObjectOutputStream objectOut = null;
		try {
			objectOut = new ObjectOutputStream(out);
			objectOut.writeObject(objectGraph);
			return true;
		} catch (Exception e) {
			if (this.logger.isLoggable(LOG_LEVEL)) {
				Logger.getLogger(JavaObjectGraphSerializer.class.getName()).log(LOG_LEVEL, "Could not serialize due to exception", e);
			}
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

	public boolean serialize(Object objectGraph, File outputFile) {
		if (objectGraph == null || outputFile == null) {
			if (this.logger.isLoggable(LOG_LEVEL)) {
				Logger.getLogger(JavaObjectGraphSerializer.class.getName()).log(LOG_LEVEL,
						"Skiping serialization since either object or output fule is null");
			}
			return false;
		}
		try {
			boolean temp = serialize(objectGraph, new FileOutputStream(outputFile));
			if (this.logger.isLoggable(LOG_LEVEL)) {
				Logger.getLogger(JavaObjectGraphSerializer.class.getName()).log(Level.INFO, "Java Object Graph serilized to " + outputFile);
			}
			return temp;
		} catch (FileNotFoundException e) {
			if (this.logger.isLoggable(LOG_LEVEL)) {
				Logger.getLogger(JavaObjectGraphSerializer.class.getName()).log(LOG_LEVEL, "Could not serialize due to exception", e);
			}
			return false;
		}
	}

	public <T> T deserialize(Class<T> type, File directory, String fileName) {
		try {
			return deserialize(type, new FileInputStream(new File(ContractCheck.mustNotBeNull(directory, "directory"), ContractCheck.mustNotBeNull(
					fileName, "fileName"))));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found " + fileName + " in directory " + directory, e);
		}
	}

	public <T> T deserialize(Class<T> type, InputStream in) {
		ObjectInputStream objectIn = null;
		try {
			objectIn = new ObjectInputStream(ContractCheck.mustNotBeNull(in, "in"));
			return type.cast(objectIn.readObject());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Cannot deserialize instance of " + type, e);
		} catch (IOException e) {
			throw new RuntimeException("Cannot deserialize instance of " + type, e);
		} finally {
			if (objectIn != null) {
				try {
					objectIn.close();
				} catch (IOException e) {
					// we ignore the close
				}
			}
		}
	}

	public <T> T deserialize(Class<T> type, File file) {
		try {
			return deserialize(type, new FileInputStream(ContractCheck.mustNotBeNull(file, "file")));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found " + file, e);
		}
	}
}
