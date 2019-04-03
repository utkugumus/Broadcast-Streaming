

import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class PanelImagen extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Object BLOQUEO_FONDO = new Object();
	private BufferedImage fondo = null;
	
	public PanelImagen() throws HeadlessException {
		this.setDoubleBuffered(true);
	}
	
	public void setFondo(Image nuevoFondo) {
		synchronized (BLOQUEO_FONDO) {
			if (fondo == null) {
				fondo = new BufferedImage(nuevoFondo.getWidth(null), 
											nuevoFondo.getHeight(null),
											BufferedImage.TYPE_INT_RGB);
			}
			else if (fondo.getWidth() != nuevoFondo.getWidth(null)
					|| fondo.getHeight() != nuevoFondo.getHeight(null)) {
				fondo.flush(); //flush de los viejos recursos
				fondo = new BufferedImage(nuevoFondo.getWidth(null), 
											nuevoFondo.getHeight(null),
											BufferedImage.TYPE_INT_RGB);
			}
			Graphics graficos = fondo.createGraphics();
			graficos.drawImage(nuevoFondo, 0, 0, null);
		}
		repaint();
	}
	
	public void paint(Graphics graf) {
		super.paint(graf);
		synchronized (BLOQUEO_FONDO) {
			if (fondo != null) {
				graf.drawImage(fondo, 0, 0, getWidth(), getHeight(), null);
			}
		}
	}

}

