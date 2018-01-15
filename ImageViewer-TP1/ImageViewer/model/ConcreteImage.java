package model;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ConcreteImage extends Image{
	private final byte TAILLE_ENTETE = 54;
	private final int MASK = 0b11111111;

	BufferedInputStream fileReader;
	private BufferedImage image;
	
	private byte[] tabEtiquette = new byte[TAILLE_ENTETE];
	private int height;
	private int width;
	private int bpp;
	private int compression;
		
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
			/*for (int i = 0; i < tabEtiquette.length; i++) {
				System.out.println("byte #" + i + " : " + tabEtiquette[i]);
			}*/
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
			//seulement supporter les images BMP non-compresse de 24 bits par pixels
			if(compression == 0 && bpp == 24) {
				image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
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
