package com.doctorms.Configuration.Interceptor;


import com.doctorms.JWT.JwtUtil;
import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class FeignAuthInterceptor {

	private final JwtUtil jwtUtil;

	@Value("${spring.application.name}")
	private String serviceName;

	@Bean
	public RequestInterceptor requestInterceptor() {
		return template -> {

			log.info("Entered in Doctor Feign Interceptor");

			ServletRequestAttributes attributes =
					(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();


			if (attributes != null) {
				HttpServletRequest request = attributes.getRequest();
				String authHeader = request.getHeader("Authorization");

				if (authHeader != null && authHeader.startsWith("Bearer ")) {
					log.info("Feign: Forwarding USER token from DoctorMS");
					template.header("Authorization", authHeader);
					return;
				}
			}

			//  Doctor → Patient (SERVICE → SERVICE)
			String serviceToken =
					jwtUtil.generateServiceToServiceToken(serviceName);

			log.info("Feign: Using SERVICE token for {}", serviceName);
			template.header("Authorization", "Bearer " + serviceToken);
		};
	}
}