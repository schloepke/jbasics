package org.jbasics.parser;

import java.lang.reflect.Method;

import org.jbasics.pattern.builder.Builder;

public class BuilderState {
	private final Builder<?> builder;
	private final Builder<?> parent;
	private final Method parentMethodToAdd;

	public BuilderState(Builder<?> builder, Builder<?> parent, Method parentMethodToAdd) {
		this.builder = builder;
		this.parent = parent;
		this.parentMethodToAdd = parentMethodToAdd;
	}

	public void buildAndAdd() {
		try {
			this.parentMethodToAdd.invoke(this.parent, this.builder.build());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
