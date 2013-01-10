package htmer;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;

import base.data;
import base.element;

public class htmll extends NodeVisitor {
	/**
	 * Each of element contains a set of text_node of one page, so parameter VV
	 * can store all text_node of many pages.
	 */
	private Vector<element> VV;
	/**
	 * Put all text_node of all pages into a hash_table HH, so we can figure out
	 * which text_node appear at every page. The parameter String store the
	 * text_node's text and element store the mark of the page and the times of
	 * the text_node appear at the page,here the mark is the filename.
	 */
	private Hashtable<String, element> HH;
	/**
	 * X is associated with HH and X is defined for the accumulation of entropy.
	 */
	private Vector<Hashtable<String, Integer>> X;
	/**
	 * Store the template of all the pages. according to it, we can filter the
	 * pages into text.
	 */
	private element E;
	/**
	 * Record the count of page to get the template.
	 */
	private int page_count;
	/**
	 * Just some mark for the program working effectively.
	 */
	private boolean symbol;
	/**
	 * Parameter for multi_thread process.
	 */
	private int pages = 100;
	/**
	 * Set <code>true</code> when traversing a PRE tag.
	 */
	private boolean pre;
	/**
	 * Set <code>true</code> when traversing a SCRIPT tag.
	 */
	private boolean script;
	/**
	 * Set <code>true</code> when traversing a STYLE tag.
	 */
	private boolean style;
	/**
	 * Store the filename of the file being processed.
	 */
	private String filepath;
	/**
	 * Define the max_count_of_page to get the template.
	 */
	private final static int THE_MAX_PAGE_COUNT_OF_GETMARK = 1000;
	/**
	 * Define file separator.
	 */
	private final static String FILE_SEPARATOR = File.separator;
	/**
	 * Define the name of the template for storing.
	 */
	private final static String NAME_OF_TEMPLATE = "filter";
	/**
	 * Define the format of the page which will be processed. such as HTM,HTML
	 * and so on.
	 */
	private final static String THE_FORMAT_OF_TARGET = ".htm";

	/**
	 * htmll_constructor parameters initialization.
	 */
	public htmll() {
		VV = new Vector<element>();
		HH = new Hashtable<String, element>();
		X = new Vector<Hashtable<String, Integer>>();
		E = new element();
		page_count = 0;
		symbol = false;
		pre = false;
		script = false;
		style = false;
	}

	/**
	 * Set the parameter of multi_thread. That is how many pages will be
	 * processed in each thread.
	 * 
	 * @param x
	 */
	public void set_pages(int x) {
		pages = x;
	}

	/**
	 * Set the filename of the file being processed.
	 */
	private void set_filepath(String s) {
		filepath = s;
	}

	/**
	 * Set the symbol of PRE,SCRIPT,STYLE tag.
	 * 
	 * @see org.htmlparser.visitors.NodeVisitor#visitTag(org.htmlparser.Tag)
	 */
	public void visitTag(Tag tag) {
		if (isPreTag(tag)) {
			pre = true;
		}
		if (isScriptTag(tag)) {
			script = true;
		}
		if (isStyleTag(tag)) {
			style = true;
		}
	}

	/**
	 * Visit the text_node and do some process.
	 * 
	 * @see org.htmlparser.visitors.NodeVisitor#visitStringNode(org.htmlparser.Text)
	 */
	public void visitStringNode(Text stringNode) {
		String txt = stringNode.getText();
		if (!pre && !style && !script) {
			if (VV.size() < page_count) {
				element e = new element();
				VV.add(e);
			}
			VV.elementAt(page_count - 1).V.add(new data(txt));
			if (!symbol) {
				if (HH == null || !HH.containsKey(txt)) {
					element e = new element();
					e.V.add(new data(filepath, 1));
					HH.put(txt, e);
				} else if (HH.containsKey(txt)) {
					int t = HH.get(txt).V.elementAt(0).get_mark();
					HH.get(txt).V.elementAt(0).set_mark(++t);
				}
			} else if (HH.containsKey(txt)) {
				int length = HH.get(txt).V.size();
				if (!HH.get(txt).V.elementAt(length - 1).get_txt()
						.equalsIgnoreCase(filepath))
					HH.get(txt).V.add(new data(filepath, 1));
				else {
					int t = HH.get(txt).V.elementAt(length - 1).get_mark();
					HH.get(txt).V.elementAt(length - 1).set_mark(++t);
				}
			}
		}
	}

