package com.ruoyi.file.service;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.api.domain.SysFile;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 * 
 * @author ruoyi
 */
public interface ISysFileService
{
    /**
     * 文件上传接口
     * 
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
     String uploadFile(MultipartFile file) throws Exception;


     void deleteFile(String filePath);
}
