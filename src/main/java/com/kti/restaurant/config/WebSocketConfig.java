package com.kti.restaurant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/socket-publisher");
        registry.setApplicationDestinationPrefixes("/socket-subscriber");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	registry.addEndpoint("/socket").setAllowedOrigins("http://a5e611d31cdb44040a154028599832cd-559647522.eu-central-1.elb.amazonaws.com").withSockJS();
        registry.addEndpoint("/socket").setAllowedOrigins("*");
        

    }
}
