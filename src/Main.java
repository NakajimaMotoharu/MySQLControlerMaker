public class Main {
	public static void main(String[] args) {
		if (args.length != 2){
			System.out.println("java -jar ***.jar csvFileName");
			System.exit(1);
		}
		Setting.table = Setting.deleteExtensions(args[0]);
		Setting.init();

		CSV csv = ReadCSV.input(args[0]);
		MakeCFile.makeC(args[1], csv);
	}
}
