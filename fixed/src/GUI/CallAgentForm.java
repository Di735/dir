package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

import Logic.*;

public class CallAgentForm extends TemplateForm {

	private JPanel criteriaPanel;
	private JPanel generalCriteria;
	private JPanel inhabitedCriteria;

	private JRadioButton inhabited, uninhabited;

	private JCheckBox applyMinFloor;
	private JSpinner minFloor;
	private JCheckBox applyMaxFloor;
	private JSpinner maxFloor;

	private JCheckBox applyMinSquare;
	private JSpinner minSquare;
	private JCheckBox applyMaxSquare;
	private JSpinner maxSquare;

	private JSpinner minDate;
	private JSpinner maxDate;

	private JCheckBox hasFurniture;
	private JCheckBox hasTelephone;
	private JCheckBox hasInternetAccess;
	private JCheckBox hasWasher;
	private JCheckBox hasFrigde;

	private JTable grid;
	private JScrollPane scroll;

	private JButton find;
	private JButton lookupImages;
	private JButton exit;

	public CallAgentForm() {
		super("Оператор", 1000, 700);
		setLayout(new BorderLayout());

		criteriaPanel = new JPanel();
		criteriaPanel.setBorder(BorderFactory.createEtchedBorder());
		criteriaPanel.setPreferredSize(new Dimension(220, 400));
		add(criteriaPanel, BorderLayout.WEST);

		criteriaPanel.setLayout(new FlowLayout());

		//Жилое/нежилое
		{
			ButtonGroup bg = new ButtonGroup();

			inhabited = createRadioButton("Жилое");
			uninhabited = createRadioButton("Нежилое");

			bg.add(inhabited);
			bg.add(uninhabited);

			criteriaPanel.add(inhabited);
			criteriaPanel.add(uninhabited);
		}

		//Общие критерии
		{
			generalCriteria = new JPanel();
			generalCriteria.setPreferredSize(new Dimension(220, 200));
			generalCriteria.setBorder(BorderFactory.createEtchedBorder());

			generalCriteria.add(new JLabel("Общие обязательные требования:"));

			applyMinFloor = createCheckBox("Этаж от", "applyMinFloor");
			generalCriteria.add(applyMinFloor);
			minFloor = createSpinner(0, -2, 100);
			minFloor.setEnabled(false);
			generalCriteria.add(minFloor);

			applyMaxFloor = createCheckBox("Этаж до", "applyMaxFloor");
			generalCriteria.add(applyMaxFloor);
			maxFloor = createSpinner(0, -2, 100);
			maxFloor.setEnabled(false);
			generalCriteria.add(maxFloor);

			applyMinSquare = createCheckBox("Площадь от", "applyMinSquare");
			generalCriteria.add(applyMinSquare);
			minSquare = createSpinner(0.0, 0.0, 10000.0);
			minSquare.setEnabled(false);
			generalCriteria.add(minSquare);

			applyMaxSquare = createCheckBox("Площадь до", "applyMaxSquare");
			generalCriteria.add(applyMaxSquare);
			maxSquare = createSpinner(0.0, 0.0, 10000.0);
			maxSquare.setEnabled(false);
			generalCriteria.add(maxSquare);

			minDate = createDateSpinner();
			generalCriteria.add(minDate);
			maxDate = createDateSpinner();
			generalCriteria.add(maxDate);

			criteriaPanel.add(generalCriteria);
		}

		//Критерии обитаемого жилья
		{
			inhabitedCriteria = new JPanel();
			inhabitedCriteria.setBorder(BorderFactory.createEtchedBorder());
			inhabitedCriteria.setPreferredSize(new Dimension(220, 170));

			inhabitedCriteria.add(new JLabel("Обязательные требования к жилью:"));

			hasFurniture = createLargeCheckBox("Есть мебель", "furniture");
			inhabitedCriteria.add(hasFurniture);

			hasTelephone = createLargeCheckBox("Есть телефон", "telephone");
			inhabitedCriteria.add(hasTelephone);

			hasInternetAccess = createLargeCheckBox("Есть интернет", "internet");
			inhabitedCriteria.add(hasInternetAccess);

			hasWasher = createLargeCheckBox("Есть стиральная машина", "washer");
			inhabitedCriteria.add(hasWasher);

			hasFrigde = createLargeCheckBox("Есть холодильник", "fridge");
			inhabitedCriteria.add(hasFrigde);

			criteriaPanel.add(inhabitedCriteria);
		}

		find = createLargeButton("Найти", "find");
		criteriaPanel.add(find);
		
		lookupImages = createLargeButton("Просмотр изображений", "lookup images");
		criteriaPanel.add(lookupImages);
		
		exit = createLargeButton("Выход", "exit");
		criteriaPanel.add(exit);

		inhabited.setSelected(true);

		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		String command = e.getActionCommand();

		switch (command) {
		case "applyMinFloor": {
			minFloor.setEnabled(applyMinFloor.isSelected());
			break;
		}

		case "applyMaxFloor": {
			maxFloor.setEnabled(applyMaxFloor.isSelected());
			break;
		}

		case "applyMinSquare": {
			minSquare.setEnabled(applyMinSquare.isSelected());
			break;
		}

		case "applyMaxSquare": {
			maxSquare.setEnabled(applyMaxSquare.isSelected());
			break;
		}

		case "applyDate": {
			break;
		}

		case "find": {
			Requirements req = new Requirements();

			//Формируем требования
			if (inhabited.isSelected())
				req.addRequirement("inhabited");

			if (uninhabited.isSelected())
				req.addRequirement("uninhabited");

			DateFormat dt = Global.getDateFormat();

			Date from = (Date) minDate.getValue();
			Date to = (Date) maxDate.getValue();

			req.addRequirement("free after " + dt.format(from));
			req.addRequirement("free before " + dt.format(to));

			if (applyMinFloor.isSelected())
				req.addRequirement("floor > " + minFloor.getValue().toString());

			if (applyMaxFloor.isSelected()) {
				req.addRequirement("floor < " + maxFloor.getValue().toString());
			}

			if (applyMinSquare.isSelected())
				req.addRequirement("square > " + minSquare.getValue().toString());

			if (applyMaxSquare.isSelected())
				req.addRequirement("square < " + maxSquare.getValue().toString());

			if (inhabited.isSelected()) {
				//Доп. требования только для жилья
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

				if (state != 0)
					req.addRequirement("state: " + state);

			}

			System.err.println(req.getRequirements());

			ArrayList<Immobility> searchResult = (ArrayList<Immobility>) Registry.responseSearchRequest(req);
			for (Immobility i : searchResult)
				System.err.println(i);
			if (inhabited.isSelected())
				formInhabited(searchResult, from, to);
			else
				formUninhabited(searchResult, from, to);

			revalidate();
			break;
		}
		
		case "exit": {
			dispose();
			break;
		}
		
		case "lookup images": {
			if (grid != null && grid.getSelectedRow() != -1) {
				new ImmobilityImagesLookup((Immobility) grid.getValueAt(grid.getSelectedRow(), 0));
			}
				
			break;
		}
		}
	}

