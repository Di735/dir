package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;

import Logic.Global;
import Logic.Immobility;
import Logic.Inhabited;
import Logic.Uninhabited;

public class AddImmobilityForm extends TemplateDialog {
	private JRadioButton inhabited, uninhabited;

	private JPanel generalPanel;

	private JLabel perscriptionLabel;
	private JTextField perscription;
	
	private JLabel addressLabel;

	private JLabel cityLabel;
	private JTextField city;

	private JLabel streetLabel;
	private JTextField street;

	private JLabel houseLabel;
	private JTextField house;

	private JLabel appartmentLabel;
	private JTextField appartment;

	private JLabel floorLabel;
	private JSpinner floor;
	private JLabel squareLabel;
	private JSpinner square;

	private JPanel inhabitedPanel;

	private JCheckBox hasFurniture;
	private JCheckBox hasTelephone;
	private JCheckBox hasInternetAccess;
	private JCheckBox hasWasher;
	private JCheckBox hasFrigde;

	private JButton addImage;
	private JButton create, exit;

	private ArrayList<String> pictureFiles = new ArrayList<String>();

	public AddImmobilityForm() {
		super("Создание объекта недвижимости", 435, 350);
		setResizable(false);
		setLayout(new BorderLayout());

		generalPanel = new JPanel();
		generalPanel.setBorder(BorderFactory.createEtchedBorder());
		generalPanel.setPreferredSize(new Dimension(220, 220));
		add(generalPanel, BorderLayout.WEST);

		//Жилое/нежилое
		{
			ButtonGroup bg = new ButtonGroup();

			inhabited = createRadioButton("Жилое");
			uninhabited = createRadioButton("Нежилое");

			bg.add(inhabited);
			bg.add(uninhabited);

			generalPanel.add(inhabited);
			generalPanel.add(uninhabited);
		}

		//Предназначение
		{
			perscriptionLabel = new JLabel("Тип");
			perscriptionLabel.setPreferredSize(VERY_SHORT_LABEL_SIZE);
			generalPanel.add(perscriptionLabel);
			
			perscription = new JTextField();
			perscription.setPreferredSize(SHORT_TEXT_FIELD_SIZE);
			generalPanel.add(perscription);
		}
		
		//Адрес
		{
			addressLabel = new JLabel("Адрес");
			addressLabel.setPreferredSize(LONG_LABEL_SIZE);
			generalPanel.add(addressLabel);

			cityLabel = new JLabel("Город");
			cityLabel.setPreferredSize(VERY_SHORT_LABEL_SIZE);
			generalPanel.add(cityLabel);
			city = new JTextField();
			city.setPreferredSize(SHORT_TEXT_FIELD_SIZE);
			generalPanel.add(city);

			streetLabel = new JLabel("Улица");
			streetLabel.setPreferredSize(VERY_SHORT_LABEL_SIZE);
			generalPanel.add(streetLabel);
			street = new JTextField();
			street.setPreferredSize(SHORT_TEXT_FIELD_SIZE);
			generalPanel.add(street);

			houseLabel = new JLabel("Дом");
			houseLabel.setPreferredSize(VERY_SHORT_LABEL_SIZE);
			generalPanel.add(houseLabel);
			house = new JTextField();
			house.setPreferredSize(SHORT_TEXT_FIELD_SIZE);
			generalPanel.add(house);

			appartmentLabel = new JLabel("Квартира");
			appartmentLabel.setPreferredSize(VERY_SHORT_LABEL_SIZE);
			generalPanel.add(appartmentLabel);
			appartment = new JTextField();
			appartment.setPreferredSize(SHORT_TEXT_FIELD_SIZE);
			generalPanel.add(appartment);

			inhabitedPanel = new JPanel();
			inhabitedPanel.setPreferredSize(new Dimension(200, 200));
			inhabitedPanel.setBorder(BorderFactory.createEtchedBorder());
			add(inhabitedPanel, BorderLayout.CENTER);
		}

		//Этаж и площадь
		{
			floorLabel = new JLabel("Этаж");
			floorLabel.setPreferredSize(SHORT_LABEL_SIZE);
			generalPanel.add(floorLabel);

			floor = createSpinner(0, -2, 100);
			generalPanel.add(floor);

			squareLabel = new JLabel("Площадь");
			squareLabel.setPreferredSize(SHORT_LABEL_SIZE);
			generalPanel.add(squareLabel);

			square = createSpinner(0.0, 0.0, 10000.0);
			generalPanel.add(square);
		}

		//Кнопки
		{
			addImage = createLargeButton("Добавить изображение", "add image");
			generalPanel.add(addImage);

			create = createButton("Создать", "create");
			generalPanel.add(create);

			exit = createButton("Выход", "exit");
			generalPanel.add(exit);
		}

		{

			inhabitedPanel.add(new JLabel("Состояние жилья:"));

			hasFurniture = createLargeCheckBox("Есть мебель", "furniture");
			inhabitedPanel.add(hasFurniture);

			hasTelephone = createLargeCheckBox("Есть телефон", "telephone");
			inhabitedPanel.add(hasTelephone);

			hasInternetAccess = createLargeCheckBox("Есть интернет", "internet");
			inhabitedPanel.add(hasInternetAccess);

			hasWasher = createLargeCheckBox("Есть стиральная машина", "washer");
			inhabitedPanel.add(hasWasher);

			hasFrigde = createLargeCheckBox("Есть холодильник", "fridge");
			inhabitedPanel.add(hasFrigde);
		}

		inhabited.setSelected(true);
		setVisible(true);
	}

