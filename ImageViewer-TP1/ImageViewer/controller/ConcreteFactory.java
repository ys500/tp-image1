package controller;

import java.io.File;

import model.ConcreteImage;
import model.Image;

public class ConcreteFactory implements ImageFactory {

	private static ConcreteFactory instance = null;
	
	private ConcreteFactory(){}
	
	@Override
	public Image build(File file) {
		return new ConcreteImage(file);
	}
	
	public static synchronized ConcreteFactory getInstance() {
		if(instance == null)
			instance = new ConcreteFactory();
		return instance;
	}

}
