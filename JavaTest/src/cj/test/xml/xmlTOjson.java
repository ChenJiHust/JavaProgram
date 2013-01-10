package cj.test.xml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

public class xmlTOjson {

	private static final String STR_JSON = 
		"{\"name\":\"Michael\",\"address\":{\"city\":\"Suzou\",\"street\":\" Changjiang Road \",\"postcode\":100025},\"blog\":\"http://www.ij2ee.com\"}";

	private static final String STR_XML = 
		"<dblp><paper year=\"2009\"><author id=\"123\">Tom</author><author id=\"124\">Jim</author><title>XML</title><category>DB</category></paper><paper year=\"2010\"><author id=\"125\">Green</author><title>SQL</title><category>DB</category>	</paper></dblp>";

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
//		 TODO Auto-generated method stub
		 String xml = json2xml(STR_JSON);
		 System.out.println(xml);
		 String json = xml2json(STR_XML);
		 System.out.println(json);
		BufferedReader in = new BufferedReader(new FileReader(
				"D:\\西门子卫生项目\\CDA医疗文档xml\\SampleCDADocument.xml"));
		String s, s2 = new String();
		while ((s = in.readLine()) != null)
			s2 += s + "\n";
		System.out.println(s2);
		in.close();
		json = xml2json(s2);
		BufferedWriter outBufferedWriter = new BufferedWriter(new FileWriter(
				"D:\\西门子卫生项目\\CDA医疗文档xml\\SampleCDADocument.json"));
		outBufferedWriter.write(json);
		return;
	}

	public static String xml2json(String xmlString) {
		return new XMLSerializer().read(xmlString).toString();
	}

	public static String json2xml(String json) {
		JSONObject jobj = JSONObject.fromObject(json);
		String xmlString = new XMLSerializer().write(jobj);
		return xmlString;
	}
}
