package com.atguigu.srb.oss.service;

import java.io.InputStream;

/**
 * @author cz
 * @create 2021/4/23 20:53
 */
public interface FileService {
    /**
     * 文件上传至阿里云
     */
    String upload(InputStream inputStream, String module, String fileName);

    void removeFile(String url);
}
