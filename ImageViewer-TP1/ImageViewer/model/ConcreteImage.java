package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ConcreteImage extends Image{
	private final static byte TAILLE_ENTETE = 54;
	private final static int DEBUT_DONNEES_IMAGE = 55;

	BufferedInputStream fileReader;
	private BufferedImage image;
	
	private byte[] tabEtiquette = new byte[TAILLE_ENTETE];
	private int height;
	private int width;
	private int bpp;
	private int compression;
	private int size;
		
	public ConcreteImage(File file) {
			try {
				fileReader = new BufferedInputStream(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				fileReader.read(tabEtiquette, 0, TAILLE_ENTETE);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			initDonneesEntete();
			
			if(compression == 0 && bpp == 24) {
				image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				dessinerImage();
			}
			else {
				System.err.println("Type d'image non-supporte: bmp non-compresse de 24 bpp seulment");
			}
	}
	private void initDonneesEntete() {
		width = tabEtiquette[18] & 0b11111111 | 
				((tabEtiquette[19] & 0b11111111) << 8) | 
				((tabEtiquette[20] & 0b11111111) << 16) |
				((tabEtiquette[21] & 0b11111111) << 24);
        height = tabEtiquette[22] & 0b11111111 | 
        		((tabEtiquette[23] & 0b11111111) << 8) | 
        		((tabEtiquette[24] & 0b11111111) << 16) | 
        		((tabEtiquette[25] & 0b11111111) << 24);
        bpp = tabEtiquette[28] & 0b11111111 | 
				((tabEtiquette[29] & 0b11111111) << 8);
        compression = tabEtiquette[30] & 0b11111111 | 
        		((tabEtiquette[31] & 0b11111111) << 8) | 
        		((tabEtiquette[32] & 0b11111111) << 16) | 
        		((tabEtiquette[33] & 0b11111111) << 24);
        size = tabEtiquette[34] & 0b11111111 | 
        		((tabEtiquette[35] & 0b11111111) << 8) | 
        		((tabEtiquette[36] & 0b11111111) << 16) | 
        		((tabEtiquette[37] & 0b11111111) << 24);
	}
	private void dessinerImage() {
		tabEtiquette = new byte[DEBUT_DONNEES_IMAGE + size];
		try {
			fileReader.read(tabEtiquette, DEBUT_DONNEES_IMAGE, size);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int i = 0;
		for (int y = height - 1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				int r = convertir(tabEtiquette[i]);
				int v = convertir(tabEtiquette[i + 1]);
				int b = convertir(tabEtiquette[i + 2]);
				i += 3;
				Color rvb = new Color(r, b, v);
				image.setRGB(x, y, rvb.getRGB());
			}
		}
	}
	private int convertir(int couleur) {
		if (couleur == -1) {
			return 254;
		} else if (couleur < 0) {
			return couleur + 128;
		} else {
			return couleur;
		}
	}
	@Override
	public BufferedImage draw() {
		return image;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

}
