package com.platform.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.platform.service.SysConfigService;
import com.platform.utils.R;

/**
 * 系统首页Controller
 *
 */
@RestController
@RequestMapping("/sys/main")
public class SysMainController extends AbstractController {
    @Autowired
    private SysConfigService sysConfigService;


    /**
     * 
     *
     * @return R
     */
    @RequestMapping("/initData")
    public R info() {
    	Map<String,Object> resultMap= new HashMap<String,Object>();
    	resultMap.put("loginUser", getUser().getUsername());
        return R.ok().put("result", resultMap);
    }

}
