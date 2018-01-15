package model;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ConcreteImage extends Image{
	private final byte TAILLE_ENTETE = 54;

	BufferedInputStream fileReader;
	private BufferedImage image;
	
	private byte[] tabEnteteBMP = new byte[TAILLE_ENTETE];
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
				fileReader.read(tabEnteteBMP, 0, TAILLE_ENTETE);
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < tabEnteteBMP.length; i++) {
				System.out.println("byte #" + i + " : " + tabEnteteBMP[i]);
			}
		
			//width: tabEnteteBMP[18] a tabEnteteBMP[21](4 octets)
			width = tabEnteteBMP[18];
			//height: tabEnteteBMP[22] a tabEnteteBMP[25](4 octets)
			height = tabEnteteBMP[22];
			// bpp/bitCount:  tabEnteteBMP[28] et tabEnteteBMP[29](2 octets)
			bpp = tabEnteteBMP[28];
			//compression:tabEnteteBMP[30] a tabEnteteBMP[33](4 octets)
			compression = tabEnteteBMP[30];
			//seulement supporter les images BMP non-compresse de 24 bits par pixels
			if(compression == 0 && bpp == 24) {
				//image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
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
