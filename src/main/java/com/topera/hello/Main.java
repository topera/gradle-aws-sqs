package com.topera.hello;

import software.amazon.awssdk.core.auth.ProfileCredentialsProvider;
import software.amazon.awssdk.core.regions.Region;
import software.amazon.awssdk.services.sqs.SQSClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public class Main {

    private static final Region REGION = Region.US_EAST_1;
    private static final String PROFILE_NAME = "default";

    public static void main(String[] args) {
        // create sqs client
        SQSClient sqsClient = createSQSClient();

        // create and send messages
        sqsClient.sendMessage(createMessage("message-1"));
        sqsClient.sendMessage(createMessage("message-2"));
    }

    private static SendMessageRequest createMessage(String messageBody) {
        return SendMessageRequest.builder()
                .messageBody(messageBody)
                .queueUrl("https://sqs.us-east-1.amazonaws.com/727193322906/test-queue")
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
