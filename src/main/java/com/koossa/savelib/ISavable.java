package com.koossa.savelib;

import java.io.File;

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
	
	/**
	 * Saves the object into the specified folder with the specified name.
	 * @param folder folder to save the file to
	 * @param name the name of the saved file
	 */
	default void saveTo(File folder, String name) {
		SaveSystem.saveTo(this, folder, name);
	}
	
	/**
	 * Loads the data to a object of given class. If no saved data is found in the specified location, a new implementation of the class is created.
	 * @param <T> The type of object to return
	 * @param clazz The class the data belongs to
	 * @param folder the folder where the saved data can be found
	 * @param name the name of the saved data file
	 * @return the loaded object or null
	 */
	default <T> T loadFrom(Class<T> clazz, File folder, String name) {
		return SaveSystem.loadFrom(clazz, folder, name);
	}

}
