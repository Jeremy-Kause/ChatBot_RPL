package com.demikopi.sistemUser;

import java.util.List;

public class ChatResponse {

    public static class ChatImage {
        private final String title;
        private final String subtitle;
        private final String imagePath;

        public ChatImage(String title, String subtitle, String imagePath) {
            this.title = title == null ? "" : title;
            this.subtitle = subtitle == null ? "" : subtitle;
            this.imagePath = imagePath == null || imagePath.isBlank() ? null : imagePath;
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public String getImagePath() {
            return imagePath;
        }
    }

    private final String text;
    private final List<ChatImage> images;

    private ChatResponse(String text, List<ChatImage> images) {
        this.text = text == null ? "" : text;
        this.images = images == null ? List.of() : List.copyOf(images);
    }

    public static ChatResponse text(String text) {
        return new ChatResponse(text, List.of());
    }

    public static ChatResponse withImage(String text, String imagePath) {
        return new ChatResponse(text, List.of(new ChatImage(null, null, imagePath)));
    }

    public static ChatResponse withImages(String text, List<ChatImage> images) {
        return new ChatResponse(text, images);
    }

    public String getText() {
        return text;
    }

    public String getImagePath() {
        return images.isEmpty() ? null : images.get(0).getImagePath();
    }

    public List<ChatImage> getImages() {
        return images;
    }

    public boolean hasImage() {
        return images.stream().anyMatch(image -> image.getImagePath() != null);
    }
}
