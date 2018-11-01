package solution;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Stack;

import javax.management.RuntimeErrorException;
import javax.swing.JOptionPane;

import view.menu.MenuFrame;

/**
 * @author weizhiwei <br/>
 * 2018-10-31 22:39
 */
public class Solver {
	private int[] init, target;
	private int step, length;
	private Stack<int[]> arr;
	private MenuFrame menu;
	private PriorityQueue<Node> open;//用优先队列，获取速度快
	private HashSet<Node> close;//读多写少，用hashset，查找省时间
	class Node implements Comparable<Node>{
		Node pre;
		int[] stat;
		int g,h;//g是离初始状态的距离，h是离目标状态的距离
		int blank_idx;
		public Node(int[] tmp, int depth, int index, Node previous) {
			this.stat = tmp.clone();
			this.g = depth;
			this.h = cntH();
			this.blank_idx = index;
			this.pre = previous;
		}
		/**
		 * 采取的启发函数h(n)=P(n)，即每个数码与其目标位置之间距离的总和
		 * @return 
		 */
		private int cntH() {
			int c = 0;
			for(int i = 1; i <= 9; i++) {
				if(stat[i]!=target[i]) {
					for(int j = 1; j <= 9; j++) {
						if(stat[i]==target[j]){
							//定义距离为横向距离差加纵向距离差
							int tx = (i-1)/3+1;
							int ty = (i-1)%3+1;
							int ttx = (j-1)/3 + 1;
							int tty = (j-1)%3 + 1;
							c += Math.abs(tx-ttx)+Math.abs(ty-tty);
							break;
						}
					}
				}
			}
			return c;
		}
		@Override
		public boolean equals(Object obj) {//重写equals方法辅助Hashset
			// TODO Auto-generated method stub
			int[] tmp = ((Node)obj).stat;
			for(int i = 1; i <= 9; i++) {
				if(stat[i]!=tmp[i])
					return false;
			}
			return true;
		}
		@Override
		public int compareTo(Node o) {
			// TODO Auto-generated method stub
			return h+g-o.h-o.g;
		}
	}
	public void setMenu(MenuFrame menu) {
		this.menu = menu;
	}
	/**
	 * 将运行过程中的每一个最短路节点都保存，并按照顺序排好方便检索
	 * length为路径总长度
	 * @param in
	 * @param tar
	 */
	public Solver() {
		this.arr = new Stack<>();
		this.step = 0;
		this.length = 0;
		open = new PriorityQueue<>();
		close = new HashSet<>();
	}
	public void run(int[] in, int[] tar) {
		init = in.clone();
		target = tar.clone();
		System.out.println("开始运行程序！");
		//这里是代码正体
		for(int i = 1; i <= 9; i++) {
			if(init[i]==0) {//必然有解，不然bug
				open.add(new Node(init, 0, i, null));//根节点
				break;
			}
		}
		if(open.isEmpty()) {
			throw new RuntimeErrorException(null, "没有空格，有问题！");
		}
		//拓展所有节点，检查hashset中是否包含
		Node tarNode = null;
		int cnting = 0;
		while(cnting++<100000) {
			Node tmp = open.poll();
			if(tmp.equals(new Node(target, -1, -1, null))) {
				tarNode = tmp;
				break;				
			}
			int i = tmp.blank_idx;
			if(i-3>0) {
				exchange(tmp.stat, i, i-3);
				if(!close.contains(tmp) && !open.contains(tmp)) {
					open.add(new Node(tmp.stat, tmp.g+1, i-3, tmp));
				}
				exchange(tmp.stat, i, i-3);
			}
			if(i+3<=9) {
				exchange(tmp.stat, i, i+3);
				if(!close.contains(tmp)&& !open.contains(tmp)) {
					open.add(new Node(tmp.stat, tmp.g+1, i+3, tmp));
				}
				exchange(tmp.stat, i, i+3);
			}
			if((i+1) % 3 != 0 && (i+1)<10) {
				exchange(tmp.stat, i, i+1);
				if(!close.contains(tmp)&& !open.contains(tmp)) {
					open.add(new Node(tmp.stat, tmp.g+1, i+1, tmp));
				}
				exchange(tmp.stat, i, i+1);
			}
			if((i-1) %3 != 0 && (i-1)>=0) {
				exchange(tmp.stat, i, i-1);
				if(!close.contains(tmp)&& !open.contains(tmp)) {
					open.add(new Node(tmp.stat, tmp.g+1, i-1, tmp));
				}
				exchange(tmp.stat, i, i-1);
			}
			close.add(tmp);
		}
		if(cnting>=100000) {
			JOptionPane.showMessageDialog(null, "没有解！");
			throw new RuntimeErrorException(null, "没有解！");
		}
		while(tarNode!=null) {
			arr.push(tarNode.stat);
			tarNode = tarNode.pre;
		}
		length = arr.size();
		menu.preStep(getStep(0));//初始化
		menu.nextStep(getStep(1));
		System.out.printf("待扩展节点：%5d\n已扩展节点：%5d\n",open.size(), close.size());
	}
	private void exchange(int[] stat, int i, int j) {
		// TODO Auto-generated method stub
		int tmp = stat[i];
		stat[i] = stat[j];
		stat[j] = tmp;
	}
	public void pre_step() {
		if(step==0) {
			System.out.println("第一步，不能再向前了！");
		}else {
			step--;
			System.out.println("前一步是……");
			menu.preStep(getStep(step));
			menu.nextStep(getStep(step+1));
		}
	}
	/**
	 * 获取某一步的状态
	 * @param st
	 * @return
	 */
	private int[] getStep(int st) {
		// TODO Auto-generated method stub
		return arr.get(length-1-st);
	}
	public void next_step() {
		if(step>=length-2) {
			System.out.println("最后一步，不能再向后了！");
		}else {
			step++;
			System.out.println("后一步是……");
			menu.nextStep(getStep(step+1));
			menu.preStep(getStep(step));
		}
	}
}
