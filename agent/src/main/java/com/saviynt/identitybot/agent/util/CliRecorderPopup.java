package com.saviynt.identitybot.agent.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.saviynt.identitybot.agent.constants.Constants;

@Component
public class CliRecorderPopup {

	JWindow frame;
	@Value("${cobot.server.url}")
	String cobotServerUrl;
	Timer timer;
	boolean visible=true;
	public void showWindow() {
		frame = new JWindow();
		File file = null;
		ImageIcon img = null;
		File file1 = null;
		ImageIcon img1 = null;
		File file2 = null;
		ImageIcon img2 = null;
		File file3 = null;
		ImageIcon img3 = null;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		try {
//			file = ResourceUtils.getFile("icon_menu64.png");
//			file1 = ResourceUtils.getFile("grey.png");
//			file2 = ResourceUtils.getFile("grey1.png");
//			file3 = ResourceUtils.getFile("record.png");
			java.net.URL logo = new URL(cobotServerUrl+"img/logo.png");
			img = new ImageIcon(logo);
			java.net.URL blank = new URL(cobotServerUrl+"img/grey.png");
			img1 = new ImageIcon(blank);
			java.net.URL line = new URL(cobotServerUrl+"img/grey1.png");
			img2 = new ImageIcon(line);
			java.net.URL recorder = new URL(cobotServerUrl+"img/record.png");
			img3 = new ImageIcon(recorder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		JPanel panel = new JPanel();
		Shape shape = new RoundRectangle2D.Double(10, 10, 770, 70, 50, 70);
		if(img==null) {
			img =new ImageIcon(file.getAbsolutePath());
		}
		if(img1==null) {
			img1 =new ImageIcon(file1.getAbsolutePath());
		}
		if(img2==null) {
			img2 =new ImageIcon(file2.getAbsolutePath());
		}
		if(img3==null) {
			img3=new ImageIcon(file3.getAbsolutePath());
		}
		Image resizeimg = getScaledImage(img.getImage(), 40, 40);
		ImageIcon resize = new ImageIcon(resizeimg);
		JLabel imageLabel = new JLabel(resize);
		
		Image resizeimg1 = getScaledImage(img1.getImage(), 2, 40);
		ImageIcon resize1 = new ImageIcon(resizeimg1);
		JLabel imageLabel1 = new JLabel(resize1);
		//imageLabel1.setBounds(0, 0, 55, 55);

		Image resizeimg2 = getScaledImage(img2.getImage(), 8, 50);
		ImageIcon resize2 = new ImageIcon(resizeimg2);
		
		Image temp = getScaledImage(img2.getImage(), 18, 18);
		ImageIcon tempimage = new ImageIcon(temp);
		
		JLabel imageLabel2 = new JLabel(resize2); 
		JLabel imageLabel3 = new JLabel(resize2); 
		JLabel imageLabel5 = new JLabel(resize2); 
		JLabel templabel = new JLabel(tempimage); 
		
		Image resizeimg3 = getScaledImage(img3.getImage(), 18, 18);
		ImageIcon resize3 = new ImageIcon(resizeimg3);
		JLabel imageLabel4 = new JLabel(resize3); 

		
		JLabel lb = new JLabel(Constants.CLI_POPUP_MESSSAGE);
		lb.setFont(new Font("Serif,Helvetica Neue, Arial, sans-serif", Font.PLAIN, 24));
		//lb.setBounds(110, 0, 400, 65);
		lb.setForeground(Color.RED);
		templabel.setVisible(false);
		panel.add(imageLabel);
		panel.add(imageLabel2);
		panel.add(imageLabel1);
		panel.add(imageLabel3);
		panel.add(imageLabel4);
		panel.add(templabel);
		panel.add(imageLabel5);
		panel.add(lb);
		panel.setBorder(new LineBorder(Color.RED,2,false));
		panel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		
		frame.getContentPane().add(panel);
		timer = new Timer(1000, new ActionListener() {
		      @Override
		      public void actionPerformed(ActionEvent e) {
		    	  visible = !visible;
		    	  imageLabel4.setVisible(visible);
		    	  templabel.setVisible(!visible);
		    	  
		      }
		    });
		    timer.setRepeats(true);
		    // Aprox. 60 FPS
		    timer.setDelay(1000);
		    timer.start();
		//frame.setShape(shape);
		frame.setSize(560, 70);
		centreWindow(frame);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);

	}

	private Image getScaledImage(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();

		return resizedImg;
	}
	public void centreWindow(JWindow frame) {
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
	    frame.setLocation((x+x)-10, (y+y)-100);
	}

	public void closeWindow() {
		timer.stop();
		frame.dispose();
	}
	public static void main(String[] args) {
		CliRecorderPopup demo = new CliRecorderPopup();
		demo.showWindow();
	}	
	
}
