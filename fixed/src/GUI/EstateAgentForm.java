package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.*;

import javax.swing.*;

import Logic.*;

public class EstateAgentForm extends TemplateForm {

	private JPanel upperPanel;
	private JPanel controlPanel;
	private JCheckBox useCustomerFilter;
	private JComboBox<Customer> customerFilter;
	private JCheckBox useImmobilityFilter;
	private JComboBox<Immobility> immobilityFilter;

	private JSpinner newIssue;
	private JButton changeIssueDate;

	private JButton newContract;
	private JButton exit;

	private JTable leasingGrid;
	private JTable hiringGrid;

	private ArrayList<LeasingContract> allLeasing = new ArrayList<LeasingContract>();
	private ArrayList<HiringContract> allHiring = new ArrayList<HiringContract>();

	JTabbedPane tabbed = new JTabbedPane();

	public EstateAgentForm() {
		super("Риэлтор " + Global.activeUser.getName(), 600, 400);
		setLayout(new BorderLayout());

		upperPanel = new JPanel();
		upperPanel.setPreferredSize(new Dimension(210, 40));
		upperPanel.setLayout(new FlowLayout());
		upperPanel.setBorder(BorderFactory.createEtchedBorder());

		useCustomerFilter = createCheckBox("Только с", "customer filter");
		upperPanel.add(useCustomerFilter);

		customerFilter = new JComboBox<Customer>();
		customerFilter.setPreferredSize(LONG_TEXT_FIELD_SIZE);
		upperPanel.add(customerFilter);

		useImmobilityFilter = createCheckBox("Только на", "immobility filter");
		upperPanel.add(useImmobilityFilter);
		immobilityFilter = new JComboBox<Immobility>();
		immobilityFilter.setPreferredSize(LONG_TEXT_FIELD_SIZE);
		upperPanel.add(immobilityFilter);
		
		customerFilter.addActionListener(this);
		immobilityFilter.addActionListener(this);

		updateFilters();

		add(upperPanel, BorderLayout.NORTH);

		controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(210, 40));
		controlPanel.setLayout(new FlowLayout());
		controlPanel.setBorder(BorderFactory.createEtchedBorder());
		add(controlPanel, BorderLayout.WEST);

		add(tabbed, BorderLayout.CENTER);

		updateGrid();

		newContract = createLargeButton("Заключить договор", "new contract");
		controlPanel.add(newContract);

		controlPanel.add(new JLabel("Новый срок:"));
		newIssue = createDateSpinner();
		controlPanel.add(newIssue);

		changeIssueDate = createLargeButton("Изменить срок действия", "change issue");
		controlPanel.add(changeIssueDate);

		exit = createLargeButton("Выход", "exit");
		controlPanel.add(exit);

