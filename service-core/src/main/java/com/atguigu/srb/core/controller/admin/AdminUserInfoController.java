package com.atguigu.srb.core.controller.admin;


import com.atguigu.common.result.R;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.pojo.query.UserInfoQuery;
import com.atguigu.srb.core.service.UserInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author CaoZheng
 * @since 2021-04-18
 */
@Api(tags = "会员管理")
@Slf4j
//@CrossOrigin
@RestController
@RequestMapping("/admin/core/userInfo")
public class AdminUserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @ApiOperation("获取会员分页列表")
    @GetMapping("/list/{page}/{limit}")
    public R listPage(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable("page") Long page,
            @ApiParam(value = "当前记录数", required = true)
            @PathVariable("limit") Long limit,
            @ApiParam(value = "查询对象", required = false)
                    UserInfoQuery userInfoQuery
    ) {
        Page<UserInfo> pageParam = new Page<>(page,limit);
        IPage<UserInfo> pageModel = userInfoService.listPage(pageParam,userInfoQuery);
        return R.ok().data("pageModel",pageModel);


    }

    @ApiOperation("锁定与解锁")
    @PutMapping("/lock/{id}/{status}")
    public R lock(
            @ApiParam(value = "用户id", required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "用户status", required = true)
            @PathVariable("status") Integer status
    ){
        userInfoService.lock(id,status);
        return R.ok().message(status == 1?"解锁成功":"锁定成功");
    }



}
