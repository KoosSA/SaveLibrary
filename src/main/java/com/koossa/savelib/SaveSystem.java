package com.koossa.savelib;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

/**
 * Used to interface with the Save System. Handles saving and loading objects from storage.
 * @author KoosSA
 *
 */
public class SaveSystem {

	private static Gson gson;
	private static File saveFolder;
	private static File dataFolder;
	private static String writeStr;
	private static File readFile, writeFile;

	/**
	 * Initializes the save system. Should be called only once before the library is used usually on program startup.
	 * @param saveFolder The main folder to which data is saved. 
	 * @param dataFolder The main folder to which internal data is saved.
	 */
	public static void init(File saveFolder, File dataFolder) {
		gson = new Gson();
		SaveSystem.saveFolder = saveFolder;
		SaveSystem.dataFolder = dataFolder;
		if (!saveFolder.exists()) {
			saveFolder.mkdirs();
		}
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}
	}

	/**
	 * Saves an object to storage.
	 * @param obj The object to be saved
	 * @param isGameData Is the data saved part of the internal data or not? Best to leave false at the moment.
	 * @param subfolder Sub folder to which to save data
	 * @param name of the saved file
	 * @return true / false depending on if the data was saved or not
	 */
	public static boolean save(Object obj, boolean isGameData, String subfolder, String name) {
		File folder = isGameData ? dataFolder : saveFolder;
		File sub = new File(folder, subfolder);
		if (!sub.exists()) {
			sub.mkdirs();
		}
		if (gson == null) {
			System.err.println("Please Initialise SaveSystem, aborting saving file.");
			return false;
		}
		writeStr = gson.toJson(obj);
		writeFile = new File(sub, name);
		if (writeStr.length() > 0) {
			try {
				FileWriter writer = new FileWriter(writeFile);
				writer.write(writeStr);
				writer.close();
				return true;
			} catch (IOException e) {
				System.err.println("Failed to save file: " + name);
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	/**
	 * Loads a saved file to an object. If no saved file i found, a new object with default implementation will be returned.
	 * @param <T> The type of object to return.
	 * @param clazz Class of the object to be loaded. Ideally a class implementing {@link ISavable} interface
	 * @param isGameData Is the data saved part of the internal data or not? Best to leave false at the moment.
	 * @param subfolder Sub folder to which to save data
	 * @param name of the saved file
	 * @return the loaded object or null
	 */
	public static <T> T load(Class<T> clazz, boolean isGameData, String subfolder, String name) {
		if (gson == null) {
			System.err.println("Please Initialise SaveSystem, aborting file loading.");
			return null;
		}
		File folder = isGameData ? dataFolder : saveFolder;
		File sub = new File(folder, subfolder);
		if (!sub.exists()) {
			sub.mkdirs();
		}
		try {
			readFile = new File(sub, name);
			JsonReader reader = new JsonReader(new FileReader(readFile));
			Object o = gson.fromJson(reader, clazz);
			reader.close();
			return clazz.cast(o);
		} catch (IOException e) {
			System.err.println("Failed to load file: " + name + " Creating default implementation.");
			try {
				T o = clazz.cast(clazz.getConstructors()[0].newInstance());
				if (o instanceof ISavable) {
					((ISavable) o).save(isGameData, name, subfolder);
				}
				return o;
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | SecurityException e1) {
				e1.printStackTrace();
				System.err.println("Failed to load file: " + name);
			}
		}
		return null;
	}
	
	/**
	 * Saves the object into the specified folder with the specified name.
	 * @param folder folder to save the file to
	 * @param name the name of the saved file
	 */
	public static boolean saveTo(Object obj, File folderToSaveTo, String name) {
		if (!folderToSaveTo.exists()) {
			folderToSaveTo.mkdirs();
		}
		if (gson == null) {
			System.err.println("Please Initialise SaveSystem, aborting saving file.");
			return false;
		}
		writeStr = gson.toJson(obj);
		writeFile = new File(folderToSaveTo, name);
		if (writeStr.length() > 0) {
			try {
				FileWriter writer = new FileWriter(writeFile);
				writer.write(writeStr);
				writer.close();
				return true;
			} catch (IOException e) {
				System.err.println("Failed to save file: " + name);
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Loads the data to a object of given class. If no saved data is found in the specified location, a new implementation of the class is created.
	 * @param <T> The type of object to return
	 * @param clazz The class the data belongs to
	 * @param folder the folder where the saved data can be found
	 * @param name the name of the saved data file
	 * @return the loaded object or null
	 */
	public static <T> T loadFrom(Class<T> clazz, File folderToLoadFrom, String name) {
		if (gson == null) {
			System.err.println("Please Initialise SaveSystem, aborting file loading.");
			return null;
		}
		if (!folderToLoadFrom.exists()) {
			folderToLoadFrom.mkdirs();
		}
		try {
			readFile = new File(folderToLoadFrom, name);
			JsonReader reader = new JsonReader(new FileReader(readFile));
			Object o = gson.fromJson(reader, clazz);
			reader.close();
			return clazz.cast(o);
		} catch (IOException e) {
			System.err.println("Failed to load file: " + name + " Creating default implementation.");
			try {
				T o = clazz.cast(clazz.getConstructors()[0].newInstance());
				if (o instanceof ISavable) {
					((ISavable) o).saveTo(folderToLoadFrom, name);
				}
				return o;
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | SecurityException e1) {
				e1.printStackTrace();
				System.err.println("Failed to load file: " + name);
			}
		}
		return null;
	}

}
