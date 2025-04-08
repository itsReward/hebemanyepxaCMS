package com.hebe.hebemanyepxa.config.thymeleaf

import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.thymeleaf.context.IExpressionContext
import org.thymeleaf.context.IWebContext
import org.thymeleaf.dialect.AbstractDialect
import org.thymeleaf.dialect.IExpressionObjectDialect
import org.thymeleaf.expression.IExpressionObjectFactory

@Configuration
class ThymeleafConfig {
    @Bean
    fun servletRequestDialect(): ServletRequestDialect {
        return ServletRequestDialect()
    }
}

class ServletRequestDialect : AbstractDialect("ServletRequestDialect"), IExpressionObjectDialect {

    override fun getExpressionObjectFactory(): IExpressionObjectFactory {
        return ServletRequestExpressionFactory()
    }

    private class ServletRequestExpressionFactory : IExpressionObjectFactory {

        override fun getAllExpressionObjectNames(): Set<String> {
            return setOf("request", "httpServletRequest")
        }

        override fun buildObject(context: IExpressionContext?, expressionObjectName: String?): Any? {
            if (context !is IWebContext) {
                return null
            }

            return when (expressionObjectName) {
                "request", "httpServletRequest" -> {
                    (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)?.request
                }
                else -> null
            }
        }

        override fun isCacheable(expressionObjectName: String?): Boolean {
            return false
        }
    }
}