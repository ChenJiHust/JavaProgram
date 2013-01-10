package htmer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.Translate;
import org.htmlparser.visitors.NodeVisitor;

import base.element;
public class filter extends NodeVisitor implements Runnable{
	private int index;
	private element E;
	private Vector<String> filelist;
	private StringBuffer mbuffer;
	private boolean mark;
    private static final String NEWLINE = System.getProperty ("line.separator");
    private static final int NEWLINE_SIZE = NEWLINE.length ();
    private boolean pre;
    private boolean script;
    private boolean style;
    private String filepath;
	public filter(element E2,Vector<String> filelist2) {
		index=0;
		E=E2;
		filelist=filelist2;
		mbuffer=new StringBuffer(4096);
		mark=true;
        pre= false;
        script=false;
        style=false;
	}
	private void set_filepath(String s){
		filepath =s;
	}
    public void visitTag(Tag tag)
    {
    	if(tag.breaksFlow())
    		carriageReturn ();
        if (isPreTag(tag)){
            pre= true;
        }
        if(isScriptTag(tag)){
        	script=true;
        }
        if(isStyleTag(tag)){
        	style=true;
        }
    }

    public void visitStringNode(Text stringNode) {
    	String txt = stringNode.getText();
        if (!pre&&!style&&!script) {
        	boolean b=txt.equalsIgnoreCase(E.V.elementAt(index).get_txt());
            if(mark&&!b){
                txt = Translate.decode (txt);
                txt = txt.replace ('\u00a0', ' ');
                new collapse(mbuffer, txt).txt();
            }
            if(index+1<E.V.size()&&b){
            	index++;
            	if(E.V.elementAt(index).get_mark()==1)
            		mark=true;
            	else
            		mark=false;
            }
        }
    }

    public void visitEndTag(Tag tag)
    {
    	if(tag.getStartPosition()!=tag.getEndPosition()){
    		if (isPreTag(tag)){
    			pre= false;
    		}
    		if(isScriptTag(tag)){
    			script=false;
    		}
    		if(isStyleTag(tag)){
    			style=false;
    		}
    	}
    }

    private boolean isPreTag(Tag tag) {
        return tag.getTagName().equalsIgnoreCase("PRE");
    }
    private boolean isScriptTag(Tag tag) {
        return tag.getTagName().equalsIgnoreCase("SCRIPT");
    }
    private boolean isStyleTag(Tag tag) {
        return tag.getTagName().equalsIgnoreCase("STYLE");
    }
	public void run(){
		try {
			if(filelist.size()!=0){
				for(int i=0;i<filelist.size();i++)
					pp(filelist.elementAt(i));
			}
		} catch (ParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void pp(String file) throws IOException, ParserException{
		index=0;
		mark=true;
		mbuffer.delete(0, mbuffer.length());
		set_filepath(file);
        Parser parser = new Parser (filepath);
        parser.setEncoding(new DectedEncode(filepath).dectedEncode());
        parser.visitAllNodesWith (this);
		if(index==E.V.size()-1){
			String dest=file.substring(0,file.indexOf(".htm"))+".txt";
			BufferedWriter writer=new BufferedWriter(new FileWriter(dest));
			writer.write(mbuffer.toString());
			writer.close();
		}
		else
			System.out.println("Exception happened when analysis "+file+" with template!");		
	}
    private void carriageReturn ()
    {
        int length;

        length = mbuffer.length ();
        if ((0 != length) // don't append newlines to the beginning of a buffer
            && ((NEWLINE_SIZE <= length) // not enough chars to hold a NEWLINE
            && (!mbuffer.substring (
                length - NEWLINE_SIZE, length).equals (NEWLINE))))
            mbuffer.append (NEWLINE);
    }
	
}