package com.doctorms.Configuration.Interceptor;


import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
@Slf4j
public class FeignAuthInterceptor {

	@Bean
	public RequestInterceptor requestInterceptor() {
		return template -> {
			log.info("Entered in Doctor Interceptor");
			ServletRequestAttributes attributes =
					(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

			if (attributes == null) {
				log.warn("FeignAuthInterceptor: No ServletRequestAttributes found");
				return;
			}

			String authHeader =
					attributes.getRequest().getHeader("Authorization");

			if (authHeader == null) {
				log.warn("FeignAuthInterceptor: Authorization header is NULL");
				return;
			}

			log.info("FeignAuthInterceptor: Forwarding Authorization header -> {}",
					authHeader.substring(0, Math.min(25, authHeader.length())) + "...");

			template.header("Authorization", authHeader);
		};
	}
}
