package com.hisabKitab.springProject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
            .setAllowedOrigins(
                "https://hisab-kitab-business.netlify.app",
                "http://localhost:3000",
                "http://127.0.0.1:3000"
            )
            .withSockJS();
        
        // Add endpoint without SockJS for native WebSocket support
        registry.addEndpoint("/ws")
            .setAllowedOrigins(
                "https://hisab-kitab-business.netlify.app",
                "http://localhost:3000",
                "http://127.0.0.1:3000"
            );
    }
}