	private void formUninhabited(ArrayList<Immobility> searchResult, Date from, Date to) {
		if (scroll != null)
			remove(scroll);

		String labels[] = { "Адрес, назначение", "Этаж", "Площадь", "Цена"};

		Object rawData[][] = new Object[searchResult.size()][labels.length];

		for (int i = 0; i < searchResult.size(); i++) {
			Uninhabited in = (Uninhabited) searchResult.get(i);

			rawData[i][0] = in;
			rawData[i][1] = in.getFloor();
			rawData[i][2] = in.getSquare();
			LeasingContract lc = Registry.getActiveLeasingContract(in, from, to);
			rawData[i][3] = lc == null ? "null" : lc.getPrice();
		}

		grid = new JTable(rawData, labels) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		scroll = new JScrollPane(grid);
		add(scroll, BorderLayout.CENTER);
	}

	private void formInhabited(ArrayList<Immobility> searchResult, Date from, Date to) {

		//		for (Immobility i : searchResult)
		//			System.err.println(i);

		if (scroll != null)
			remove(scroll);

		String labels[] = { "Адрес", "Этаж", "Площадь", "Цена", "Мебель", "Телефон", "Интернет",
				"Стир. машина", "Холодильник" };

		Object rawData[][] = new Object[searchResult.size()][labels.length];

		for (int i = 0; i < searchResult.size(); i++) {
			Inhabited in = (Inhabited) searchResult.get(i);

			rawData[i][0] = in;
			rawData[i][1] = in.getFloor();
			rawData[i][2] = in.getSquare();
			LeasingContract lc = Registry.getActiveLeasingContract(in, from, to);
			rawData[i][3] = lc == null ? "null" : lc.getPrice();

			for (int j = 0; j < 5; j++) {
				rawData[i][4 + j] = ((in.getState() & (1 << j)) == 0) ? "Нет" : "Есть";
			}
		}

		grid = new JTable(rawData, labels) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		scroll = new JScrollPane(grid);
		add(scroll, BorderLayout.CENTER);
	}

	public void stateChanged(ChangeEvent e) {
		super.stateChanged(e);
		hasFurniture.setEnabled(inhabited.isSelected());
		hasTelephone.setEnabled(inhabited.isSelected());
		hasInternetAccess.setEnabled(inhabited.isSelected());
		hasWasher.setEnabled(inhabited.isSelected());
		hasFrigde.setEnabled(inhabited.isSelected());
	}

}
