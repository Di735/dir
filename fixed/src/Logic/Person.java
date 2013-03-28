package Logic;
import java.util.*;
import java.io.*;


public class Person implements SaveableIndexable, Comparable<Person>{

	private static String rus = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя";
	private static String eng = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static String digits = "0123456789";
	
	private static int lastId = 0;// значение id, последнего созданного/считанного объекта, увеличенный на 1
	protected String phone, name, passport;
	private int id;
	protected Person(BufferedReader in) throws IOException{ this.loadPersonFromStream(in);}
	public Person (String name) throws IllegalAccessException { 
		id = lastId++;
		setName(name);
	}
	public String getName() { return name; }
	
	
//	проверить name на соответствие формату : 3 подстроки над алфавитом {А…Я, а…я, ‘-‘}, разделённые пробелом.
//	если строка не соответствует необходимому формату – выбросить иключение
//		IllegalArgumentException
	
	public void setName (String name) throws IllegalAccessException { 
		StringTokenizer st = new StringTokenizer(name);
		boolean ok = true;
		String ret = "", cur;
		for(int i = 0; i < 3; i++){
			cur = st.nextToken();
			for(int j = 0; j < cur.length(); j++)
				if(!rus.contains(cur.charAt(j) + "")) ok = false;
			if(i != 0)
				ret = ret + " ";
			ret = ret + cur;
		}
		
		if(!ok || st.hasMoreTokens()) throw new IllegalAccessException();
		this.name = ret;
	 }
	
	public String getPhoneNumber () { return phone; }
	
	/* присвоить полю number значение параметра number, удалив из него символы, 
	не принадлежащие {0 … 9, ‘+’, ‘-‘, ‘*’, ‘#’, ‘ ‘, ‘,’}; */
	public void setPhoneNumber (String number) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < number.length(); i++){
			boolean ok = false;
			char c = number.charAt(i);
			ok |= '0' <= c && c <= '9';
			ok |= " +-*#,".contains(c + "");
			if(ok) sb.append(c);
		}
		this.phone = sb.toString();
	}
	public String getPassportData() {
		return passport;
	}
	/*проверить passportData на соответствие формату : строка, имеющая вид: серия, пробел, номер, где серия, номер – строки, состоящие из {0…9, A…Z, a…z, А…Я, а…я}, 
			в случае отсутствия номера или серии пробела нет.
			если строка не соответствует необходимому формату – выбросить иключение
			IllegalArgumentException
		присвоить полю passport значение параметра passport;*/
	public void setPassportData (String passportData) throws IllegalArgumentException {
        int pos = 0;
        while(pos < passportData.length() && passportData.charAt(pos) != ' '){
                String c = passportData.charAt(pos) + "";
                if(!(rus.contains(c) || eng.contains(c) || digits.contains(c)))
                        throw new IllegalArgumentException("неизвестный символ в номере | паспортные данные " + c);
                pos++;
        }
        pos++;
        boolean nonEmptySecond = false;
        while(pos < passportData.length()){
                nonEmptySecond = true;
                String c = passportData.charAt(pos) + "";
                if(!(rus.contains(c) || eng.contains(c) || digits.contains(c)))
                        throw new IllegalArgumentException();
                pos++;
        }
        if(!nonEmptySecond && pos != passportData.length() + 1) throw new IllegalArgumentException("лишний пробел в конце строки | паспортные данные");
        if(passportData.length() == 0)throw new IllegalArgumentException("неверный формат, пустая строка | паспортные данные");
        this.passport = passportData;  
}
	// записать в поток все поля(в порядке: id, phone, name, passport), разделять ‘|’
	public void savePersonToStream(PrintWriter out){
		if(phone.length() == 0) phone = " ";
		out.println(id + "|" + phone + "|" + name + "|" + passport);
	}

	// присвоить значениям полей(в порядке: id, phone, name, passport) считываемые значения, значения полей разделены ‘|’
	// присвоить полю lastId максимум из lastId, (id + 1)
	public void loadPersonFromStream(BufferedReader in) throws IOException {
		StringTokenizer st = new StringTokenizer(in.readLine(), "|", false);
		id = Integer.parseInt(st.nextToken());
		phone = st.nextToken();
		name = st.nextToken();
		passport = st.nextToken();
		lastId = Math.max(lastId, id + 1);
	}
	public int getId() { return id; }
	/* вернуть отрицательное(положительное) челое число, если поле name текущего 
			объекта лексикографически меньше(больше) поля name объетка p
		в случае лексикографического равенства вернуть разность id. */
	public int compareTo(Person p){
//		if(getName().compareTo(p.getName()) != 0) return getName().compareTo(p.getName());
//		return id - p.getId();
		return getName().compareTo(p.getName());
	}
	
	@Override
	public String toString() {
		return name;
	}
}
