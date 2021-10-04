package com.ibm.ssi.controller.company.service.impl;

import java.util.Optional;

import com.ibm.ssi.controller.company.domain.EmailTemplate;
import com.ibm.ssi.controller.company.repository.EmailTemplateRepository;
import com.ibm.ssi.controller.company.service.EmailTemplateService;
import com.ibm.ssi.controller.company.service.dto.EmailTemplateDTO;
import com.ibm.ssi.controller.company.service.exceptions.EmailTemplateNotFoundException;
import com.ibm.ssi.controller.company.service.mapper.EmailTemplateMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private final Logger log = LoggerFactory.getLogger(EmailTemplateServiceImpl.class);

    @Autowired
    EmailTemplateRepository emailTemplateRepository;

    @Autowired
    EmailTemplateMapper emailTemplateMapper;

    @Override
    public EmailTemplateDTO updateEmailTemplate(EmailTemplateDTO emailTemplateDTO) throws EmailTemplateNotFoundException{
        log.debug("Update email template");

        Optional<EmailTemplate> existingEmailTemplate = this.emailTemplateRepository.findById(emailTemplateDTO.getId());

        if (existingEmailTemplate.isPresent()) {
            this.emailTemplateRepository.save(this.emailTemplateMapper.emailTemplateDTOToEmailTemplate(emailTemplateDTO));

            return emailTemplateDTO;
        } else {
            throw new EmailTemplateNotFoundException();
        }
    }

     /**
     * Get the stored emailTemplate.
     *
     * @return the entity.
     */
    @Override
    public EmailTemplateDTO getEmailTemplate() {
        log.debug("Get email template");

        return this.emailTemplateMapper.emailTemplateToEmailTemplateDTO(this.emailTemplateRepository.findById("email-template").get());
    }
}
