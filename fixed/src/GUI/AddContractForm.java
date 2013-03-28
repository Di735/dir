package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

import Logic.*;

public class AddContractForm extends TemplateDialog {

	private JPanel generalPanel;

	private JLabel customerLabel;
	private JComboBox<Customer> customer;
	private JButton addCustomer;

	private JLabel immobilityLabel;
	private JComboBox<Immobility> immobility;
	private JButton addImmobility;

	private JSpinner from, to;
	private JPanel typePanel;

	private JRadioButton hiring, leasing;

	private JLabel priceLabel;
	private JSpinner price;

	private JLabel percentageLabel;
	private JSpinner percentage;

	private JLabel payMannerLabel;
	private JComboBox<String> payManner;

	private JButton create, exit;

	private final static String[] manners = {HiringContract.ONE_MONTH_FORWARD, HiringContract.ONE_MONTH_BACK, HiringContract.DAILY};

	public AddContractForm() {
		super("Заключение договора", 340, 280);
		setResizable(false);
		setLayout(new BorderLayout());

		generalPanel = new JPanel();
		generalPanel.setLayout(new FlowLayout());
		generalPanel.setBorder(BorderFactory.createEtchedBorder());
		generalPanel.setPreferredSize(new Dimension(340, 90));

		customerLabel = new JLabel("Клиент");
		customerLabel.setPreferredSize(VERY_SHORT_LABEL_SIZE);
		generalPanel.add(customerLabel);

		customer = new JComboBox<Customer>();
		customer.setPreferredSize(TEXT_FIELD_SIZE);
		generalPanel.add(customer);

		addCustomer = createTinyButton("+", "add customer");
		generalPanel.add(addCustomer);

		immobilityLabel = new JLabel("Объект");
		immobilityLabel.setPreferredSize(VERY_SHORT_LABEL_SIZE);
		generalPanel.add(immobilityLabel);

		immobility = new JComboBox<Immobility>();
		immobility.setPreferredSize(TEXT_FIELD_SIZE);
		generalPanel.add(immobility);

		addImmobility = createTinyButton("+", "add immobility");
		generalPanel.add(addImmobility);

		updateComboBoxData();

		generalPanel.add(new JLabel("С"));
		from = createDateSpinner();
		generalPanel.add(from);
		generalPanel.add(new JLabel("по"));
		to = createDateSpinner();
		generalPanel.add(to);

		typePanel = new JPanel();
		add(typePanel, BorderLayout.CENTER);
		typePanel.setLayout(new FlowLayout());
		{

			ButtonGroup bg = new ButtonGroup();

			leasing = createRadioButton("Сдача");
			typePanel.add(leasing);
			hiring = createRadioButton("Съём");
			typePanel.add(hiring);

			bg.add(leasing);
			bg.add(hiring);
		}
		//TODO В зависимости от флажка сдачи или аренды менять текст.
		priceLabel = new JLabel("Цена:");
		priceLabel.setPreferredSize(LONG_LABEL_SIZE);
		typePanel.add(priceLabel);

		price = createSpinner(0.0, 0.0, 1000000.0);
		typePanel.add(price);

		percentageLabel = new JLabel("Процент отчислений агентству:");
		percentageLabel.setPreferredSize(LONG_LABEL_SIZE);
		typePanel.add(percentageLabel);

		percentage = createSpinner(30.0, 0.0, 100.0);
		typePanel.add(percentage);

		payMannerLabel = new JLabel("Порядок оплаты");
		payMannerLabel.setPreferredSize(SHORT_LABEL_SIZE);
		typePanel.add(payMannerLabel);

		payManner = new JComboBox<String>(manners);
		typePanel.add(payManner);

		create = createButton("Создать", "create");
		typePanel.add(create);

		exit = createButton("Выход", "exit");
		typePanel.add(exit);

		leasing.setSelected(true);

		add(generalPanel, BorderLayout.NORTH);
		setVisible(true);
	}

	private void updateComboBoxData() {
		clearComboBox(immobility);
		clearComboBox(customer);

		for (Customer c : Global.customers.getStore())
			customer.addItem(c);

		for (Immobility i : Global.inhabitedImmobilities.getStore())
			immobility.addItem(i);

		for (Immobility i : Global.uninhabitedImmobilities.getStore())
			immobility.addItem(i);
		revalidate();
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		String command = e.getActionCommand();
		switch (command) {

		case "create": {
			
			if (customer.getSelectedItem() == null || immobility.getSelectedItem() == null) {
				errorMessage("Пожалуйста, укажите клиента и объект недвижимости.");
				break;
			}
			
			Date fromDate = (Date) from.getValue();
			Date toDate = (Date) to.getValue();

//			infoMessage(fromDate + "\n" +toDate +  "\n" + fromDate.compareTo(toDate));
			if (fromDate.compareTo(toDate) > 0) {
				errorMessage("Дата окончания договора не может быть меньше даты заключения.");
			} else {
				Immobility immo = (Immobility) immobility.getSelectedItem();
				Customer cus = (Customer) customer.getSelectedItem();
				EstateAgent agent = (EstateAgent) Global.activeUser;
				
				if (leasing.isSelected()) {
					LeasingContract lc = new LeasingContract(immo, cus, agent, (Double) price.getValue(),
							(Double) percentage.getValue(), fromDate, toDate);
					
					try {
						agent.signLeasingContract(lc);
						infoMessage("Договор успешно добавлен.");
						Global.save();
					} catch (Exception e1) {
						errorMessage("Пересекается с аналогичным контрактом.");
//						errorMessage(e1.toString());
//						e1.printStackTrace();
					}
				}
				else {
					HiringContract hc = new HiringContract(immo, cus, agent, (String) payManner.getSelectedItem(), fromDate, toDate);
					try {
						Registry.addHiringContract(hc);
						infoMessage("Договор успешно добавлен.");
						Global.save();
					}
					catch (Exception ex) {
						errorMessage(ex.toString());
					}
				}
				
			}

			break;
		}

		case "add immobility": {
			new AddImmobilityForm();
			updateComboBoxData();
			break;
		}

		case "add customer": {
			new AddCustomerForm();
			updateComboBoxData();
			break;
		}

		case "exit": {
			dispose();
			break;
		}
		}
	}

	public void stateChanged(ChangeEvent e) {
		super.stateChanged(e);
		if (hiring.isSelected()) {
			priceLabel.setText("Цена съёма:");
		} else if (leasing.isSelected()) {
			priceLabel.setText("Цена сдачи:");
		}

		payMannerLabel.setEnabled(hiring.isSelected());
		payManner.setEnabled(hiring.isSelected());

		priceLabel.setEnabled(leasing.isSelected());
		price.setEnabled(leasing.isSelected());
		percentageLabel.setEnabled(leasing.isSelected());
		percentage.setEnabled(leasing.isSelected());
	}

}
