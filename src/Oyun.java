import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Oyun extends JPanel implements KeyListener,ActionListener{
	
	Random rand=new Random();
	private BufferedImage arabaResmi;
	private BufferedImage arkaplan;
	private int arabaGenislik;
	private int arabaYukseklik;
	private ArrayList<Araba> arabalar=new ArrayList<Araba>(); 
	private int arabaX=258;
	private int arabaY=780;
	private int arabaHiz=15;
	private int beklemeSüresi=0;//Araba spawn süresi
	private int gecenSüre=0;//Oyun oynama süresi
	private int gecenArabaSayisi=0;
	private int yaris_süresi=60;//Saniye cinsinden yarýþ süresi
	private boolean oyunDevamMi=false;
	private boolean ilkOynayisMi=true;
	Timer zamanlayici=new Timer(5,this);
	
	public Oyun(){
		try
		{
			arabaResmi = ImageIO.read(new FileInputStream(new File("./images/red_car.png")));
			arkaplan   = ImageIO.read(new FileInputStream(new File("./images/road_top_view.jpg")));
		}
		catch (FileNotFoundException e) {System.out.println(e);} 
		catch (IOException e) {System.out.println(e);}
		this.setBackground(Color.WHITE);
		arabaGenislik=arabaResmi.getWidth()/4;
		arabaYukseklik=arabaResmi.getHeight()/4;
		
	}
	
	public boolean KontrolEt(){ // Arabamýzýn diðer arabalar ile karþýlaþma durumunu kontrol edip çarparsa true çarpmazsa false döner.
		for(Araba araba:arabalar)
		{
			if(new Rectangle(araba.getX(),araba.getY(),araba.getArabaGenislik(),araba.getArabaYukseklik()).intersects(new Rectangle(arabaX,arabaY,arabaGenislik,arabaYukseklik))){
				return true;
			}
		}
		return false;
	}
	public void ÝlkOynayisMi(Graphics g){
		if(ilkOynayisMi==true) // Bir kereliðe mahsus oyun giriþinde mesaj gösterilir.
		{
			Font myFont = new Font ("Helvetica", 1, 15);
			g.setColor(Color.white);
			g.setFont(myFont);
			g.drawString("Oyun'a baþlamak için SPACE'e basýn. !", 160, 100);
			ilkOynayisMi=false;
		}
	}
	public void UstMenu(Graphics g) // Yukarýda belirtilen kare ve içerisinde yazý burada belirleniyor.
	{
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 600, 50);
		Font myFont = new Font ("Helvetica", 1, 14);
		g.setColor(Color.black);
		g.setFont(myFont);
		g.drawString("Geçen Süre : "+(gecenSüre/1000.0), 240, 30);
		
	}
	@Override
	public void paint(Graphics g){
		super.paint(g);
		if(KontrolEt()) //  Arabamýzýn diðer arabalar ile karþýlaþma durumunu kontrol edip çarparsa true çarpmazsa false döner.
		{
			zamanlayici.stop();
			String message = "Kaybettiniz..\nÇarpmadan yanýnýzdan geçen araba sayýsý :"+
							gecenArabaSayisi+"\nGeçirdiðiniz Süre : " + gecenSüre/1000.0;
			JOptionPane.showMessageDialog(this, message);
			System.exit(0);
		}
		if(gecenSüre >= (yaris_süresi*1000)) // Belirlenen yarýþ dakikalarýný karþýlaþtýrýr ve geçmiþse onun bittiðini belirtir.
		{
			zamanlayici.stop();
			String message = "Kazandýnýz.\n"+yaris_süresi+" saniyede "+gecenArabaSayisi+" kadar arabayi geçerek \n birinci oldunuz !";
			JOptionPane.showMessageDialog(this, message);
			System.exit(0);
		}
		gecenSüre+=20; // Milisaniye cinsinden 5 ms eklenir.
		g.drawImage(arkaplan, 0, 0,600, 1000, this);
		g.drawImage(arabaResmi, arabaX, arabaY,arabaGenislik, arabaYukseklik, this);
		for(int i=0;i<arabalar.size();i++)
		{
			if(arabalar.get(i).getY()>900)
			{
				arabalar.remove(i); // Y ekseninden çýkan araba yok oluyor.
				gecenArabaSayisi++;
			}
		}
		
		for(Araba araba:arabalar)
		{
			//Var olan arabalar çiziliyor.
			g.drawImage(araba.getArabaResmi(), araba.getX(), araba.getY(), araba.getArabaGenislik(), araba.getArabaYukseklik(), this);
		}
		UstMenu(g);
		ÝlkOynayisMi(g);
	}
	@Override
	public void repaint()
	{
		if(beklemeSüresi<87.5) // Arabalarýn doðarken birbiriyle çakýþmamasý için bir süre bekletilir.
		{
			beklemeSüresi++;
		}
		else
		{
			beklemeSüresi=0; // Bekleme süresi sýfýrlanýr ve yeni bir araba eklenir.
			try { arabalar.add(new Araba(rand.nextInt(540), -20));} 
			catch (FileNotFoundException e) {} 
			catch (IOException e) {}
		}
		super.repaint();
	}
	
	public void actionPerformed(ActionEvent arg0) {
		for(Araba araba:arabalar)
		{
			araba.setY(araba.getY()+4);// Araba y ekseninde ilerletiliyor
		}
		repaint();
		
	}
	public void keyPressed(KeyEvent arg0) {
		int harfKodu=arg0.getKeyCode();
		if(oyunDevamMi==true){ // Oyunun durup durmadigi kontrol edilir.
			if(harfKodu == KeyEvent.VK_LEFT)//Sola gidiþ
			{
				if(arabaX <= 0)
				{
					arabaX=0;
				}
				else
				{
					arabaX-=arabaHiz;
				}
			}
			else if(harfKodu == KeyEvent.VK_RIGHT)//Saða gidiþ
			{
				if(arabaX >= 510)
				{
					arabaX=510;
				}
				else
				{
					arabaX+=arabaHiz;
				}
			}
			else if(harfKodu == KeyEvent.VK_UP)//Yukarý Gidiþ
			{
				if(arabaY <= 600)
				{
					arabaY=600;
				}
				else
				{
					arabaY-=arabaHiz;
				}
			}
			else if(harfKodu == KeyEvent.VK_DOWN)//Alta gidiþ
			{
				if(arabaY >= 800)
				{
			 		arabaY=800;
				}
				else
				{
					arabaY+=arabaHiz;
				}
			}
		}
		if(harfKodu == KeyEvent.VK_SPACE)//Oyun durdurma / devam ettirme
		{
			if(oyunDevamMi == false)
			{
				zamanlayici.start();
				oyunDevamMi=true;
			}
			else
			{
				zamanlayici.stop();
				Graphics g=getGraphics();
				Font myFont = new Font ("Helvetica", 1, 15);
				g.setColor(Color.white);
				g.setFont(myFont);
				g.drawString("Oyun'a devam etmek için SPACE'e basýn. !", 150, 100);
				oyunDevamMi=false;
			}
		}
	}
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void keyTyped(KeyEvent arg0) {
		
	}
}
