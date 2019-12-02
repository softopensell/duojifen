package com.platform.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.entity.PaymentLogEntity;
import com.platform.service.PaymentLogService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

/**
 * Controller
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 22:33:28
 */
@RestController
@RequestMapping("paymentlog")
public class PaymentLogController {
    @Autowired
    private PaymentLogService paymentLogService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("paymentlog:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<PaymentLogEntity> paymentLogList = paymentLogService.queryList(query);
        int total = paymentLogService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(paymentLogList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("paymentlog:info")
    public R info(@PathVariable("id") Long id) {
        PaymentLogEntity paymentLog = paymentLogService.queryObject(id);

        return R.ok().put("paymentLog", paymentLog);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("paymentlog:save")
    public R save(@RequestBody PaymentLogEntity paymentLog) {
        paymentLogService.save(paymentLog);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("paymentlog:update")
    public R update(@RequestBody PaymentLogEntity paymentLog) {
        paymentLogService.update(paymentLog);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("paymentlog:delete")
    public R delete(@RequestBody Long[] ids) {
        paymentLogService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<PaymentLogEntity> list = paymentLogService.queryList(params);

        return R.ok().put("list", list);
    }
}
