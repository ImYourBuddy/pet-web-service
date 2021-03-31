package com.imyourbuddy.petwebapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

//@Configuration
public class SecuritySocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(
            MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpDestMatchers("/socket/**").permitAll()
                .anyMessage().permitAll()  ;
    }
}
