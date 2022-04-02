package com.koossa.savelib;

public interface ISavable {

	default void save(boolean isGameData, String name, String folderName) {
		SaveSystem.save(this, isGameData, folderName, name);
	}

	default <T> T load(Class<T> clazz, boolean isGameData, String name, String folderName) {
		return SaveSystem.load(clazz, isGameData, folderName, name);
	}

}
