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
package org.jbasics.types.factories;

import org.jbasics.pattern.delegation.MutableDelegate;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.types.delegates.LazyDelegate;

import java.util.*;

/**
 * This is the main place where all JBasics types create their required collection instances. <p> In order to replace
 * the collections used by JBasics you can set the Delegate o the Factory. This should normally happen before the
 * CollectionsFactory is used. However there is the guarantee that JBasics never casts a returned value to anything
 * other than the interface it returns. </p> <p> Using this is especially useful when you need to replace a list
 * instance by a test instance or any sort of proxy in order to test the lazy creation of instances somewhere in the
 * framework. </p> <p> The contract defined that any new...Instance method called needs to return a newly created empty
 * instance of the given type. In certain cases another implicit rule applies as well like when creating a {@link
 * RandomAccess} list it is required that the list has constant time accessing elements randomly. For more information
 * see the explicit method. </p> <p> If at any given time it is required to switch back from a changed factory you can
 * remove the delegate in order to let the next usage trigger the lazy creation of the default factory. </p>
 *
 * @author Stephan Schloepke
 * @since 1.0.0
 */
public class CollectionsFactory {
	public static final MutableDelegate<CollectionsFactory> DELEGATE = new LazyDelegate<CollectionsFactory>(
			new Factory<CollectionsFactory>() {
				public CollectionsFactory newInstance() {
					return new CollectionsFactory();
				}
			});

	public static final CollectionsFactory instance() {
		return DELEGATE.delegate();
	}

	public <E> List<E> newRandomAccessListInstance() {
		return newListInstance();
	}

	public <E> List<E> newListInstance() {
		return new ArrayList<E>();
	}

	public <E> List<E> newSequentialAccessListInstance() {
		return newListInstance();
	}

	public <E> Queue<E> newQueueInstance() {
		return new LinkedList<E>();
	}

	public <E> Set<E> newSetInstance() {
		return new HashSet<E>();
	}

	public <E> Set<E> newOrderedSetInstance() {
		return new LinkedHashSet<E>();
	}

	public <E> SortedSet<E> newSortedSetInstance() {
		return new TreeSet<E>();
	}

	public <K, V> Map<K, V> newMapInstance() {
		return new HashMap<K, V>();
	}

	public <K, V> Map<K, V> newOrderedMapInstance() {
		return new LinkedHashMap<K, V>();
	}

	public <K, V> SortedMap<K, V> newSortedMapInstance() {
		return new TreeMap<K, V>();
	}
}
