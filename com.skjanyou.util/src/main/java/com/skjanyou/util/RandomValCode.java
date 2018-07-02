package com.skjanyou.util;



import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;



public final class RandomValCode {
	
	private int width = 120;
	private int height = 35;
	private int codeCount = 4;
	
	private int x,y,fontHeight;
	
	private char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	
	private Random random = new Random();
	
	private BufferedImage buffImg;
	private Graphics2D graphics;
	
	public RandomValCode()
	{
		x = width / (codeCount + 2);
		y = height - 4;
		fontHeight = height - 2;
		
		buffImg = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		graphics = buffImg.createGraphics();
	}
	
	public String createRanValCode()
	{
		//设置随机背景
		graphics.setColor(getRandColor(200,250));
		graphics.fillRect(0, 0, width, height);
		
		//设置字体
		Font font = new Font("hakuyoxingshu7000", Font.ITALIC | Font.BOLD, fontHeight);
		graphics.setFont(font);
		
		//设置边框颜色
//		graphics.setColor(Color.BLACK);
//		graphics.drawRect(0, 0, width - 1, height - 1);
		
		//绘制随机线条，增强辨识难度
		graphics.setColor(Color.BLACK);
		for (int i = 0; i < 10; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			graphics.drawLine(x, y, x + xl, y + yl);
		}
		
		StringBuffer randomCode = new StringBuffer();
		int red = 0, green = 0, blue = 0;
		// 随机产生codeCount数字的验证码。
		for (int i = 0; i < codeCount; i++) {
			// 得到随机产生的验证码数字。
			String strRand = String.valueOf(codeSequence[random.nextInt(36)]);
			// 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);
			// 用随机产生的颜色将验证码绘制到图像中。
			graphics.setColor(new Color(red, green, blue));
			graphics.drawString(strRand, (i + 1) * x, y);
			// 将产生的四个随机数组合在一起。
			randomCode.append(strRand);
		}
		
		return randomCode.toString();
	}
	
	//获得图片
	public BufferedImage getImage(){
		return this.buffImg;
	}
	
	//生成随机颜色
	private Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
}