		setVisible(true);
	}

	private void updateFilters() {
		clearComboBox(immobilityFilter);
		clearComboBox(customerFilter);

		for (Customer c : Global.customers.getStore())
			customerFilter.addItem(c);

		for (Immobility i : Global.inhabitedImmobilities.getStore())
			immobilityFilter.addItem(i);

		for (Immobility i : Global.uninhabitedImmobilities.getStore())
			immobilityFilter.addItem(i);

		customerFilter.setEnabled(false);
		immobilityFilter.setEnabled(false);
		revalidate();
	}

	private void updateGrid() {
		int index = tabbed.getSelectedIndex();
		
		tabbed.removeAll();
		DateFormat dt = Global.getDateFormat();
		{
			String labels[] = { "Объект", "Клиент", "Дата заключения", "Дата окончания", "Цена",
					"Процент отчислений" };

			Collection<LeasingContract> leasing = Registry.getAllLeasingContracts();
			TreeSet<LeasingContract> set = new TreeSet<LeasingContract>();
			
//			set.addAll(leasing);
			
			for (LeasingContract lc: leasing) {
				//Фильтр клиента
				if (!useCustomerFilter.isSelected() || lc.getCustomer() == customerFilter.getSelectedItem()) {
					if (!useImmobilityFilter.isSelected() || lc.getImmobility() == immobilityFilter.getSelectedItem())
						set.add(lc);
				}
			}

			Object rawData[][] = new Object[set.size()][labels.length];
			leasingGrid = new JTable(rawData, labels) {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};

			int v = 0;

			allLeasing.clear();
			for (LeasingContract lc : set) {
				rawData[v][0] = lc.getImmobility();
				rawData[v][1] = lc.getCustomer();
				rawData[v][2] = dt.format(lc.getDate());
				rawData[v][3] = dt.format(lc.getIssue());
				rawData[v][4] = lc.getPrice();
				rawData[v][5] = lc.getPercentage();
				allLeasing.add(lc);
				v++;
			}

			tabbed.add(new JScrollPane(leasingGrid), "Договоры сдачи");
		}

		{
			String labels[] = { "Объект", "Клиент", "Дата заключения", "Дата окончания", "Цена",
					"Форма оплаты" };

			Collection<HiringContract> hiring = Registry.getAllHiringContracts();
			TreeSet<HiringContract> set = new TreeSet<HiringContract>();
//			set.addAll(hiring);
			
			for (HiringContract lc: hiring) {
				//Фильтр клиента
				if (!useCustomerFilter.isSelected() || lc.getCustomer() == customerFilter.getSelectedItem()) {
					if (!useImmobilityFilter.isSelected() || lc.getImmobility() == immobilityFilter.getSelectedItem())
						set.add(lc);
				}
			}


			Object rawData[][] = new Object[set.size()][labels.length];
			hiringGrid = new JTable(rawData, labels) {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};

			int v = 0;

			allHiring.clear();
			for (HiringContract lc : set) {
				rawData[v][0] = lc.getImmobility();
				rawData[v][1] = lc.getCustomer();
				rawData[v][2] = dt.format(lc.getDate());
				rawData[v][3] = dt.format(lc.getIssue());
				rawData[v][4] = lc.getPrice();
				rawData[v][5] = lc.getMannerOfPayment();

				allHiring.add(lc);
				v++;
			}

			tabbed.add(new JScrollPane(hiringGrid), "Договоры съёма");
		}

		tabbed.setSelectedIndex(index == -1 ? 0 : index);
		revalidate();
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		String command = e.getActionCommand();
		switch (command) {

		case "change issue": {
			if (tabbed.getSelectedIndex() == 0) {
				//Leasing
				System.err.println("state 0");
				try {
					Date date = (Date) newIssue.getValue();
					int index = leasingGrid.getSelectedRow();
					if (index == -1) {
						errorMessage("Вы должны выбрать договор.");
					} else {
						LeasingContract lc = allLeasing.get(index);
						Registry.updateIssueDateOfLeasingContract(lc, date);
						infoMessage("Дата обновлена успешно");
						updateGrid();
					}
				} catch (Throwable ex) {
					errorMessage(ex.toString());
					ex.printStackTrace();
				}
			} else {
				//Hiring
				System.err.println("state 1");
				try {
					Date date = (Date) newIssue.getValue();
					int index = hiringGrid.getSelectedRow();
					if (index == -1) {
						errorMessage("Вы должны выбрать договор.");
					} else {
						HiringContract lc = allHiring.get(index);
						Registry.updateIssueDateOfHiringContract(lc, date);
						infoMessage("Дата обновлена успешно");
						updateGrid();
					}
				} catch (Throwable ex) {
					errorMessage(ex.toString());
					ex.printStackTrace();
				}
			}
			
			try {
				Global.save();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			break;
		}

		case "customer filter": {
			customerFilter.setEnabled(useCustomerFilter.isSelected());
			updateGrid();
			break;
		}

		case "immobility filter": {
			immobilityFilter.setEnabled(useImmobilityFilter.isSelected());
			updateGrid();
			break;
		}

		case "new contract": {
			new AddContractForm();
			updateGrid();
			break;
		}
		case "exit": {
			dispose();
			break;
		}
		
		default:
			updateGrid();
		}
	}

}
