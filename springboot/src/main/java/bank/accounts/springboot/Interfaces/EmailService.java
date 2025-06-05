package bank.accounts.springboot.Interfaces;

import bank.accounts.springboot.Config.EmailDetails;

import java.util.List;

public interface EmailService {

    String sendEmailWithAttachments(EmailDetails emailDetails, String[] recipients);
}
