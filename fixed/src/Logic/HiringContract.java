package Logic;
import java.io.*;
import java.text.ParseException;
import java.util.Date;
import java.util.StringTokenizer;

import javax.imageio.spi.RegisterableService;
public class HiringContract extends Contract{
	public final static String ONE_MONTH_FORWARD = "Предоплата, помесячная";
	public final static String ONE_MONTH_BACK = "Постоплата, помесечная";
	public final static String DAILY = "Посуточно";
	protected double price;
	protected String manner; 
	protected boolean active; 
	public HiringContract (Immobility im, Customer cus, EstateAgent ea, String manner, Date from, Date to) {
		super(im, cus, ea, from, to);
		if(manner != ONE_MONTH_BACK && manner != ONE_MONTH_FORWARD && manner != DAILY)
			throw new IllegalArgumentException();
		this.manner = manner;
		active = true;
		LeasingContract lc = Registry.getActiveLeasingContract(im, from, to);
		if(lc == null)
			price = Double.NaN;
		else
			price = lc.getPrice();
	}
	public HiringContract(BufferedReader in) throws IOException, ParseException { super(in); loadHiringContractFromStream(in); }
	public double getPrice() { 
		return price;
	}
	public void setPrice(double price){ 
		this.price = price; 
	}
	public String getMannerOfPayment(){ 
		return manner; 
	}
	//установить поле manner равным параметру manner. manner должна иметь значение одной из констант  ONE_MONTH_FORWARD, ONE_MONTH_BACK, DAILY.
	public void setMannerOfPаyment(String manner){
		if(manner != ONE_MONTH_BACK && manner != ONE_MONTH_FORWARD && manner != DAILY)
			throw new IllegalArgumentException();
		this.manner = manner;
	}

	// проверяет возможно ли замена даты истечения договора на указанную (условия соответствуют ограничениям на дату метода setNewIssueDate())
	public void checkNewIssueDate(Date newIssueDate){
		Date cur = new Date();
		if(Registry.dif(cur, newIssueDate) > 0) throw new RuntimeException("желаемая дата уже прошла");
		if(Registry.dif(issueDate, newIssueDate) < 0) throw new RuntimeException("желаемая дата позднее установленной");
	}

	/*  создать объект класса Date newIssueDate с сегодняшней датой
		проверить, что newIssueDate не позднее issueDate, иначе выбросить исключение
			«желаемая дата позднее установленной»
		проверить, что newIssueDate не раньше getTheEarliestIssue, иначе выбросить исключение
			«слишком ранняя дата, недвижимость уже снята» 
		изменить дату истечения.
			*/

	public void setNewIssueDate(Date newIssueDate){
		Date cur = new Date();
		if(Registry.dif(cur, newIssueDate) > 0) throw new RuntimeException("желаемая дата уже прошла");
		if(Registry.dif(issueDate, newIssueDate) < 0) throw new RuntimeException("желаемая дата позднее установленной");
		issueDate = newIssueDate;
	}
	/*	вызвать метод saveToStream родительского класса
		записать в поток все поля(в порядке: price, manner, active), разделять символом ‘|’ */
	
	public void saveHiringContractToStream(PrintWriter out){
		super.saveContractToStream(out);
		out.println(price + "|" + manner + "|" + active);
	}
	// присвоить значениям полей(в порядке:  price, manner, active) считываемые значения, значения полей разделены ‘|’;
	public void loadHiringContractFromStream(BufferedReader in) throws IOException{ 
			StringTokenizer st = new StringTokenizer(in.readLine(), "|", false);
			price = Double.parseDouble(st.nextToken());
			manner = st.nextToken();
			active = st.nextToken() == "true";
	}
}	
