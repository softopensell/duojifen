/**
 * beidou-core#com.baidu.beidou.common.utils.FileUtils.java
 */
package com.platform.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;

import org.springframework.util.FileCopyUtils;

import net.coobird.thumbnailator.geometry.Positions;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 文件操作的方法集
 */
public class FileUtils extends FileCopyUtils{

	 public static String getConfigImagePath(){
		 URL url=null;
		 url=FileUtils.class.getClassLoader().getResource("");
		 String path=url.getPath();
		 return path;
	 }
	 public static void localPrint(File file) throws PrintException, IOException{
	        //构建打印请求属性集  
	        HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();  
	        //设置打印格式，因为未确定类型，所以选择autosense  
	        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;  
	        //查找所有的可用的打印服务  
	        PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);  
	        //定位默认的打印服务  
	        PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
	        DocPrintJob job = defaultService.createPrintJob(); //创建打印作业  
	        FileInputStream fis = new FileInputStream(file); //构造待打印的文件流  
	        DocAttributeSet das = new HashDocAttributeSet();  
	        Doc doc = new SimpleDoc(fis, flavor, das);  
	        job.print(doc, pras);  
	        fis.close();
	       
		}
		
		public static  byte[] getImageDataAsImagePath(String fileAndPath){
			// set data
			byte[] data=null;
			FileInputStream input = null;
			File tmpFile = new File(fileAndPath);
			
			try {
				long fileSize = tmpFile.length();
				data = new byte[(int) fileSize];
				input = new FileInputStream(tmpFile);
				input.read(data);
				input.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
			return data;
		}
		
		
		 /**
		 * 
		 * @param fileName
		 */
		public static List readFileByLines(String fileName) {
			List<String> list=new ArrayList<String>();
			File file = new File(fileName);
			System.out.println(file.getName());
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
				String tempString = null;
				int line = 1;
				while ((tempString = reader.readLine()) != null) {
					line++;
					list.add(tempString);
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e1) {
					}
				}
			}
			
			return list;
		}
		
		
		public static List readFileByLinesAsInputStream(InputStream in,String charset) {
			List<String> list=new ArrayList<String>();
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(in,charset));
				String tempString = null;
				int line = 1;
				while ((tempString = reader.readLine()) != null) {
					System.out.println("line " + line + ": " + tempString);
					line++;
					list.add(tempString);
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e1) {
					}
				}
			}
			
			return list;
		}
		public static List readFileByLinesAsInputStream(String fileName) {
			List<String> list=new ArrayList<String>();
			BufferedReader reader = null;
			 InputStream in = FileUtils.class.getClassLoader().getResourceAsStream(fileName); 
			try {
				reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
				
				String tempString = null;
				int line = 1;
				while ((tempString = reader.readLine()) != null) {
					//System.out.println("line " + line + ": " + tempString);
					line++;
					list.add(tempString);
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e1) {
					}
				}
			}
			
			return list;
		}
		 public static InputStream getInputStreamAsFilePath(String fileName) { 
	         InputStream in = FileUtils.class.getClassLoader().getResourceAsStream(fileName); 
	         return in; 
	    } 
		 public static File  getFileAsFilePath(String fileName) { 
	         File file=new File(getConfigImagePath()+"/"+fileName);
	         return file; 
	    } 
	/**
	 * 关闭文件流
	 * @param w
	 */
	public final static void closeWriter(Writer w) {
		if(w != null)
			try{
				w.close();
			}catch(Exception e){
			}
	}
	
	
	/**
	 * 关闭文件流
	 * @param r
	 */
	public final static void closeReader(Reader r) {
		if(r != null)
			try{
				r.close();
			}catch(Exception e){
			}
	}
	
	/**
	 * 关闭文件流
	 * @param os
	 */
	public final static void closeOutputStream(OutputStream os) {
		if(os != null)
			try{
				os.close();
			}catch(Exception e){
			}
	}
	
	/**
	 * 关闭文件流
	 * @param is
	 */
	public final static void closeInputStream(InputStream is) {
		if(is != null)
			try{
				is.close();
			}catch(Exception e){
			}
	}
	public final static ByteArrayOutputStream transformFileToBytStream(String pathAndFileName) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try{
		    // 创建输入流和内存输出流
			FileInputStream input = new FileInputStream(pathAndFileName);
		    int b;
		    // 从文件读取数据，并写入到内存缓冲区中
		    while((b = input.read()) > 0) {
		        output.write(b);
		    }
		}catch (IOException e) {
			e.printStackTrace();
		}  
	  return output;
	}
	
	
	  public final static void SaveFileFromInputStream(InputStream stream,String pathAndFileName) {      
			FileOutputStream fs;
			try {
				fs = new FileOutputStream(pathAndFileName);
				byte[] buffer = new byte[1024 * 1024];
				int byteread = 0;
				while ((byteread = stream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
					fs.flush();
				}
				fs.close();
				stream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	  
	  public final static File SaveFileFromInputStream(byte[] buffer,String pathAndFileName) {      
		  BufferedOutputStream stream = null;
	        File file = null;
	        try {
	            file = new File(pathAndFileName);
	            FileOutputStream fstream = new FileOutputStream(file);
	            stream = new BufferedOutputStream(fstream);
	            stream.write(buffer);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally{
	            if (stream != null){
	                try{
	                    stream.close();
	                } catch (IOException e1){
	                    e1.printStackTrace();
	                }
	            }
	        }
	       return file;
	    }
	  
	  public static String getExtensionName(String filename) {   
	        if ((filename != null) && (filename.length() > 0)) {   
	            int dot = filename.lastIndexOf('.');   
	            if ((dot >-1) && (dot < (filename.length() - 1))) {   
	                return filename.substring(dot + 1);   
	            }   
	        }   
	        return filename;   
	    }   
	  public static String getFileName(String filePathName) {   
		  if ((filePathName != null) && (filePathName.length() > 0)) {   
			  int dot = filePathName.lastIndexOf('/');   
			  if ((dot >-1) && (dot < (filePathName.length() - 1))) {   
				  return filePathName.substring(dot + 1);   
			  }   
		  }   
		  return filePathName;   
	  }   
	  public static boolean mkdir(String path){
		  boolean flag=false;
		  File dir = new File( path);
			if (!dir.exists()) {
				try {
					dir.mkdirs();
					flag=true;
				} catch (Exception e) {
					e.printStackTrace();
					flag=false;
				}
			}
			return flag;
	  }
	  /**
		 * 返回 kb
		 * @param filePath
		 * @return
		 */
		public static long getFileSize(String filePath){
			File file = new File(filePath);
			if(!file.exists()){
				return 0;
			}
			return file.length()/1024;
		}
		
		
		/**
		  * @Descriptionmap 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		  * @Date 2015-01-26
		  * @param path 图片路径
		  * @return
		  */
		 public static String imageToBase64(String path) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		 	byte[] data = null;
		 	// 读取图片字节数组
		 	try {
		 		InputStream in = new FileInputStream(path);
		 		data = new byte[in.available()];
		 		in.read(data);
		 		in.close();
		 	} catch (IOException e) {
		 		e.printStackTrace();
		 	}
		 	// 对字节数组Base64编码
		 	BASE64Encoder encoder = new BASE64Encoder();
		 	return encoder.encode(data);// 返回Base64编码过的字节数组字符串
		 }

		 /**
		  * @Descriptionmap 对字节数组字符串进行Base64解码并生成图片
		  * @Date 2015-01-26
		  * @param base64 图片Base64数据
		  * @param path 图片路径
		  * @return
		  */
		 public static boolean base64ToImage(String base64, String path) {// 对字节数组字符串进行Base64解码并生成图片
		 	if (base64 == null){ // 图像数据为空
		 		return false;
		 	}
		 	File savaPath=new File(path);
		 	if(!savaPath.exists()) {
		 		mkdir(savaPath.getParentFile().getAbsolutePath());
		 	}
		 	BASE64Decoder decoder = new BASE64Decoder();
		 	try {
		 		if(base64.indexOf(",")>0)base64= base64.substring(base64.indexOf(",") + 1);
		 		// Base64解码
		 		byte[] bytes = decoder.decodeBuffer(base64);
		 		for (int i = 0; i < bytes.length; ++i) {
		 			if (bytes[i] < 0) {// 调整异常数据
		 				bytes[i] += 256;
		 			}
		 		}
		 		// 生成jpeg图片
		 		OutputStream out = new FileOutputStream(path);
		 		out.write(bytes);
		 		out.flush();
		 		out.close();
		 		return true;
		 	} catch (Exception e) {
		 		return false;
		 	}
		 }
		 
			
		public static void findFiles(String pathName,int depth,List<String> files) throws IOException{
				//获取pathName的File对象
				File dirFile = new File(pathName);
				//判断该文件或目录是否存在，不存在时在控制台输出提醒
				if (!dirFile.exists()) {
					System.out.println("do not exit");
					return ;
				}
				//判断如果不是一个目录，就判断是不是一个文件，时文件则输出文件路径
				if (!dirFile.isDirectory()) {
					if (dirFile.isFile()&&!dirFile.isHidden()) {
						System.out.println(dirFile.getCanonicalFile());
						files.add(dirFile.getAbsolutePath());
					}
					return ;
				}
				
				for (int j = 0; j < depth; j++) {
					System.out.print("  ");
				}
				System.out.print("|--");
				System.out.println(dirFile.getName());
				//获取此目录下的所有文件名与目录名
				String[] fileList = dirFile.list();
				int currentDepth=depth+1;
				for (int i = 0; i < fileList.length; i++) {
					//遍历文件目录
					String string = fileList[i];
					//File("documentName","fileName")是File的另一个构造器
					File file = new File(dirFile.getPath(),string);
					String name = file.getName();
					//如果是一个目录，搜索深度depth++，输出目录名后，进行递归
					if (file.isDirectory()) {
						//递归
						findFiles(file.getCanonicalPath(),currentDepth,files);
					}else if(file.isFile()&&!file.isHidden()){
						//如果是文件，则直接输出文件名
						for (int j = 0; j < currentDepth; j++) {
							System.out.print("   ");
						}
						System.out.print("|--");
						System.out.println(name);
						files.add(file.getAbsolutePath());
					}
				}
			}

		 
		 public static void main(String[] args) {
			List<String> files=new ArrayList<String>();
			
			try {
				FileUtils.findFiles("/Volumes/Entertainme/照片/亦诗亦画/京城凤凰画院活动照片 整理 6.12/", 1, files);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(ObjToStringUtil.objToString(files));
			for(String fileName:files){
				String tempName=FileUtils.getFileName(fileName);
				System.out.println("tempName="+tempName);
				ImageThumbnailatorUtil.CompressPicBySize(fileName, "/Volumes/Entertainme/照片/亦诗亦画/deala/"+tempName+"_deal.jpg", 1680, true,null, Positions.BOTTOM_RIGHT);
			}
			
			
		}
		 
	  
}
