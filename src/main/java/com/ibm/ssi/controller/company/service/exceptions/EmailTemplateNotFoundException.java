package com.ibm.ssi.controller.company.service.exceptions;

public class EmailTemplateNotFoundException extends Exception {
  private static final long serialVersionUID = 1L;

  public EmailTemplateNotFoundException() {
      super("An Email template with the given id does not exist.");
  }
}
