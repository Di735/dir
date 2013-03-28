package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.TreeSet;

import javax.swing.*;

import Logic.*;

public class AuthorizationForm extends TemplateDialog {

	private JLabel loginLabel;
	private JLabel passwordLabel;
	private JComboBox<Employee> login;
	private JPasswordField password;
	private JButton authorize;
	private JButton exit;

	public AuthorizationForm() {
		super("Авторизация", 350, 130);
		setResizable(false);

		setLayout(new FlowLayout());

		loginLabel = new JLabel("Пользователь:");
		loginLabel.setPreferredSize(SHORT_LABEL_SIZE);
		add(loginLabel);

		login = new JComboBox<Employee>();
		for (Admin a : Global.admins.getStore())
			login.addItem(a);
		
		for (EstateAgent ea : Global.estateAgents.getStore())
			login.addItem(ea);
		
		for (CallAgent ca : Global.callAgents.getStore())
			login.addItem(ca);
		login.setPreferredSize(TEXT_FIELD_SIZE);
		add(login);

		passwordLabel = new JLabel("Пароль:");
		passwordLabel.setPreferredSize(SHORT_LABEL_SIZE);
		add(passwordLabel);

		password = new JPasswordField();
		password.setPreferredSize(TEXT_FIELD_SIZE);
		add(password);

		authorize = createButton("Войти", "authorize");
		add(authorize);

		exit = createButton("Выход", "exit");
		add(exit);

		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		String command = e.getActionCommand();
		switch (command) {
		case "authorize": {
			String pass = new String(password.getPassword());

			//Админ
			Object selected = login.getSelectedItem();
			Global.activeUser = (Employee) selected;
			
			if (selected instanceof Admin && ((Admin) selected).authorize(pass)) {
				dispose();
				new UserAdministrationForm();
			} else if (selected instanceof CallAgent && ((CallAgent) selected).authorize(pass)) {
				dispose();
				new CallAgentForm();
			} else if (selected instanceof EstateAgent && ((EstateAgent) selected).authorize(pass)) {
				dispose();
				new EstateAgentForm();
			} else {
				errorMessage("Не удалось войти в систему: вы ввели неверный пароль.");
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
