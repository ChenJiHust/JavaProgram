package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import base.data;
import base.element;


public class Evaluations {
	private static final String oriEncode = "utf-8,gb2312,gbk,iso-8859-1";
	public Vector<element> VV;
	public Hashtable<String,element> HH;
	public Vector<Hashtable<String,Integer>> X;
	//store the strings that appear many times
	public Hashtable<String,String> AM;
	public Vector<String> buffer;
	public element E;
	int page_count,index;
	boolean symbol;
	double threshold;
	//arguments to evaluate
	int desired,discovered,common;
	double p,r,score;
	element desire;
	/**
	 * html_constructor
	 */
	public Evaluations(){
		VV=new Vector<element>();
		HH=new Hashtable<String,element>();
		X=new Vector<Hashtable<String,Integer>>();
		AM=new Hashtable<String,String>();
		buffer=new Vector<String>();
		E=new element();
		page_count=0;
		index=0;
		symbol=false;
		desired=discovered=common=0;
		p=r=score=0.0;
		desire=new element();
	}
	/**
	 * get_DOM
	 * @param filepath
	 * @return
	 * @throws ParserException
	 */
	private NodeIterator get_DOM(String filepath) throws ParserException{
		Parser parser=new Parser(filepath);
		String charset=dectedEncode(filepath);
		parser.setEncoding(charset);		
		return parser.elements();
	}
	/**
	 * html_filtering
	 * @param filepath
	 * @throws ParserException
	 * @throws IOException
	 */
	private void get_template(String filepath) throws ParserException, IOException{
		for(NodeIterator i=get_DOM(filepath);i.hasMoreNodes();)
			process(i.nextNode(),0,filepath);
	}
	private void getmark(File fileDir) throws ParserException, IOException{
			String[] files = fileDir.list();
			for(String file:files){
				if(file.contains(".htm")){
					String filepath=fileDir.getPath()+File.separator+file;
					if(!symbol&&page_count==1)
						symbol=true;
					page_count++;
					get_template(filepath);
				}
			}
			Enumeration<String> e=HH.keys();
			while(e.hasMoreElements()){
				String t=e.nextElement();
				if(HH.get(t).V.size()==page_count&&(HH.get(t).is(1)||HH.get(t).is(2))){
					if(HH.get(t).is(2))
						AM.put(t," ");
				}
				else
					HH.remove(t);
			}
			if(HH.size()==0){
				System.out.println("Template extraction failed!");
				System.exit(0);
			}
			else{
				get_judge();
//				optimize();
			}
	}
	/**
	 * process node
	 * @param node
	 * @param option
	 * @param writer
	 * @throws ParserException
	 * @throws IOException
	 */
	private void process(Node node,int option,String filepath) throws ParserException, IOException{
		String tagname=null;
		String txt=null;
		if(node instanceof TagNode){
			 TagNode tag = (TagNode)node;
			 tagname=tag.getTagName();
			 if(!tagname.equalsIgnoreCase("PRE")&&!tagname.equalsIgnoreCase("SCRIPT")&&!tagname.equalsIgnoreCase("STYLE")){
				 NodeList n1=node.getChildren();
				 if(n1!=null){
					 for(NodeIterator i=n1.elements();i.hasMoreNodes();){
						 process(i.nextNode(),option,filepath);
					 }
				 }
			 }
		}
		if(node instanceof TextNode){
			TextNode text=(TextNode)node;
			txt=text.getText();
			if((txt.length()==2&&txt.charAt(0)==13&&txt.charAt(1)==10)){}
			else{
				txt=txt.replaceAll("&nbsp;","");
			    txt=txt.replaceAll("&lt;", "<");
			    txt=txt.replaceAll("&gt;", ">");
				StringBuffer sb=new StringBuffer(256);
				collapse(sb,txt);
				String content=sb.toString();
				if(content.length()!=0&&(content.length()!=1||content.charAt(0)!=65279)){
					if(option==0){
						if(VV.size()<page_count){
							element e=new element();
							VV.add(e);
						}
						VV.elementAt(page_count-1).V.add(new data(content));
						if(!symbol){
							if(HH==null||!HH.containsKey(content)){
								element e=new element();
								e.V.add(new data(filepath,1));
								HH.put(content,e);
							}
							else if(HH.containsKey(content)){
								int t=HH.get(content).V.elementAt(page_count-1).get_mark();
								HH.get(content).V.elementAt(page_count-1).set_mark(++t);
							}
						}
						else if(HH.containsKey(content)){
							int length=HH.get(content).V.size();
							if(!HH.get(content).V.elementAt(length-1).get_txt().equalsIgnoreCase(filepath))
								HH.get(content).V.add(new data(filepath,1));
							else{
								int t=HH.get(content).V.elementAt(length-1).get_mark();
								HH.get(content).V.elementAt(length-1).set_mark(++t);
							}
						}
					}
					else{
						if(index<E.V.size()&&content.equalsIgnoreCase(E.V.elementAt(index).get_txt())){
							if(E.V.elementAt(index).get_mark()!=0){
								discovered+=buffer.size();
							}
							if(desire.V.elementAt(index).get_mark()!=0)
								desired+=buffer.size();
							if(E.V.elementAt(index).get_mark()!=0&&desire.V.elementAt(index).get_mark()!=0)
								common+=buffer.size();
							buffer.clear();
							index++;
						}
						else
							buffer.add(txt);
					}
				}
			}
		}
	}
	/**
	 * text's compression
	 * @param buffer
	 * @param string
	 */
	protected void collapse (StringBuffer buffer, String string){
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
	private void pre_entropy() throws IOException{
		StringBuffer SB=new StringBuffer(256);
		BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
		for(int i=0;i<VV.size();i++){
			index=0;
			Vector<data> p=VV.elementAt(i).V;
			for(int j=0;j<p.size();j++){
				String content=p.elementAt(j).get_txt();
				SB.append(content);
				if(HH.containsKey(content)){
					String x=SB.toString();
					if(i==0){
						E.V.add(new data(content));
						System.out.println(x);
						if(in.readLine().equalsIgnoreCase("1"))
							desire.V.add(new data(content,1));
						else
							desire.V.add(new data(content,0));
					}
					if(index>=X.size()){
						Hashtable<String,Integer> t=new Hashtable<String,Integer>();
						t.put(x, new Integer(1));
						X.add(t);
					}
					else{
						if(X.elementAt(index).containsKey(x)){
							int c=X.elementAt(index).get(x).intValue();
							c++;
							X.elementAt(index).remove(x);
							X.elementAt(index).put(x, new Integer(c));
						}
						else
							X.elementAt(index).put(x, new Integer(1));
					}
					index++;
					SB.delete(0, SB.length());
				}
			}
		}
	}
	private double get_entropy(Hashtable<String,Integer> x,int count){
		double result=0.0;
		if(count==1)
			return 1;
		double[] a=new double[x.size()];
		int i=0;
		Enumeration<String> e=x.keys();
		while(e.hasMoreElements()){
			String t=e.nextElement();
			a[i]=x.get(t).intValue()/(count+result);
			i++;
		}
		for(i=0;i<x.size();i++)
			result=result-a[i]*(Math.log(a[i])/Math.log(count));
		return result;
	}
	private void get_judge() throws IOException{
		pre_entropy();
		Hashtable<String,Integer> x=X.elementAt(0);
		threshold=get_entropy(x,page_count)-0.0001;
//		System.out.println(threshold);
		E.V.elementAt(0).set_mark(1);
		for(int i=1;i<X.size();i++){
			x=X.elementAt(i);
			double t=get_entropy(x,page_count);
//			System.out.println(t);
			if(t<threshold)
				E.V.elementAt(i).set_mark(0);
			else
				E.V.elementAt(i).set_mark(1);
		}
	}
	//optimize template
	protected void optimize(){
		int index=E.V.elementAt(0).get_mark(),i=1,size=E.V.size();;
		while(i<size){
			int t=E.V.elementAt(i).get_mark();
			if(t==0&&t==index&&!AM.containsKey(E.V.elementAt(i-1).get_txt())){
				E.V.removeElementAt(i-1);
				--size;
			}
			else{
				index=t;
				i++;
			}
		}
//		for(i=0;i<size;i++)
//			System.out.println(E.V.elementAt(i).get_txt()+" "+E.V.elementAt(i).get_mark());
	}
	private void filter(String file,BufferedWriter wr) throws ParserException, IOException{
		index=0;
		for(NodeIterator i=get_DOM(file);i.hasMoreNodes();)
			process(i.nextNode(),1,null);
//		System.out.println(common+" "+desired+" "+discovered);
		p=(common+0.0)/discovered;
		r=(common+0.0)/desired;
		score=2*p*r/(p+r);
		wr.write(file+"\tp:"+p+"\tr:"+r+"\tF"+score);
		wr.newLine();
		buffer.clear();
	}
	private void filter(File fileDir) throws ParserException, IOException{
		String[] files = fileDir.list();
		BufferedWriter wr=new BufferedWriter(new FileWriter(fileDir.getPath()+File.separator+"evaluation.txt"));
		for(String file:files){
			if(file.contains(".htm")){
				String filepath=fileDir.getPath()+File.separator+file;
				filter(filepath,wr);
				p=r=score=0.0;
				desired=discovered=common=0;
			}
		}
		wr.close();
	}
	public void process(String filefolder) throws ParserException, IOException{
		File file=new File(filefolder);
		getmark(file);
		filter(file);
	}
	public static String dectedEncode(String url) {
		String[] encodes = oriEncode.split(",");
		for (int i = 0; i < encodes.length; i++) {
			if (dectedCode(url, encodes[i])) {
				return encodes[i];
			}
		}
		return " ";
	}

	public static boolean dectedCode(String url, String encode) {
		try {
			Parser parser = new Parser(url);
			parser.setEncoding(encode);
			for (NodeIterator e = parser.elements(); e.hasMoreNodes();) {
				Node node = (Node) e.nextNode();
				if (node instanceof Html || node instanceof BodyTag) {
					return true;
				}
			}
		} catch (Exception e) {
		}

		return false;
	}
	public static void main(String[] args) throws ParserException, IOException{
		if(args.length!=1)
			System.out.println("usage error! java -jar htmlfiter.jar [filename]");
		else{
			Evaluations x=new Evaluations();	
			x.process(args[0]);
			System.out.println("process over! ");
		}
	}
}
