package com.koossa.savelib;

/**
 * Add this interface to any Class that can be saved for later reference.
 * @author KoosSA
 *
 */
public interface ISavable {

	/**
	 * Saves the current object to the file system.
	 * @param isGameData Is the data saved part of the internal data or not? Best to leave false at the moment.
	 * @param name of the file to save the data as
	 * @param folderName where the data is being saved
	 */
	default void save(boolean isGameData, String name, String folderName) {
		SaveSystem.save(this, isGameData, folderName, name);
	}

	/**
	 * Loads a save file to object from the file system.
	 * @param <T> The type of object to return.
	 * @param clazz represents the class of data to be loaded
	 * @param isGameData Is the data saved part of the internal data or not? Best to leave false at the moment.
	 * @param name of the saved file
	 * @param folderName where the saved file can be found.
	 * @return the loaded object or null
	 */
	default <T> T load(Class<T> clazz, boolean isGameData, String name, String folderName) {
		return SaveSystem.load(clazz, isGameData, folderName, name);
	}

}
