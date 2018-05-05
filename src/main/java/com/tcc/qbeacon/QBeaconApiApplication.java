package com.tcc.qbeacon;


import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QBeaconApiApplication {

	public static void main(String[] args) throws MqttException {
		SpringApplication.run(QBeaconApiApplication.class, args);
	}
}
