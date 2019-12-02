package com.platform.api;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.platform.annotation.IgnoreAuth;
import com.platform.entity.SysOssEntity;
import com.platform.oss.CloudStorageConfig;
import com.platform.oss.OSSFactory;
import com.platform.service.SysConfigService;
import com.platform.service.SysOssService;
import com.platform.util.ApiBaseAction;
import com.platform.utils.Constant;
import com.platform.utils.R;
import com.platform.utils.RRException;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/common")
public class ApiCommonController extends ApiBaseAction {
	@Autowired
	private SysOssService sysOssService ;
	@Autowired
	private SysConfigService sysConfigService ;
	 private final static String KEY = Constant.CLOUD_STORAGE_CONFIG_KEY;
    @ApiOperation(value = "公共文件上传")
    @IgnoreAuth
    @RequestMapping("/upload")
    public R upload(@RequestParam("uploadFileObj") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new RRException("上传文件不能为空");
        }
        //上传文件
        String path = OSSFactory.build().upload(file);
        String url=path;
        if(path!=null&&path.indexOf("http")<0) {
        	//保存文件信息
        	CloudStorageConfig config = sysConfigService.getConfigObject(KEY, CloudStorageConfig.class);
        	url=config.getProxyServer()+path;
        }
        if(path.startsWith("/")) {
        	path=path.substring(1);
        }
        SysOssEntity ossEntity = new SysOssEntity();
        ossEntity.setUrl(url);
        ossEntity.setCreateDate(new Date());
        ossEntity.setOssFromType(1000);
        sysOssService.save(ossEntity);
        String fileName = file.getOriginalFilename();
        String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String fileUrl=url;
        String fileSize=file.getSize()+"";
        R r = new R();
        r.put("url", url);
        r.put("link", url);
        r.put("fileName", fileName);
        r.put("extensionName", extensionName);
        r.put("filePath", path);
        r.put("fileUrl", fileUrl);
        r.put("fileSize", fileSize);
        return r;
    }
}
