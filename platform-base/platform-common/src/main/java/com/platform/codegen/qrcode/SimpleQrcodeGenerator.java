/**
 * Copyright (c) 2016-~, Bosco.Liao (bosco_liao@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.platform.codegen.qrcode;


import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.Version;
import com.platform.codegen.AbstractGenerator;
import com.platform.codegen.Codectx;
import com.platform.codegen.QrcodeGenerateException;
import com.platform.codegen.qrcode.Qrcode.Logo;
import com.platform.codegen.qrcode.QrcodeWriter.QRCodeBitMatrix;
import com.platform.codegen.utils.FileUtils;
import com.platform.codegen.utils.HttpUtils;
import com.platform.codegen.utils.UrlUtils;

public class SimpleQrcodeGenerator extends AbstractGenerator implements QrcodeGenerator {

	private static final ThreadQrcode QRCODE = new ThreadQrcode();

	private final QrcodeConfig qrcodeConfig;

	public SimpleQrcodeGenerator() {
		this(new QrcodeConfig());
	}

	public SimpleQrcodeGenerator(QrcodeConfig qrcodeConfig) {
		super();
		this.qrcodeConfig = qrcodeConfig;
	}

	@Override
	public QrcodeGenerator setLogo(String path, boolean remote) {
		QRCODE.setLogo(path, remote);
		return this;
	}
	@Override
	public QrcodeGenerator setBackgroundImage(String backgroundImage) {
		qrcodeConfig.setBackgroundImage(backgroundImage);
		return this;
	}

	@Override
	public QrcodeConfig getQrcodeConfig() {
		return this.qrcodeConfig;
	}

	@Override
	public QrcodeGenerator generate(String content) {
		QRCODE.setImage(generateQrcode(getQrcodeConfig(), QRCODE.getLogo(), content));
		return this;
	}

	@Override
	public QrcodeGenerator generate(String content, String logoPath) {
		return (QrcodeGenerator) setLogo(logoPath).generate(content);
	}

	@Override
	public BufferedImage getImage(boolean clear) {
		try {
			return QRCODE.getImage();
		} finally {
			if (clear) {
				clear();
			}
		}
	}

	@Override
	public void clear() {
		QRCODE.remove();
	}

	@Override
	public boolean toFile(String pathname) throws IOException {
		try {
			return ImageIO.write(QRCODE.getImage(), Codectx.IMAGE_TYPE, new File(pathname));
		} finally {
			clear();
		}
	}

	@Override
	public boolean toStream(OutputStream output) throws IOException {
		try {
			return ImageIO.write(QRCODE.getImage(), Codectx.IMAGE_TYPE, output);
		} finally {
			clear();
		}
	}

	/**
	 * Generate implement.
	 * @param config {@link QrcodeConfig}
	 * @param logo {@link Logo}
	 * @param content qrcode content
	 * @return BufferedImage instance
	 */
	private static BufferedImage generateQrcode(QrcodeConfig config, Logo logo, final String content) {
		try {
			QRCodeBitMatrix m = new QrcodeWriter().encodeX(content, BarcodeFormat.QR_CODE, config.getWidth(),
					config.getHeight(), config.getHints());

			BufferedImage image = toBufferedImage(m, config);

			/**
			 * render image margin
			 */
			image = setRadius(image, config.getBorderRadius(), config.getBorderSize(), config.getBorderColor(),
					config.getBorderStyle(), config.getBorderDashGranularity(), config.getMargin());
			/**
			 * insert logo in the middle of the image
			 */
			if (logo != null && logo.getPath() != null && logo.getPath().length() > 0) {
				byte[] bytes;
				if (logo.isRemote()) {
					bytes = HttpUtils.readStreamToByteArray(logo.getPath());
				} else {
					bytes = FileUtils.readFileToByteArray(new File(logo.getPath()));
				}
				if (bytes.length > 0) {
					addLogo(image, bytes, config);
				}
			}
			return image;

		} catch (Exception e) {
			throw new QrcodeGenerateException(e);
		}
	}
	
	public static void addLogo(final BufferedImage image, final byte[] logo, final QrcodeConfig config)
			throws IOException {
		ByteArrayInputStream input = new ByteArrayInputStream(logo);
		
		BufferedImage srcImage = image, logoImage = null;
		logoImage = ImageIO.read(input);
		if (logoImage == null) {
			return;
		}
		//3、画笔对象 
		Graphics2D graphics = srcImage.createGraphics();
		
		/**
		 * get logo width & height
		 */
		final int ratio = config.getLogoConfig().getRatio();
		int ratioWidthOfCodeImage = srcImage.getWidth() / ratio;
		int ratioHeightOfCodeImage = srcImage.getHeight() / ratio;
		int width = logoImage.getWidth() > ratioWidthOfCodeImage ? ratioWidthOfCodeImage : logoImage.getWidth();
		int height = logoImage.getHeight() > ratioHeightOfCodeImage ? ratioHeightOfCodeImage : logoImage.getHeight();
		
		/**
		 * get logo panel position
		 */
		int padding = config.getLogoConfig().getPadding() * 2;
		int margin = config.getLogoConfig().getMargin() * 2;
		int w = width + padding + margin, h = height + padding + margin;
		int positionX = (srcImage.getWidth() - w) / 2;
		int positionY = (srcImage.getHeight() - h) / 2;
		/**
		 * render panel, 1px offset is added to ensure center position
		 */
		Shape shape = new RoundRectangle2D.Float(positionX, positionY, (float) (w + 1), (float) (h + 1),
				config.getLogoConfig().getPanelArcWidth(), config.getLogoConfig().getPanelArcHeight());
		if(!config.getLogoConfig().isBackgroundColorIsTransparent()){
			graphics.setColor(getColor(config.getLogoConfig().getPanelColor()));
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.fill(shape);
		}
		
		/**
		 * get logo position
		 */
		positionX += config.getLogoConfig().getMargin();
		positionY += config.getLogoConfig().getMargin();
		/**
		 * render logo background
		 */
		if(!config.getLogoConfig().isBackgroundColorIsTransparent()){
			
		shape = new RoundRectangle2D.Float(positionX, positionY, width + padding, height + padding,
				config.getLogoConfig().getArcWidth(), config.getLogoConfig().getArcHeight());
		graphics.setColor(getColor(config.getLogoConfig().getBackgroundColor()));
		graphics.fill(shape);
		
		}else{
			BufferedImage backgroundImage = null;
			if (config.getBackgroundImage() != null && config.getBackgroundImage().length() > 0) {
				byte[] bytes;
				if (UrlUtils.isUrl(config.getBackgroundImage())) {
					bytes = HttpUtils.readStreamToByteArray(config.getBackgroundImage());
				} else {
					bytes = FileUtils.readFileToByteArray(new File(config.getBackgroundImage()));
				}
				if (bytes.length > 0) {
					ByteArrayInputStream bgInput = new ByteArrayInputStream(bytes);
					backgroundImage = ImageIO.read(bgInput);
					backgroundImage=ImageThumbnailatorUtil.CropPicBySize(backgroundImage, image.getWidth(), image.getHeight());
				}
				
				if((backgroundImage.getWidth()-w)/2>=0&& (backgroundImage.getHeight()-h)/2>=0){
					BufferedImage logoBackgroundImage = ImageThumbnailatorUtil.CropPicBySize(backgroundImage, (backgroundImage.getWidth()-w)/2, (backgroundImage.getHeight()-h)/2, w, h);
					graphics.drawImage(logoBackgroundImage.getScaledInstance(w, h, Image.SCALE_SMOOTH), positionX-config.getLogoConfig().getMargin(), positionY-config.getLogoConfig().getMargin(), null);
				}
			}
			
		
		}
		/**
		 * draw logo
		 */
		positionX += config.getLogoConfig().getPadding();
		positionY += config.getLogoConfig().getPadding();
		
		

        // 透明度  
        float alpha = 0.5f;      
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,alpha));  
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));   

		graphics.drawImage(logoImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), positionX, positionY, null);
		/**
		 * border
		 */
		graphics.setStroke(new BasicStroke(config.getLogoConfig().getBorderSize()));
		graphics.setColor(getColor(config.getLogoConfig().getBorderColor()));
		/**
		 * anti-aliasing
		 */
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.draw(shape);

		/**
		 * flush
		 */
		graphics.dispose();
		logoImage.flush();
		srcImage.flush();
	}
	
	private static BufferedImage toBufferedImage(final QRCodeBitMatrix matrix, final QrcodeConfig config) throws IOException {

		BitMatrix bitMatrix = matrix.getBitMatrix();

		Version version = matrix.getQrcode().getVersion();

		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();

		/**
		 * Formula for calculating number of modules on each side: 
		 * (Version - 1) * 4 + 21.
		 */
		int modules = (version.getVersionNumber() - 1) * 4 + 21;
		int[] topLeftOnBit = bitMatrix.getTopLeftOnBit();

		QreyesPosition position = new QreyesPosition(modules, topLeftOnBit);

		int moduleHeight = position.getModuleHeight(height);
		int moduleWidth = position.getModuleWidth(width);

		/**
		 * Calculating codeEyes position.
		 */
		int leftStartX = topLeftOnBit[0] + moduleWidth * QreyesRenderStrategy.POINT_BORDER.getStart();
		int leftEndX = topLeftOnBit[0] + moduleWidth * QreyesRenderStrategy.POINT_BORDER.getEnd();
		int topStartY = topLeftOnBit[1] + moduleHeight * QreyesRenderStrategy.POINT_BORDER.getStart();
		int topEndY = topLeftOnBit[1] + moduleHeight * QreyesRenderStrategy.POINT_BORDER.getEnd();
		int rightStartX = topLeftOnBit[0] + moduleWidth * (modules - QreyesRenderStrategy.POINT_BORDER.getEnd());
		int rightEndX = width - topLeftOnBit[0] - moduleWidth * QreyesRenderStrategy.POINT_BORDER.getStart();
		int bottomStartY = height - topLeftOnBit[1] - moduleHeight * QreyesRenderStrategy.POINT_BORDER.getEnd();
		int bottomEndY = height - topLeftOnBit[1] - moduleHeight * QreyesRenderStrategy.POINT_BORDER.getStart();

		/**
		 * Build image.
		 */
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// custom color.
		int masterColor = getColor(config.getMasterColor()).getRGB();
		int slaveColor = getColor(config.getSlaveColor()).getRGB();
		BufferedImage backgroundImage = null;
		
		
		/**
		 * insert bg in the middle of the image
		 */
		if (config.getBackgroundImage() != null && config.getBackgroundImage().length() > 0) {
			byte[] bytes;
			if (UrlUtils.isUrl(config.getBackgroundImage())) {
				bytes = HttpUtils.readStreamToByteArray(config.getBackgroundImage());
			} else {
				bytes = FileUtils.readFileToByteArray(new File(config.getBackgroundImage()));
			}
			if (bytes.length > 0) {
				ByteArrayInputStream input = new ByteArrayInputStream(bytes);
				backgroundImage = ImageIO.read(input);
				backgroundImage=ImageThumbnailatorUtil.CropPicBySize(backgroundImage, width, height);
			}
			
		}

         
		for (int y = 0; y < height; y++) {
			
			for (int x = 0; x < width; x++) {
				// top left
				if (x >= leftStartX && x < leftEndX && y >= topStartY && y < topEndY) {} 
				// top right
				else if (x >= rightStartX && x < rightEndX && y >= topStartY && y < topEndY) {} 
				// bottom left
				else if (x >= leftStartX && x < leftEndX && y >= bottomStartY && y < bottomEndY) {} 
				// non codeEyes region
				else {
			          if(backgroundImage!=null){
			        	  
			        	  int orgW=backgroundImage.getWidth();
				          int orgH=backgroundImage.getHeight();
				          int WF=(width-backgroundImage.getWidth())/2;
				          int HF=(height-backgroundImage.getHeight())/2;
				          int colorX=x-WF;
				          int colorY=y-HF;
				          
				          if(colorX>=0&&colorX<orgW&&colorY>=0&&colorY<orgH){
				        	  slaveColor= backgroundImage.getRGB(colorX, colorY);
				          }
			          }
			          
			          image.setRGB(x, y, bitMatrix.get(x, y) ? masterColor : slaveColor);
					}
			}
		}

		position.setPosition(leftStartX, leftEndX, topStartY, topEndY, rightStartX, rightEndX, bottomStartY,
				bottomEndY);
		Color border = getColor(config.getCodeEyesBorderColor());
		Color point = getColor(config.getCodeEyesPointColor());
		QreyesFormat format = config.getCodeEyesFormat();
		new MultiFormatQreyesRenderer().render(image, format, position, new Color(slaveColor), border, point);

		return image;
	}

}