	/**
	 * Reset the symbol of PRE,SCRIPT,STYLE tag.
	 * 
	 * @see org.htmlparser.visitors.NodeVisitor#visitEndTag(org.htmlparser.Tag)
	 */
	public void printVector() {

	}

	public void visitEndTag(Tag tag) {
		if (tag.getStartPosition() != tag.getEndPosition()) {
			if (isPreTag(tag)) {
				pre = false;
			}
			if (isScriptTag(tag)) {
				script = false;
			}
			if (isStyleTag(tag)) {
				style = false;
			}
		}
	}

	/**
	 * Judge if the tag is a PRE tag.
	 */
	private boolean isPreTag(Tag tag) {
		return tag.getTagName().equalsIgnoreCase("PRE");
	}

	/**
	 * Judge if the tag is a SCRIPT tag.
	 */
	private boolean isScriptTag(Tag tag) {
		return tag.getTagName().equalsIgnoreCase("SCRIPT");
	}

	/**
	 * Judge if the tag is a STYLE tag.
	 */
	private boolean isStyleTag(Tag tag) {
		return tag.getTagName().equalsIgnoreCase("STYLE");
	}

	/**
	 * html_filtering
	 * 
	 * @param filepath
	 * @throws ParserException
	 * @throws IOException
	 */
	private void get_template(String file) throws ParserException, IOException {
		set_filepath(file);
		Parser parser = new Parser(filepath);
		parser.setEncoding(new DectedEncode(filepath).dectedEncode());
		parser.visitAllNodesWith(this);
	}

	/**
	 * 生成模板。
	 * */
	private void getmark(File fileDir) throws ParserException, IOException,
			ClassNotFoundException {
		String s = fileDir.getPath();
		if (!read(s)) {
			String[] files = fileDir.list();
			for (String file : files) {
				if (file.contains(THE_FORMAT_OF_TARGET)
						&& page_count < THE_MAX_PAGE_COUNT_OF_GETMARK) {
					String filepath = fileDir.getPath() + FILE_SEPARATOR + file;
					if (!symbol && page_count == 1)
						symbol = true;
					page_count++;
					get_template(filepath);
				}
			}
//			System.out.println("下面是VV输出。");
//			System.out.println(VV);
//			System.out.println("下面是第一次HH输出");
//			System.out.println(HH);
			Enumeration<String> e = HH.keys();
			while (e.hasMoreElements()) {
				String t = e.nextElement();
				if (HH.get(t).V.size() == page_count && HH.get(t).is(1)) {

				}// ||HH.get(t).is(2))){}
				else
					HH.remove(t);
			}
			if (HH.size() == 0) {
				System.out.println("Template extraction failed!");
				System.exit(0);
			} else {
				// printVector();
//				System.out.println("下面是第二次HH输出");
//				System.out.println(HH);
				get_judge();
//				optimize();
				write(s);
			}
		}
	}

	private void pre_entropy() {
		StringBuffer SB = new StringBuffer(256);
		int index;
		for (int i = 0; i < VV.size(); i++) {
			index = 0;
			Vector<data> p = VV.elementAt(i).V;
			for (int j = 0; j < p.size(); j++) {
				String content = p.elementAt(j).get_txt();
				SB.append(content);
				if (HH.containsKey(content)) {
					if (i == 0)
						E.V.add(new data(content));
					String x = SB.toString();
					if (index >= X.size()) {
						Hashtable<String, Integer> t = new Hashtable<String, Integer>();
						t.put(x, new Integer(1));
						X.add(t);
					} else {
						if (X.elementAt(index).containsKey(x)) {
							int c = X.elementAt(index).get(x).intValue();
							c++;
							X.elementAt(index).remove(x);
							X.elementAt(index).put(x, new Integer(c));
						} else
							X.elementAt(index).put(x, new Integer(1));
					}
					index++;
					SB.delete(0, SB.length());
				}
			}
		}
//		System.out.println("下面是E输出");
//		System.out.println(E);
//		System.out.println("下面是X输出");
//		System.out.println(X);
	}

