package com.cupdata.commons.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

@Configuration
@ConditionalOnClass({JSON.class, FastJsonHttpMessageConverter.class})//1
@ConditionalOnProperty(//2
        name = {"spring.http.converters.preferred-json-mapper"},
        havingValue = "fastjson",
        matchIfMissing = true
)
public class FastJsonAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean({FastJsonHttpMessageConverter.class})//3
    public HttpMessageConverters fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

        FastJsonConfig fastJsonConfig = new FastJsonConfig();//4
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat,
                //SerializerFeature.WriteClassName,
                SerializerFeature.WriteMapNullValue
        );
        ValueFilter valueFilter = new ValueFilter() {//5
            //o 是class
            //s 是key值
            //o1 是value值
            @Override
            public Object process(Object o, String s, Object o1) {
                if (null == o1) {
                    o1 = "";
                }
                return o1;
            }
        };
        fastJsonConfig.setSerializeFilters(valueFilter);

        //TODO 2018/3/16
        //处理中文乱码问题 已经在配置文件中强制转换utf-8.
        //List<MediaType> fastMediaTypes = new ArrayList<>();
        //fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        //converter.setSupportedMediaTypes(fastMediaTypes);

        converter.setFastJsonConfig(fastJsonConfig);

        HttpMessageConverter<?> messageConverter = converter;

        return new HttpMessageConverters(messageConverter);
    }

}