package com.cupdata.ihuyi.utils;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Component
public class ServerPort implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    private static String serverPort;

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        serverPort = String.valueOf(event.getEmbeddedServletContainer().getPort());
        System.out.println(serverPort);
  
    }

    public static String getPort() {
        return serverPort;
    }

}
