package com.ibm.ssi.controller.company.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ibm.ssi.controller.company.util.validation.CustomPattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@org.springframework.data.mongodb.core.mapping.Document(collection = "emailTemplate")
public class EmailTemplate extends AbstractAuditingEntity {

    @Id
    private String id = "email-template";

    @Size(max = 200)
    @Field("subject")
    @NotNull
    @CustomPattern
    private String subject;

    @Size(max = 10000)
    @Field("bodyBeforeQR")
    @NotNull
    private String bodyBeforeQR;

    @Size(max = 10000)
    @Field("bodyAfterQR")
    @NotNull
    private String bodyAfterQR;

    public EmailTemplate() { // needed by Jackson
    }

    public EmailTemplate(@Size(max = 200) String subject,
            @Size(max = 10000) String bodyBeforeQR,
            @Size(max = 10000) String bodyAfterQR) {
        this.subject = subject;
        this.bodyBeforeQR = bodyBeforeQR;
        this.bodyAfterQR = bodyAfterQR;
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
