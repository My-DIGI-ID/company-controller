package com.ibm.ssi.controller.company.service;

import java.util.List;

import com.ibm.ssi.controller.company.service.dto.EmailTemplateDTO;
import com.ibm.ssi.controller.company.service.exceptions.EmailTemplateNotFoundException;

public interface EmailTemplateService {

    EmailTemplateDTO updateEmailTemplate(EmailTemplateDTO emailTemplateDTO) throws EmailTemplateNotFoundException;

    EmailTemplateDTO getEmailTemplate();
}
