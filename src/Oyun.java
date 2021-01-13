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
	private int beklemeS�resi=0;//Araba spawn s�resi
	private int gecenS�re=0;//Oyun oynama s�resi
	private int gecenArabaSayisi=0;
	private int yaris_s�resi=60;//Saniye cinsinden yar�� s�resi
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
	
	public boolean KontrolEt(){ // Arabam�z�n di�er arabalar ile kar��la�ma durumunu kontrol edip �arparsa true �arpmazsa false d�ner.
		for(Araba araba:arabalar)
		{
			if(new Rectangle(araba.getX(),araba.getY(),araba.getArabaGenislik(),araba.getArabaYukseklik()).intersects(new Rectangle(arabaX,arabaY,arabaGenislik,arabaYukseklik))){
				return true;
			}
		}
		return false;
	}
	public void �lkOynayisMi(Graphics g){
		if(ilkOynayisMi==true) // Bir kereli�e mahsus oyun giri�inde mesaj g�sterilir.
		{
			Font myFont = new Font ("Helvetica", 1, 15);
			g.setColor(Color.white);
			g.setFont(myFont);
			g.drawString("Oyun'a ba�lamak i�in SPACE'e bas�n. !", 160, 100);
			ilkOynayisMi=false;
		}
	}
	public void UstMenu(Graphics g) // Yukar�da belirtilen kare ve i�erisinde yaz� burada belirleniyor.
	{
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 600, 50);
		Font myFont = new Font ("Helvetica", 1, 14);
		g.setColor(Color.black);
		g.setFont(myFont);
		g.drawString("Ge�en S�re : "+(gecenS�re/1000.0), 240, 30);
		
	}
	@Override
	public void paint(Graphics g){
		super.paint(g);
		if(KontrolEt()) //  Arabam�z�n di�er arabalar ile kar��la�ma durumunu kontrol edip �arparsa true �arpmazsa false d�ner.
		{
			zamanlayici.stop();
			String message = "Kaybettiniz..\n�arpmadan yan�n�zdan ge�en araba say�s� :"+
							gecenArabaSayisi+"\nGe�irdi�iniz S�re : " + gecenS�re/1000.0;
			JOptionPane.showMessageDialog(this, message);
			System.exit(0);
		}
		if(gecenS�re >= (yaris_s�resi*1000)) // Belirlenen yar�� dakikalar�n� kar��la�t�r�r ve ge�mi�se onun bitti�ini belirtir.
		{
			zamanlayici.stop();
			String message = "Kazand�n�z.\n"+yaris_s�resi+" saniyede "+gecenArabaSayisi+" kadar arabayi ge�erek \n birinci oldunuz !";
			JOptionPane.showMessageDialog(this, message);
			System.exit(0);
		}
		gecenS�re+=20; // Milisaniye cinsinden 5 ms eklenir.
		g.drawImage(arkaplan, 0, 0,600, 1000, this);
		g.drawImage(arabaResmi, arabaX, arabaY,arabaGenislik, arabaYukseklik, this);
		for(int i=0;i<arabalar.size();i++)
		{
			if(arabalar.get(i).getY()>900)
			{
				arabalar.remove(i); // Y ekseninden ��kan araba yok oluyor.
				gecenArabaSayisi++;
			}
		}
		
		for(Araba araba:arabalar)
		{
			//Var olan arabalar �iziliyor.
			g.drawImage(araba.getArabaResmi(), araba.getX(), araba.getY(), araba.getArabaGenislik(), araba.getArabaYukseklik(), this);
		}
		UstMenu(g);
		�lkOynayisMi(g);
	}
	@Override
	public void repaint()
	{
		if(beklemeS�resi<87.5) // Arabalar�n do�arken birbiriyle �ak��mamas� i�in bir s�re bekletilir.
		{
			beklemeS�resi++;
		}
		else
		{
			beklemeS�resi=0; // Bekleme s�resi s�f�rlan�r ve yeni bir araba eklenir.
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
			if(harfKodu == KeyEvent.VK_LEFT)//Sola gidi�
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
			else if(harfKodu == KeyEvent.VK_RIGHT)//Sa�a gidi�
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
			else if(harfKodu == KeyEvent.VK_UP)//Yukar� Gidi�
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
			else if(harfKodu == KeyEvent.VK_DOWN)//Alta gidi�
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
				g.drawString("Oyun'a devam etmek i�in SPACE'e bas�n. !", 150, 100);
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
