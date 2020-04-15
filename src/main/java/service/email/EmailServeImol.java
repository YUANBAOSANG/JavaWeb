package service.email;

import dao.BaseDao;
import dao.email.EmailDao;
import dao.email.EmailDaoImpl;

import org.apache.commons.lang3.RandomStringUtils;


import pojo.Email;
import util.Constants;
import util.SendEmail;

import java.sql.Connection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmailServeImol implements EmailService {
    private static EmailDao emailDao = new EmailDaoImpl();
    private static Email fromEmail;
    static {
        Connection connection = BaseDao.getConnection();
        try {
            fromEmail = emailDao.getEmailById(connection,"-1");
            System.out.println("fromEmail为："+fromEmail+"-------");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeSQLResource(connection,null,null);
        }
    }
    @Override
    public void massEmail(String msg, String subject) {
        Connection connection = BaseDao.getConnection();
        try {
            List<String> list = emailDao.getEmailList(connection);
            new SendEmail.Builder().content(msg).fromEmail(fromEmail.getEmail()).
                    fromEmailPassword(String.valueOf(fromEmail.getCode())).
                    subject(subject).toEmail(list).build().start();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @Override
    public void sendEmail(String id, String msg, String subject) {
        Connection connection = BaseDao.getConnection();
        try {
            List<String> list = new ArrayList<>();
            list.add(emailDao.getEmailById(connection,id).getEmail());
            new SendEmail.Builder().content(msg).fromEmail(fromEmail.getEmail()).
                    fromEmailPassword(String.valueOf(fromEmail.getCode())).
                    subject(subject).toEmail(list).build().start();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void setCode(String id) {
        Constants.Code = createCode(id);
    }

    private String createCode(String id) {
        Connection connection = BaseDao.getConnection();
        String code = RandomStringUtils.randomAlphanumeric(6);
        System.out.println("code : "+code);
        if(connection!=null){
            try {
                emailDao.updateCode(connection,id,code);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return code;
    }
}
