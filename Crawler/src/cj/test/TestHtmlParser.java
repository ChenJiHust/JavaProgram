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
				"简易爬虫");

//		extracLinks("http://www.baidu.com");
//		StringBean sb = new StringBean();
//		sb.setLinks(false);//设置结果中去点链接
//		sb.setURL("http://www.baidu.com");//设置你所需要滤掉网页标签的页面 url
//		System.out.println(sb.getStrings());//打印结果
	}

	// 循环访问所有节点，输出包含关键字的值节点
	public static void extractKeyWordText(String url, String keyword) {
		try {
			// 生成一个解析器对象，用网页的 url 作为参数
			Parser parser = new Parser(url);
			// 设置网页的编码,这里只是请求了一个 gb2312 编码网页
			parser.setEncoding("gb2312");
			// 迭代所有节点, null 表示不使用 NodeFilter
			NodeList list = parser.parse(null);
			// 从初始的节点列表跌倒所有的节点
			processNodeList(list, keyword);
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	private static void processNodeList(NodeList list, String keyword) {
		// 迭代开始
		SimpleNodeIterator iterator = list.elements();
		while (iterator.hasMoreNodes()) {
			Node node = iterator.nextNode();
			// 得到该节点的子节点列表
			NodeList childList = node.getChildren();
			// 孩子节点为空，说明是值节点
			if (null == childList) {
				// 得到值节点的值
				String result = node.toPlainTextString();
				// 若包含关键字，则简单打印出来文本
				if (result.indexOf(keyword) != -1)
					System.out.println(result);
			} // end if
			// 孩子节点不为空，继续迭代该孩子节点
			else {
				processNodeList(childList, keyword);
			}// end else
		}// end wile
	}

	// 获取一个网页上所有的链接和图片链接
	public static void extracLinks(String url) {
		try {
			Parser parser = new Parser(url);
			parser.setEncoding("gb2312");
			// 过滤 <frame> 标签的 filter，用来提取 frame 标签里的 src 属性所、表示的链接
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
			// OrFilter 来设置过滤 <a> 标签，<img> 标签和 <frame> 标签，三个标签是 or 的关系
			OrFilter rorFilter = new OrFilter(
					new NodeClassFilter(LinkTag.class), new NodeClassFilter(
							ImageTag.class));
			OrFilter linkFilter = new OrFilter(rorFilter, frameFilter);
			// 得到所有经过过滤的标签
			NodeList list = parser.extractAllNodesThatMatch(linkFilter);
			for (int i = 0; i < list.size(); i++) {
				Node tag = list.elementAt(i);
				if (tag instanceof LinkTag)// <a> 标签
				{
					LinkTag link = (LinkTag) tag;
					String linkUrl = link.getLink();// url
					String text = link.getLinkText();// 链接文字
					System.out.println(linkUrl + "**********" + text);
				} else if (tag instanceof ImageTag)// <img> 标签
				{
					ImageTag image = (ImageTag) list.elementAt(i);
					System.out.print(image.getImageURL() + "********");// 图片地址
					System.out.println(image.getText());// 图片文字
				} else// <frame> 标签
				{
					// 提取 frame 里 src 属性的链接如 <frame src="test.html"/>
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
