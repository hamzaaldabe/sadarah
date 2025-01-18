package com.example.sadarah.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String verificationCode, String username) throws MessagingException, MessagingException {
        // Define the email content
        String emailContent = """
            <!DOCTYPE html>
            <html lang="ar">
              <head>
                <meta charset="UTF-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                <title>Email Verification</title>
                <style>
                  body {
                    font-family: Inter, system-ui, Avenir, Helvetica, Arial, sans-serif;
                    margin: 0;
                    padding: 0;
                    background-color: #f2f3f6;
                    color: #22273a;
                    direction: rtl;
                  }
                  .container {
                    max-width: 600px;
                    margin: 40px auto;
                    background-color: #ffffff;
                    border-radius: 8px;
                    overflow: hidden;
                    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
                  }
                  .header {
                    background-color: #22273a;
                    color: #ffffff;
                    text-align: center;
                    padding: 20px;
                  }
                  .content {
                    padding: 20px;
                  }
                  .code-box {
                    text-align: center;
                    background-color: #f2f3f6;
                    color: #22273a;
                    font-size: 24px;
                    font-weight: bold;
                    letter-spacing: 4px;
                    padding: 15px;
                    border: 2px solid #ff6620;
                    border-radius: 8px;
                    margin: 20px 0;
                  }
                  .footer {
                    text-align: center;
                    font-size: 12px;
                    color: #777;
                    margin: 20px 0;
                  }
                </style>
              </head>
              <body>
                <div class="container">
                  <div class="header">
                    <h1>تأكيد البريد الإلكتروني</h1>
                  </div>
                  <div class="content">
                    <p>مرحبًا %s،</p>
                    <p>شكرًا لانضمامك إلينا! للتحقق من عنوان بريدك الإلكتروني، يرجى استخدام رمز التحقق المكون من 6 أرقام أدناه:</p>
                    <div class="code-box">%s</div>
                  </div>
                  <div class="footer">
                    <p>الصدارة لمواد البناء - جميع الحقوق محفوظة. &copy; 2025</p>
                  </div>
                </div>
              </body>
            </html>
            """.formatted(username, verificationCode);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Set email parameters
        helper.setTo(toEmail);
        helper.setSubject("تأكيد البريد الإلكتروني");
        helper.setText(emailContent, true);

        mailSender.send(message);
    }
}
