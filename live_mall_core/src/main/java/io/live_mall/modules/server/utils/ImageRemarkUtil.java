package io.live_mall.modules.server.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.ImageIcon;

/*******************************************************************************
 * Description: 图片水印工具类 
 * @author zengshunyao
 * @version 1.0
 */
public class ImageRemarkUtil {

    // 水印透明度
    private static float alpha = 0.2f;
    // 水印横向位置
    private static int positionWidth = 150;
    // 水印纵向位置
    private static int positionHeight = 300;
    // 水印文字字体
    private static Font font = new Font("宋体", Font.BOLD, 80);
    // 水印文字颜色
    private static Color color = Color.red;
    // 水印之间的间隔
    private static final int XMOVE = 320;
    // 水印之间的间隔
    private static final int YMOVE = 320;
    
    /**
     * 
     * @param alpha
     *            水印透明度
     * @param positionWidth
     *            水印横向位置
     * @param positionHeight
     *            水印纵向位置
     * @param font
     *            水印文字字体
     * @param color
     *            水印文字颜色
     */
    public static void setImageMarkOptions(float alpha, int positionWidth,
            int positionHeight, Font font, Color color) {
        if (alpha != 0.0f)
            ImageRemarkUtil.alpha = alpha;
        if (positionWidth != 0)
            ImageRemarkUtil.positionWidth = positionWidth;
        if (positionHeight != 0)
            ImageRemarkUtil.positionHeight = positionHeight;
        if (font != null)
            ImageRemarkUtil.font = font;
        if (color != null)
            ImageRemarkUtil.color = color;
    }

    /**
     * 给图片添加水印图片
     * 
     * @param iconPath
     *            水印图片路径
     * @param srcImgPath
     *            源图片路径
     * @param targerPath
     *            目标图片路径
     */
    public static void markImageByIcon(String iconPath, String srcImgPath,
            String targerPath,Integer degree) {
		try {
				FileInputStream fileInputStream = new FileInputStream(new File(srcImgPath));
			   markImageByIcon(iconPath, fileInputStream, targerPath, degree);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
    }

    /**
     * 给图片添加水印图片、可设置水印图片旋转角度
     * 
     * @param iconPath
     *            水印图片路径
     * @param srcImgPath
     *            源图片路径
     * @param targerPath
     *            目标图片路径
     * @param degree
     *            水印图片旋转角度
     */
    public static void markImageByIcon(String iconPath, InputStream srcImgPath,
            String targerPath, Integer degree) {
        OutputStream os = null;
        try {

        	Image srcImg = ImageIO.read(srcImgPath);

            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);

            // 1、得到画笔对象
            Graphics2D g = buffImg.createGraphics();

            // 2、设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            g.drawImage(
                    srcImg.getScaledInstance(srcImg.getWidth(null),
                            srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0,
                    null);
            // 3、设置水印旋转
            if (null != degree) {
                g.rotate(Math.toRadians(degree),
                        (double) buffImg.getWidth() / 2,
                        (double) buffImg.getHeight() / 2);
            }

            // 4、水印图片的路径 水印图片一般为gif或者png的，这样可设置透明度
            ImageIcon imgIcon = new ImageIcon(iconPath);

            // 5、得到Image对象。
            Image img = imgIcon.getImage();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));

            // 6、水印图片的位置
			/* g.drawImage(img, positionWidth, positionHeight, null); */
            
