package com.koossa.savelib;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class SaveSystem {
	
	private static Gson gson;
	private static File saveFolder;
	private static String writeStr;
	private static File readFile, writeFile;
	
	public static void init(File folder) {
		gson = new Gson();
		saveFolder = folder;
		if (!saveFolder.exists()) {
			saveFolder.mkdirs();
		}
	}
	
	public static boolean save(Object obj) {
		if (gson == null) {
			System.err.println("Please Initialise SaveSystem, aborting saving file.");
			return false;
		}
		writeStr = gson.toJson(obj);
		writeFile = new File(saveFolder, obj.getClass().getSimpleName() + ".json");
		if (writeStr.length() > 0) {
			try {
				FileWriter writer = new FileWriter(writeFile);
				writer.write(writeStr);
				writer.close();
				return true;
			} catch (IOException e) {
				System.err.println("Failed to save file: " + obj.getClass().getSimpleName() + ".json");
				e.printStackTrace();
				return false;
			} 
		}
		return false;
	}
	
	public static <T> T load(Class<T> clazz) {
		if (gson == null) {
			System.err.println("Please Initialise SaveSystem, aborting file loading.");
			return null;
		}
		try {
			readFile = new File(saveFolder, clazz.getSimpleName() + ".json");
			JsonReader reader = new JsonReader(new FileReader(readFile));
			Object o = gson.fromJson(reader, clazz);
			reader.close();
			return clazz.cast(o);
		} catch (IOException e) {
			System.err.println("Failed to load file: " + clazz.getSimpleName() + ".json. Creating default implementation.");
			try {
				T o = clazz.cast(clazz.getConstructors()[0].newInstance());
				if (o instanceof ISavable) {
					((ISavable) o).save();
				}
				return o;
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | SecurityException e1) {
				e1.printStackTrace();
				System.err.println("Failed to load file: " + clazz.getSimpleName() + ".json");
			}
		}
		return null;
	}
	
}
