package aws.sns;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;

public class TopicService {

	protected static boolean sayHello() {
		System.out.println("Hello World!");
		return true;
	}
	private static AmazonSNS snsClient = AmazonSNSClient.builder().build();

	protected static boolean createTopic(String name) {
		// Create an Amazon SNS topic.
		final CreateTopicRequest createTopicRequest = new CreateTopicRequest(name);
		final CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);

		// Print the topic ARN.
		System.out.println("Topic Created and ARN : " + createTopicResult.getTopicArn());
		    
		// Print the request ID for the CreateTopicRequest action.
		System.out.println("CreateTopicRequest: " + snsClient.getCachedResponseMetadata(createTopicRequest));
		return true;
	}
	protected static boolean deleteTopic(String name) {
		
		DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest(name);
		snsClient.deleteTopic(deleteTopicRequest);
		System.out.println("Topic Deleted:" + name);
		
		System.out.println("DeleteTopicRequest: " + snsClient.getCachedResponseMetadata(deleteTopicRequest));
		return true;
	}
	protected static boolean subscribe(String name) {
		return true;
	}
	protected static void publishMessage(String body) {
	}
}
