import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class MakeCFile {
	private static String[] template = null;
	private static int[] command = null;

	public static void makeC(String fileName, CSV csv){
		Path path = Paths.get(fileName);
		makeTemplate();
		try {
			BufferedWriter bufferedWriter = Files.newBufferedWriter(path);
			for (int i = 0; i < template.length; i = i + 1){
				writeString(bufferedWriter, template[i]);
				switch (command[i]) {
					case 0 -> writeNewLine(bufferedWriter);
					case 1 -> writeDoubleQuotation(bufferedWriter);
					case 2 -> writeEscapeNewLine(bufferedWriter);
					case 3 -> writeMySQL(bufferedWriter, Setting.table, csv);
					case 4 -> writeConnect(bufferedWriter);
					case 5 -> writeDelete(bufferedWriter, Setting.table);
					case 6 -> writeCreate(bufferedWriter, csv, Setting.table);
				}
			}
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeString(BufferedWriter bufferedWriter, String string){
		try {
			bufferedWriter.write(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeNewLine(BufferedWriter bufferedWriter){
		writeString(bufferedWriter, "\n");
	}

	private static void writeDoubleQuotation(BufferedWriter bufferedWriter){
		writeString(bufferedWriter, "\"");
	}

	private static void writeEscapeNewLine(BufferedWriter bufferedWriter){
		writeString(bufferedWriter, "\\n");
	}

	private static void writeMySQL(BufferedWriter bufferedWriter, String table, CSV csv){
		for (int i = 0; i < csv.getSizeC(); i = i + 1){
			writeInsert(bufferedWriter, table, makeContents(csv, i));
		}
	}

	private static void writeInsert(BufferedWriter bufferedWriter, String table, String contents){
		writeString(bufferedWriter, "	");
		writeString(bufferedWriter, "mysql_query(&buf, ");
		writeDoubleQuotation(bufferedWriter);
		writeString(bufferedWriter, String.format("INSERT INTO %s VALUES %s", table, contents));
		writeDoubleQuotation(bufferedWriter);
		writeString(bufferedWriter, ");");
		writeNewLine(bufferedWriter);
	}

	private static void writeDelete(BufferedWriter bufferedWriter, String table){
		writeString(bufferedWriter, "	");
		writeString(bufferedWriter, "mysql_query(&buf, ");
		writeDoubleQuotation(bufferedWriter);
		writeString(bufferedWriter, String.format("delete from %s ", table));
		writeDoubleQuotation(bufferedWriter);
		writeString(bufferedWriter, ");");
		writeNewLine(bufferedWriter);
	}

	private static void writeCreate(BufferedWriter bufferedWriter, CSV csv, String table){
		writeString(bufferedWriter, "	");
		writeString(bufferedWriter, "mysql_query(&buf, ");
		writeDoubleQuotation(bufferedWriter);
		writeString(bufferedWriter, String.format("create table %s %s", table, makeTag(csv)));
		writeDoubleQuotation(bufferedWriter);
		writeString(bufferedWriter, ");");
		writeNewLine(bufferedWriter);
	}

	private static void writeConnect(BufferedWriter bufferedWriter){
		writeString(bufferedWriter, "	");
		writeString(bufferedWriter, "db = mysql_real_connect(&buf, ");
		writeDoubleQuotation(bufferedWriter);
		writeString(bufferedWriter, Setting.server);
		writeDoubleQuotation(bufferedWriter);
		writeString(bufferedWriter, ", ");
		writeDoubleQuotation(bufferedWriter);
		writeString(bufferedWriter, Setting.user);
		writeDoubleQuotation(bufferedWriter);
		writeString(bufferedWriter, ", ");
		writeDoubleQuotation(bufferedWriter);
		writeString(bufferedWriter, Setting.password);
		writeDoubleQuotation(bufferedWriter);
		writeString(bufferedWriter, ", ");
		writeDoubleQuotation(bufferedWriter);
		writeString(bufferedWriter, Setting.database);
		writeDoubleQuotation(bufferedWriter);
		writeString(bufferedWriter, ", 0, NULL, 0);");
		writeNewLine(bufferedWriter);
	}

	private static void makeTemplate(){
		template = new String[20];
		command = new int[20];

		Arrays.fill(command, 0);
		//command 1 = '"', 2 = "\n", 3 = insert, 4 = connect, 5 = delete, 6 = create

		template[0] = "#include<stdio.h>";
		template[1] = "#include<stdlib.h>";
		template[2] = "#include<mysql/mysql.h>";

		template[3] = "int main(void){";
		template[4] = "	MYSQL buf, *db;";
		template[5] = "	mysql_init(&buf);";
		template[6] = "";
		//connect
		command[6] = 4;
		template[7] = "	if (db == NULL) {";
		template[8] = "		printf(";
		command[8] = 1;
		template[9] = "connect error";
		command[9] = 2;
		template[10] = "";
		command[10] = 1;
		template[11] = ");";
		template[12] = "		exit(1);";
		template[13] = "	}";
		template[14] = "";
		//create
		command[14] = 6;
		template[15] = "";
		//delete
		command[15] = 5;
		//insert
		template[16] = "";
		command[16] = 3;
		template[17] = "	mysql_close(&buf);";
		template[18] = "	return 0;";
		template[19] = "}";
	}

	private static String makeContents(CSV csv, int colum){
		String ans = "(", tmp;

		for (int i = 0; i < csv.getSizeR(); i = i + 1){
			if (i + 1 != csv.getSizeR()){
				tmp = ans + csv.getData(colum, i) + ", ";
			} else {
				tmp = ans + csv.getData(colum, i);
			}
			ans = tmp;
		}

		return ans + ")";
	}

	private static String makeTag(CSV csv){
		String ans = "(", tmp;

		for (int i = 0; i < csv.getSizeR(); i = i + 1){
			if (i + 1 != csv.getSizeR()){
				tmp = ans + String.format("%s %s, ", csv.getTag(i), csv.getTagType(i));
			} else {
				tmp = ans + String.format("%s %s", csv.getTag(i), csv.getTagType(i));
			}
			ans = tmp;
		}

		return ans + ")";
	}
}
