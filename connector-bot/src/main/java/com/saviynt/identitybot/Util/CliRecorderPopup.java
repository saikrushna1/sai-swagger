package com.saviynt.identitybot.Util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
public class CliRecorderPopup {

	JWindow frame;

	public void showWindow() {
		frame = new JWindow();
		File file = null;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		try {
			file = ResourceUtils.getFile("classpath:static/img/icon_menu64.png");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		JPanel panel = new JPanel();
		Shape shape = new RoundRectangle2D.Double(10, 0, 490, 70, 50, 70);
		ImageIcon img = new ImageIcon(file.getAbsolutePath());
		Image resizeimg = getScaledImage(img.getImage(), 50, 50);
		ImageIcon resize = new ImageIcon(resizeimg);
		JLabel imageLabel = new JLabel(resize);
		//imageLabel.setBounds(0, 30, 0, 0);

		JLabel lb = new JLabel(Constants.CLI_POPUP_MESSSAGE);
		lb.setFont(new Font("Serif", Font.PLAIN, 28));
		//lb.setBounds(110, 0, 400, 65);
		lb.setForeground(Color.RED);

		panel.add(imageLabel);
		panel.add(lb);
		panel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

		frame.getContentPane().add(panel);

		frame.setShape(shape);
		frame.setSize(520, 70);
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
	    frame.setLocation(x+x, y+y);
	}

	public void closeWindow() {
		frame.dispose();
	}
	public static void main(String[] args) {
		CliRecorderPopup demo = new CliRecorderPopup();
		demo.showWindow();
	}	
}
