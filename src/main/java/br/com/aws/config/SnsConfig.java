package br.com.aws.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.Topic;

import br.com.aws.config.local.SnsCreate;

@Configuration
@Profile("!local")
public class SnsConfig {

	private static final Logger LOG = LoggerFactory.getLogger(SnsConfig.class);

	
	@Value("${aws.region}")
	private String awsRegion;
	
	@Value("${aws.sns.topic.product.events.arn}")
	private String productEventsTopic;
	
	
	@Bean
	public AmazonSNS snsClient() {
		return AmazonSNSClientBuilder
				.standard()
				.withRegion(awsRegion)
				.withCredentials(new DefaultAWSCredentialsProviderChain())
				.build();
		
		
	}
	
	@Bean(name = "productEventsTopic")
	public Topic snsProductEventsTopic() {
		return new Topic().withTopicArn(productEventsTopic);
	}
}
