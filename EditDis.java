
import java.util.Scanner;

public class EditDis {
	private static Scanner input;
	
	public int min(int m1,char c1,char c2,int m2,int m3) {
		int min = -1;
		if(c1 != c2)//如果c1 c2不相等，编辑距离加一
			m1 +=1;
		min = m1 < m2 ? m1:m2;//比较三种方式编辑距离最小的值
		min = min <m3 ? min : m3;
		return min;
	}
	public int Distance(String s1,String s2) {
		int i,j;
		int l1 = s1.length()+1,l2=s2.length()+1;
		int[][] m = new int[l1][l2];
		for(i = 0;i<l1; i++) {//二维矩阵初始化
			for(j = 0; j<l2; j++)
				m[i][j] = 0;
		}
		//如果编辑距离的其中一个字符串为单个字符，那么最直接的方式替换和插入
		//所以两个字符串首字母的编辑距离为需要匹配的字符串的长度
		for(i = 1;i<l1; i++) {
				m[i][0] = i;
		}
		for(j = 1; j<l2; j++) {
			m[0][j] = j;
		}
		//动态规划求解，将插入删除替换操作的最小编辑距离填入对应位置
		for(i = 1; i<l1; i++) {
			for(j = 1; j<l2; j++)
				m[i][j] = min(m[i-1][j-1],s1.charAt(i-1),s2.charAt(j-1),m[i-1][j]+1,m[i][j-1]+1);
		}
		/*for(i = 0;i<l1; i++) {
			for(j = 0; j<l2; j++)
				System.out.print(m[i][j]+" ");
			System.out.println();
		}*/
		return m[l1-1][l2-1];//最右下角的值就对应最终的编辑距离
	}

	public static void main(String[] args) {
		input = new Scanner(System.in); 
		System.out.println("输入两个需要计算编辑距离的字符串：");
        String str1 = input.nextLine();  
        String str2 = input.nextLine(); 
        EditDis ed = new EditDis();
        //ed.Distance("ALGORITHM","ALTRUISTIC")
        System.out.println("edit distance = "+ed.Distance(str1,str2));
        //System.out.println("edit distance ="+ed.Distance("fast","cats"));
	}
}
