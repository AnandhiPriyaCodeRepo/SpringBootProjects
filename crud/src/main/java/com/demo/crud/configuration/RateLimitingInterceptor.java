package com.demo.crud.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitingInterceptor implements HandlerInterceptor {

	@Autowired
	private Bucket rateLimitingBucket;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		try {
			ConsumptionProbe probe = rateLimitingBucket.tryConsumeAndReturnRemaining(1);
			if (probe.isConsumed()) {
				return true;
			} else {
				response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
				response.getWriter().write("You have exhausted the API request Quota");
				return false;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return false;
	}

}
