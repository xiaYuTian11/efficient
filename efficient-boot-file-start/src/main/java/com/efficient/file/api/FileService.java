package com.efficient.file.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.efficient.file.model.dto.DownloadVO;
import com.efficient.file.model.entity.SysFileInfo;
import com.efficient.file.model.vo.FileVO;
import com.efficient.file.properties.FileProperties;
import com.efficient.file.util.PathUtil;
import com.efficient.common.result.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static com.efficient.file.constant.FileConstant.DOWNLOAD_LINE;
import static com.efficient.file.constant.FileConstant.POINT;

/**
 * @author TMW
 * @since 2022/4/26 9:28
 */
public interface FileService extends IService<SysFileInfo> {

    /**
     * 文件上传
     *
     * @param file   文件
     * @param unique 是否唯一
     * @return 返回文件信息
     */
    Result upload(MultipartFile file, boolean unique) throws Exception;

    /**
     * 获取服务器上的文件
     *
     * @return
     */
    FileVO getFile(DownloadVO downloadVO);

    FileProperties getProperties();

    /**
     * 创建文件路径
     *
     * @param fileName 文件名称
     * @param isCreate 是否生成文件
     * @return 文件路径
     * @throws Exception
     */
    default File createFile(String fileName, boolean isCreate) throws Exception {
        String basePath = getProperties().getTempPath() + DOWNLOAD_LINE;
        // 根据文件格式重新设置根路径
        if (fileName.lastIndexOf(POINT) != -1) {
            String suffix = fileName.substring(fileName.lastIndexOf(POINT));
            basePath += PathUtil.getFileUrlFolder(suffix);
        }

        File file = new File(basePath);
        final File parentFile = file.getParentFile();
        if (!file.exists()) {
            parentFile.mkdirs();
        }
        if (isCreate) {
            if (!file.exists()) {
                file.createNewFile();
            }
        }
        return file;
    }

    /**
     * 保存文件信息
     *
     * @param file 文件
     * @return 主键
     */
    String saveFileInfo(File file);

    /**
     * 根据文件ID删除文件
     *
     * @param fileId
     * @return
     */
    boolean delete(String fileId);
}
