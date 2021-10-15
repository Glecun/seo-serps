package com.glecun.seoserps

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource(value=["classpath:local.properties"], ignoreResourceNotFound=true)
class SeoSerpsConfiguration
