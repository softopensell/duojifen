package com.platform.codegen;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.platform.codegen.qrcode.QrcodeGenerator;
import com.platform.codegen.qrcode.QreyesFormat;
import com.platform.codegen.qrcode.SimpleQrcodeGenerator;
import com.platform.codegen.utils.IOUtils;




public class TestQrGen {

	private static final String content = "https://baike.baidu.com/item/%E5%97%B7%E5%A4%A7%E5%96%B5/19817560?fr=aladdin";
	public QrcodeGenerator generator = new SimpleQrcodeGenerator();
	private String localLogoPath="/Users/softopensell/Documents/coinWorkSpace/coins/yaotuofu-common/tuoma_logo.png";
	
	public static void main(String[] args) {
		TestQrGen testQrGen=new TestQrGen();
		System.out.println(testQrGen.localLogoPath);
		try {
			testQrGen.testDefault();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void testDefault() throws IOException {
//		generator.generate(content).toFile("AodaCat_default.png");
		testLocalLogo();
//		testRemoteLogo();
		testCustomConfig();
		testCustomCodeEyes();
	}

	/**

	 * 添加本地logo

	 * @throws IOException

	 */
	public void testLocalLogo() throws IOException {
	  String backgroundImage="/Users/softopensell/Documents/coinWorkSpace/coins/yaotuofu-common/a.jpg";
		generator.getQrcodeConfig()
		.setBorderSize(20)
		.setPadding(10)
		.setMasterColor("#778899")
		.setLogoBorderColor("#00b38a")
		.setCodeEyesPointColor("#00b38a")
		.setCodeEyesBorderColor("#00b38a")
		.setBorderColor("#00b38a")
		.setCodeEyesFormat(QreyesFormat.DR2_BORDER_R_POINT)
		.setBackgroundImage(backgroundImage)
		.setLogoBackgroundColor("#778899")
		.setLogoBackgroundColorIsTransparent(false);
//		generator.getQrcodeConfig().setCodeEyesFormat(QreyesFormat.DR2_BORDER_R_POINT);
		boolean success = generator.setLogo(this.localLogoPath).generate(content).toFile("AodaCat_local_logo.png");
		System.out.println(success);
		 generator.setLogo(this.localLogoPath).generate(content).getImage();
		generator.getQrcodeConfig().setBackgroundImage(null);
	}

	/**

	 * 添加在线logo

	 * @throws IOException

	 */
	public void testRemoteLogo() throws IOException {
		generator.setRemoteLogo("http://www.demlution.com/site_media/media/photos/2014/11/06/3JmYoueyyxS4q4FcxcavgJ.jpg");
		System.out.println(generator.generate("https://www.apple.com/cn/").toFile("Apple_remote_logo.png"));
		
	}

	/**

	 * 自定义二维码配置

	 * @throws IOException

	 */
	public void testCustomConfig() throws IOException {
		generator.getQrcodeConfig()
			.setBorderSize(2)
			.setPadding(10)
			.setMasterColor("#00BFFF")
			.setLogoBorderColor("#B0C4DE");
		
		System.out.println(generator.setLogo(this.localLogoPath).generate(content).toFile("AodaCat_custom.png"));
	}
	
	/**

	 * 自定义二维码码眼颜色

	 * @throws IOException

	 */
	public void testCustomCodeEyes() throws IOException {
		generator.getQrcodeConfig()
			.setMasterColor("#778899")
			.setLogoBorderColor("#778899")
			.setCodeEyesPointColor("#BC8F8F")
			.setCodeEyesFormat(QreyesFormat.DR2_BORDER_R_POINT);
		System.out.println(generator.setLogo(this.localLogoPath).generate(content).toFile("AodaCat_custom_eyes.png"));
		
		generator.getQrcodeConfig()
		.setCodeEyesFormat(QreyesFormat.C_BORDER_C_POINT);
		
		System.out.println(generator.setLogo(this.localLogoPath).generate(content).toFile("AodaCat_custom_eyes_cc.png"));
		generator.getQrcodeConfig().setCodeEyesFormat(QreyesFormat.C_BORDER_R_POINT);
		System.out.println(generator.setLogo(this.localLogoPath).generate(content).toFile("AodaCat_custom_eyes_cr.png"));
		generator.getQrcodeConfig().setCodeEyesFormat(QreyesFormat.DR2_BORDER_C_POINT);
		System.out.println(generator.setLogo(this.localLogoPath).generate(content).toFile("AodaCat_custom_eyes_dr.png"));
		generator.getQrcodeConfig().setCodeEyesFormat(QreyesFormat.DR2_BORDER_R_POINT);
		System.out.println(generator.setLogo(this.localLogoPath).generate(content).toFile("AodaCat_custom_eyes_drr.png"));
	generator.getQrcodeConfig().setCodeEyesFormat(QreyesFormat.R2_BORDER_C_POINT);
	System.out.println(generator.setLogo(this.localLogoPath).generate(content).toFile("AodaCat_custom_eyes_r2.png"));
	generator.getQrcodeConfig().setCodeEyesFormat(QreyesFormat.R2_BORDER_R_POINT);
	System.out.println(generator.setLogo(this.localLogoPath).generate(content).toFile("AodaCat_custom_eyes_r2r.png"));
	generator.getQrcodeConfig().setCodeEyesFormat(QreyesFormat.R_BORDER_C_POINT);
	System.out.println(generator.setLogo(this.localLogoPath).generate(content).toFile("AodaCat_custom_eyes_rc.png"));
	generator.getQrcodeConfig().setCodeEyesFormat(QreyesFormat.R_BORDER_R_POINT);
	System.out.println(generator.setLogo(this.localLogoPath).generate(content).toFile("AodaCat_custom_eyes_rr.png"));
}
	
/**

	 * 写入输出流

	 * @throws IOException

	 */
	public void testWriteToStream() throws IOException {
		OutputStream ous = null;
		try {
			ous = new FileOutputStream("Qrcode_out.png");
			System.out.println(generator.generate(content).toStream(ous));
		} finally {
			IOUtils.closeQuietly(ous);
		}
	}
	
	public void testGetImage() throws IOException {
		BufferedImage image = generator.generate(content).getImage();
		ImageIO.write(image, "png", new File("Qrcode_out.png"));
	}

}
  