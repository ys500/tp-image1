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
	private final int MASK = 0b11111111;

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
			
			//seulement supporter les images BMP non-compresse de 24 bits par pixels
			if(compression == 0 && bpp == 24) {
				image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				dessinerImage();
			}
	}
	
	private void initDonneesEntete() {
		//width: tabEtiquette[18] a tabEtiquette[21](4 octets)
		width = tabEtiquette[18] & MASK | 
				((tabEtiquette[19] & MASK) << 8) | 
				((tabEtiquette[20] & MASK) << 16) |
				((tabEtiquette[21] & MASK) << 24);
		//height: tabEtiquette[22] a tabEtiquette[25](4 octets)
        height = tabEtiquette[22] & MASK | 
        		((tabEtiquette[23] & MASK) << 8) | 
        		((tabEtiquette[24] & MASK) << 16) | 
        		((tabEtiquette[25] & MASK) << 24);
        //bpp/bitCount:  tabEtiquette[28] et tabEtiquette[29](2 octets)
        bpp = tabEtiquette[28] & MASK | 
				((tabEtiquette[29] & MASK) << 8);
        //compression:tabEtiquette[30] a tabEtiquette[33](4 octets)
        compression = tabEtiquette[30] & MASK | 
        		((tabEtiquette[31] & MASK) << 8) | 
        		((tabEtiquette[32] & MASK) << 16) | 
        		((tabEtiquette[33] & MASK) << 24);
      //size:tabEtiquette[34] a tabEtiquette[37](4 octets)
        size = tabEtiquette[34] & MASK | 
        		((tabEtiquette[35] & MASK) << 8) | 
        		((tabEtiquette[36] & MASK) << 16) | 
        		((tabEtiquette[37] & MASK) << 24);
	}
	private void dessinerImage() {
		tabEtiquette = new byte[DEBUT_DONNEES_IMAGE + size];
		try {
			fileReader.read(tabEtiquette, 0, DEBUT_DONNEES_IMAGE + size);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int position = 0;
		
		for (int y = height - 1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				int r = convert(tabEtiquette[position]);
				int v = convert(tabEtiquette[position + 1]);
				int b = convert(tabEtiquette[position + 2]);
				position += 3;
				Color rvb = new Color(r, v, b);
				image.setRGB(x, y, rvb.getRGB());
			}
		}
	}
	private int convert(int couleur) {
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
