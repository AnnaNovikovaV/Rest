package ru.kata.spring.boot_security.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OpenBrowserAfterStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${server.port:8080}")
    private String serverPort;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            Runtime rt = Runtime.getRuntime();
            rt.exec("cmd /c start http://localhost:" + serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
