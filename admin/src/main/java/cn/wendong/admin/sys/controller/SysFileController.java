package cn.wendong.admin.sys.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.wendong.admin.common.page.RestResult;
import cn.wendong.admin.common.page.RestResultGenerator;
import cn.wendong.admin.core.enums.ResultEnum;
import cn.wendong.admin.core.exception.ResponseException;
import cn.wendong.admin.core.utils.FileUploadUtil;
import cn.wendong.admin.sys.entity.SysFile;
import cn.wendong.admin.sys.service.SysFileService;

/**
 * 
 * @author MB yangtdo@qq.com
 * @date 2018-12-07
 */
@Controller
public class SysFileController {

    @Autowired
    private SysFileService sysFileService;

    /**
     * 上传web格式图片
     */
    @PostMapping("/upload/image")
    @ResponseBody
    public RestResult uploadImage(@RequestParam("image") MultipartFile multipartFile) {

        // 创建File实体对象
        SysFile file = FileUploadUtil.getFile(multipartFile);
        file.setPath("/images" + file.getPath());

        return saveImage(multipartFile, file);
    }

    /**
     * 上传web格式头像
     */
    @PostMapping("/upload/picture")
    @ResponseBody
    public RestResult<SysFile> uploadPicture(@RequestParam("picture") MultipartFile multipartFile) {

        // 创建File实体对象
        SysFile file = FileUploadUtil.getFile(multipartFile);
        file.setPath("/picture" + file.getPath());

        return saveImage(multipartFile, file);
    }

    /**
     * 保存上传的web格式图片
     */
    private RestResult saveImage(MultipartFile multipartFile, SysFile file) {
        // 判断是否为支持的图片格式
        String[] types = {
                "image/gif",
                "image/jpg",
                "image/jpeg",
                "image/png"
        };
        if(!FileUploadUtil.isContentType(multipartFile, types)){
            throw new ResponseException(ResultEnum.NO_FILE_TYPE);
        }

        // 判断图片是否存在
        SysFile isFile = sysFileService.isFile(FileUploadUtil.getFileSHA1(multipartFile));
        if (isFile != null) {
            return RestResultGenerator.success(isFile);
        }

        try {
            FileUploadUtil.transferTo(multipartFile, file);
        } catch (IOException | NoSuchAlgorithmException e) {
            return RestResultGenerator.error("上传头像失败");
        }

        // 将文件信息保存到数据库中
        sysFileService.save(file);
        return RestResultGenerator.success(file);
    }

}
