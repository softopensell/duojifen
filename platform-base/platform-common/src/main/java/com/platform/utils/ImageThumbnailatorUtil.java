package com.platform.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * 
 * @author softopensell
 * 
 */
public class ImageThumbnailatorUtil{

	/**
	 * 指定大小进行缩放
	 *
	 * size(width,height) 若图片横比200小，高比300小，不变
	 * 若图片横比200小，高比300大，高缩小到300，图片比例不变 若图片横比200大，高比300小，横缩小到200，图片比例不变
	 * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300
	 * keepAspectRatio(false) 默认是按照比例缩放的
	 * 不按照比例，指定大小进行缩放
	 * @throws IOException
	 */
	public static void CompressPicBySize(String orgImage,String outImage,int width,int height,boolean isRatio) throws Exception {
		try {
			File file=new File(orgImage);
			if(!file.exists()){
				return;
			}
			
			Thumbnails.of(orgImage).size(width, height).keepAspectRatio(isRatio).toFile(
					outImage);	
		} catch (Exception e) {
		}
	}
 public static void CompressPicBySize(String orgImage,String outImage,int width,int height,boolean isRatio,String wareImage,Position position){
	try {	
	    File file=new File(orgImage);
		if(!file.exists()){
			return;
		}
        if(wareImage!=null&&new File(wareImage).exists()){
        	if(position==null)position=Positions.BOTTOM_RIGHT;
        	Thumbnails.of(orgImage).watermark(position,ImageIO.read(new File(wareImage)), 1f).size(width, height).keepAspectRatio(isRatio).toFile(
					outImage);	
        }else{
        	Thumbnails.of(orgImage).size(width, height).keepAspectRatio(isRatio).toFile(
					outImage);	
        }
		} catch (Exception e) {
	 }		
	}
	
	public static void CompressPicBySize(String orgImage,String outImage,int width,boolean isRatio) throws Exception {
		BufferedImage tempImage = ImageIO.read(new File(orgImage));  
		int imageWidth = tempImage.getWidth();  
		double scale=1;
		if(width<imageWidth){
			scale=(double)width/imageWidth;
			if(scale<=0.1){
				scale=0.1;
			}
			Thumbnails.of(orgImage).scale(scale).toFile(outImage);
		}else
			Thumbnails.of(orgImage).scale(scale).toFile(outImage);
	}
	public static void CompressPicBySize(String orgImage,String outImage,int width,boolean isRatio,String wareImage,Position position){
		try {	
		    File file=new File(orgImage);
			if(!file.exists()){
				return;
			}
			
			BufferedImage tempImage = ImageIO.read(new File(orgImage));  
			int imageWidth = tempImage.getWidth();  
			double scale=1;
			if(width<imageWidth){
				scale=(double)width/imageWidth;
				if(scale<=0.1){
					scale=0.1;
				}
			}
			
	        if(wareImage!=null&&new File(wareImage).exists()){
	        	if(position==null)position=Positions.BOTTOM_RIGHT;
	        	Thumbnails.of(orgImage).watermark(position,ImageIO.read(new File(wareImage)), 1f).scale(scale).toFile(
						outImage);	
	        }else{
	        	Thumbnails.of(orgImage).scale(scale).toFile(outImage);
	        }
			} catch (Exception e) {
		 }		
		}
	
	
	
	
	public static void CropPicBySize(String orgImage,String outImage,int width,int height,String wareImage,Position position) throws IOException {
		
	    File file=new File(orgImage);
		if(!file.exists()){
			return;
		}
		BufferedImage tempImage = ImageIO.read(file);  
		    int orgW = tempImage.getWidth();
	        int orgH = tempImage.getHeight();
	        
	        int hfW=(orgW-width)/2;
	        int hfH=(orgH-height)/2;
	        
	        if(orgH<height){
	        	height=orgH;
	        	hfH=0;
	        }
	        if(orgW<width){
	        	width=orgW;
	        	hfW=0;
	        }
	        
	        if(wareImage!=null&&new File(wareImage).exists()){
	        	if(position==null)position=Positions.BOTTOM_RIGHT;
	        	Thumbnails.of(orgImage).watermark(position,ImageIO.read(new File(wareImage)), 0.5f).sourceRegion(0+hfW, 0+hfH, width+hfW, height+hfH).scale(1).toFile(outImage);;		
	        }else{
	        	Thumbnails.of(orgImage).sourceRegion(0+hfW, 0+hfH, width+hfW, height+hfH).scale(1).toFile(outImage);;		
	        }
				
	}
	public static void CropPicBySize(String orgImage,String outImage,int width,int height) throws IOException {
		File file=new File(orgImage);
		if(!file.exists()){
			return;
		}
		BufferedImage tempImage = ImageIO.read(file);  
		    int orgW = tempImage.getWidth();
	        int orgH = tempImage.getHeight();
	        
	        int hfW=(orgW-width)/2;
	        int hfH=(orgH-height)/2;
	        
	        if(orgH<height){
	        	height=orgH;
	        	hfH=0;
	        }
	        if(orgW<width){
	        	width=orgW;
	        	hfW=0;
	        }
	        
	        Thumbnails.of(orgImage).sourceRegion(0+hfW, 0+hfH, width+hfW, height+hfH).scale(1).toFile(outImage);;
	}
	
