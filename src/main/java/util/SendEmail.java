package util;



import com.sun.mail.util.MailSSLSocketFactory;
import org.junit.Test;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class SendEmail extends Thread {
    private List<String> toEmail;
    private String subject;
    private String content;
    private String fromEmail;
    private String fromEmailPassword;
    public static class Builder{
        private List<String> toEmail;
        private String subject;
        private String content;
        private String fromEmail;
        private String fromEmailPassword;
        public  SendEmail build(){
            return new SendEmail(this);
        }
        public Builder toEmail(List<String> list){
            this.toEmail = list;
            return this;
        }
        public Builder subject(String subject){
            this.subject = subject;
            return this;
        }
        public Builder content(String content){
            this.content = content;
            return this;
        }
        public Builder fromEmail(String fromEmail){
            this.fromEmail = fromEmail;
            return this;
        }
        public Builder fromEmailPassword(String fromEmailPassword){
            this.fromEmailPassword = fromEmailPassword;
            return this;
        }

    }
    public SendEmail(Builder builder) {
        toEmail = builder.toEmail;
        subject = builder.subject;
        content = builder.content;
        fromEmail = builder.fromEmail;
        fromEmailPassword = builder.fromEmailPassword;
    }



    @Override
    public void run() {
        Properties prop = new Properties();
        prop.setProperty("mail.host","smtp.qq.com");
        prop.setProperty("mail.transport.protocol","smtp");
        prop.setProperty("mail.smtp.auth","true");

        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        prop.put("mail.smtp.ssl.enable","true");
        prop.put("mail.smtp.socketFactory",sf);
            //创建session保证在整个会话中有效
            Session session = Session.getDefaultInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail,fromEmailPassword);
                }
            });
            InternetAddress[] internetAddresses = toAddressArray();





            session.setDebug(true);

            Transport ts = session.getTransport();
            ts.connect("smtp.qq.com",fromEmail,fromEmailPassword);

            MimeMessage message = new MimeMessage(session);
            message.setSubject(subject);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO,internetAddresses);
            message.setContent(content,"text/html;charset=utf-8");

            ts.sendMessage(message,message.getAllRecipients());
            ts.close();
        } catch (GeneralSecurityException | NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private InternetAddress[] toAddressArray(){
        if(Objects.isNull(toEmail)||toEmail.size()==0){
            throw new NullPointerException("toEmailList为空，检查一下");
        }

//        InternetAddress[] objects = (InternetAddress[])toEmail.stream().map(t->{
//            try {
//                return new InternetAddress(t);
//            } catch (AddressException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }).toArray();
        InternetAddress[] internetAddresses = new InternetAddress[toEmail.size()];
        int i=0;
        for (String s:toEmail) {
            try {
                internetAddresses[i++] = new InternetAddress(s);
            } catch (AddressException e) {
                e.printStackTrace();
            }
        }
        return internetAddresses;
    }





}
