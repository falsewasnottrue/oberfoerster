package visual;

import java.awt.*;
import java.awt.event.*;


public class ZoomPane extends ScrollPane implements ActionListener
{
    private Zoomable child;
    private PopupMenu popup;

    public Component addZoom(Component aChild)
    {
	child=(Zoomable)aChild;
	popup=((Zoomable)aChild).createPopup();
       	MenuItem menuItem = new MenuItem("Zoom in",new MenuShortcut(KeyEvent.VK_A));
      	menuItem.addActionListener(this);
	popup.add(menuItem);
	menuItem = new MenuItem("Zoom out",new MenuShortcut(KeyEvent.VK_MINUS));
       	menuItem.addActionListener(this);
	popup.add(menuItem);
	add(popup);
	addMouseListener (new MyMouseListener());
	return super.add(aChild);
    }

    public void actionPerformed(ActionEvent e) {
	System.out.println(e.getActionCommand());
	if (e.getActionCommand().equals("Zoom in"))
	    child.zoom(+2);
	if (e.getActionCommand().equals("Zoom out"))
	    child.zoom(-2);
	validate();
	repaint();
    }

    class MyMouseListener  extends MouseAdapter implements MouseListener {
	public void mousePressed(MouseEvent e) {
	    maybeShowPopup(e);
	}

	public void mouseReleased(MouseEvent e) {
	    maybeShowPopup(e);
	}

	private void maybeShowPopup(MouseEvent e) {
	    System.out.println("hier will ich hin !");
	    if (e.isPopupTrigger()) {
		popup.show(e.getComponent(),
			   e.getX(), e.getY());
	    }
	}
    }	

}
