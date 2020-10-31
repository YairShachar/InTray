package intray.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class DataBase {
	ArrayList<Entry> entries;
	String dbPath;

	public DataBase() {
		entries = new ArrayList<Entry>();
	}

	public DataBase(String path) {
		entries = new ArrayList<Entry>();
		loadDataBase(path);

	}

	public boolean addEntry(Entry entry) {
		return entries.add(entry);
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public boolean deleteEntry(int entryId) {
		System.out.println("DataBase: deleting entry id " + entryId);
		return entries.remove(entryId) != null;
	}

	public void deleteMultipleEntries(int[] indexes) {
		Vector<Entry> todelete = new Vector<Entry>();
		for (int i = 0; i < indexes.length; i++) {
			todelete.add(entries.get(indexes[i]));
		}
		for (Entry entry : todelete) {
			entries.remove(entry);
		}
	}

	public void saveDataBase(String path) throws IOException {
		File file = new File(path);
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		Entry[] entriesArray = entries.toArray(new Entry[entries.size()]);
		oos.writeObject(entriesArray);
		oos.close();
	}

	public boolean loadDataBase(String path) {
		File file = new File(path);
		if (!file.exists()) {
			entries.clear();
			try {
				saveDataBase(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Entry[] entriesArray;
			entriesArray = (Entry[]) ois.readObject();
			entries.clear();
			entries.addAll(Arrays.asList(entriesArray));
			ois.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Entry getEntry(int row) {
		if (row < 0) {
			return null;
		}
		return entries.get(row);
	}

}
