package cn.wendong.admin.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.wendong.admin.core.utils.FileUploadUtil;

/**
 * 文件上传配置
 * @author MB
 * @date 2018-12-02
 */
@Configuration
public class UploadFilePathConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(FileUploadUtil.getPathPattern()).addResourceLocations("file:" + FileUploadUtil.getUploadPath());
    }
}
