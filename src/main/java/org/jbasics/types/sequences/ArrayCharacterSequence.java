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
package org.jbasics.types.sequences;

/**
 * {@link CharSequence} which is based on an array of characters without copying or modifying the array. <p> Because
 * this {@link CharSequence} does not alter or copy the array the caller needs to be sure that the character array is
 * not altered from outside. If so this {@link CharSequence} would change it's behavior. The benefit of this {@link
 * CharSequence} is that a portion of a char array can be used as sequence offering additional sub sequences without the
 * need to clone or copy the char array. This is useful when the array is quite large and only a portion needs to be
 * used as sequence. </p> <p> In case one requires a {@link CharSequence} with the guarantee that it cannot be altered
 * from the outside ones it is created java offers the quite useful class {@link String} for this. </p>
 *
 * @author Stephan Schloepke
 */
public class ArrayCharacterSequence implements CharSequence {
	private final char[] characters;
	private final int offset;
	private final int length;

	/**
	 * Creates an {@link CharSequence} for the given character array using the whole array.
	 *
	 * @param characters The characters
	 */
	public ArrayCharacterSequence(final char[] characters) {
		this(characters, 0, characters.length);
	}

	/**
	 * Create a {@link CharSequence} for the given characters starting at the given offset with the given length.
	 *
	 * @param characters The array (must not be null).
	 * @param offset     The offset in the array (must be in the range of the array length).
	 * @param length     The length to use (must be in the range of the array length without the the offset (offset +
	 *                   length &lt; chars.length).
	 */
	public ArrayCharacterSequence(final char[] characters, final int offset, final int length) {
		if (characters == null) {
			throw new IllegalArgumentException("Null parameter: characters");
		}
		this.characters = characters;
		if (offset < 0) {
			throw new IndexOutOfBoundsException("Offset cannot be negative");
		}
		if (offset >= characters.length) {
			throw new IndexOutOfBoundsException("Offset exceeds character array length");
		}
		this.offset = offset;
		this.length = length;
	}

	/**
	 * Returns the length of this {@link CharSequence}.
	 *
	 * @return The length
	 *
	 * @see CharSequence#length()
	 */
	public int length() {
		return this.length;
	}

	/**
	 * Returns the character at the given index.
	 *
	 * @param index The index.
	 *
	 * @return The character at the index.
	 *
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @see CharSequence#charAt(int)
	 */
	public char charAt(final int index) {
		if (index >= this.length) {
			throw new IndexOutOfBoundsException();
		}
		return this.characters[this.offset + index];
	}

	/**
	 * Returns a sub {@link CharSequence} starting at the start and up to the end.
	 *
	 * @param start The start index (must be in range).
	 * @param end   The end index (must be in range and is exclusive).
	 *
	 * @return A {@link CharSequence} representing a sub sequence of this {@link CharSequence}.
	 *
	 * @throws IndexOutOfBoundsException If the start or end is out of the index.
	 * @see CharSequence#subSequence(int, int)
	 */
	public CharSequence subSequence(final int start, final int end) {
		if (start < 0 || start >= this.length) {
			throw new IndexOutOfBoundsException("Start index out of range");
		}
		if (end < start || end > this.length) {
			throw new IndexOutOfBoundsException("End index out of range");
		}
		return new ArrayCharacterSequence(this.characters, this.offset + start, end - start);
	}

	/**
	 * Returns a string as of {@link CharSequence#toString()} contract.
	 *
	 * @return A {@link String} of this {@link CharSequence}
	 *
	 * @see CharSequence#toString()
	 */
	@Override
	public String toString() {
		return new String(this.characters, this.offset, this.length);
	}
}
