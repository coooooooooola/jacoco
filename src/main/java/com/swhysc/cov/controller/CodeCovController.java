package com.swhysc.cov.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CodeCovController {

    @GetMapping("/test")
    public JSONObject collectCov() {
        return new JSONObject();
    }
}
