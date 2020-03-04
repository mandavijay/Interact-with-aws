package aws.sns;

import org.junit.Test;

public class FanOutDemoTest {

	
	@Test
	public void test() {
		TopicService.sayHello();
	}
	
	@Test
	public void createTopicTest() {
		TopicService.createTopic("first-topic");
	}
	
	@Test
	public void deleteTopicTest() {
		TopicService.deleteTopic("arn:aws:sns:ap-south-1:109688537152:first-topic");
	}

}
