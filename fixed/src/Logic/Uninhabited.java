package Logic;
import java.io.*;
import java.util.StringTokenizer;
public class Uninhabited extends Immobility{
	protected String prescription; 
	public Uninhabited (String address, double area, int floor, String prescription){
		super(address, area, floor);
		this.prescription = prescription.replaceAll("|", "");
	}
	protected Uninhabited(BufferedReader in) throws IOException { super(in); loadUninhabitedFromStream(in); }
	String getPrescription(){
		return prescription;
	}
	void setPrescription(String prescription){
		this.prescription = prescription.replaceAll("|", "");
	}
	/* вызвать метод test(req) родительского класса, в случае возвращенного результата, равного false, вернуть false
		проверить соответствие полей данного объекта каждому из ограничений объекта req,
			в случае не соответствия вернуть false;
		вернуть true;*/
	
	public boolean test(Requirements req){
		for(String s : req.getRequirements()){
			StringTokenizer st = new StringTokenizer(s);
			String type = st.nextToken();
	 		switch(type){
				case "uninhabited": break;
				case "inhabited": return false;
				case "square": break;
				case "floor": break;
				case "state:": throw new RuntimeException("кто-то накосячил в формате ограничения НЕЖИЛОЕ! " + s);
				case "free": break;
				default: throw new RuntimeException("кто-то накосячил в формате ограничения " + s);
			}
		}
		return super.test(req);
	}
	
	/*вызвать метод saveToStream родительского класса
		записать в поток все поля(в порядке: prescription), после каждой записи записывать ‘|’ */
	public void saveUninhabitedToStream(PrintWriter out){
		super.saveImmobilityToStream(out);
		out.println(prescription);
	}
	//присвоить значениям полей(в порядке:  prescription) считываемые значения
	public void loadUninhabitedFromStream(BufferedReader in) throws IOException { 
			prescription = in.readLine();
	}
	
	public String toString() {
		return address + "(" + prescription + ")";
	}
}