            int FONT_SIZE=200;
            int width = srcImg.getWidth(null);
            int height = srcImg.getHeight(null);
            int x = -width / 2;
            int y = -height / 2;
            int markWidth =FONT_SIZE;// 字体长度
            int markHeight = FONT_SIZE;// 字体高度
            // 循环添加水印
            while (x < width * 1.5) {
                y = -height / 2;
                while (y < height * 1.5) {
                	g.drawImage(img,x, y, null);
                    y += markHeight + YMOVE;
                }
                x += markWidth + XMOVE;
            }
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            // 7、释放资源
            g.dispose();
            // 8、生成图片
            os = new FileOutputStream(targerPath);
            ImageIO.write(buffImg, "JPG", os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给图片添加水印文字
     * 
     * @param logoText
     *            水印文字
     * @param srcImgPath
     *            源图片路径
     * @param targerPath
     *            目标图片路径
     */
    public static void markImageByText(String logoText, String srcImgPath,
            String targerPath) {
        try {
			markImageByText(logoText, new FileInputStream(new File(srcImgPath)), targerPath, null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void markImageByText(String logoText, String srcImgPath,
            String targerPath,Integer degree) {
        try {
			markImageByText(logoText, new FileInputStream(new File(srcImgPath)), targerPath, degree);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * 给图片添加水印文字、可设置水印文字的旋转角度
     * 
     * @param logoText
     * @param srcImgPath
     * @param targerPath
     * @param degree
     */
    
    
    public static void markImageByText(String logoText, InputStream srcImgPath,
            String targerPath, Integer degree) {

        InputStream is = null;
        FileImageOutputStream  os = null;
        try {
            // 1、源图片
            Image srcImg = ImageIO.read(srcImgPath);
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);

            // 2、得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            // 3、设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(
                    srcImg.getScaledInstance(srcImg.getWidth(null),
                            srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0,
                    null);
            // 4、设置水印旋转
            if (null != degree) {
                g.rotate(Math.toRadians(degree),
                        (double) buffImg.getWidth() / 2,
                        (double) buffImg.getHeight() / 2);
            }
            // 5、设置水印文字颜色
            g.setColor(color);
            // 6、设置水印文字Font
            g.setFont(font);
            // 7、设置水印文字透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            // 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
            int FONT_SIZE=80;
            int width = srcImg.getWidth(null);
            int height = srcImg.getHeight(null);
            int x = -width / 2;
            int y = -height / 2;
            int markWidth =FONT_SIZE;// 字体长度
            int markHeight = FONT_SIZE;// 字体高度
            // 循环添加水印
            while (x < width * 1.5) {
                y = -height / 2;
                while (y < height * 1.5) {
                	g.drawString (logoText, x, y);
                    y += markHeight + YMOVE;
                }
                x += markWidth + XMOVE;
            }
            // 9、释放资源
            g.dispose();
            // 10、生成图片
            File file = new File(targerPath);
            if(!file.exists()){
            	file.createNewFile();
            }
            os = new FileImageOutputStream(file);
            Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");
    		if (iter.hasNext()) {
    			ImageWriter writer = iter.next();
    			ImageWriteParam param = writer.getDefaultWriteParam();
    			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    			param.setCompressionQuality(0.92f);
    			try {
    				writer.setOutput(os);
    				writer.write(null, new IIOImage(buffImg, null, null), param);
    				writer.dispose();
    			} catch (Exception ex) {
    				ex.printStackTrace();
    				System.out.println("===异常了==");
    			}
    		}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
    	// 给图片添加水印文字,水印文字旋转-45
		/*
		String srcImgPath = "F:\\data\\dome.jpg";
		String logPath = "F:\\data\\logo.jpg";
		String targerTextPath2 = "F:\\data\\419338355447169025.png";
		System.out.println("给图片添加水印文字开始...");
		markImageByIcon(logPath, srcImgPath, targerTextPath2, -45);
		System.out.println("给图片添加水印文字结束...");
		*/
        // 给图片添加水印文字,水印文字旋转-45
        String srcImgPath = "F:\\data\\dome.jpg";
        String logPath = "F:\\data\\logo.jpg";
        String targerTextPath2 = "F:\\data\\419338355447169025.png";
        System.out.println("给图片添加水印文字开始...");
        markImageByText("华夏盛世", srcImgPath, targerTextPath2, -45);
        System.out.println("给图片添加水印文字结束...");
    }

}