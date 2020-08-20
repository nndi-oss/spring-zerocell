package com.nndi_tech.spring.web.zerocell.examples;

import com.nndi_tech.spring.web.zerocell.ZerocellRequestBody;
import com.nndi_tech.spring.web.zerocell.examples.model.Customer;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.config.ConfigLoader;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.springsupport.SimpleJavaMailSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@Import(SimpleJavaMailSpringSupport.class)
public class ExcelToEmailController {

    @Autowired // or roll your own, as long as SimpleJavaMailSpringSupport is processed first
    private Mailer mailer;


    @PostMapping("/excel-email/upload")
    public void uploadToFilter(@ZerocellRequestBody List<Customer> request) {
        request.forEach(this::sendEmailToCustomer);
    }

    private void sendEmailToCustomer(Customer customer) {
        Email email = EmailBuilder.startingBlank()
            .from("noreply@nndi-tech.com")
            .to(customer.getFullName(), customer.getEmail())
            .withSubject("Spring Zerocell Newsletter")
            //.withHTMLText("<img src='cid:wink1'><b>We should meet up!</b><img src='cid:wink2'>")
            .withPlainText(String.format("Hello %s, \nPlease view this email in a modern email client!", customer.getFullName()))
            .buildEmail();

        Mailer mailer = MailerBuilder.withDebugLogging(true)
            .withSessionTimeout(10 * 1000)
            .withDebugLogging(true)
            .async()
            .buildMailer();

        mailer.sendMail(email);
    }
}
