package org.example.twosixtagram.domain.common.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.example.twosixtagram.domain.common.filter.LoginFilter;
import org.example.twosixtagram.domain.user.repository.UserRepository;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//수동으로 빈 등록
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final UserRepository userRepository;

    @Bean
    public FilterRegistrationBean loginFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        //Filter 등록
        filterRegistrationBean.setFilter(new LoginFilter(userRepository));
        //filter 순서 설정
        filterRegistrationBean.setOrder(1);
        //전체 URL에 filter 적용
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}
