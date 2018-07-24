
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Map;
import java.util.TreeMap;


public class PosIntersect {
	
	class DocAndIndex{
		private Map<Integer, ArrayList<Integer>> index;
		public DocAndIndex() {
			index = new TreeMap<Integer, ArrayList<Integer>>();
		}
		private void insert(int doc,int i) {
			ArrayList<Integer> list = new ArrayList<Integer>();
			list.add(i);
			list.sort(null);
			index.put(doc,list);
		}
	}
	
	public Map<String,DocAndIndex> dir; 
	static Formatter formatter = new Formatter(System.out);
	
	public PosIntersect() {
		// TODO Auto-generated constructor stub
		dir = new TreeMap<String,DocAndIndex>(new Comparator<String>(){
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
	
	public void Insert(String term,int doc,int index) {//term为单词(索引)，docID为所在文档
		if(!dir.containsKey(term)) {//如果字典里没有这个单词，则直接加入
			DocAndIndex dIndex = new DocAndIndex();//建立包含文档号doc和对应位置index的对象
			dIndex.insert(doc, index);//对象的insert操作会建立一个新的关于文档号和位置index的Map数据结构
			dir.put(term, dIndex);//和term一起加入到字典中，按照升序插入
		}
		else {//如果字典里包含了这个term
			DocAndIndex dIndex = dir.get(term);//获取这个term所对应的object信息
			if(!dIndex.index.containsKey(doc)) {//是否已经包含这个docID,不包含则进入
				dIndex.insert(doc,index);//新建包含新的文档号的Key-value关系
				dir.put(term, dIndex);//将位置index和对应文档匹配存储
			}else {//如果这个文档号已经存在，则只需要新增index
				ArrayList<Integer> list = dIndex.index.get(doc);
				list.add(index);//更新该文档号对应的index信息
				dIndex.index.put(doc, list);//更新term信息
			}
		}
	}
	class threeEle{
		int docId,p1,p2;
		public threeEle() {
			docId = 0;
			p1 = 0;
			p2 = 0;
		}
		
		public  threeEle(int docid,int p1,int p2) {
			docId = docid;
			this.p1 = p1;
			this.p2 = p2;
		}
		
	}
	/*
	 Iterator it = emails.keySet().iterator(); 
	  while (it.hasNext()){ 
	    String key; 
	    key=(String)it.next(); 
	    logger.info("email-" + key + ":" + emails.get(key)); 
	   } 
	*/
	public void Intersection(String t1,String t2) {
		Map<Integer, ArrayList<Integer>> di1 = dir.get(t1).index;//根据搜索的term，得到两个term对应的文档和位置信息
		Map<Integer, ArrayList<Integer>> di2 = dir.get(t2).index;
		ArrayList<threeEle> answer = new ArrayList<threeEle>();//保存搜索结果
		for(Integer d1 : di1.keySet()) {//遍历第一个term所在的文档号
			if(di2.containsKey(d1)) {//如果第二个term也存在于该文档号，则进行查找
				ArrayList<Integer> l = new ArrayList<Integer>();//保存临时查找结果
				ArrayList<Integer> pp1 = di1.get(d1);//获取term1，term2在该文档所对应位置的indexlist
				ArrayList<Integer> pp2 = di2.get(d1);
				int l1 = pp1.size(),i=0;
				int l2 = pp2.size(),j=0;
				while( i<l1 ) {	//遍历第一个term的位置
					j = 0;
					while( j<l2 ) {//遍历第二个term的位置，找到相对距离为1的位置
						int gap = pp1.get(i) - pp2.get(j);
						//int gap = Math.abs(pp1.get(i) - pp2.get(j));
						//if( gap <= 1 && gap >0) {
						if(gap >= -1 && gap <0) {
						//if( (gap > 0 && gap <1 ) || (gap == 1)) {//符合距离条件则加入临时保存的l中
							l.add(pp2.get(j));
						}else if(pp2.get(j) > pp1.get(i)) {//超过距离条件不再查找
							break;
						}
						j++;
					}	
					int k=0;//清空之前的搜索结果
					//while( k<l.size() && (pp1.get(i)-l.get(k)) >1) {
					while( k<l.size() && Math.abs(l.get(k)-pp1.get(i)) >1) {
						k++;
					}
					for(;k<l.size();k++) {//加入到最终的结果中
						answer.add(new threeEle(d1,pp1.get(i),l.get(k)));
					}
					i++;
				}
			}
		}
		for(threeEle teEle : answer) {
			System.out.println("DocId:" +teEle.docId +" pp1: "+teEle.p1+" pp2:"+teEle.p2);
		}
	}
	
	public void print() {
		//formatter.format("%-10s %-10s %-10s\n", "term", "docId", "Index lists");
		for (String s : dir.keySet()) {
			formatter.format("%-15s",s);
				DocAndIndex fId = dir.get(s);
				Map<Integer, ArrayList<Integer>> map = fId.index;
				for (Integer t : map.keySet()) {
					System.out.print(t+":(");
					//formatter.format("%-10s",t+":(");
						ArrayList<Integer> list = map.get(t);
						for (int tmp : list) {
					    System.out.print(tmp+","); 
						//formatter.format("%-1s",tmp+",");  
			        }
					System.out.print(");  ");
				}
				  System.out.println(""); 
			}
	}
	
	public static void main(String[] args) {
		PosIntersect pi = new PosIntersect();
		pi.Insert("angles", 2, 36);
		pi.Insert("angles", 2, 174);
		pi.Insert("angles", 2, 252);
		pi.Insert("angles", 2, 651);
		pi.Insert("angles", 4, 12);
		pi.Insert("angles", 4, 20);
		pi.Insert("angles", 2, 431);
		pi.Insert("angles", 2, 435);
		pi.Insert("angles", 7, 17);
		pi.Insert("fools", 2, 1);
		pi.Insert("fools", 2, 17);
		pi.Insert("fools", 2, 74);
		pi.Insert("fools", 2, 222);
		pi.Insert("fools", 2, 430);
		pi.Insert("fools", 2, 434);
		pi.Insert("fools", 4, 21);
		pi.Insert("fools", 4, 34);
		pi.Insert("fools", 4, 13);
		pi.Insert("fools", 7, 18);
		pi.Insert("fools", 7, 19);
		pi.Insert("fools", 7, 20);
		pi.print();
		pi.Intersection("angles", "fools");
	}
}
