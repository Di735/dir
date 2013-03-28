package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

import javax.swing.*;

import Logic.Customer;
import Logic.Global;

public class AddCustomerForm extends TemplateDialog {

	private JLabel fullNameLabel;
	private JTextField fullName;

	private JLabel passportDataLabel;
	private JLabel passportNumberLabel;
	private JLabel phoneLabel;
	private JTextField passportSeries;
	private JTextField passportNumber;
	private JTextField phone;

	private JButton create, exit;

	public AddCustomerForm() {
		super("Создание клиента", 400, 150);
		setResizable(false);
		setLayout(new FlowLayout());

		fullNameLabel = new JLabel("ФИО:");
		fullNameLabel.setPreferredSize(SHORT_LABEL_SIZE);
		add(fullNameLabel);

		fullName = new JTextField();
		fullName.setPreferredSize(LONG_TEXT_FIELD_SIZE);
		add(fullName);

		passportDataLabel = new JLabel("Паспорт серии");
		passportDataLabel.setPreferredSize(SHORT_LABEL_SIZE);
		add(passportDataLabel);

		passportSeries = new JTextField();
		passportSeries.setPreferredSize(VERY_SHORT_TEXT_FIELD_SIZE);
		add(passportSeries);

		passportNumberLabel = new JLabel("№");
		add(passportNumberLabel);

		passportNumber = new JTextField();
		passportNumber.setPreferredSize(TEXT_FIELD_SIZE);
		add(passportNumber);

		phoneLabel = new JLabel("Телефон:");
		phoneLabel.setPreferredSize(SHORT_LABEL_SIZE);
		add(phoneLabel);

		phone = new JTextField();
		phone.setPreferredSize(LONG_TEXT_FIELD_SIZE);
		add(phone);

		create = createButton("Создать", "create");
		add(create);

		exit = createButton("Выход", "exit");
		add(exit);

		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		String command = e.getActionCommand();
		switch (command) {
		case "create": {
			Customer cus;
			try {
				cus = new Customer(fullName.getText());
			} catch (Throwable e2) {
				errorMessage("Вы указали некорректное имя.");
//				e2.printStackTrace();
				return;
			}
			
			String passportData;
			if (passportSeries.getText().length() == 0 || passportNumber.getText().length() == 0)
				passportData = passportSeries.getText() + passportNumber.getText();
			else
				passportData = passportSeries.getText() + " " + passportNumber.getText();
			cus.setPassportData(passportData);
			cus.setPhoneNumber(phone.getText());
			
			if (Global.customers.containsValue(cus)) {
				errorMessage("Клиент " + cus + " уже существует.");
			}
			else {
				Global.customers.add(cus);
				infoMessage("Клиент " + cus + " успешно создан.");
				try {
					Global.save();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
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
