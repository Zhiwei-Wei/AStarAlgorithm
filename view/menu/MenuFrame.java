package view.menu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.management.RuntimeErrorException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import solution.Solver;
import style.font.FontClass;

/**
 * @author weizhiwei <br/>
 * 2018-10-29 17:01
 */
public class MenuFrame extends JFrame{
	private int[] vis = new int[10], init = new int[10], target = new int[10];
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_WIDTH = 800;
	private static final int DEFAULT_HEIGHT = 600;
	private JButton start;
	private int idx_jp;
	public Solver sv = new Solver();
	private int[] jpx, jpy;
	private JPanel[] arr_jp,arr_jp2;
	private JPanel jp_init, jp_target, jp_cur, jp_nxt;
	private MyListener m1 = new MyListener();
	private JButton jb_init, jb_target, jb1,jb2;
	public MenuFrame() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		FontClass.loadIndyFont();
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);//长宽
		appearCenter();//居中显示
		setTitle("实验三：A*八数码问题-1522010231魏智伟");
		setResizable(false);
		setLayout(null);
		arr_jp = new JPanel[9];
		for(int i = 1; i <= 8; i++) {
			arr_jp[i] = new JPanel();
			arr_jp[i].setBounds(85*i, 30, 45, 45);
			arr_jp[i].setVisible(true);
			arr_jp[i].setBackground(Color.white);
			arr_jp[i].addMouseListener(m1);
			arr_jp[i].addMouseMotionListener(m1);
			arr_jp[i].setBorder(BorderFactory.createRaisedBevelBorder());
			JLabel tmp = new JLabel(String.valueOf(i));
			tmp.setFont(new Font("微软雅黑", Font.BOLD, 20));
			arr_jp[i].add(tmp);
			this.add(arr_jp[i]);
		}
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		jp_init = new JPanel();
		jp_init.setBounds(85,100,150,150);
		jp_init.setLayout(new GridLayout(3, 3));
		arr_jp2 = new JPanel[10];
		jpx = new int[10];
		jpy = new int[10];
		for(int i = 1; i <= 9; i++) {
			JPanel tmp = new JPanel();
			arr_jp2[i] = tmp;
			tmp.setPreferredSize(new Dimension(50,50));
			tmp.setBorder(BorderFactory.createLoweredSoftBevelBorder());
			jp_init.add(arr_jp2[i]);
		}
		jp_target = new JPanel();
		jp_target.setBounds(500,100,150,150);
		jp_target.setLayout(new GridLayout(3, 3));
		for(int i = 1; i <= 9; i++) {
			JPanel tmp = new JPanel();
			tmp.setPreferredSize(new Dimension(50,50));
			tmp.setBorder(BorderFactory.createLoweredSoftBevelBorder());
			jp_target.add(tmp);
		}
		this.add(jp_target);
		int a = 85, b = 85+50, c = 185, d = 100, e = 150, f = 200;
		jpx = new int[] {0,a,b,c,a,b,c,a,b,c};
		jpy = new int[] {0,d,d,d,e,e,e,f,f,f};
		jb_target = new JButton("目标状态");
		jb_target.setBounds(500,250,150,50);
		jb_target.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				boolean ck = false;
				System.out.print("目标状态：");
				for(int i = 1; i < 10; i++) {
					System.out.print(vis[i]+" ");
					if(vis[i]==0)
						ck = true;
				}
				if(!ck) {
					//因为比较懒，在设置初始状态的时候是假设一次性搞定不会重复设置的
					//所以对于不合法的操作不去进行。
					//这里只进行粗浅验证
					throw new RuntimeErrorException(null,"目标状态设立错误，请不要重复设立！");
				}else {
					int t = JOptionPane.showConfirmDialog(null, "确定状态设立无误？");
					if(t==0) {
						jb_target.setEnabled(false);
						target = vis.clone();
						draw2();
						reset();
						start.setVisible(true);
					}
				}
				System.out.println();
			}
		});
		jp_cur = new JPanel(new GridLayout(3, 3));
		jp_nxt = new JPanel(new GridLayout(3, 3));
		jp_cur.setBounds(85,330,150,150);
		jp_nxt.setBounds(500,330,150,150);
		this.add(jp_cur);
		this.add(jp_nxt);
		jb1 = new JButton("<<<上一步");
		jb2 = new JButton("下一步>>>");
		jb1.setBounds(85,480,150,50);
		jb2.setBounds(500,480,150,50);
		jb1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				sv.pre_step();
			}
		});
		jb2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				sv.next_step();
			}
		});
		this.add(jb2);
		this.add(jb1);
		jb1.setVisible(false);
		jb2.setVisible(false);
		start = new JButton("开始推导");
		start.setBounds(290, 240, 150, 50);
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				start.setEnabled(false);
				sv.run(init, target);
				jb1.setVisible(true);
				jb2.setVisible(true);
			}
		});
		this.add(start);
		start.setVisible(false);
		this.add(jb_target);
		jb_init = new JButton("初始状态");
		jb_init.setBounds(85,250,150,50);
		jb_init.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				boolean ck = false;
				System.out.print("初始状态：");
				for(int i = 1; i < 10; i++) {
					System.out.print(vis[i]+" ");
					if(vis[i]==0)
						ck = true;
				}
				if(!ck) {
					//因为比较懒，在设置初始状态的时候是假设一次性搞定不会重复设置的
					//所以对于不合法的操作不去进行。
					//这里只进行粗浅验证
					throw new RuntimeErrorException(null,"初始状态设立错误，请不要重复设立！");
				}else {
					int t = JOptionPane.showConfirmDialog(null, "确定状态设立无误？");
					if(t==0) {
						jb_init.setEnabled(false);
						init = vis.clone();
						draw();
						reset();
					}
				}
				System.out.println();
			}
		});
		jp_init.setVisible(true);
		jp_init.setBorder(BorderFactory.createEtchedBorder());
		jp_target.setBorder(BorderFactory.createEtchedBorder());
		this.add(jp_init);
		this.add(jb_init);
		sv.setMenu(this);
	}
	public void preStep(int[] pre) {
		jp_cur.removeAll();
		for(int i = 1; i <= 9 ;i++) {
			JPanel tmp = new JPanel();
			tmp.setPreferredSize(new Dimension(50,50));
			tmp.setBorder(BorderFactory.createLoweredSoftBevelBorder());
			if(pre[i]!=0) {
				JLabel ll = new JLabel(String.valueOf(pre[i]));
				ll.setFont(new Font("微软雅黑", Font.BOLD, 20));
				tmp.add(ll);
				tmp.setBackground(Color.WHITE);
			}
			jp_cur.add(tmp);
		}
		jp_cur.revalidate();
	}
	public void nextStep(int[] next) {
		jp_nxt.removeAll();
		for(int i = 1; i <= 9 ;i++) {
			JPanel tmp = new JPanel();
			tmp.setPreferredSize(new Dimension(50,50));
			tmp.setBorder(BorderFactory.createLoweredSoftBevelBorder());
			if(next[i]!=0) {
				JLabel ll = new JLabel(String.valueOf(next[i]));
				ll.setFont(new Font("微软雅黑", Font.BOLD, 20));
				tmp.add(ll);
				tmp.setBackground(Color.WHITE);
			}
			jp_nxt.add(tmp);
		}
		jp_nxt.revalidate();
	}
	protected void draw2() {
		// TODO Auto-generated method stub
		jp_target.removeAll();
		for(int i = 1; i <= 9; i++) {
			JPanel tmp = new JPanel();
			tmp.setPreferredSize(new Dimension(50,50));
			tmp.setBorder(BorderFactory.createLoweredSoftBevelBorder());
			if(vis[i]!=0) {
				JLabel ll = new JLabel(String.valueOf(vis[i]));
				ll.setFont(new Font("微软雅黑", Font.BOLD, 20));
				ll.setForeground(Color.WHITE);
				tmp.add(ll);
				tmp.setBackground(Color.GRAY);
			}
			jp_target.add(tmp);
		}
		jp_target.revalidate();
		jpx = new int[10];
		jpy = new int[10];//不让标签落地了
	}
	protected void draw() {
		// TODO Auto-generated method stub
		jp_init.removeAll();
		for(int i = 1; i <= 9; i++) {
			JPanel tmp = new JPanel();
			tmp.setPreferredSize(new Dimension(50,50));
			tmp.setBorder(BorderFactory.createLoweredSoftBevelBorder());
			if(vis[i]!=0) {
				JLabel ll = new JLabel(String.valueOf(vis[i]));
				ll.setFont(new Font("微软雅黑", Font.BOLD, 20));
				ll.setForeground(Color.WHITE);
				tmp.add(ll);
				tmp.setBackground(Color.GRAY);
			}
			jp_init.add(tmp);
		}
		jp_init.revalidate();
		int a = 415+85, b = 415+85+50, c = 415+185, d = 100, e = 150, f = 200;
		jpx = new int[] {0,a,b,c,a,b,c,a,b,c};
		jpy = new int[] {0,d,d,d,e,e,e,f,f,f};
	}
	protected void reset() {
		// TODO Auto-generated method stub
		for(int i = 1; i <= 8; i++) {
			arr_jp[i].setBounds(85*i, 30, 45, 45);
		}
		vis = new int[10];
	}
	private void appearCenter() {
		// TODO Auto-generated method stub
		int windowWidth = this.getWidth();                    //获得窗口宽
        int windowHeight = this.getHeight();                  //获得窗口高
        Toolkit kit = Toolkit.getDefaultToolkit();             //定义工具包
        Dimension screenSize = kit.getScreenSize();            //获取屏幕的尺寸
        int screenWidth = screenSize.width;                    //获取屏幕的宽
        int screenHeight = screenSize.height;                  //获取屏幕的高
        this.setLocation(screenWidth/2-windowWidth/2, 
        		screenHeight/2-windowHeight/2);//设置窗口居中显示
	}
	class MyListener extends MouseAdapter {
		//这两组x和y为鼠标点下时在屏幕的位置和拖动时所在的位置
				int newX, newY, oldX, oldY;
		//这两个坐标为组件当前的坐标
				int startX, startY;
				@Override
				public void mousePressed(MouseEvent e) {
		//此为得到事件源组件
					Component cp = (Component) e.getSource();
		//当鼠标点下的时候记录组件当前的坐标与鼠标当前在屏幕的位置
					startX = cp.getX();
					startY = cp.getY();
					oldX = e.getXOnScreen();
					oldY = e.getYOnScreen();
				}
				@Override
				public void mouseDragged(MouseEvent e) {
					Component cp = (Component) e.getSource();
		//拖动的时候记录新坐标
					newX = e.getXOnScreen();
					newY = e.getYOnScreen();
		//设置bounds,将点下时记录的组件开始坐标与鼠标拖动的距离相加
					cp.setBounds(startX + (newX - oldX), startY + (newY - oldY), cp.getWidth(), cp.getHeight());
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					Component cp = (Component)e.getSource();
					if(!check(cp.getX(), cp.getY())) {
						cp.setBounds(startX, startY, cp.getWidth(), cp.getHeight());
					}else {
						cp.setBounds(jpx[idx_jp]+4, jpy[idx_jp]+4, cp.getWidth(), cp.getHeight());
						int tidx = 0;
						for(int i = 1; i <= 8; i++) {
							if(arr_jp[i]==e.getSource()) {
								tidx = i;
								break;
							}
						}
						vis[idx_jp] = tidx;
					}
				}
			}
	public boolean check(int x, int y) {
		// TODO Auto-generated method stub
		x+=22;
		y+=22;
		for(int i = 1; i <= 9; i++)
			if(x>=jpx[i] && x < jpx[i]+50 && y >= jpy[i] && y<jpy[i]+50) {
				idx_jp = i;
				return true;
			}
		return false;
	}
}
