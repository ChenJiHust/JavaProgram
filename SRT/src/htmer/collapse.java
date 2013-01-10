package htmer;

public class collapse {
	private String string;
	private StringBuffer buffer;
	public collapse(StringBuffer b,String s){
		string=s;
		buffer=b;
	}
	/**
	 * text's compression
	 * @param buffer
	 * @param string
	 */
	public void txt(){
        int chars;
        char character;
        int mCollapseState=0;
        chars = string.length ();
        if (0 != chars){
            for (int i = 0; i < chars; i++){
                character = string.charAt (i);
				switch (character){
                    // see HTML specification section 9.1 White space
                    // http://www.w3.org/TR/html4/struct/text.html#h-9.1
                    case '\u0020':
                    case '\u0009':
                    case '\u000C':
                    case '\u200B':
                    case '\r':
                    case '\n':
                        if (0 != mCollapseState)
                            mCollapseState = 1;
                        break;
                    default:
                        if (1 == mCollapseState)
                            buffer.append (' ');
                        mCollapseState = 2;
                        buffer.append (character);
                }
            }
        }
    }
}
