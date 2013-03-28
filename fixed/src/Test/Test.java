package Test;

import java.io.IOException;
import java.text.ParseException;

import GUI.*;
import Logic.Global;

public class Test {
	public static void main(String[] args) {
		new Test().run();
	}

	private void run() {
//		new AuthorizationForm();
//		new UserAdministrationForm();
//		new AddUserForm();
//		new OperatorForm();
//		new AddCustomerForm();
//		new AddImmobilityForm();
//		new AddContractForm();
		
		
		try {
			Global.load();
			final AuthorizationForm af = new AuthorizationForm();
		} catch (IOException | ParseException e) {
			//TODO
			e.printStackTrace();
		}
		
	}

}
