package Logic;
import java.io.*;
import java.util.*;

public class Immobility implements Testable, SaveableIndexable, Comparable<Immobility>{
	protected double area;
	protected int floor, id;
	protected String address;
	protected ArrayList<String> picturesPaths; 
	private static int lastId = 0;// значение id, последнего созданного/считанного объекта, увеличенный на 1	
	
	public Immobility (String address, double area, int floor) {
		setAddress(address);
		this.area = area;
		this.floor = floor;
		picturesPaths = new ArrayList<String>();
		id = lastId++;
	}
	protected Immobility(BufferedReader in) throws IOException{ loadImmobilityFromStream(in); }
	public String getAddress (){return address; }
	public void setAddress (String address) { 
		StringTokenizer st = new StringTokenizer(address, ",", false);
		try{
		for(int i = 0; i < 3; i++){
			String s = st.nextToken();
			if(s == null) throw new RuntimeException(); 
		}
		
		String ss = st.nextToken().replaceAll(" ", "");
		if(!(ss.charAt(0) == '-' && ss.length() == 1))
			Integer.parseInt(ss);
		
		} catch(Throwable e){
			throw new RuntimeException("формат адреса не соответствует ограничениям: город, улица, дом, квартира (или '-') через запятую | " + address);
		}
		System.err.println("ok, address =" + address);
		this.address = address; 
		}
	public double getSquare () { return area; }
	public int getFloor () { return floor; }
	public ArrayList<String> getPicturesPaths () { return picturesPaths; }
	
	public void addPicture (String path) {
		picturesPaths.add(path);
	}
	public void removePicture (int id){
		picturesPaths.remove(id);
	}
	//	записать в поток  поля(в порядке: id, area,  floor, address) после каждой записи разделять ‘|’
	//	записать в поток  поля(в порядке: pictures.size(), все элементы pictures ) каждая запись в отдельной строчке
	public void saveImmobilityToStream(PrintWriter out){
		out.println(id + "|" + area + "|" + floor + "|"+ address);
		out.println(picturesPaths.size());
		for(String s : picturesPaths)
			out.println(s);
	}
//	присвоить значениям полей(в порядке: id, area,  floor, address)считываемые  значения, значения полей разделены ‘|’
//	записать в lastId максимум из (lastId, id + 1)
//	создать пустой список соответствующего типа и присвоить полю pictures
//	считать кол-во элементов picturesPaths, записанных в файл
//	считать нужное количество строк и добавить в список picturesPaths
	public void loadImmobilityFromStream(BufferedReader in) throws NumberFormatException, IOException{
		String s;
		StringTokenizer st = new StringTokenizer(s = in.readLine(), "|", false);
		id = Integer.parseInt(st.nextToken());
		lastId = Math.max(lastId, id + 1);
		area = Double.parseDouble(st.nextToken());
		floor = Integer.parseInt(st.nextToken());
		address = st.nextToken();

		picturesPaths = new ArrayList<String>();
		int cnt = Integer.parseInt(in.readLine());
		for(int i = 0; i < cnt; i++)
			picturesPaths.add(in.readLine());
	}
	public int getId(){ return id; }
	
	/* вернуть отрицательное(положительное) челое число, если поле name текущего 
			объекта лексикографически меньше(больше) поля address объетка p
		в случае лексикографического равенства вернуть разность id. */
	public int compareTo(Immobility im){
		//if(address.compareTo(im.address) != 0)
		return address.compareTo(im.address);
		//return id - im.getId();
	}
//	проверить соответствие полей данного объекта каждому из ограничений 		
//	объекта req,  в случае не соответствия вернуть false; иначе вернуть true;
	
	public boolean test(Requirements req){ 
		for(String s : req.getRequirements()){
			StringTokenizer st = new StringTokenizer(s);
			String type = st.nextToken();
			switch(type){
				case "uninhabited": break;
				case "inhabited": break;
				case "square":
				{
					boolean less = st.nextToken().equals("<"); 
					double value = Double.parseDouble(st.nextToken());
					if(area < value != less && area != value) return false;
					break;
				}
				case "floor":
				{
					boolean less = st.nextToken().equals("<"); 
					int value = Integer.parseInt(st.nextToken());
					if(floor < value != less && floor != value) return false;
					break;
				}
				case "state:": break;
				case "free": break;
				default: throw new RuntimeException("кто-то накосячил в формате ограничения " + s);
			}
		}
		return true;
	}
	///
}
