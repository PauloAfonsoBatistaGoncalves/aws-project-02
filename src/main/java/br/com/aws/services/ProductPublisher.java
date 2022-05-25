package br.com.aws.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.Topic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.aws.config.local.SnsCreate;
import br.com.aws.enums.EventType;
import br.com.aws.model.Envelope;
import br.com.aws.model.Product;
import br.com.aws.model.ProductEvent;

@Service
public class ProductPublisher {
	private static final Logger LOG = LoggerFactory.getLogger(ProductPublisher.class);

	private AmazonSNS snsClient;
	private Topic productEventsTopic;
	private ObjectMapper objectMapper;

	public ProductPublisher(AmazonSNS snsClient,
			@Qualifier("productEventsTopic") Topic productEventsTopic,
			ObjectMapper objectMapper) {
		this.snsClient = snsClient;
		this.productEventsTopic = productEventsTopic;
		this.objectMapper = objectMapper;
	}

	public void publishProductEvent(Product product, EventType eventType, String username) {
		ProductEvent productEvent = new ProductEvent();
		productEvent.setCode(product.getCode());
		productEvent.setUsername(product.getUsername());
		productEvent.setId(product.getId());

		Envelope envelope = new Envelope();
		envelope.setEventType(eventType);
		try {
			envelope.setData(objectMapper.writeValueAsString(productEvent));
			
			PublishResult publishResult = snsClient.publish(
					productEventsTopic.getTopicArn(),
					objectMapper.writeValueAsString(envelope));
			
			 LOG.info("MessageId: {}", publishResult.getMessageId());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}



	}
}