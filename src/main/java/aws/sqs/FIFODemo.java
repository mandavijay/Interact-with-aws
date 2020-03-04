package aws.sqs;

import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

public class FIFODemo {

	private static AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

	public static void main(String[] args) {

		String queueName = "demo-fifo.fifo";

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

		System.out.println("Reading Messages second time");
		readMessages(queueUrl, 10);

		// Deleting Queue
		
		//System.out.println("Deleting Queue");
		//sqs.deleteQueue(queueUrl);

	}

	private static void sendMessages(String queueUrl, int count) {
		while (count > 0) {
			SendMessageRequest send_msg_request = new SendMessageRequest().withQueueUrl(queueUrl)
					.withMessageBody("hello world! Message number " + UUID.randomUUID());
			send_msg_request.setMessageGroupId("demoGroup");
			
			final SendMessageResult sendMessageResult = sqs.sendMessage(send_msg_request);
			
			final String sequenceNumber = sendMessageResult.getSequenceNumber();
            final String messageId = sendMessageResult.getMessageId();
			
            System.out.println("SendMessage succeed with messageId "
                    + messageId + ", sequence number " + sequenceNumber + "\n");
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
		for (Message message : messages) {
			System.out.println("Message");
            System.out.println("  MessageId:     "
                    + message.getMessageId());
            System.out.println("  ReceiptHandle: "
                    + message.getReceiptHandle());
            System.out.println("  MD5OfBody:     "
                    + message.getMD5OfBody());
            System.out.println("  Body:          "
                    + message.getBody());
            for (final Entry<String, String> entry : message.getAttributes()
                    .entrySet()) {
                System.out.println("Attribute");
                System.out.println("  Name:  " + entry.getKey());
                System.out.println("  Value: " + entry.getValue());
            }
            //sqs.deleteMessage(queueUrl, message.getReceiptHandle());
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
		System.out.println("Intitated Queue Creation!");
		
		CreateQueueRequest create_request = new CreateQueueRequest(name)
				.addAttributesEntry("ContentBasedDeduplication", "true")
				.addAttributesEntry("FifoQueue", "true")
				.addAttributesEntry("MessageRetentionPeriod", "86400");
		
		try {
			sqs.createQueue(create_request);
		} catch (AmazonSQSException e) {
			if (!e.getErrorCode().equals("QueueAlreadyExists")) {
				System.out.println("Queue already exist!");
			}
		}
		
		wait(20);
		return true;

	}

}
