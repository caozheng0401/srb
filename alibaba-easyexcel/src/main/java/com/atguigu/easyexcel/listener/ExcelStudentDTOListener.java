package com.atguigu.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.easyexcel.dto.ExcelStudentDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author cz
 * @create 2021/4/22 15:01
 */
@Slf4j
public class ExcelStudentDTOListener extends AnalysisEventListener<ExcelStudentDTO> {
    @Override
    public void invoke(ExcelStudentDTO excelStudentDTO, AnalysisContext analysisContext) {

        log.info("读到一条数据"+excelStudentDTO);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("读入完成");
    }
}
