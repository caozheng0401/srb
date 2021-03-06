package com.atguigu.srb.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.srb.core.listener.ExcelDictDTOListener;
import com.atguigu.srb.core.mapper.DictMapper;
import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.atguigu.srb.core.service.DictService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author CaoZheng
 * @since 2021-04-18
 */
@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Resource
    private RedisTemplate redisTemplate;

    //执行导入
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importData(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelDictDTO.class, new ExcelDictDTOListener(baseMapper)).sheet().doRead();
        log.info("importData finished");
    }

    /**
     * 执行导出
     *
     * @return
     */
    @Override
    public List<ExcelDictDTO> listDictData() {
        List<Dict> dicts = baseMapper.selectList(null);
        //创建exceldictdto
        ArrayList<ExcelDictDTO> excelDictDTOS = new ArrayList<>(dicts.size());
        dicts.forEach(dict -> {
            ExcelDictDTO excelDictDTO = new ExcelDictDTO();
            BeanUtils.copyProperties(dict, excelDictDTO);
            excelDictDTOS.add(excelDictDTO);
        });
        return excelDictDTOS;


    }

    /**
     * 根据parentid查询
     *
     * @param parentId
     * @return
     */
    @Override
    public List<Dict> listByParentId(Long parentId) {
        //首先查询redis中是否存在数据列表
        List<Dict> dicts = null;
        try {
            log.info("将redis读取数据");
            List<Dict> dictList = (List<Dict>) redisTemplate.opsForValue().get("srb:core:dictList:" + parentId);
            if (dictList != null) {
                return dictList;
            }
            //如果存在则从redis中直接返回数据列表
        } catch (Exception e) {
            log.error("redis服务器异常" + ExceptionUtils.getStackTrace(e));
        }

        log.info("从数据库中读取数据");
        //不存在查询数据库
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id", parentId);
        dicts = baseMapper.selectList(dictQueryWrapper);
        dicts.forEach(dict -> {
            //判断当前结点是否有子节点
            boolean b = this.hasChildren(dict.getId());
            dict.setHasChildren(b);
        });


        try {
            //将数据存到redis
            log.info("将值存入redis");
            redisTemplate.opsForValue().set("srb:core:dictList:" + parentId, dicts, 5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("redis服务器异常" + ExceptionUtils.getStackTrace(e));
        }
        //返回数据
        return dicts;
    }

    /**
     * 判断当前id是有子节点
     *
     * @param id
     * @return
     */
    private boolean hasChildren(Long id) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id", id);
        Integer integer = baseMapper.selectCount(dictQueryWrapper);
        if (integer.intValue() > 0) {
            return true;
        }
        return false;
    }
}
