import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class OyunEkrani extends JFrame {

	static final int GENISLIK =600 ;
	static final int YUKSEKLIK = 1000;
	static final String OYUN_ISMI  = " Race against Time";
    public OyunEkrani(String oyunIsmi) throws HeadlessException{
    	super(oyunIsmi);
    }

    public static void main(String[] args) {
    	
    	OyunEkrani ekran =new OyunEkrani(OYUN_ISMI);
    	
    	ekran.setTitle(OYUN_ISMI);
    	ekran.setSize(GENISLIK,YUKSEKLIK);
    	ekran.setResizable(false);
        ekran.setLocationRelativeTo(null);
        ekran.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Oyun oyun=new Oyun();
        oyun.requestFocus();//input icin odaklaniyor
        oyun.addKeyListener(oyun);
        oyun.setFocusable(true);//direk bana odaklan
        oyun.setFocusTraversalKeysEnabled(false);
        
        ekran.add(oyun);
        ekran.setVisible(true);
    	
    }
}