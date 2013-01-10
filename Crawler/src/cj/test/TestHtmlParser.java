package cj.test;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

public class TestHtmlParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		extractKeyWordText(
				"https://www.ibm.com/developerworks/cn/opensource/os-cn-crawler/",
				"��������");

//		extracLinks("http://www.baidu.com");
//		StringBean sb = new StringBean();
//		sb.setLinks(false);//���ý����ȥ������
//		sb.setURL("http://www.baidu.com");//����������Ҫ�˵���ҳ��ǩ��ҳ�� url
//		System.out.println(sb.getStrings());//��ӡ���
	}

	// ѭ���������нڵ㣬��������ؼ��ֵ�ֵ�ڵ�
	public static void extractKeyWordText(String url, String keyword) {
		try {
			// ����һ����������������ҳ�� url ��Ϊ����
			Parser parser = new Parser(url);
			// ������ҳ�ı���,����ֻ��������һ�� gb2312 ������ҳ
			parser.setEncoding("gb2312");
			// �������нڵ�, null ��ʾ��ʹ�� NodeFilter
			NodeList list = parser.parse(null);
			// �ӳ�ʼ�Ľڵ��б�������еĽڵ�
			processNodeList(list, keyword);
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	private static void processNodeList(NodeList list, String keyword) {
		// ������ʼ
		SimpleNodeIterator iterator = list.elements();
		while (iterator.hasMoreNodes()) {
			Node node = iterator.nextNode();
			// �õ��ýڵ���ӽڵ��б�
			NodeList childList = node.getChildren();
			// ���ӽڵ�Ϊ�գ�˵����ֵ�ڵ�
			if (null == childList) {
				// �õ�ֵ�ڵ��ֵ
				String result = node.toPlainTextString();
				// �������ؼ��֣���򵥴�ӡ�����ı�
				if (result.indexOf(keyword) != -1)
					System.out.println(result);
			} // end if
			// ���ӽڵ㲻Ϊ�գ����������ú��ӽڵ�
			else {
				processNodeList(childList, keyword);
			}// end else
		}// end wile
	}

	// ��ȡһ����ҳ�����е����Ӻ�ͼƬ����
	public static void extracLinks(String url) {
		try {
			Parser parser = new Parser(url);
			parser.setEncoding("gb2312");
			// ���� <frame> ��ǩ�� filter��������ȡ frame ��ǩ��� src ����������ʾ������
			NodeFilter frameFilter = new NodeFilter() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public boolean accept(Node node) {
					if (node.getText().startsWith("frame src=")) {
						return true;
					} else {
						return false;
					}
				}
			};
			// OrFilter �����ù��� <a> ��ǩ��<img> ��ǩ�� <frame> ��ǩ��������ǩ�� or �Ĺ�ϵ
			OrFilter rorFilter = new OrFilter(
					new NodeClassFilter(LinkTag.class), new NodeClassFilter(
							ImageTag.class));
			OrFilter linkFilter = new OrFilter(rorFilter, frameFilter);
			// �õ����о������˵ı�ǩ
			NodeList list = parser.extractAllNodesThatMatch(linkFilter);
			for (int i = 0; i < list.size(); i++) {
				Node tag = list.elementAt(i);
				if (tag instanceof LinkTag)// <a> ��ǩ
				{
					LinkTag link = (LinkTag) tag;
					String linkUrl = link.getLink();// url
					String text = link.getLinkText();// ��������
					System.out.println(linkUrl + "**********" + text);
				} else if (tag instanceof ImageTag)// <img> ��ǩ
				{
					ImageTag image = (ImageTag) list.elementAt(i);
					System.out.print(image.getImageURL() + "********");// ͼƬ��ַ
					System.out.println(image.getText());// ͼƬ����
				} else// <frame> ��ǩ
				{
					// ��ȡ frame �� src ���Ե������� <frame src="test.html"/>
					String frame = tag.getText();
					int start = frame.indexOf("src=");
					frame = frame.substring(start);
					int end = frame.indexOf(" ");
					if (end == -1)
						end = frame.indexOf(">");
					frame = frame.substring(5, end - 1);
					System.out.println(frame);
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

}
