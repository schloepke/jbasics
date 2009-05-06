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
package org.jbasics.types.singleton;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jbasics.pattern.singleton.Singleton;

public final class SingletonManager {
	// This instance is not a singleton since every singleton instance is
	// automaticly registered with
	// this singleton controller. Also we want to make sure that all singletons
	// are registered and known.
	// And we do not want to loose this information on the run. However we will
	// recognize a timestamp
	// when the controller was created.
	private final static SingletonManager INSTANCE = new SingletonManager();

	private final Set<Singleton<?>> singletons;
	private final long createTimestamp;

	public static SingletonManager instance() {
		return SingletonManager.INSTANCE;
	}

	public synchronized void registerSingleton(final Singleton<?> singleton) {
		if (!this.singletons.contains(singleton)) {
			this.singletons.add(singleton);
		}
	}

	public boolean isRegistered(final Singleton<?> singleton) {
		return this.singletons.contains(singleton);
	}

	public void resetAllSingletons() {
		for (Singleton<?> singleton : this.singletons) {
			synchronized (singleton) {
				singleton.resetInstance();
			}
		}
	}

	public List<InfoSnapshot> getSingletonInfos() {
		List<InfoSnapshot> result = new ArrayList<InfoSnapshot>(this.singletons.size());
		for (Singleton<?> singleton : this.singletons) {
			result.add(new InfoSnapshot(singleton, true));
		}
		return result;
	}

	public InfoSnapshot createSingletonInfoSnapshot(final Singleton<?> singleton) {
		return new InfoSnapshot(singleton, isRegistered(singleton));
	}

	public void listSingletons(final PrintStream out) {
		for (InfoSnapshot info : getSingletonInfos()) {
			out.println(info);
		}
	}

	public long getCreateTimestamp() {
		return this.createTimestamp;
	}

	private SingletonManager() {
		this.singletons = new HashSet<Singleton<?>>();
		this.createTimestamp = System.currentTimeMillis();
	}

	public static class InfoSnapshot {
		private final boolean registered;
		private final Class<?> singletonTypeClass;
		private final int singletonTypeHashCode;
		private final Class<?> factoryClass;
		private final int factoryHashCode;
		private final boolean instanciated;
		private final int instanceHashCode;
		private final Class<?> instanceClass;

		protected InfoSnapshot(final Singleton<?> singleton, final boolean registered) {
			assert singleton != null;
			this.registered = registered;
			this.singletonTypeClass = singleton.getClass();
			this.singletonTypeHashCode = singleton.hashCode();
			this.instanciated = singleton.isInstanciated();
			if (singleton instanceof AbstractManageableSingleton<?>) {
				this.factoryClass = ((AbstractManageableSingleton<?>) singleton).getFactoryClass();
				this.factoryHashCode = ((AbstractManageableSingleton<?>) singleton).getFactory().hashCode();
				this.instanceClass = ((AbstractManageableSingleton<?>) singleton).getInstanceClass();
			} else {
				this.factoryClass = null;
				this.factoryHashCode = 0;
				this.instanceClass = this.instanciated ? singleton.instance().getClass() : null;
			}
			this.instanceHashCode = this.instanciated ? singleton.instance().hashCode() : 0;
		}

		public boolean isRegistered() {
			return this.registered;
		}

		public Class<?> getSingletonTypeClass() {
			return this.singletonTypeClass;
		}

		public int getSingletonTypeHashCode() {
			return this.singletonTypeHashCode;
		}

		public Class<?> getFactoryClass() {
			return this.factoryClass;
		}

		public int getFactoryHashCode() {
			return this.factoryHashCode;
		}

		public boolean isInstanciated() {
			return this.instanciated;
		}

		public Class<?> getInstanceClass() {
			return this.instanceClass;
		}

		public int getInstanceHashCode() {
			return this.instanceHashCode;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + this.singletonTypeHashCode;
			result = prime * result + this.factoryHashCode;
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null || getClass() != obj.getClass()) {
				return false;
			}
			InfoSnapshot other = (InfoSnapshot) obj;
			if (this.factoryHashCode == 0 || this.factoryHashCode != other.factoryHashCode
			        || this.singletonTypeHashCode != other.singletonTypeHashCode) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			StringBuilder temp = new StringBuilder(this.singletonTypeClass.getSimpleName());
			temp.append(" (@").append(this.singletonTypeHashCode).append("): ");
			if (this.instanciated) {
				temp.append("INSTANCIATED ").append(this.instanceClass).append(" (@").append(this.instanceHashCode).append(")");
			} else {
				temp.append("NOT INSTANCIATED ").append(this.instanceClass).append(" (factory: ").append(this.factoryClass).append(")");
			}
			return temp.toString();
		}
	}

}
