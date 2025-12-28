package com.example.stafflink;

public class MessageModel {

    public String type;
    public String title;
    public String body;
    public boolean isRead;
    public long createdAt;
    public Sender sender;

    public MessageModel() {}

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public boolean isRead() {
        return isRead;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public static class Sender {
        public String id;
        public String name;
        public String role;

        public Sender() {}
    }
}
