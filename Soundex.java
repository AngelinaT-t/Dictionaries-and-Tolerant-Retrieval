
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Soundex {
	
	class FreqAndId{
		private int freq;
		private List<Integer> postingList;
		
		public FreqAndId() {
			// TODO Auto-generated constructor stub
			freq = 0;
			postingList = new ArrayList<Integer>();
		}
		private void addFreq() {
			freq ++;
		}
		private void adddocId(int id) {
			postingList.add(id);
			postingList.sort(null);
		}
	}
	
	public Map<String,FreqAndId> dir; 
	public Map<String, ArrayList<String>> soundex;
	static Formatter formatter = new Formatter(System.out);
	
	public Soundex() {
		// TODO Auto-generated constructor stub
		dir = new TreeMap<String,FreqAndId>(new Comparator<String>(){
             public int compare(String o1, String o2) {
                 //如果有空值，直接返回0
            	 	o1 = o1.toLowerCase();//将term的值转成小写
            	 	o2 = o2.toLowerCase();
                 if (o1 == null || o2 == null)
                     return 0; 
                return String.valueOf(o1).compareTo(String.valueOf(o2));//然后进行比较
             }
		 });
		soundex = new TreeMap<String, ArrayList<String>>();
	}
	public String setSoundex(String term) {
		int i;
		//String t = term;
		term = term.toLowerCase();
		for(i=1;i<term.length();i++) {//除首字母外，将其余的每个字符转换为对应的数字
			char c = term.charAt(i);
			if(c == 'a' || c == 'e'|| c == 'i'|| c == 'o'|| c == 'u'|| c == 'h'|| c == 'w'|| c == 'y')
				term = term.substring(0, i)+"0"+term.substring(i+1);
			else if(c == 'b' || c == 'f'|| c == 'p'|| c == 'v')
				term = term.substring(0, i)+"1"+term.substring(i+1);
			else if(c == 'c' || c == 'g'|| c == 'j'|| c == 'k'|| c == 'q'|| c == 's'|| c == 'x'|| c == 'z')
				term = term.substring(0, i)+"2"+term.substring(i+1);
			else if(c == 'd' || c == 't')
				term = term.substring(0, i)+"3"+term.substring(i+1);
			else if(c == 'l' )
				term = term.substring(0, i)+"4"+term.substring(i+1);
			else if(c == 'm' || c == 'n')
				term = term.substring(0, i)+"5"+term.substring(i+1);
			else if(c == 'r' )
				term = term.substring(0, i)+"6"+term.substring(i+1);
		}
		for(i=1;i<term.length()-1;i++) {//删除连续的字符，只保留其中一个
			if(term.charAt(i) == term.charAt(i+1))
				term = term.substring(0, i)+term.substring(i+1);
		}
		for(i=1;i<term.length();i++) {//将0全部移到末尾
			if(term.charAt(i) == '0')
				term = term.substring(0, i)+term.substring(i+1)+"0";
		}
		return term.substring(0,4);//生成的字符串的前4位为对应编码
		
	}
	
	public void Insert(String term,int docID) {//term为单词(索引)，docID为所在文档
		if(!dir.containsKey(term)) {//如果字典里没有这个单词，则直接加入
			FreqAndId fId = new FreqAndId();//建立包含频率freq和postingList的对象
			fId.addFreq();//freq设置为1
			fId.adddocId(docID);//postingList加入docID
			dir.put(term, fId);//和term一起加入到字典中，按照升序插入
		}
		else {//如果字典里包含了这个term
			FreqAndId fId = dir.get(term);//获取这个term所对应的object信息
			if(!fId.postingList.contains(docID)) {//是否已经包含这个docID,不包含则进入
				fId.addFreq();//freq++
				fId.adddocId(docID);//postingList加入新的docID
				dir.put(term, fId);//新的信息插入时为替代原来的信息(map Key值唯一)
			}
		}
		/****************************/
		String code = setSoundex(term);//生成term对应的编码
		System.out.println(term + " code =  "+code);
		if(!soundex.containsKey(code)) {//如果编码表里没有这个编码，则直接加入
			ArrayList<String> list = new ArrayList<String>();
			list.add(term);//并且把当前的term加入到该编码对应的单词列表中
			soundex.put(code, list);//和编码一起构成一个索引表
		}
		else {//如果字典里包含了这个编码
			ArrayList<String> list = soundex.get(code);
			if(!list.contains(term)) {//查看这个编码是否已经包含该term
				list.add(term);//没有则更新
				soundex.put(code, list);
			}
		}
	}
	public void search(String s) {
		String code = setSoundex(s);//搜索时，先生成搜索编码
		if(soundex.containsKey(code)) {//查看编码库里是否有对应的编码信息
			System.out.println("The search result:");
			for(String ss : soundex.get(code)) {	//遍历得到该编码对应的所有单词term
				System.out.print("term:" + ss +" ");
				FreqAndId fId = dir.get(ss);
				formatter.format("freq:%-5s",fId.freq);
				 for (int tmp : fId.postingList) { //打印每个term所包含的文档信息
					    System.out.print("-->"); 
						formatter.format("%-1s",tmp);  
				 }
				 System.out.println();
			}
		}else {//没有对应编码则表示没有这个term的资料
			System.out.println("no search!");
		}
	}
	
	public void print() {
		System.out.println();
		formatter.format("%-10s %-10s %-10s\n", "term", "doc.freq", "posting lists");
		for (String s : dir.keySet()) {
			formatter.format("%-15s",s);
				FreqAndId fId = dir.get(s);
				formatter.format("%-10s",fId.freq);
				 for (int tmp : fId.postingList) { 
					    System.out.print("-->"); 
						formatter.format("%-1s",tmp);  
			        }
				  System.out.println(""); 
			}
	}
	
	public static void main(String[] args) {
		
		Soundex br = new Soundex();
		br.Insert("Herman", 2);	
		br.Insert("Herman", 3);	
		br.Insert("Herman", 4);	
		br.Insert("Herman", 5);		
		br.Insert("erman", 9);	
		br.Insert("Hrman", 18);	
		br.Insert("Hermn", 10);
		br.print();
		System.out.println();
		br.search("herman");
		//br.Intersection("brutus","Calpurnia");
			
		
	}
}
