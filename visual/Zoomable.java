package visual;
import java.awt.PopupMenu;

public interface Zoomable
{
    public PopupMenu createPopup();

    public void zoom(int delta);
	
}
