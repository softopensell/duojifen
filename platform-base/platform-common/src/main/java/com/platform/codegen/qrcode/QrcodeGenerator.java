
package com.platform.codegen.qrcode;

import java.awt.image.BufferedImage;

import com.platform.codegen.Generator;

public interface QrcodeGenerator extends Generator {

	QrcodeConfig getQrcodeConfig();

	QrcodeGenerator generate(String content, String logoPath);

	QrcodeGenerator setLogo(String path, boolean remote);
	QrcodeGenerator setBackgroundImage(String path);

	BufferedImage getImage(boolean clear);

	default QrcodeGenerator setLogo(String path) {
		return setLogo(path, false);
	}

	default QrcodeGenerator setRemoteLogo(String path) {
		return setLogo(path, true);
	}
	@Override
	default BufferedImage getImage() {
		return getImage(true);
	}

}
