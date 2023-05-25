package com.almightyvats.sensorsafe.service;

import com.almightyvats.sensorsafe.model.custom.SanityCheckType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String SUBJECT = "SensorSafe Alert";

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(String to, String sensorName, String stationName, Date timestamp, List<SanityCheckType> sanityCheckTypes) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(SUBJECT);
        message.setText(buildEmailBody(sensorName, stationName, timestamp, sanityCheckTypes));
        mailSender.send(message);
    }

    public String buildEmailBody(String sensorName, String stationName, Date timestamp, List<SanityCheckType> sanityCheckTypes) {
        StringBuilder sb = new StringBuilder();
        sb.append("Sensor: ").append(sensorName).append("\n");
        sb.append("Station: ").append(stationName).append("\n");
        sb.append("Timestamp: ").append(timestamp).append("\n");
        sb.append("Sanity check errors: ").append("\n");
        sanityCheckTypes.forEach(sanityCheckType -> sb.append(sanityCheckType).append("\n"));
        return sb.toString();
    }
}
