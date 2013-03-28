package Logic;
import java.io.*;

public class Customer extends Person{
	protected String contacts;
	public Customer(String name) throws IllegalAccessException { super(name); contacts = ""; }
	protected Customer(BufferedReader in) throws IOException{
		super(in);
		loadCustomerFromStream(in);
	}

	public String getContacts (){ 
		return contacts;
	}
	//Устанавливает значение поля contacts равным значению параметра contacts, удалив из него вхождения символа ‘|’;
	public void setContacts (String contacts){
		while(contacts.contains("|"))
			contacts = contacts.replaceAll("|", "");
		this.contacts = contacts;
	}
	/*  вызвать метод saveToStream родительского класса
		записать в поток все поля(в порядке: contacts), разделённые ‘|’ */
	
	public void saveCustomerToStream(PrintWriter out){
		super.savePersonToStream(out);
		if(contacts.length() == 0) contacts = " ";
		out.println(contacts);
	}
	// присвоить значениям полей(в порядке:  contacts) считываемые значения, значения полей разделены ‘|’
	public void loadCustomerFromStream(BufferedReader in) throws IOException{
		contacts = in.readLine();
	}
}
