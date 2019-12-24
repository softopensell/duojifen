package com.platform.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;

import com.platform.entity.ActivityItemEntity;
import com.platform.entity.PaymentOutEntity;
import com.platform.service.PaymentOutService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

/**
 * Controller
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 10:07:38
 */
@RestController
@RequestMapping("/async/demo/test")
public class DemoTestController extends AbstractController{
	private List<DeferredResult<String>> deferredResultList = new ArrayList<>();
	@Autowired
	private PaymentOutService paymentOutService;
	 
	@ResponseBody
	@GetMapping("queryList")
	public List<PaymentOutEntity> queryList() {
		Map<String, Object> map = new HashMap<>();
		map.put("user_id", 1158);
		map.put("create_time", "2019-07-04 17:14:51");
		return paymentOutService.queryList(map);
	}
	
	@ResponseBody
	@GetMapping("allList")
	public R allList(@RequestParam Map<String, Object> params) {
		 //查询列表数据
        Query query = new Query(params);
        List<PaymentOutEntity> activityItemList = paymentOutService.queryList(query);
        int total = paymentOutService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(activityItemList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
		
	}
//    
    @ResponseBody
    @GetMapping("/hello")
    public WebAsyncTask<String> helloGet() throws Exception {
        System.out.println(Thread.currentThread().getName() + " 主线程start");

        Callable<String> callable = () -> {
            System.out.println(Thread.currentThread().getName() + " 子子子线程start");
            TimeUnit.SECONDS.sleep(5); //模拟处理业务逻辑，话费了5秒钟
            System.out.println(Thread.currentThread().getName() + " 子子子线程end");

            return "hello world";
        };
        // 采用WebAsyncTask 返回 这样可以处理超时和错误 同时也可以指定使用的Excutor名称
        WebAsyncTask<String> webAsyncTask = new WebAsyncTask<>(3000, callable);
        // 注意：onCompletion表示完成，不管你是否超时、是否抛出异常，这个函数都会执行的
        webAsyncTask.onCompletion(() -> System.out.println("程序[正常执行]完成的回调"));
        // 这两个返回的内容，最终都会放进response里面去===========
        webAsyncTask.onTimeout(() -> "程序[超时]的回调");
        System.out.println(Thread.currentThread().getName() + " 主线程end");
        return webAsyncTask;
    }
    
    @ResponseBody
    @GetMapping("/hello3")
    public WebAsyncTask<R> helloGet3() throws Exception {
        System.out.println(Thread.currentThread().getName() + " 主线程start");

        Callable<R> callable = () -> {
            System.out.println(Thread.currentThread().getName() + " 子子子线程start");
            TimeUnit.SECONDS.sleep(1); //模拟处理业务逻辑，话费了5秒钟
            System.out.println(Thread.currentThread().getName() + " 子子子线程end");
            return R.ok();
        };
        // 采用WebAsyncTask 返回 这样可以处理超时和错误 同时也可以指定使用的Excutor名称
        WebAsyncTask<R> webAsyncTask = new WebAsyncTask<>(3000, callable);
        // 注意：onCompletion表示完成，不管你是否超时、是否抛出异常，这个函数都会执行的
        webAsyncTask.onCompletion(() -> System.out.println("程序[正常执行]完成的回调"));
        // 这两个返回的内容，最终都会放进response里面去===========
        webAsyncTask.onTimeout(() -> R.error("数据问题"));
        System.out.println(Thread.currentThread().getName() + " 主线程end");
        
        return webAsyncTask;
    }
    
    @ResponseBody
    @GetMapping("/hello2")
    public DeferredResult<String> hello2() throws Exception {
        DeferredResult<String> deferredResult = new DeferredResult<>();
        //先存起来，等待触发
        deferredResultList.add(deferredResult);
        return deferredResult;
    }

    @ResponseBody
    @GetMapping("/setHelloToAll")
    public void helloSet() throws Exception {
        // 让所有hold住的请求给与响应
        deferredResultList.forEach(d -> d.setResult("say hello to all"));
    }
    
}
