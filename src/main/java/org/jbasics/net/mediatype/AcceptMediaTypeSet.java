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
package org.jbasics.net.mediatype;

import org.jbasics.checker.ContractCheck;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Container to hold {@link AcceptMediaTypeRange} instances to match against them in order importance. <p>
 * MediaTypeRanges which are accepted are sorted based on their qualify factor (accept media type range parameter q).
 * </p> <p> This set is meant to actually query what is accepted by a server. After parsing the accept parameters of a
 * request or a head request you can use {@link #matchClosest(MediaType, MediaType...)} in order to match the closest
 * media type of those given. If multiple media types would match with the same qualify factor there is no guarantee
 * which is returned. In case that media types are preferred the best way is to first try to match the closest of the
 * preferred media types and if non matches than match with all available. </p>
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
public class AcceptMediaTypeSet implements SortedSet<AcceptMediaTypeRange>, Serializable {
	private final SortedSet<AcceptMediaTypeRange> acceptMediaTypes;

	/**
	 * Creates a new {@link AcceptMediaTypeSet} instance to add accept media type headers to it.
	 */
	public AcceptMediaTypeSet() {
		this.acceptMediaTypes = new TreeSet<AcceptMediaTypeRange>();
	}

	/**
	 * Add the accept media type string to the accepted media types.
	 *
	 * @param acceptMediaTypeStrings The string to add.
	 */
	public void add(final String... acceptMediaTypeStrings) {
		if (acceptMediaTypeStrings == null) {
			return;
		}
		for (String temp : acceptMediaTypeStrings) {
			add(temp);
		}
	}

	/**
	 * Add the accept media type string to the accepted media types.
	 *
	 * @param acceptMediaTypeStrings The string to add.
	 */
	public void add(final String acceptMediaTypeString) {
		String[] splits = ContractCheck.mustNotBeNullOrEmpty(acceptMediaTypeString, "acceptMediaTypeString").split(",");
		for (String split : splits) {
			String typeString = split.trim();
			if (typeString.length() > 0) {
				this.acceptMediaTypes.add(AcceptMediaTypeRange.valueOf(typeString));
			}
		}
	}

	/**
	 * Add the enumeration to the accepted media types.
	 *
	 * @param acceptMediaTypeStrings The enumeration of accept header strings.
	 */
	public void add(final Enumeration<String> acceptMediaTypeStrings) {
		if (acceptMediaTypeStrings == null) {
			return;
		}
		while (acceptMediaTypeStrings.hasMoreElements()) {
			add(acceptMediaTypeStrings.nextElement());
		}
	}

	/**
	 * Add the collection of accepted media type strings.
	 *
	 * @param acceptMediaTypeStrings A collection of media type strings.
	 */
	public void add(Collection<String> acceptMediaTypeStrings) {
		if (acceptMediaTypeStrings == null) {
			return;
		}
		for (String acceptMediaTypeString : acceptMediaTypeStrings) {
			add(acceptMediaTypeString);
		}
	}

	/**
	 * Returns the closest match of the given list of media types or if non matches the default one.
	 *
	 * @param defaultType The default type returned if non of the list matches (can be null for no default). This one is
	 *                    NOT checked against the accepted media types if you want to check that this one is accepted
	 *                    put it in the list to check and set the default to null.
	 * @param list        The list of media types to check and find the best accepted one.
	 *
	 * @return The best accepted Media type or the default if non matches.
	 */
	public MediaType matchClosest(final MediaType defaultType, final MediaType... list) {
		if (list != null && list.length > 0) {
			for (AcceptMediaTypeRange acceptType : this.acceptMediaTypes) {
				for (MediaType type : list) {
					if (acceptType.isAccepted(type)) {
						return type;
					}
				}
			}
		}
		return defaultType;
	}

	// -----------------------------------------------------------
	// SortedSet interface delegation to the internal sorted set
	// -----------------------------------------------------------

	/*
	 * (non-Javadoc)
	 * @see java.util.SortedSet#comparator()
	 */
	public Comparator<? super AcceptMediaTypeRange> comparator() {
		return this.acceptMediaTypes.comparator();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.SortedSet#subSet(java.lang.Object, java.lang.Object)
	 */
	public SortedSet<AcceptMediaTypeRange> subSet(AcceptMediaTypeRange fromElement, AcceptMediaTypeRange toElement) {
		return this.acceptMediaTypes.subSet(fromElement, toElement);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.SortedSet#headSet(java.lang.Object)
	 */
	public SortedSet<AcceptMediaTypeRange> headSet(AcceptMediaTypeRange toElement) {
		return this.acceptMediaTypes.headSet(toElement);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.SortedSet#tailSet(java.lang.Object)
	 */
	public SortedSet<AcceptMediaTypeRange> tailSet(AcceptMediaTypeRange fromElement) {
		return this.acceptMediaTypes.tailSet(fromElement);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.SortedSet#first()
	 */
	public AcceptMediaTypeRange first() {
		return this.acceptMediaTypes.first();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.SortedSet#last()
	 */
	public AcceptMediaTypeRange last() {
		return this.acceptMediaTypes.last();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Set#size()
	 */
	public int size() {
		return this.acceptMediaTypes.size();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Set#isEmpty()
	 */
	public boolean isEmpty() {
		return this.acceptMediaTypes.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Set#contains(java.lang.Object)
	 */
	public boolean contains(Object acceptMediaType) {
		return this.acceptMediaTypes.contains(acceptMediaType);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Set#iterator()
	 */
	public Iterator<AcceptMediaTypeRange> iterator() {
		return this.acceptMediaTypes.iterator();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Set#toArray()
	 */
	public Object[] toArray() {
		return this.acceptMediaTypes.toArray();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Set#toArray(T[])
	 */
	public <T> T[] toArray(T[] array) {
		return this.acceptMediaTypes.toArray(array);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Set#add(java.lang.Object)
	 */
	public boolean add(AcceptMediaTypeRange acceptMediaType) {
		return this.acceptMediaTypes.add(acceptMediaType);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Set#remove(java.lang.Object)
	 */
	public boolean remove(Object acceptMediaType) {
		return this.acceptMediaTypes.remove(acceptMediaType);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Set#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection<?> collection) {
		return this.acceptMediaTypes.containsAll(collection);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Set#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends AcceptMediaTypeRange> collection) {
		return this.acceptMediaTypes.addAll(collection);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Set#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection<?> collection) {
		return this.acceptMediaTypes.retainAll(collection);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Set#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection<?> collection) {
		return this.acceptMediaTypes.removeAll(collection);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Set#clear()
	 */
	public void clear() {
		this.acceptMediaTypes.clear();
	}
}
