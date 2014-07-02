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
package org.jbasics.pattern.pooling;

/**
 * A Pool keeps instances which can be acquired and released. <p> A Pool is useful when complex to create instances
 * should be pooled. At the time a pool instance is acquired the pool guarantees that no other caller can acquire the
 * same instance unless it is guaranteed that the instance is thread safe. After releasing the instance the caller must
 * no longer us the instance or keep a reference to the instance. It is not guaranteed that the next call to acquire
 * returns the same instance. </p> <p> In case that the call to the release method returns false the pool does not
 * accept the instance back into its pool. This is commonly the case when the pool size is limited. This should not be
 * seen as an error but rather give the caller the information that the caller now can do anything with the instance
 * even keeping it for later use. It is recommended though to not keep the instance and instead let the garbage
 * collector clean it up since the policy of the pool usually limits the instances due to a reason which can not be seen
 * by the caller. In the case that the caller wants to keep the instance it should be kept in a softly reachable manner
 * so the Garbage Collector can finalize the instance on memory demand. In such a case the caller must acquire a new
 * instance once the softly reachable reference is finalized. </p>
 *
 * @param <T> The type of the pool instance.
 *
 * @author Stephan Schloepke
 */
public interface Pool<T> {
	/**
	 * Acquire an instance from the pool which becomes private to the caller upon releasing it to the pool. <p>
	 * Implementor must guarantee that the acquire returns an instance or otherwise throw a runtime exception. </p>
	 *
	 * @return The instance acquired (Can not be null).
	 */
	T acquire();

	/**
	 * Releases the instance to the pool.
	 *
	 * @param object The instance to release (should not be null).
	 *
	 * @return True if the instance is accepted back to the pool and must not be used or referenced by the caller.
	 */
	boolean release(T object);
}
