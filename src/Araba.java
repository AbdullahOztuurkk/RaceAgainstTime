import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;


public class Araba {
	private BufferedImage arabaResmi;
	private int x;
	private int y;
	private int arabaGenislik;
	private int arabaYukseklik;
	public Araba(int x,int y) throws FileNotFoundException, IOException
	{
		arabaResmi=ImageIO.read(new FileInputStream(new File("./images/yellow_car.png")));
		this.x=x;
		this.y=y;
		arabaGenislik=arabaResmi.getWidth()/4;
		arabaYukseklik=arabaResmi.getHeight()/4;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public BufferedImage getArabaResmi() {
		return arabaResmi;
	}

	public int getArabaGenislik() {
		return arabaGenislik;
	}

	public void setArabaGenislik(int arabaGenislik) {
		this.arabaGenislik = arabaGenislik;
	}

	public int getArabaYukseklik() {
		return arabaYukseklik;
	}

	public void setArabaYukseklik(int arabaYukseklik) {
		this.arabaYukseklik = arabaYukseklik;
	}
}
