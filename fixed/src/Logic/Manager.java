package Logic;
import java.io.*;
import java.util.*;

public class Manager <Type extends SaveableIndexable & Comparable<? super Type> >{
	//тайна, покрытая протектедом!
	protected HashMap<Integer, Type> storeHM;
	protected TreeSet<Type> storeTS;
	private final LoaderSaver< Type> ls;
	// конструктор, создаёт пустой объект класса менеджер
	public Manager(LoaderSaver<Type> loaderSaver){
		this.ls = loaderSaver;
		storeHM = new HashMap<Integer, Type>();
		storeTS = new TreeSet<Type>();
	}
	// добавляет указанный элемент из менеджера
	public void add(Type element){
		storeHM.put(element.getId(), element);
		storeTS.add(element);
	}
	// удаляет указанный элемент из менеджера
	void remove(Type element){ 
		storeHM.remove(element.getId());
		storeTS.remove(element);
	}
	// возвращает элемент мэнеджера с getId() == id, либо null (в случае отсутствия)
	public Type getElement(int id){
		return storeHM.get(id);
	}
	// проверяет, содержится ли переданный объект в менеджере
	public boolean containsValue(Type value){
		return storeTS.contains(value);
	}
	// отсортированный список всех объектов менеджера
	public TreeSet<Type> getStore(){
		return storeTS;
	}
	public void saveToStream(PrintWriter out) {
		out.println(storeTS.size());
		for(Type t : storeTS)
			ls.saveToStream(out, t);
	}
	public void loadFromStream(BufferedReader in) throws NumberFormatException, IOException{
		int cnt = Integer.parseInt(in.readLine());
		for(int i = 0; i < cnt; i++){
			Type t = ls.loadFromStream(in);
			storeTS.add(t);
			storeHM.put(t.getId(), t);
		}
	}
}
