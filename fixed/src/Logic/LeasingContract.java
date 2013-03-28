package Logic;
import java.io.*;
import java.text.ParseException;
import java.util.*;

public class LeasingContract extends Contract{
	double price, percentage;
	public LeasingContract (Immobility im, Customer cus, EstateAgent ea, double price, double percentage, Date from, Date to) {
		super(im, cus, ea, from, to);
		this.price = price;
		this.percentage = percentage;
	}
	public LeasingContract (BufferedReader in) throws IOException, ParseException {super(in); loadLeasingContractFromStream(in); }
	public double getPrice() {
		return price;
	}
	public void setPrice(double price){
		this.price = price;
	}
	public double getPercentage(){
		return percentage;
	}
	public void setPercentage (double percentage){
		this.percentage = percentage;
	}
	/*вызвать метод saveToStream родительского класса
		записать в поток все поля(в порядке: price, percentage), после каждой записи записывать ‘|’*/
	
	public void saveLeasingContractToStream(PrintWriter out){
		super.saveContractToStream(out);
		out.println(price + "|" + percentage);
	}
	/*вызвать метод loadFromStream родительского класса
		присвоить значениям полей(в порядке:  price, percentage) считываемые значения, значения полей разделены ‘|’ */
	
	public void loadLeasingContractFromStream(BufferedReader in) throws IOException {
		StringTokenizer st = new StringTokenizer(in.readLine(), "|", false);
		price = Double.parseDouble(st.nextToken());
		percentage = Double.parseDouble(st.nextToken());
	}
	// проверяет возможно ли замена даты истечения договора на указанную (условия соответствуют ограничениям на дату метода setNewIssueDate())
	public void checkNewIssueDate(Date newIssueDate){
		
		System.err.println("old = " + issueDate);
		System.err.println("new = " + newIssueDate);
		if(Registry.dif(newIssueDate, issueDate) > 0) throw new RuntimeException("Желаемая дата позже установленной");
		if(Registry.dif(new Date(), (newIssueDate)) > 0) throw new RuntimeException("Желаемая дата ранее текущей");
		if(Registry.dif(date, newIssueDate) > 0) throw new RuntimeException("Желаемая дата ранее начальной");
		Date earliest = Registry.getEarliestIssueDate(immobility, date, newIssueDate);
		System.err.println("earliest = " + earliest);
		System.err.println("new + " + newIssueDate);
		if(Registry.dif(earliest, newIssueDate) > 0) throw new RuntimeException("слишком ранняя дата, недвижимость уже снята");
	}
//		проверить, что newIssueDate не позднее issueDate, иначе выбросить исключение
//			«желаемая дата позднее установленной»
//		проверить, что newIssueDate не раньше Registry.getEarliestIssueDate(immobility), иначе выбросить исключение	«слишком ранняя дата, недвижимость уже снята»	
	protected void setNewIssueDate (Date newIssueDate){ 
		if(Registry.dif(newIssueDate, issueDate) > 0) throw new RuntimeException("Желаемая дата позже установленной");
		if(Registry.dif(new Date(), issueDate) > 0) throw new RuntimeException("желаемая дата расторжения договора уже прошла");
		Date earliest = Registry.getEarliestIssueDate(immobility, date, newIssueDate);
		if(Registry.dif(earliest, newIssueDate) > 0) throw new RuntimeException("слишком ранняя дата, недвижимость уже снята");
		issueDate = newIssueDate;
	}
}
