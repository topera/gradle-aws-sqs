package com.topera.hello;

import software.amazon.awssdk.core.auth.ProfileCredentialsProvider;
import software.amazon.awssdk.core.regions.Region;
import software.amazon.awssdk.services.sqs.SQSClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public class Main {

    private static final Region REGION = Region.US_EAST_1;
    private static final String PROFILE_NAME = "default";
    private static final String HELLO_WORLD_QUEUE = "hello-world-queue";

    public static void main(String[] args) {
        // create sqs client
        SQSClient sqsClient = createSQSClient();

        // create queue and get its url
        CreateQueueResponse queue = sqsClient.createQueue(createCreateQueue());
        String queueUrl = queue.queueUrl();
        System.out.println("Queue created with url " + queueUrl);

        // create and send messages
        sqsClient.sendMessage(createMessage(queueUrl, "message-1"));
        sqsClient.sendMessage(createMessage(queueUrl, "message-2"));
        sqsClient.sendMessage(createMessage(queueUrl, "message-3"));
        System.out.println("Three messages sent");

        // get one message from queue
        ReceiveMessageResponse receiveMessageResponse = sqsClient.receiveMessage(createReceiveMessageRequest(queueUrl));
        System.out.println("Body of first message: " + receiveMessageResponse.messages().get(0).body());

        System.out.println("There are still at least 2 messages in the queue. Please check AWS console.");

    }

    private static ReceiveMessageRequest createReceiveMessageRequest(String queueUrl) {
        return ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .build();
    }

    private static CreateQueueRequest createCreateQueue() {
        return CreateQueueRequest.builder()
                .queueName(HELLO_WORLD_QUEUE)
                .build();
    }

    private static SendMessageRequest createMessage(String queueUrl, String messageBody) {
        return SendMessageRequest.builder()
                .messageBody(messageBody)
                .queueUrl(queueUrl)
                .build();
    }

    private static SQSClient createSQSClient() {
        return SQSClient.builder()
                .region(REGION)
                .credentialsProvider(createCredentialsProvider())
                .build();
    }

    private static ProfileCredentialsProvider createCredentialsProvider() {
        return ProfileCredentialsProvider.builder()
                .profileName(PROFILE_NAME)
                .build();
    }

}
