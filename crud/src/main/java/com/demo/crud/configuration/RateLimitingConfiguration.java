package com.demo.crud.configuration;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

@Configuration
public class RateLimitingConfiguration {

	@Bean
	public Bucket rateLimitingBucket() {
		Bandwidth limit = Bandwidth.classic(10, Refill.intervally(3, Duration.ofMinutes(1)));
		Bucket bucket = Bucket.builder().addLimit(limit).build();
		return bucket;
	}

}
