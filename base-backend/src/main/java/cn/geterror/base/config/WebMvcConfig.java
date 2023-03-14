package cn.geterror.base.config;

import cn.geterror.base.exception.SecretNotFoundException;
import cn.geterror.base.interceptor.BAInterceptor;
import cn.geterror.base.util.BAUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    public static void main(String[] args) {
        BAUtil baUtil = new BAUtil() {
            @Override
            public String getSecretByClientId(String clientId) throws SecretNotFoundException {
                if("test".equals(clientId)){
                    return "testKey";
                }
                throw new SecretNotFoundException();
            }
        };
        String authorization = baUtil.getAuthorization("/demo/alive", "GET", "test", "testKey");
        return;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        BAUtil baUtil = new BAUtil() {
            @Override
            public String getSecretByClientId(String clientId) throws SecretNotFoundException {
                if("test".equals(clientId)){
                    return "testKey";
                }
                throw new SecretNotFoundException();
            }
        };
        registry.addInterceptor(new BAInterceptor(baUtil,100));
    }
}
