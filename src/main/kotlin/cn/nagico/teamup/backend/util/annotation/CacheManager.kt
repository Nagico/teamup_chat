package cn.nagico.teamup.backend.util.annotation

import org.springframework.stereotype.Service

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Service
annotation class CacheManager