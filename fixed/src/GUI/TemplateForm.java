package GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TemplateForm extends JFrame implements ActionListener, ChangeListener{
	
	protected final static Dimension TINY_BUTTON_SIZE = new Dimension(45, 22);
	protected final static Dimension STANDART_BUTTON_SIZE = new Dimension(100, 22);
	protected final static Dimension LARGE_BUTTON_SIZE = new Dimension(200, 22);
	protected final static Dimension VERY_SHORT_TEXT_FIELD_SIZE = new Dimension(60, 22);
	protected final static Dimension SHORT_TEXT_FIELD_SIZE = new Dimension(130, 22);
	protected final static Dimension TEXT_FIELD_SIZE = new Dimension(200, 22);
	protected final static Dimension LONG_TEXT_FIELD_SIZE = new Dimension(281, 22);
	protected final static Dimension VERY_SHORT_LABEL_SIZE = new Dimension(60, 22);
	protected final static Dimension SHORT_LABEL_SIZE = new Dimension(100, 22);
	protected final static Dimension LONG_LABEL_SIZE = new Dimension(200, 22);
	protected final static Dimension SPINNER_SIZE = new Dimension(80, 25);
	protected final static Dimension DATE_SPINNER_SIZE = new Dimension(101, 25);
	
	public TemplateForm(String title, int width, int height) {
		setBounds(20, 20, width, height);
		setTitle(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	protected JButton createTinyButton (String title, String actionCommand) {
		JButton ret = new JButton(title);
		ret.setPreferredSize(TINY_BUTTON_SIZE);
		ret.setActionCommand(actionCommand);
		ret.addActionListener(this);
		return ret;
	}
	
	protected JButton createButton (String title, String actionCommand) {
		JButton ret = new JButton(title);
		ret.setPreferredSize(STANDART_BUTTON_SIZE);
		ret.setActionCommand(actionCommand);
		ret.addActionListener(this);
		return ret;
	}
	
	protected JButton createLargeButton (String title, String actionCommand) {
		JButton ret = new JButton(title);
		ret.setPreferredSize(LARGE_BUTTON_SIZE);
		ret.setActionCommand(actionCommand);
		ret.addActionListener(this);
		return ret;
	}
	
	protected JRadioButton createRadioButton (String title) {
		JRadioButton ret = new JRadioButton(title);
		ret.setPreferredSize(STANDART_BUTTON_SIZE);
		ret.addChangeListener(this);
		return ret;
	}
	
	protected JCheckBox createCheckBox (String title, String actionCommand) {
		JCheckBox ret = new JCheckBox(title);
		ret.setActionCommand(actionCommand);
		ret.setPreferredSize(STANDART_BUTTON_SIZE);
		ret.addActionListener(this);
		return ret;
	}
	
	protected JCheckBox createLargeCheckBox (String title, String actionCommand) {
		JCheckBox ret = new JCheckBox(title);
		ret.setActionCommand(actionCommand);
		ret.setPreferredSize(LARGE_BUTTON_SIZE);
		ret.addActionListener(this);
		return ret;
	}
	
	protected JSpinner createSpinner (int current, int min, int max) {
		JSpinner ret = new JSpinner(new SpinnerNumberModel(current, min, max, 1));
		ret.setPreferredSize(SPINNER_SIZE);
		return ret;
	}
	
	protected JSpinner createSpinner (double current, double min, double max) {
		JSpinner ret = new JSpinner(new SpinnerNumberModel(current, min, max, 0.1));
		ret.setPreferredSize(SPINNER_SIZE);
		return ret;
	}
	
	protected JSpinner createDateSpinner () {
		SpinnerDateModel model = new SpinnerDateModel();
		model.setCalendarField(Calendar.DAY_OF_MONTH);
		JSpinner ret = new JSpinner(model);
		ret.setModel(model);
		ret.setEditor(new JSpinner.DateEditor(ret, "dd:MM:yyyy"));
		ret.setPreferredSize(DATE_SPINNER_SIZE);
		return ret;
	}
	
	protected void errorMessage (String s) {
		JOptionPane.showMessageDialog(null, s, "Ошибка",
                JOptionPane.ERROR_MESSAGE);
	}
	
	protected void infoMessage (String s) {
		JOptionPane.showMessageDialog(null, s, "Уведомление",
                JOptionPane.INFORMATION_MESSAGE);
	}

	@SuppressWarnings("unused")
	@Override
	public void stateChanged(ChangeEvent e) {
		
	}

	@SuppressWarnings("unused")
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	
	protected void clearComboBox (JComboBox box) {
		DefaultComboBoxModel theModel = (DefaultComboBoxModel)box.getModel();
		theModel.removeAllElements();
	}

}
