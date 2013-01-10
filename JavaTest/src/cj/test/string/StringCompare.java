package cj.test.string;

public class StringCompare {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	    A();
	    B();
	    C();
	    C2();
	    D();
	    E();
	}

	public static void A() {
	    String str1 = "java";
	    String str2 = "java";
	    System.out.println(str1 == str2);  //true
	  }
	  public static void B() {
	    String str1 = new String("java");
	    String str2 = new String("java");
	    System.out.println(str1 == str2);  //false
	  }
	  public static void C() {
	    String str1 = "java";
	    String str2 = "blog";
	    String s = str1 + str2;
	    System.out.println(s == "javablog");  //false
	  }
	  public static void C2() {
	    String str1 = "javablog";
	    String str2 = "java"+"blog";    //在编译时被优化成String str2 = "javablog";
	    System.out.println(str1 == str2);  //true
	  }
	  public static void D() {
	    String s1 = "java";
	    String s2 = new String("java");
	    System.out.println(s1.intern() == s2.intern());  //true
	  }

	  public static void E() {
	    String str1 = "java";
	    String str2 = new String("java");
	    System.out.println(str1.equals(str2));  //true
	  }

}
