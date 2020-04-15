package service.email;

public interface EmailService {
    void massEmail(String msg, String subject);
    void sendEmail(String id,String msg,String subject);
    void setCode(String id);
}
