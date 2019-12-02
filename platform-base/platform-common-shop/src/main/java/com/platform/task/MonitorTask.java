package com.platform.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.platform.facade.PaymentPluginFacade;

@Component("monitorTask")
public class MonitorTask {

    @Autowired
    private PaymentPluginFacade paymentPluginFacade;

    public void everyDayMonitor() {
        paymentPluginFacade.monitorUser();
    }

}
