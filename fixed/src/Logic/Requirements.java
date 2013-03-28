package Logic;
import java.io.*;
import java.util.*;

/*
 *   requirements – список, состоящий из строк, имеющих один из форматов:
 *    
					“uninhabited” - если ищется нежилая недвижимость.
					“inhabited” - если ищется жилая недвижимость.
					“area > X” / ”square < X”, где X - целое число - если ищется недвижимость площади больше/меньше Х (включительно).
					“floor > X”/”floor < X”, где X - целое число - если ищется недвижимость на этаже больше/меньше Х (включительно).
					“state X”, где X - байт, являющееся битовой маской, биты которого кодируют необходимые предъявляемые требования к искомому помещению. 
					0-ой бит- наличие мебели, 1ый-наличие телефона 2-ой -наличие интернета, 3-ий наличие стиральной машины, 4-ый наличие холодильника.
					“free before DD:MM:YYYY”/”free after DD:MM:YYYY” - если 	ищется недвижимость, свободная до/после указанной даты в заданном формате. Дата	должна существовать в григорианском	календаре.
 */

/* 
 		switch(type){
			case "uninhabited": break;
			case "inhabited": break;
			case "square": break;
			case "floor": break;
			case "state:": break;
			case "free": break;
			default: throw new RuntimeException("кто-то накосячил в формате ограничения " + s);
		}
 */
public class Requirements { 
	protected List<String> requirements;
	public Requirements() {
		requirements = new ArrayList<String>();
	}

	public void addRequirement (String req) {
		requirements.add(req);
	}
	public List<String> getRequirements() {
		return requirements;
	}
}