	public static BufferedImage CropPicBySize(String orgImage,int width,int height) throws IOException {
	        
		File file=new File(orgImage);
		if(!file.exists()){
			return null;
		}
		BufferedImage tempImage = ImageIO.read(file);
		
		    int orgW = tempImage.getWidth();
	        int orgH = tempImage.getHeight();
	        
	        int hfW=(orgW-width)/2;
	        int hfH=(orgH-height)/2;
	        
	        if(orgH<height){
	        	height=orgH;
	        	hfH=0;
	        }
	        if(orgW<width){
	        	width=orgW;
	        	hfW=0;
	        }
	        
	        
	        int imageWidth = tempImage.getWidth();  
			double scale=1;
			if(width<imageWidth){
				scale=(double)width/imageWidth;
				if(scale<=0.1){
					scale=0.1;
				}
				return Thumbnails.of(orgImage).scale(scale).asBufferedImage();
			}else
				return Thumbnails.of(orgImage).scale(scale).asBufferedImage();
//	      return Thumbnails.of(orgImage).sourceRegion(0+hfW, 0+hfH, width+hfW, height+hfH).scale(1).asBufferedImage();
	}
	
	public static BufferedImage CropPicBySize(BufferedImage tempImage,int width,int height) throws IOException {
		
		int orgW = tempImage.getWidth();
		int orgH = tempImage.getHeight();
		
		int hfW=(orgW-width)/2;
		int hfH=(orgH-height)/2;
		
		if(orgH<height){
			height=orgH;
			hfH=0;
		}
		if(orgW<width){
			width=orgW;
			hfW=0;
		}
		
		
		int imageWidth = tempImage.getWidth();  
		double scale=1;
		if(width<imageWidth){
			scale=(double)width/imageWidth;
			if(scale<=0.1){
				scale=0.1;
			}
			return Thumbnails.of(tempImage).scale(scale).asBufferedImage();
		}else
			return Thumbnails.of(tempImage).scale(scale).asBufferedImage();
//	      return Thumbnails.of(orgImage).sourceRegion(0+hfW, 0+hfH, width+hfW, height+hfH).scale(1).asBufferedImage();
	}
	
	/**
	 * 截取图像模块
	 * @param tempImage
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 * @throws IOException
	 */
  public static BufferedImage CropPicBySize(BufferedImage tempImage,int x,int y,int width,int height) throws IOException {
		
	      return Thumbnails.of(tempImage).sourceRegion(x, y, width, height).scale(1).asBufferedImage();
	}
	
	
	/**
	 * 按照比例进行缩放
	 * scale(比例) eg:0.25f
	 * @throws IOException
	 */
	public static void CompressPicByScale(String orgImage,String outImage,double scale) throws IOException {
		Thumbnails.of(orgImage).scale(scale)
				.toFile(outImage);
	}

	

	/**
	 * 旋转
	 *  rotate(角度),正数：顺时针 负数：逆时针
	 * @throws IOException
	 */
	public static void CompressPicByLocal(String orgImage,String outImage,int rotate) throws IOException {
		Thumbnails.of(orgImage).rotate(rotate).toFile(
				outImage);
	}

