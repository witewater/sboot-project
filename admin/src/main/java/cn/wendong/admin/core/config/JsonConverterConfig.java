package cn.wendong.admin.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

import cn.wendong.admin.core.utils.JsonMapper;

/**
 * json数据返回转化配置
 * 主要解决对属性空值的问题
 * @author MB yangtdo@qq.com
 * @date 2019-02-06
 */
@Configuration
public class JsonConverterConfig {

	@Bean
    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        /**
         * JsonInclude.Include.ALWAYS          默认
		 * JsonInclude.Include.NON_DEFAULT     属性为默认值不序列化
		 * JsonInclude.Include.NON_EMPTY       属性为 空（""） 或者为 NULL 或者 [] 都不序列化
		 * JsonInclude.Include.NON_NULL        属性为NULL   不序列化
         */
        JsonMapper mapper = new JsonMapper(Include.NON_NULL);
        converter.setObjectMapper(mapper);
        return converter;
    }
	
}
