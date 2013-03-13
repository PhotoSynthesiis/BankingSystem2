import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.sql.Date;
import java.util.Properties;

/**
 * 本类测试简单邮件 直接用邮件发送 
 *
 * @author Administrator
 *
 */
public class test
{
    public static void main(String args[])
    {
        Date date = new Date(Date.valueOf("").getTime());
        System.out.println(date.getTime());
    }

    private static void sendEmail() {
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
        // 设定mail server
        senderImpl.setHost("smtp.gmail.com");
        senderImpl.setPort(587);

        // 建立邮件消息
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        // 设置收件人，寄件人 用数组发送多个邮件
        // String[] array = new String[] {"sun111@163.com","sun222@sohu.com"};
        // mailMessage.setTo(array);
        mailMessage.setTo("photosynthesiis@gmail.com");
        mailMessage.setFrom("test@163.com");
        mailMessage.setSubject(" 测试简单文本邮件发送！ ");
        mailMessage.setText(" 测试我的简单邮件发送机制！！ ");

        senderImpl.setUsername("twufeedmycat@gmail.com"); // 根据自己的情况,设置username
        senderImpl.setPassword("@twu292012"); // 根据自己的情况, 设置password

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
        prop.put("mail.smtp.timeout", "25000");
        prop.put("mail.smtp.starttls.enable", "true");
        senderImpl.setJavaMailProperties(prop);

        // 发送邮件
        senderImpl.send(mailMessage);

        System.out.println(" 邮件发送成功.. ");
    }
}  