	/**
	 * 水印
	 * watermark(位置(Positions.BOTTOM_RIGHT)，水印图，透明度 0.5f)
	 * Positions.CENTER,
	 * @throws IOException
	 */
	public static void CompressPicByWare(String orgImage,String outImage,String wareImage,Position position) throws Exception {
		File orgImageFile=new File(orgImage);
		if (orgImageFile.length()>1*1024*1024) {
			CompressPicBySize(orgImage, outImage, 800, 1024, true);
		}
		
		if(wareImage!=null&&new File(wareImage).exists()){
			Thumbnails.of(orgImage).watermark(
					position,
					ImageIO.read(new File(wareImage)), 0.5f)
					.outputQuality(0.8f).scale(1).toFile(outImage);
		}else{
			CompressPicBySize(orgImage, outImage, 800, 1024, true);
		}
		
	}
	/**
	 * 裁剪
	 * 图片中心400*400的区域
	 * @throws IOException
	 */
	public void CompressPicByCut(String orgImage,String outImage,Position position,int width,int height) throws IOException {
		Thumbnails.of(orgImage).sourceRegion(position, width,
				height).size(width, height).keepAspectRatio(false).toFile(
						outImage);
		
//		/**
//		 * 图片中心400*400的区域
//		 */
//		Thumbnails.of("images/test.jpg").sourceRegion(Positions.CENTER, 400,
//				400).size(200, 200).keepAspectRatio(false).toFile(
//				"images/image_region_center.jpg");
//		/**
//		 * 图片右下400*400的区域
//		 */
//		Thumbnails.of("images/test.jpg").sourceRegion(Positions.BOTTOM_RIGHT,
//				400, 400).size(200, 200).keepAspectRatio(false).toFile(
//				"images/image_region_bootom_right.jpg");
//		/**
//		 * 指定坐标
//		 */
//		Thumbnails.of("images/test.jpg").sourceRegion(600, 500, 400, 400).size(
//				200, 200).keepAspectRatio(false).toFile(
//				"images/image_region_coord.jpg");
	}

	/**
	 * 转化图像格式
	 * 
	 * @throws IOException
	 */
	public void test7() throws IOException {
		/**
		 * outputFormat(图像格式)
		 */
		Thumbnails.of("images/test.jpg").size(1280, 1024).outputFormat("png")
				.toFile("images/image_1280x1024.png");
		Thumbnails.of("images/test.jpg").size(1280, 1024).outputFormat("gif")
				.toFile("images/image_1280x1024.gif");
	}

	/**
	 * 输出到OutputStream
	 * 
	 * @throws IOException
	 */
	public void test8() throws IOException {
		/**
		 * toOutputStream(流对象)
		 */
		OutputStream os = new FileOutputStream(
				"images/image_1280x1024_OutputStream.png");
		Thumbnails.of("images/test.jpg").size(1280, 1024).toOutputStream(os);
	}

	/**
	 * 输出到BufferedImage
	 * 
	 * @throws IOException
	 */
	public void test9() throws IOException {
		/**
		 * asBufferedImage() 返回BufferedImage
		 */
		BufferedImage thumbnail = Thumbnails.of("images/test.jpg").size(1280,
				1024).asBufferedImage();
		ImageIO.write(thumbnail, "jpg", new File(
				"images/image_1280x1024_BufferedImage.jpg"));
	}
	public static void main(String[] args) {
//		try {
////			ImageThumbnailatorUtil.CompressPicByWare("a.JPG", "a111.JPG", "jiaoke_watermark.png", Positions.BOTTOM_RIGHT);
////			ImageThumbnailatorUtil.CompressPicBySize("ceshi.jpeg", "ces600.JPG", 600, 400, true);
////			ImageThumbnailatorUtil.CompressPicBySize("sss_org.jpeg", "sss_org_800.JPG", 800,true);
////			ImageThumbnailatorUtil.CropPicBySize("sss_org_600_200.JPG", "sss_org_600_200.JPG", 800,200);
////			ImageThumbnailatorUtil.CompressPicByWare("2.JPG", "a2.JPG", "jiaoke_watermark_min.png", Positions.BOTTOM_CENTER);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		ImageThumbnailatorUtil.CompressPicBySize("/Users/softopensell/Documents/doc/一诗一画/IMG_4281.JPG", "/Users/softopensell/Documents/doc/一诗一画/IMG_42821.jpg", 1000,false, "ysyh_watermark_big_400.png", Positions.BOTTOM_RIGHT);
	}
	
}
