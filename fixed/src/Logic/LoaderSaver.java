package Logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public interface LoaderSaver<Type> {
	Type loadFromStream(BufferedReader in) throws IOException;
	void saveToStream(PrintWriter out, Type obj);
}
