package com.demo.sqs;

import java.util.List;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class SQSDelayedMessagesDemo {

	private static AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

	public static void main(String[] args) {

		String queueName = "srl-first-queue";

		if(!createQueue(queueName)) {
			System.out.println("Something went wrong while creating queue");
			return;
		}

		// Getting URL of Queue
		String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
		System.out.println("Queue URL" + queueUrl);

		// Sending 5 messages
		System.out.println("Sending Messages");
		sendMessages(queueUrl, 5);

		// Consumer reads immediately
		System.out.println("Reading Messages");
		readMessages(queueUrl, 10);

		wait(30);
		System.out.println("Reading Messages");
		readMessages(queueUrl, 10);

		// Deleting Queue
		
		System.out.println("Deleting Queue");
		sqs.deleteQueue(queueUrl);

	}

	private static void sendMessages(String queueUrl, int count) {
		while (count > 0) {
			SendMessageRequest send_msg_request = new SendMessageRequest().withQueueUrl(queueUrl)
					.withMessageBody("hello world! Message number " + count);
			sqs.sendMessage(send_msg_request);
			System.out.println("Published Message " + count);
			count--;
		}
	}
	private static void readMessages(String queueUrl, int maxNoOfMessages) {
		final int maxNumberOfMessages = 10;
		final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest().withQueueUrl(queueUrl)
				.withMaxNumberOfMessages(maxNumberOfMessages);

		final ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(receiveMessageRequest);
		final List<Message> messages = receiveMessageResult.getMessages();

		System.out.println("Messages recieved " + messages.size());
		for (Message m : messages) {
			System.out.println("Message Body " + m.getBody());
			sqs.deleteMessage(queueUrl, m.getReceiptHandle());
		}
	}

	private static void wait(int seconds) {
		System.out.println("Waiting for "+seconds+" seconds");
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static boolean createQueue(String name) {
		boolean status = false;
		System.out.println("Intitated Queue Creation!");
		
		CreateQueueRequest create_request = new CreateQueueRequest(name)
				.addAttributesEntry("DelaySeconds", String.valueOf(20))
				.addAttributesEntry("MessageRetentionPeriod", "86400");
		
		try {
			sqs.createQueue(create_request);
			status = true;
		} catch (AmazonSQSException e) {
			if (!e.getErrorCode().equals("QueueAlreadyExists")) {
				status = false;
			}
		}
		
		wait(20);
		return status;

	}

}
