package chenji.interview;

//什么是最长公共子序列呢?好比一个数列 S，如果分别是两个或多个已知数列的子序列，且是所有符合此条件序列中最长的，
//则S 称为已知序列的最长公共子序列。
//举个例子，如：有两条随机序列，如 1 3 4 5 5 ，and 2 4 5 5 7 6，
//则它们的最长公共子序列便是：4 5 5。
//注意最长公共子串（Longest CommonSubstring）和最长公共子序列（LongestCommon Subsequence, LCS）的区别：
//子串（Substring）是串的一个连续的部分，子序列（Subsequence）则是从不改变序列的顺序，
//而从序列中去掉任意的元素而获得的新序列；更简略地说，前者（子串）的字符的位置必须连续，后者（子序列LCS）则不必。
//比如字符串acdfg同akdfc的最长公共子串为df，而他们的最长公共子序列是adf。

public class LCS {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// System.out.println(lcs("134595sei", "245576abcegi"));
		// System.out.println(lcs("acdabbc", "cddbacaba"));
		// System.out.println(lcsSubstring("12341chenjichenji",
		// "chenjichenjinihao"));
		System.out.println(lcSubsequence("abcd", "ad"));
	}

	// * 找两个字符串的最长公共子串，这个子串要求在原字符串中是连续的。 其实这又是一个序贯决策问题，可以用动态规划来求解。
	// * 我们采用一个二维矩阵来记录中间的结果。 这个二维矩阵怎么构造呢？
	// * 直接举个例子吧："bab"和"caba"(当然我们现在一眼就可以看出来最长公共子串是"ba"或"ab")
	// * * b a b
	// * c 0 0 0
	// * a 0 1 0
	// * b 1 0 1
	// * a 0 1 0
	// * 我们看矩阵的斜对角线最长的那个就能找出最长公共子串。
	// * 不过在二维矩阵上找最长的由1组成的斜对角线也是件麻烦费时的事，
	// * 下面改进： 当要在矩阵是填1时让它等于其左上角元素加1。 *
	// * * b a b
	// * c 0 0 0
	// * a 0 1 0
	// * b 1 0 2
	// * a 0 2 0
	// * 这样矩阵中的最大元素就是 最长公共子串的长度。
	// * 在构造这个二维矩阵的过程中由于得出矩阵的某一行后其上一行就没用了，
	// * 所以实际上在程序中可以用一维数组来代替这个矩阵。

	public static String lcsSubstring(String str1, String str2) {
		String str = null;
		if ((str1 == null) || (str2 == null))
			return str;
		int lenght = str1.length();
		int[] flag = new int[lenght];
		int max = 0;
		int poi = 0;
		for (int i = 0; i < str2.length(); i++) {
			char ch1 = str2.charAt(i);
			for (int j = lenght - 1; j > 0; j--) {
				char ch2 = str1.charAt(j);
				if (ch2 == ch1) {
					flag[j] = flag[j - 1] + 1;
					if (max < flag[j]) {
						max = flag[j];
						poi = j;
					}
				} else {
					flag[j] = 0;
				}
			}
			if (str1.charAt(0) == ch1)
				flag[0] = 1;
			else {
				flag[0] = 0;
			}
		}
		str = str1.substring(poi - max + 1, poi + 1);
		return str;
	}

	// /**
	// * 找最长公共子序列 回溯法 最长公共子序列与最长公共子串的区别在于最长公共子序列不要求在原字符串中是连续的，
	// * 比如ADE和ABCDE的最长公共子序列是ADE。 我们用动态规划的方法来思考这个问题如是求解。
	// * 首先要找到状态转移方程：
	// * 等号约定，C1是S1的最右侧字符，C2是S2的最右侧字符，S1‘是从S1中去除C1的部分，S2'是从S2中去除C2的部分。
	// * LCS(S1,S2)等于下列3项的最大者：
	// * （1）LCS（S1，S2’）
	// * （2）LCS（S1’，S2）
	// * （3）LCS（S1’，S2’）--如果C1不等于C2； LCS（S1'，S2'）+C1--如果C1等于C2；
	// * 边界终止条件：如果S1和S2都是空串，则结果也是空串。
	// *
	// * 下面我们同样要构建一个矩阵来存储动态规划过程中子问题的解。
	// * 这个矩阵中的每个数字代表了该行和该列之前的LCS的长度。
	// * 与上面刚刚分析出的状态转移议程相对应，矩阵中每个格子里的数字应该这么填，它等于以下3项的最大值：
	// * （1）上面一个格子里的数字
	// * （2）左边一个格子里的数字
	// * （3）左上角那个格子里的数字（如果 C1不等于C2）； 左上角那个格子里的数字+1（ 如果C1等于C2） 举个例子：
	// * * * G C T A
	// * * 0 0 0 0 0
	// * G 0 1 1 1 1
	// * B 0 1 1 1 1
	// * T 0 1 1 2 2
	// * A 0 1 1 2 3
	// *
	// * 填写最后一个数字时，它应该是下面三个的最大者：
	// * * （1）上边的数字2
	// * * （2）左边的数字2
	// * * （3）左上角的数字2+1=3,因为此时C1==C2
	// * 所以最终结果是3。
    // *	
	// * 在填写过程中我们还是记录下当前单元格的数字来自于哪个单元格，以方便最后我们回溯找出最长公共子串。
	// * 有时候左上、左、上三者中有多个同时达到最大，那么任取其中之一，但是在整个过程中你必须遵循固定的优先标准。
	// * 在我的代码中优先级别是左上>左>上。
	// *
	// *路径回溯没有实现。
	// */
	public static String lcSubsequenceDynamic(String str1, String str2) {
		String str = null;
		if ((str1 == null) || (str2 == null))
			return str;
		int lenght = str1.length();
		int[] flag = new int[lenght + 1];
		// int[] path = new int[lenght];
		int max = 0;
		for (int i = 0; i < str2.length(); i++) {
			char ch = str2.charAt(i);
			int tmpLeftUp = flag[0];
			for (int j = 1; j < lenght + 1; j++) {
				max = Math.max(flag[j], flag[j - 1]);
				if (ch == str1.charAt(j - 1)) {
					tmpLeftUp++;
				}
				max = Math.max(tmpLeftUp, max);
				tmpLeftUp = flag[j];
				flag[j] = max;
			}
		}
		for(int i=0;i<flag.length;i++)
			System.out.println(flag[i]);
		return str;
	}

	// /**
	// * 找最长公共子序列 分治法 直接举个例子吧："bacb"和"caba" 对于第一个字符 ’b' 有两种可能：
	// * 一：假设最长公共子序列包含这个字符
	// * * 这种情况首先必须保证字符串2也一样包含这个字符
	// * * 那么接下来找到 "acb"和“a”的最长公共子序列。
	// * * 这个解加上这个字符既是这种情况的解。
	// * 二：假设最长公共子序列不包含这个字符
	// * * 那么接下来找到 “acb”和“caba”的最长公共子序列。
	// *
	// * 比较上述两个解的长度。较长的即为最终解。
	// *
	// */
	public static String lcSubsequence(String str1, String str2) {
		if ((str1 == null) || (str2 == null))
			return null;
		if ((str1.isEmpty()) || (str2.isEmpty())) {
			return null;
		}
		char c = str1.charAt(0);
		String strTmp1 = null;
		String subStr2 = subString(str2, c);
		if (subStr2 != null) {
			if ((strTmp1 = lcSubsequence(str1.substring(1), subStr2
					.substring(1))) != null)
				strTmp1 = String.valueOf(c) + strTmp1;
			else {
				strTmp1 = String.valueOf(c);
			}
		}
		String strTmp2 = lcSubsequence(str1.substring(1), str2);
		if ((strTmp1 == null) || strTmp1.isEmpty())
			return strTmp2;
		if ((strTmp2 == null) || strTmp2.isEmpty())
			return strTmp1;
		if (strTmp1.length() > strTmp2.length())
			return strTmp1;
		else {
			return strTmp2;
		}
	}

	// 找到字符串中的第一个某个特定字符之后的字符串 string = “chenjinj” c = ‘n’ ，返回 str = “njinj”
	public static String subString(String string, char c) {
		String str = null;
		if (string == null)
			return str;
		char[] charArray = string.toCharArray();
		int i;
		for (i = 0; i < string.length(); i++) {
			if (charArray[i] == c)
				break;
		}
		if (i == string.length())
			return str;
		return string.substring(i);
	}
}