	private double get_entropy(Hashtable<String, Integer> x, int count) {
		double result = 0.0;
		if (count == 1)
			return 1;
		double[] a = new double[x.size()];
		int i = 0;
		Enumeration<String> e = x.keys();
		while (e.hasMoreElements()) {
			String t = e.nextElement();
			a[i] = x.get(t).intValue() / (count + result);
			i++;
		}
		for (i = 0; i < x.size(); i++)
			result = result - a[i] * (Math.log(a[i]) / Math.log(count));
		return result;
	}

	private void get_judge() {
		pre_entropy();
		Hashtable<String, Integer> x = X.elementAt(0);
		double threshold = get_entropy(x, page_count) - 0.0001;
		// System.out.println(threshold);
		E.V.elementAt(0).set_mark(1);
		for (int i = 1; i < X.size(); i++) {
			x = X.elementAt(i);
			double t = get_entropy(x, page_count);
			// System.out.println(t);
			if (t < threshold)
				E.V.elementAt(i).set_mark(0);
			else
				E.V.elementAt(i).set_mark(1);
		}
		System.out.println("下面是E输出");
		System.out.println(E);
	}

	/*
	 * optimize template
	 */
	protected void optimize() {
		int index = E.V.elementAt(0).get_mark(), i = 1, size = E.V.size();
		;
		while (i < size) {
			int t = E.V.elementAt(i).get_mark();
			if (t == 0 && t == index) {
				E.V.removeElementAt(i - 1);
				--size;
			} else {
				index = t;
				i++;
			}
		}
	}

	protected void write(String filename) throws IOException {
		String dest = filename + FILE_SEPARATOR + NAME_OF_TEMPLATE;
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				dest));
		for (int i = 0; i < E.V.size(); i++)
			oos.writeObject(new data(E.V.elementAt(i).get_txt(), E.V.elementAt(
					i).get_mark()));
		oos.close();
	}

	/**
	 * 读取模板。
	 * */
	private boolean read(String filename) throws IOException,
			ClassNotFoundException {
		String dest = filename + FILE_SEPARATOR + NAME_OF_TEMPLATE;
		if (!new File(dest).exists())
			return false;
		else {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					dest));
			data s;
			while (true) {
				try {
					s = (data) ois.readObject();
				} catch (EOFException e) {
					break;
				}
				E.V.add(s);
			}
			ois.close();
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	private void filter(File fileDir) throws ParserException, IOException {
		Vector<String> filelist = new Vector<String>();
		String[] files = fileDir.list();
		boolean symbol = false;
		for (String file : files) {
			if (file.contains(THE_FORMAT_OF_TARGET)) {
				String filepath = fileDir.getPath() + FILE_SEPARATOR + file;
				filelist.add(filepath);
				symbol = false;
				if (filelist.size() == pages) {
					filter f = new filter(E, (Vector<String>) filelist.clone());
					Thread thread = new Thread(f);
					thread.start();
					filelist.clear();
					symbol = true;
				}
			}
		}
		if (!symbol) {
			filter f = new filter(E, (Vector<String>) filelist.clone());
			Thread thread = new Thread(f);
			thread.start();
		}
	}

	public void process(String filefolder) throws ParserException, IOException,
			ClassNotFoundException {
		File file = new File(filefolder);
	
		getmark(file);
		filter(file);
		System.setOut(System.out);
	}

	public static void main(String[] args) throws ParserException, IOException,
			ClassNotFoundException {
		if (args.length != 1 && args.length != 2)
			System.out
					.println("usage error! java -jar htmlfiter.jar [filename] [N21]");
		else {
			htmll x = new htmll();
			if (args.length == 2)
				x.set_pages(Integer.parseInt(args[1]));
			long t1 = new Date().getTime();
			x.process(args[0]);
			long t2 = new Date().getTime();
			long t = (t2 - t1) / 1000;
			System.out.println("main thread process over! Consumed time:" + t
					+ " seconds.");
		}

	}
}