	public void stateChanged(ChangeEvent e) {
		super.stateChanged(e);

		hasFurniture.setEnabled(inhabited.isSelected());
		hasTelephone.setEnabled(inhabited.isSelected());
		hasInternetAccess.setEnabled(inhabited.isSelected());
		hasWasher.setEnabled(inhabited.isSelected());
		hasFrigde.setEnabled(inhabited.isSelected());
		
		perscription.setEnabled(uninhabited.isSelected());
		perscriptionLabel.setEnabled(uninhabited.isSelected());
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		String command = e.getActionCommand();
		switch (command) {

		case "add image": {
			 JFileChooser jfc = new JFileChooser(new File(Global.PATH_TO_IMAGES_FOLDER));
	            jfc.setFileFilter(new ImageFilter());
	            if (jfc.showOpenDialog(this) != JFileChooser.CANCEL_OPTION) {
	                try {
	                    String filename = jfc.getSelectedFile().getName();
	                    BufferedImage img = ImageIO.read(new File(Global.PATH_TO_IMAGES_FOLDER + filename));
	                    pictureFiles.add(Global.PATH_TO_IMAGES_FOLDER + filename);
	                    infoMessage("Картинка " + Global.PATH_TO_IMAGES_FOLDER + filename +" успешно добавлена.");
	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                    errorMessage("Не удалось загрузить картинку из файла.");
	                }
	            }
			
			
			break;
		}
		
		case "create": {

			if (city.getText().length() == 0 || street.getText().length() == 0
					|| house.getText().length() == 0) {
				errorMessage("Поля \"Город\", \"Улица\" и \"Дом\" обязательны для заполнения.");
			} else {
				String fullAddress = city.getText() + ", " + street.getText() + ", " + house.getText();
				if (appartment.getText().length() != 0)
					fullAddress += ", " + appartment.getText();

				if (inhabited.isSelected()) {
					//Формируем маску состояния
					int state = 0;
					if (hasFurniture.isSelected()) {
						state |= 1 << 0;
					}

					if (hasTelephone.isSelected()) {
						state |= 1 << 1;
					}

					if (hasInternetAccess.isSelected()) {
						state |= 1 << 2;
					}

					if (hasWasher.isSelected()) {
						state |= 1 << 3;
					}

					if (hasFrigde.isSelected()) {
						state |= 1 << 4;
					}

					Inhabited in = new Inhabited(fullAddress, (Double) square.getValue(),
							(Integer) floor.getValue(), state);
					for (String pic : pictureFiles) {
						in.addPicture(pic);
					}
					pictureFiles.clear();
					
					if (Global.inhabitedImmobilities.containsValue(in)) {
						errorMessage("Жильё по адресу " + fullAddress + " уже существует.");
					}
					else {
						Global.inhabitedImmobilities.add(in);
						infoMessage("Жильё по адресу " + fullAddress + " успешно создано.");
						try {
							Global.save();
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
					}

				} else {
					if (perscription.getText().length() == 0) {
						errorMessage("Пожалуйста, укажите тип сдаваемого объекта.");
					}
					else {
						Uninhabited un = new Uninhabited(fullAddress, (Double) square.getValue(),
								(Integer) floor.getValue(), perscription.getText());
						for (String pic : pictureFiles) {
							un.addPicture(pic);
						}
						pictureFiles.clear();
						
						if (Global.uninhabitedImmobilities.containsValue(un)) {
							errorMessage("Нежилой объект по адресу " + fullAddress + " уже существует.");
						}
						else {
							Global.uninhabitedImmobilities.add(un);
							infoMessage(perscription.getText() + " по адресу " + fullAddress + " успешно создано.");
							try {
								Global.save();
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}

			break;
		}

		case "exit": {
			dispose();
			break;
		}
		}
	}

}
