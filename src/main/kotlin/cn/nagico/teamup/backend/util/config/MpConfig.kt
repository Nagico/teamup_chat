package cn.nagico.teamup.backend.util.config

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MpConfig {
    @Bean
    fun mpInterceptor(): MybatisPlusInterceptor {
        val mpInterceptor = MybatisPlusInterceptor()
        // 分页排序拦截器
        mpInterceptor.addInnerInterceptor(PaginationInnerInterceptor())
        return mpInterceptor
    }
}