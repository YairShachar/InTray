package intray.logic;

import java.awt.Desktop;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import intray.gui.entriesframe.EntryListFrame;
import intray.gui.mainframe.IntrayMainFrame;
import intray.gui.mainframe.panel.submitentry.EntryEvent;
import intray.model.DataBase;
import intray.model.Entry;

public class IntrayController {

	private IntrayMainFrame intrayMainFrame;
	private DataBase db;
	private String currentDbFilePath;

	public IntrayController(IntrayMainFrame intrayMainFrame) {

		this.intrayMainFrame = intrayMainFrame;
		this.currentDbFilePath = Paths.get(intrayMainFrame.getWorkingDir()).resolve(IntrayConstants.DATABASE_FILE_NAME)
				.toString();
		this.db = new DataBase(currentDbFilePath);
	}

	public String getIntrayDir() {
		return this.intrayMainFrame.getWorkingDir();
	}

	public List<Entry> getEntries() {
		return db.getEntries();
	}

	public boolean deleteEntry(int entryId) {
		Entry entry = getEntry(entryId);
		if (entry.isHasFile()) {
			deleteFile(entry.getRelatedFilePath());
		}
		if (entry.isHasImage()) {
			deleteFile(entry.getImagePath());
		}
		return db.deleteEntry(entryId);
	}

	public boolean submitAndSaveEntryEvent(EntryEvent event) {
		Entry entry = new Entry();
		String title = event.getTitle();
		boolean success = false;
		if (title.trim().equals("")) {
			return false;
		}
		entry.setTitle(title);
		switch (event.getType()) {
		case SUBMIT_TEXT:
			success = addEntry(entry);
			break;
		case SUBMIT_IMAGE:
			success = saveNewImageEntry(entry, event.getImage(), getImageFormat(event.getImage()));
			break;
		case SUBMIT_FILE:
			success = saveNewFileEntry(entry, event.getFilePath());
			break;
		default:
			break;
		}
		EntryListFrame ef = this.intrayMainFrame.getCurrentEntriesFrame();
		if (ef != null) {
			ef.refresh();
		}
		if (success) {
			try {
				saveDataBase();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return success;
	}

	public boolean addEntry(Entry entry) {
		return db.addEntry(entry);
	}

	public void deleteMultipleEntries(int[] indexes) {
		this.db.deleteMultipleEntries(indexes);
	}

	public void saveDataBase() throws IOException {
		saveDataBase(this.currentDbFilePath);
	}

	public void loadDataBase(String string) throws IOException {
		this.db.loadDataBase(string);
	}

	public String getEntryImagePath(int row) {
		return this.db.getEntries().get(row).getImagePath();
	}

	public Entry getEntry(int row) {
		return this.db.getEntry(row);
	}

	public boolean openFile(int selectedEntryId) {
		Entry selectedEntry = getEntry(selectedEntryId);
		boolean hasFile = selectedEntry.isHasFile();
		if (selectedEntry.isHasFile()) {
			openFileFromFilePath(selectedEntry.getRelatedFilePath());
		}
		return hasFile;
	}

	public void openContainingFolderFromFilePath(int selectedEntryId) {
		Entry selectedEntry = getEntry(selectedEntryId);
		if (selectedEntry.isHasFile()) {
			openContainingFolderFromFilePath(selectedEntry.getRelatedFilePath());
		}
	}

	private void saveDataBase(String path) throws IOException {
		this.db.saveDataBase(path);

	}

	private void deleteFile(String relatedFilePath) {
		if (!new File(relatedFilePath).delete()) {
			System.out.println("IntrayController: deleteFile: " + relatedFilePath + " failed");
		}
	}

	private void openFileFromFilePath(String path) {
		if (Desktop.isDesktopSupported()) {
			File f = new File(path);
			if (f.exists()) {
				try {
					Desktop.getDesktop().open(f);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("IntrayController: openFileFromFilePath:" + path + " doesn't exist");
			}
		}
	}

	private boolean saveNewImageEntry(Entry entryData, BufferedImage bufferedImage, String format) {
		try {
			if (format == null) {
				format = IntrayConstants.DEFAULT_IMAGE_FORMAT;
			}
			File imageFile = File.createTempFile("img", "." + IntrayConstants.DEFAULT_IMAGE_FORMAT,
					new File(getIntrayDir()));
			if (ImageIO.write(bufferedImage, IntrayConstants.DEFAULT_IMAGE_FORMAT, imageFile)) {
				System.out.println(imageFile.getAbsolutePath());
				entryData.setImagePath(imageFile.getAbsolutePath());
				entryData.setHasImage(true);
				return addEntry(entryData);
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean saveNewFileEntry(Entry entryData, String filePath) {
		File sourceFile = new File(filePath);
		Image image = null;
		try {
			image = ImageIO.read(sourceFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (image != null) {
			String format = getImageFormat(image);
			BufferedImage bufferedImage = (BufferedImage) image;
			return saveNewImageEntry(entryData, bufferedImage, format);
		}
		try {
			String newFileDir;
			newFileDir = Files.createDirectories(Paths.get(getIntrayDir(), "tmpFiles", "" + System.currentTimeMillis()))
					.toString();
			File copiedFile = new File((Paths.get(newFileDir, sourceFile.getName())).toString());
			Files.copy(sourceFile.toPath(), copiedFile.toPath());
			entryData.setRelatedFilePath(copiedFile.getAbsolutePath());
			sourceFile.delete();
			return addEntry(entryData);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	private String getImageFormat(Image image) {
		ImageInputStream imageInputStream;
		try {
			imageInputStream = ImageIO.createImageInputStream(image);

			Iterator<ImageReader> imageReadersList = ImageIO.getImageReaders(imageInputStream);
			if (!imageReadersList.hasNext()) {
				return null;
			}
			ImageReader reader = imageReadersList.next();
			String format = reader.getFormatName();
			return format;
		} catch (Exception e) {
			System.out.println("IntrayTextField.getImageFormat : exception in getting image format type.");
			return null;
		}
	}

	private void openContainingFolderFromFilePath(String relatedFilePath) {
		String path = new File(relatedFilePath).getParent();
		openFileFromFilePath(path);
	}

}
