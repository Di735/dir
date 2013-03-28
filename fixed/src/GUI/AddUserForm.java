package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

import javax.swing.*;

import Logic.*;

public class AddUserForm extends TemplateDialog{

	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JLabel roleLabel;
	private JTextField login;
	private JPasswordField password;
	@SuppressWarnings("rawtypes")
	private JComboBox role;
	private JButton create;
	private JButton exit;
	
	private static final String roles[] = {"Администратор", "Риелтор", "Оператор"};
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AddUserForm() {
		super("Добавить пользователя", 350, 160);
		setResizable(false);
		
		setLayout(new FlowLayout());
		
		usernameLabel = new JLabel("Введите имя:");
		usernameLabel.setPreferredSize(SHORT_LABEL_SIZE);
		add (usernameLabel);
		
		login = new JTextField();
		login.setPreferredSize(TEXT_FIELD_SIZE);
		add(login);
		
		passwordLabel = new JLabel("Пароль:");
		passwordLabel.setPreferredSize(SHORT_LABEL_SIZE);
		add (passwordLabel);
		
		password = new JPasswordField();
		password.setPreferredSize(TEXT_FIELD_SIZE);
		add(password);
		
		roleLabel = new JLabel("Роль:");
		roleLabel.setPreferredSize(SHORT_LABEL_SIZE);
		add(roleLabel);
		
		role = new JComboBox(roles);
		role.setPreferredSize(TEXT_FIELD_SIZE);
		add(role);
		
		create = createButton("Создать", "create");
		add(create);
		
		exit = createButton("Выход", "exit");
		add(exit);
		
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		String command = e.getActionCommand();
		switch(command) {
		case "create" : {
			
			String name = login.getText();
			
			Admin a;
			EstateAgent ea;
			CallAgent ca;
			
			try {
			a = new Admin(name);
			ea = new EstateAgent(name);
			ca = new CallAgent(name);
			}
			catch (Throwable ex) {
				errorMessage("Вы ввели некорректное имя.");
				return;
			}
			
			if (Global.admins.containsValue(a)) {
				errorMessage("Администратор с таким именем уже существует.");
			} else if (Global.estateAgents.containsValue(ea)) {
				errorMessage("Риелтор с таким именем уже существует.");
			} else if (Global.callAgents.containsValue(ca)) {
				errorMessage("Оператор с таким именем уже существует");
			} else {
				String selectedRole = (String) role.getSelectedItem();
				String pass = new String (password.getPassword());
				
				if (selectedRole.equals(roles[0])) {
					Global.admins.add(a);
					a.setPassword(pass);
				} else if (selectedRole.equals(roles[1])) {
					Global.estateAgents.add(ea);
					ea.setPassword(pass);
				}
				else if (selectedRole.equals(roles[2])) {
					Global.callAgents.add(ca);
					ca.setPassword(pass);
				}
				
				infoMessage(selectedRole + " " + name + " успешно создан.");
				
				try {
					Global.save();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
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
