package Logic;
import java.io.*;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;


public class Contract implements SaveableIndexable, Testable, Comparable<Contract>{ 
	private static int lastId = 0;// значение id, последнего созданного/считанного объекта, увеличенный на 1
	protected java.util.Date  date, issueDate;
	protected Immobility immobility;
	protected Customer customer;
	protected EstateAgent estateAgent;
	protected int id; 
	public Contract(Immobility im, Customer cus, EstateAgent ea, Date from, Date to){
		id = lastId++;
		immobility = im;
		customer = cus;
		estateAgent = ea;
		date = from;
		issueDate = to;
	}
	protected Contract(BufferedReader in) throws IOException, ParseException { loadContractFromStream(in); }
	public java.util.Date getDate(){
		DateFormat df = Global.getDateFormat();
		try {
			return df.parse(df.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Immobility getImmobility(){
		return immobility;
	}
	public Customer getCustomer (){
		return customer;
	}
	public EstateAgent getEstateAgent(){
		return estateAgent;
	}
	public java.util.Date getIssue(){
		DateFormat df = Global.getDateFormat();
		try {
			return df.parse(df.format(issueDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
//		записать в поток все поля(в порядке: id, date,  issdueDate, immobility.getId(), customer.getId(), estateAgent.getId()), после каждой записи записывать ‘|’
	public void saveContractToStream(PrintWriter out){
		DateFormat df = Global.getDateFormat();
		out.println(id + "|" + df.format(date) + "|" + df.format(issueDate) + "|" + immobility.getId() + "|" + customer.getId() + "|" + estateAgent.getId());
	}
	public void loadContractFromStream(BufferedReader in) throws IOException, ParseException {
		int A, B, C;
		StringTokenizer st = new StringTokenizer(in.readLine(), "|", false);
		id = Integer.parseInt(st.nextToken());
		
		DateFormat df = Global.getDateFormat();
		date = df.parse(st.nextToken());
		issueDate = df.parse(st.nextToken());
		A = Integer.parseInt(st.nextToken());
		B = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		immobility = Global.getImmobility(A);
		customer = Global.customers.getElement(B);
		estateAgent = Global.estateAgents.getElement(C);
		lastId = Math.max(lastId, id + 1);
	}
	public int getId(){ return id; }
	//вернуть значение сравнения объектов this, e, если сортировка производится по (дата заключения, дата истечения, id договора)
	public int compareTo(Contract e){
			if(date.compareTo(e.date) != 0)
				return date.compareTo(e.date); 
			if(issueDate.compareTo(e. issueDate) != 0)
				return issueDate.compareTo(e. issueDate);
			return getId() - e.getId();
	}
	@Override
	public boolean test(Requirements req) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
