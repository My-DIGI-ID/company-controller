package com.ibm.ssi.controller.company.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ibm.ssi.controller.company.domain.EmailTemplate;
import com.ibm.ssi.controller.company.util.validation.CustomPattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailTemplateDTO {

    private final Logger log = LoggerFactory.getLogger(EmailTemplateDTO.class);
    
    @Id
    private String id = "email-template";

    @Size(max = 200)
    @Field("subject")
    @NotNull
    @CustomPattern
    private String subject;

    @Size(max = 10000)
    @Field("bodyBeforeQR")
    private String bodyBeforeQR;

    @Size(max = 10000)
    @Field("bodyAfterQR")
    private String bodyAfterQR;

    public EmailTemplateDTO() { // Need by Jackson
    }

    public EmailTemplateDTO(EmailTemplate emailTemplate) {
        this.subject = emailTemplate.getSubject();
        this.bodyBeforeQR = emailTemplate.getBodyBeforeQR();
        this.bodyAfterQR = emailTemplate.getBodyAfterQR();
    }

    public EmailTemplateDTO(@Size(max = 200) @NotNull String subject,
            @Size(max = 10000) @NotNull String bodyBeforeQR,
            @Size(max = 10000) @NotNull String bodyAfterQR) {
        this.subject = subject;
        this.bodyBeforeQR = bodyBeforeQR;
        this.bodyAfterQR = bodyAfterQR;
    }

    public String getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBodyBeforeQR() {
        return bodyBeforeQR;
    }

    public void setBodyBeforeQR(String bodyBeforeQR) {
        this.bodyBeforeQR = bodyBeforeQR;
    }

    public String getBodyAfterQR() {
        return bodyAfterQR;
    }

    public void setBodyAfterQR(String bodyAfterQR) {
        this.bodyAfterQR = bodyAfterQR;
    }

    @Override
    public String toString() {
        return "EmailTemplateDTO [subject=" + subject
            + ", bodyBeforeQR=" + bodyBeforeQR
            + ", bodyAfterQR=" + bodyAfterQR
            + "]";
    }
}
