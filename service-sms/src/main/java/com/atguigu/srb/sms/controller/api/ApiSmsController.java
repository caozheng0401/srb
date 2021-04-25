package com.atguigu.srb.sms.controller.api;

import com.atguigu.common.exception.Assert;
import com.atguigu.common.result.R;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.common.util.RandomUtils;
import com.atguigu.common.util.RegexValidateUtils;
import com.atguigu.srb.sms.client.CoreUserInfoClient;
import com.atguigu.srb.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author cz
 * @create 2021/4/23 16:38
 */
@RestController
@RequestMapping("/api/sms")
@Api(tags = "短信管理")
//@CrossOrigin
@Slf4j
public class ApiSmsController {

    @Resource
    private SmsService smsService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private CoreUserInfoClient coreUserInfoClient;

    @ApiOperation("获取验证码")
    @GetMapping("/send/{mobile}")
    public R send(
            @ApiParam(value = "手机号", required = true)
            @PathVariable("mobile") String mobile){
        //校验手机号码不能为空
        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        //校验手机号码合法性
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile),ResponseEnum.MOBILE_ERROR);

        //判断手机号是否已经注册
        boolean result= coreUserInfoClient.checkMobile(mobile);
        Assert.isTrue(result == false,ResponseEnum.MOBILE_EXIST_ERROR);

        HashMap<String,Object> map = new HashMap<>();
        String fourBitRandom = RandomUtils.getFourBitRandom();
        map.put("code", fourBitRandom);
//        smsService.send(mobile, SmsProperties.TEMPLATE_CODE,map);
        //将验证码传入redis
        redisTemplate.opsForValue().set("srb:sms:code:"+mobile,fourBitRandom,5, TimeUnit.MINUTES);
        return R.ok().message("短信发送成功");

    }
}
