package com.koossa.savelib;

public interface ISavable {
	
	default void save() {
		SaveSystem.save(this);
	}
	
	default <T> T load(Class<T> clazz) {
		return SaveSystem.load(clazz);
	}

}
