package com.demo.crud.configuration;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RateLimitingInterceptorTest {

	@Mock
	private Bucket rateLimitingBucket;

	@InjectMocks
	private RateLimitingInterceptor interceptor;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testPreHandle_RequestLimitExceeded() throws IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		PrintWriter writer = mock(PrintWriter.class);
		when(response.getWriter()).thenReturn(writer);
		when(rateLimitingBucket.tryConsumeAndReturnRemaining(1)).thenReturn(ConsumptionProbe.rejected(0, 0, 0));
		assertFalse(interceptor.preHandle(request, response, new Object()));
		verify(response).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
		verify(writer).write("You have exhausted the API request Quota");
	}

	@Test
	void testPreHandle_RequestLimitNotExceeded() throws IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		Object handler = new Object();
		when(rateLimitingBucket.tryConsumeAndReturnRemaining(1)).thenReturn(ConsumptionProbe.consumed(0, 0));
		assertTrue(interceptor.preHandle(request, response, handler));
	}
}
