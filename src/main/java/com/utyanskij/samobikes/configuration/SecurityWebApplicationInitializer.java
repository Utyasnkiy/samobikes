package com.utyanskij.samobikes.configuration;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.stereotype.Component;

//инициализатор безопасности веб-приложения в Spring Security
@Component
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
    //
}

//Этот класс практически пустой, потому что всю основную настройку безопасности вы уже выполнили в SecurityConfig. Однако, благодаря расширению AbstractSecurityWebApplicationInitializer,
//Spring Security будет автоматически настроен для веб-приложения при загрузке контекста приложения.