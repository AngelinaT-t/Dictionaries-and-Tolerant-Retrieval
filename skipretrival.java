

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class skipretrival {
	
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
	static Formatter formatter = new Formatter(System.out);
	
	public skipretrival() {
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
	}
	
	public void Intersection(String t1,String t2) {
		int i = 0,j = 0;
		FreqAndId fid1 = dir.get(t1);//根据term值获取map中对应的value值，返回为一个Object
		FreqAndId fid2 = dir.get(t2);
		
		List<Integer> l1 = fid1.postingList;//得到term所对应的postinglist
		List<Integer> l2 = fid2.postingList;
		List<Integer> list = new ArrayList<Integer>();//保存合并的结果List
		
		int len1 = l1.size(); //object的freq属性代表着postingList的长度
		int len2 = l2.size();
		int skip1 = (int)Math.sqrt(len1);//跳表的距离由postinglist的长度决定
		int skip2 = (int)Math.sqrt(len2);
		//System.out.println(skip1 + "   " + skip2);
		while( i<len1 && j<len2) {//直到遍历完其中一个postingList
			int id1 = l1.get(i);
			int id2 = l2.get(j);
			int nexti,nextj;//用于记录跳表下一个的位置
			nextj = j+skip2;
			if( id1 == id2 ) {//当前跳表指针对应的位置相同则加入合并结果
				list.add(id2);
				i++;			//L1，L2均取下一个元素
				j++;
			}
			else if( id1 < id2 ){//l1对应的值较小时，l1进行跳表操作
				nexti = i+skip1;
				//找到下一个和l2匹配的文档，或者找到比l2当前文档较大的值时，停止继续跳表
				if(nexti < len1 && (l1.get(nexti) < l2.get(j) || l1.get(nexti) == l2.get(j) )) {
					while(nexti < len1 && (l1.get(nexti) < l2.get(j) ||l1.get(nexti) == l2.get(j) )) {
						i = nexti;
						nexti +=skip1;
					}
				}else {	//如果下一个跳表位置的值已经大于l2当前的文档号，顺序取下一个值，不作跳表操作
					i ++;
				}
				//如果时l2对应的值较小时，则对l2进行同样的跳表查找
			}else if((j +skip2) < len2 && (l2.get((j +skip2)) < l1.get(i) || l2.get((j +skip2)) == l1.get(i) )){
				nextj =j+skip2;
				while(nextj < len2 && (l2.get(nextj) < l1.get(i) || l2.get(nextj) == l1.get(i) )) {
					j = nextj;
					nextj =j+skip2;
				}
			}else {
				j ++;
			}
		}
		
		System.out.print("Intersection: ");
		for (int tmp : list) {	//输出结果
		    System.out.print("-->"+tmp);   
        }
	}
	
	public void print() {
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
		
		skipretrival br = new skipretrival();
		
		br.Insert("Brutus",1);
		br.Insert("Brutus",2);
		br.Insert("Brutus",4);
		br.Insert("Brutus",11);
		br.Insert("Brutus",31);
		br.Insert("Brutus",54);
		br.Insert("Brutus",173);
		br.Insert("Brutus",174);

		br.Insert("Calpurnia",2);
		br.Insert("Calpurnia",31);
		br.Insert("Calpurnia",54);
		br.Insert("Calpurnia",101);
		
		br.Insert("Brutus",2);
		br.Insert("Brutus",4);
		br.Insert("Brutus",8);
		br.Insert("Brutus",16);
		br.Insert("Brutus",19);
		br.Insert("Brutus",23);
		br.Insert("Brutus",28);
		br.Insert("Brutus",101);

		br.Insert("Calpurnia",1);
		br.Insert("Calpurnia",2);
		br.Insert("Calpurnia",3);
		br.Insert("Calpurnia",5);
		br.Insert("Calpurnia",8);
		br.Insert("Calpurnia",23);
		br.Insert("Calpurnia",60);
		br.Insert("Calpurnia",71);
		br.Insert("Calpurnia",41);
		br.print();
		br.Intersection("brutus","Calpurnia");
		

		
		
		
	}
}
