package com.ibm.ssi.controller.company.service.mapper;

import com.ibm.ssi.controller.company.domain.EmailTemplate;
import com.ibm.ssi.controller.company.service.dto.EmailTemplateDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailTemplateMapper {

    @Autowired
    AddressMapper addressMapper;

    public EmailTemplateDTO emailTemplateToEmailTemplateDTO(EmailTemplate emailTemplate) {
        if (emailTemplate == null) {
            return null;
        }
        
        EmailTemplateDTO emailTemplateDTO = new EmailTemplateDTO();
        emailTemplateDTO.setSubject(emailTemplate.getSubject());
        emailTemplateDTO.setBodyBeforeQR(emailTemplate.getBodyBeforeQR());
        emailTemplateDTO.setBodyAfterQR(emailTemplate.getBodyAfterQR());

        return emailTemplateDTO;
    }

    public EmailTemplate emailTemplateDTOToEmailTemplate(EmailTemplateDTO emailTemplateDTO) {
        if (emailTemplateDTO == null) {
            return null;
        }

        EmailTemplate emailTemplate = new EmailTemplate();
        emailTemplate.setSubject(emailTemplateDTO.getSubject());
        emailTemplate.setBodyBeforeQR(emailTemplateDTO.getBodyBeforeQR());
        emailTemplate.setBodyAfterQR(emailTemplateDTO.getBodyAfterQR());

        return emailTemplate;
    }
}
