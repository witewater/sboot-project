package cn.wendong.springbootmail.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 *  发送邮箱
 * @author MyLj
 *
 */
@Service
public class MyMailService {
	
	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String from;
	
	/**
	 * 发送简单的邮件
	 * @param to
	 * @param subject
	 * @param content
	 */
	public void sendSimpleMail(String to,String subject,String content){
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);
		
		mailSender.send(message);
	}
	
	/**
	 * 发送Html邮件
	 * @param to
	 * @param subject
	 * @param content
	 */
	public void sendHtmlMail(String to,String subject,String content){
		MimeMessage message = null;
		try {
			message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content,true);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		mailSender.send(message);
	}
	
	/**
	 * 发送附件邮件
	 * @param to
	 * @param subject
	 * @param content
	 * @param filePath
	 */
	public void sendAttachMail(String to,String subject,String content,String filePath){
		MimeMessage message = null;
		try {
			message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content,true);
			
			FileSystemResource file = new FileSystemResource(filePath);
			String fileName = file.getFilename();
			helper.addAttachment(fileName, file);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		mailSender.send(message);
	}
	
	/**
	 * 发送图片邮件
	 * @param to
	 * @param subject
	 * @param content
	 * @param rscPath
	 * @param rscId
	 */
	public void sendInlineMail(String to,String subject,String content,String rscPath,String rscId){
		MimeMessage message = null;
		try {
			message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content,true);
			
			FileSystemResource file = new FileSystemResource(rscPath);
			 helper.addInline(rscId, file);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		mailSender.send(message);
	}
	
}
