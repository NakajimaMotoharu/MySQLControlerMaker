import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ReadCSV {
	public static CSV input(String fileName){
		Path file = Paths.get(fileName);
		try {
			List<String>list = Files.readAllLines(file);

			String[] dataSet = list.get(0).split(",");
			CSV csv = new CSV(list.size() - 1, dataSet.length);
			csv.setTag(dataSet);

			for (int i = 1; i < list.size(); i = i + 1){
				csv.setData(i - 1, list.get(i).split(","));
			}

			csv.formatting();

			return csv;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
