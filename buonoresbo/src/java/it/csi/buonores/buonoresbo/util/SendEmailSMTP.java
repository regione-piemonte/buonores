/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.mail.smtp.SMTPTransport;

@Component
public class SendEmailSMTP extends LoggerUtil {

	@Autowired
	private BuonoResBoProperties properties;

	// eliminato parametro Map<String, String> mailProperties
	public void send(String to, String bodyMessage, String oggetto) throws AddressException, MessagingException {
		logInfo("Send mail", "Entro in SendEmailSMTP metodo send");

		Properties prop = System.getProperties();
		prop.put("mail.smtp.host", properties.getServerName()); // optional, defined in SMTPTransport
		prop.put("mail.smtp.starttls.enable", true);
		prop.put("mail.smtp.port", properties.getServerPort()); // default port 25

		// System.out.println("MailFrom="+getMailFrom);
		Session session = Session.getInstance(prop, null);
		Message msg = new MimeMessage(session);

		// from (String)mailProperties.get("spring.mail.from")
		String ff = properties.getIndirizzoFrom();
		msg.setFrom(new InternetAddress(ff));

		// to
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

		msg.setSubject(oggetto);
		// content
		msg.setText(bodyMessage);
		msg.setSentDate(new Date());

		// Get SMTPTransport
		SMTPTransport t = (SMTPTransport) session.getTransport("smtp");

		// connect
		t.connect(properties.getServerName(), properties.getUsernameEmail(), properties.getPassEmail());

		// send
		t.sendMessage(msg, msg.getAllRecipients());

		t.close();

		logInfo("Send mail", "Esco da SendEmailSMTP metodo send");
	}

}
