package com.atguigu.srb.sms.service;

import java.util.Map;

/**
 * @author cz
 * @create 2021/4/23 16:26
 */
public interface SmsService {
    void send(String mobile, String templateCode, Map<String,Object> param);
}
