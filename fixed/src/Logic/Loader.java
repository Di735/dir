package Logic;
import java.io.BufferedReader;
import java.io.IOException;

public interface Loader <Type> {
	Type loadFromStream(BufferedReader in) throws IOException;
}
