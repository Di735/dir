package GUI;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class ImageFilter extends FileFilter{
	public boolean accept(File f) {
		String filename = f.getName();
		return filename.endsWith(".jpeg") || filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".bmp");
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
