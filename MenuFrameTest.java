import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;

import view.menu.MenuFrame;

/**
 * @author weizhiwei <br/>
 * 2018-10-29 21:38
 */
public class MenuFrameTest {
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		MenuFrame menu = new MenuFrame();
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menu.setVisible(true);
	}
}
