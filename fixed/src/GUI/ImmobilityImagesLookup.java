package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.IconUIResource;

import Logic.Immobility;

public class ImmobilityImagesLookup extends TemplateDialog{

	private JPanel controlPanel;
	private JButton previous, next;
	private int currentIndex = 0;
	private PicturePanel picturePanel = null;
	private ArrayList<String> filenames;
	
	public ImmobilityImagesLookup(Immobility immo) {
		super("Просмотр изображений: " + immo, 500, 500);
		
		if (immo.getPicturesPaths().size() == 0) {
			infoMessage("У данного объекта нет изображений");
			dispose();
			return;
		}
		
		filenames = immo.getPicturesPaths();
		
		System.err.println(filenames);
		setLayout(new BorderLayout());
		
		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());
		controlPanel.setPreferredSize(new Dimension(30, 30));
		
		previous = createLargeButton("Предыдущая", "previous");
		controlPanel.add(previous);
		
		next = createLargeButton("Следующая", "next");
		controlPanel.add(next);
		
		add (controlPanel, BorderLayout.SOUTH);
		updatePicturePanel();
		
		setResizable(false);
		setVisible(true);
	}
	
	private void updatePicturePanel() {
		if (picturePanel != null)
			remove(picturePanel);
		
		try {
			BufferedImage img = ImageIO.read(new File(filenames.get(currentIndex)));
			picturePanel = new PicturePanel(img);
			add (picturePanel, BorderLayout.CENTER);
		} catch (IOException e) {
			errorMessage("Не удалось загрузить изображение " + filenames.get(currentIndex) + ".");
		}
		
		revalidate();
	}

	private class PicturePanel extends JPanel{
		private JLabel label;
		
		PicturePanel(BufferedImage img) {
			Icon icon = new ImageIcon(img);
			label = new JLabel(icon);
			add(label);
		}
		
		
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		
		String command = e.getActionCommand();
		switch(command) {
		case "next": {
			currentIndex++;
			if (currentIndex >= filenames.size())
				currentIndex = 0;
			updatePicturePanel();
			break;
		}
		
		case "previous": {
			currentIndex--;
			if (currentIndex < 0)
				currentIndex = filenames.size() - 1;
			updatePicturePanel();
			break;
		}
		}
	}
}
