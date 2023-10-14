import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Setting {
	public static String server, user, password, database, table;

	public static void init(){
		Path settingFile = Paths.get("Setting.cfg");
		try {
			List<String>list = Files.readAllLines(settingFile);
			server = list.get(0);
			user = list.get(1);
			password = list.get(2);
			database = list.get(3);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String deleteExtensions(String string){
		if (string == null){
			return null;
		}
		int p = string.lastIndexOf(".");
		if (p != -1){
			return string.substring(0, p);
		}
		return string;
	}
}
