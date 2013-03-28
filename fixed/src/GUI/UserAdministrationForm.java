package GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeSet;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumn;

import Logic.*;

public class UserAdministrationForm extends TemplateForm{

	private JPanel buttonPanel;
	private JTable grid;
	private JScrollPane scroll;
	private JButton addUser;
	private JButton exit;
	
	public UserAdministrationForm() {
		super("Управление пользователями: " + Global.activeUser.getName(), 600, 400);
		setLayout(new BorderLayout());
//		setResizable(false);
		
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(130, 130));
		buttonPanel.setBorder(BorderFactory.createEtchedBorder());
		add (buttonPanel, BorderLayout.WEST);
		
		//Формирование таблицы
		updateGrid();
		
		addUser = createButton("Добавить", "add user");
		buttonPanel.add(addUser);
		
		exit = createButton("Выход", "exit");
		buttonPanel.add(exit);
		
		setVisible(true);
	}

	private void updateGrid() {
		String gridLabels[] = {"Имя пользователя", "Роль"};
		if (scroll != null) {
			remove(scroll);
		}
		
		TreeSet<Admin> admins = Global.admins.getStore();
		TreeSet<EstateAgent> estateAgents = Global.estateAgents.getStore();
		TreeSet<CallAgent> callAgents = Global.callAgents.getStore();
		
		Object data[][] = new Object[admins.size() + estateAgents.size() + callAgents.size()][2];
		
		int v = 0;
		for (Admin a : admins) {
			data[v][0] = a;
			data[v][1] = "Администратор";
			++v;
		}
		
		for (EstateAgent e : estateAgents) {
			data[v][0] = e;
			data[v][1] = "Риелтор";
			++v;
		}
		
		for (CallAgent c : callAgents) {
			data[v][0] = c;
			data[v][1] = "Оператор";
			++v;
		}
		
		grid = new JTable(data, gridLabels) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		scroll = new JScrollPane(grid);
		add(scroll, BorderLayout.CENTER);
		revalidate();
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		String command = e.getActionCommand();
		switch(command) {
		case "add user" : {
			new AddUserForm();
			updateGrid();
			break;
		}
		
		case "exit":{
			dispose();
			break;
		}
		}
	}
}
