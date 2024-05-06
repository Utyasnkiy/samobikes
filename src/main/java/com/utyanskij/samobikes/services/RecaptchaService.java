package com.utyanskij.samobikes.services;

import com.utyanskij.samobikes.Utils.RecaptchaUtil;
import com.utyanskij.samobikes.configuration.CaptchaSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


//RecaptchaService представляет сервис для проверки reCAPTCHA, используя сервис Google reCAPTCHA API.
// Он содержит метод verifyRecaptcha, который отправляет запрос на проверку reCAPTCHA на указанный URL
// и анализирует ответ для подтверждения или отклонения успешности проверки.
@Service
public class RecaptchaService {
    private static final String GOOGLE_RECAPTCHA_VERIFY_URL =
            "https://www.google.com/recaptcha/api/siteverify";

    private CaptchaSettings captchaSettings;

    @Autowired
    public void setCaptchaSettings(CaptchaSettings captchaSettings) {
        this.captchaSettings = captchaSettings;
    }

    RestTemplateBuilder restTemplateBuilder;

    @Autowired
    public void setRestTemplateBuilder(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    //отправляет запрос на Google reCAPTCHA API для проверки reCAPTCHA.
    // Возвращает строку с сообщением об ошибке,
    // если проверка не удалась, или пустую строку, если проверка прошла успешно.
    public String verifyRecaptcha(String ip,
                                  String recaptchaResponse){
        Map<String, String> body = new HashMap<>();
        body.put("secret", captchaSettings.getSecret());
        body.put("response", recaptchaResponse);
        body.put("remoteip", ip);
        ResponseEntity<Map> recaptchaResponseEntity =
                restTemplateBuilder.build()
                        .postForEntity(GOOGLE_RECAPTCHA_VERIFY_URL+
                                        "?secret={secret}&response={response}&remoteip={remoteip}",
                                body, Map.class, body);

        Map<String, Object> responseBody =
                recaptchaResponseEntity.getBody();

        boolean recaptchaSucess = (Boolean)responseBody.get("success");
        if ( !recaptchaSucess) {
            List<String> errorCodes =
                    (List)responseBody.get("error-codes");

            return errorCodes.stream()
                    .map(RecaptchaUtil.RECAPTCHA_ERROR_CODE::get)
                    .collect(Collectors.joining(", "));
        }else {
            return "";
        }
    }
}
