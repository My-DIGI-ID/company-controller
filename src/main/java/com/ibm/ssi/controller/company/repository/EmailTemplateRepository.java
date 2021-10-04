package com.ibm.ssi.controller.company.repository;

import com.ibm.ssi.controller.company.domain.EmailTemplate;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTemplateRepository extends MongoRepository<EmailTemplate, String> {
}
