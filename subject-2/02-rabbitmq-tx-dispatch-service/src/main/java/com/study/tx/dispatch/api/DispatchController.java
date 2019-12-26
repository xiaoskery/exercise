package com.study.tx.dispatch.api;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.tx.dispatch.service.DispatchService;

@RestController
@RequestMapping("/dispatch-api")
public class DispatchController {

    @Autowired
    private DispatchService dispatchService;

    @GetMapping("/dispatch")
    public String lock(String orderId) throws SQLException, InterruptedException {
        Thread.sleep(3000);
        dispatchService.dispatch(orderId);
        return "ok";
    }
}
