package chenji.interview;

public class chenjiString {

	private char[] string;
	private int length;

	public chenjiString() {
		string = new char[50];
		length = 0;
	}

	public chenjiString(String str) {
		this.length = str.length();
		this.string = new char[this.length * 2];
		for (int i = 0; i < this.length; i++) {
			this.string[i] = str.charAt(i);
		}
	}

	public int strstr(chenjiString another) {
		int index = -1;
		int j;
		if (another.length > this.length)
			return -1;
		for (int i = 0; i < this.length; i++) {
			if (this.string[i] != another.string[0])
				continue;
			for (j = 1; j < another.length; j++) {
				if (this.string[i + j] != another.string[j])
					break;
			}
			if (j == another.length) {
				index = i;
				break;
			}
		}
		return index;
	}

	public String toString() {
		String str = new String();
		for (int i = 0; i < length; i++) {
			str += String.valueOf(string[i]);
		}
		return str;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		chenjiString x = new chenjiString("panpanlovecs");
		System.out.println(x.toString());
		System.out.println(x.strstr(new chenjiString("olec")));
	}

}
