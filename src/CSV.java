import java.util.Arrays;

public class CSV {
	private String[][] data;
	private String[] tag;
	private int sizeR, sizeC;
	private boolean[] isRN, isInt;
	private int[] strSize;

	public CSV(int colum, int record){
		data = new String[colum][record];
		sizeC = colum;
		sizeR = record;
	}

	public void formatting(){
		isRN = new boolean[sizeR];
		isInt = new boolean[sizeR];
		strSize = new int[sizeR];

		Arrays.fill(isRN, true);
		Arrays.fill(isInt, true);
		Arrays.fill(strSize, 0);

		for (int i = 0; i < sizeC; i = i + 1){
			for (int j = 0; j < sizeR; j = j + 1){
				if (!isRealNumber(data[i][j])){
					isRN[j] = false;
				}
				if (!isInteger(data[i][j])){
					isInt[j] = false;
				}
				if (strSize[j] < data[i][j].length()){
					strSize[j] = data[i][j].length();
				}
			}
		}

		for (int i = 0; i < sizeC; i = i + 1){
			for (int j = 0; j < sizeR; j = j + 1){
				if (!isRN[j]){
					setData(i, j, String.format("'%s'", data[i][j]));
				}
			}
		}
	}

	public int getSizeC() {
		return sizeC;
	}

	public int getSizeR() {
		return sizeR;
	}

	public void setTag(String[] tag){
		this.tag = tag;
	}

	public void setData(int colum, String[] record){
		data[colum] = record;
	}

	public void setData(int colum, int record, String field){
		data[colum][record] = field;
	}

	public String getData(int colum, int record){
		return data[colum][record];
	}

	public String[] getData(int colum){
		return data[colum];
	}

	public String getTag(int record){
		return tag[record];
	}

	public String getTagType(int record){
		if (isRN[record]){
			if (isInt[record]){
				return "INT";
			} else {
				return "DOUBLE";
			}
		} else {
			return String.format("CHAR(%d)", strSize[record]);
		}
	}

	private static boolean isRealNumber(String string){
		try {
			Double.parseDouble(string);
			return true;
		} catch (NumberFormatException e){
			return false;
		}
	}

	private static boolean isInteger(String string){
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException e){
			return false;
		}
	}
}
