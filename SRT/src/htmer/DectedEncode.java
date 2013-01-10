package htmer;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeIterator;

public class DectedEncode {
	private static final String oriEncode = "utf-8,gb2312,gbk,iso-8859-1";
	private String filepath;
	public DectedEncode(String s){
		filepath=s;
	}
	public String dectedEncode() {
		String[] encodes = oriEncode.split(",");
		for (int i = 0; i < encodes.length; i++) {
			if (dectedCode(encodes[i]))
				return encodes[i];
		}
		return " ";
	}
	private boolean dectedCode(String encode) {
		try {
			Parser parser = new Parser(filepath);
			parser.setEncoding(encode);
			for (NodeIterator e = parser.elements(); e.hasMoreNodes();) {
				Node node = (Node) e.nextNode();
				if (node instanceof Html || node instanceof BodyTag)
					return true;
			}
		} catch (Exception e) {}
		return false;
	}
}
