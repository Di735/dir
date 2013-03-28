package Logic;
import java.io.*;
import java.util.StringTokenizer;

public class Inhabited extends Immobility{
	protected int state; /* битовая маска состояния, где 
							0-ой бит- наличие мебели, 
							1ый-наличие телефона 
							2-ой -наличие интернета, 
							3-ий наличие стиральной машины, 
							4-ый наличие холодильника.*/
	
	public Inhabited (String address, double area, int floor, int state){
		super(address, area, floor);
		this.state = state;
	}
	protected Inhabited(BufferedReader in) throws IOException { super(in); loadInhabitedFromStream(in); }
	public int getState(){
		return state;
	}
	public void setState(int state) {
		this.state = state;
	} 
	public boolean test(Requirements req){
		for(String s : req.getRequirements()){
			StringTokenizer st = new StringTokenizer(s);
			String type = st.nextToken();
	 		switch(type){
				case "uninhabited": return false;
				case "inhabited": break;
				case "square": break;
				case "floor": break;
				case "state:":
					int value = Integer.parseInt(st.nextToken());
					if((value & state) != value) return false; // не является подмаской
					break;
				case "free": break;
				default: throw new RuntimeException("кто-то накосячил в формате ограничения " + s);
			}
		}
		return super.test(req);
	}
	public void saveInhabitedToStream(PrintWriter out){
		saveImmobilityToStream(out);
		out.println(state);
	}
	public void loadInhabitedFromStream(BufferedReader in) throws NumberFormatException, IOException{ 
		state = Integer.parseInt(in.readLine());
	}
	
	public String toString() {
		return address;
	}
}
