
/*
 * 取得程序的行号和源文件名
 * */
public class LineNo {
	
	public static int getLineNumber() {
		return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}

	public static String getFileName() {
		return Thread.currentThread().getStackTrace()[2].getFileName();
	}

	public static void main(String args[]) {
		System.out.println("[" + getFileName() + " 行号 " + getLineNumber() + "]"
				+ "Hello World!");
	}
}