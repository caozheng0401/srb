package com.atguigu.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.atguigu.easyexcel.dto.ExcelStudentDTO;
import com.atguigu.easyexcel.listener.ExcelStudentDTOListener;
import org.junit.Test;

/**
 * @author cz
 * @create 2021/4/22 15:00
 */
public class ExcleReadTest {

        @Test
        public void simpleReadXlsx() {
            String fileName = "D:\\BaiduNetdiskDownload\\金融项目--尚融宝\\课件\\全栈互联网金融-尚融宝\\第三部分-SpringCloud基础设施\\simpleWrite.xlsx";
            // 这里默认读取第一个sheet
            EasyExcel.read(fileName, ExcelStudentDTO.class, new ExcelStudentDTOListener()).sheet().doRead();
        }

}
