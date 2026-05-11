package com.demikopi.sistemUser;

import java.util.List;

public class ChatResponse {

    public enum BlockType {
        TITLE,
        SECTION,
        PARAGRAPH,
        NUMBERED_ITEM,
        NUMBERED_DETAIL_ITEM,
        LIST_ITEM,
        DETAIL_ROW,
        NOTE
    }

    public static class ChatBlock {
        private final BlockType type;
        private final String title;
        private final String value;

        private ChatBlock(BlockType type, String title, String value) {
            this.type = type == null ? BlockType.PARAGRAPH : type;
            this.title = title == null ? "" : title;
            this.value = value == null ? "" : value;
        }

        public static ChatBlock title(String text) {
            return new ChatBlock(BlockType.TITLE, text, null);
        }

        public static ChatBlock section(String text) {
            return new ChatBlock(BlockType.SECTION, text, null);
        }

        public static ChatBlock paragraph(String text) {
            return new ChatBlock(BlockType.PARAGRAPH, text, null);
        }

        public static ChatBlock listItem(String title, String value) {
            return new ChatBlock(BlockType.LIST_ITEM, title, value);
        }

        public static ChatBlock numberedItem(int number, String title, String value) {
            return new ChatBlock(BlockType.NUMBERED_ITEM, number + ". " + (title == null ? "" : title), value);
        }

        public static ChatBlock numberedDetailItem(int number, String title, String value) {
            return new ChatBlock(BlockType.NUMBERED_DETAIL_ITEM, number + ". " + (title == null ? "" : title), value);
        }

        public static ChatBlock detailRow(String label, String value) {
            return new ChatBlock(BlockType.DETAIL_ROW, label, value);
        }

        public static ChatBlock note(String text) {
            return new ChatBlock(BlockType.NOTE, text, null);
        }

        public BlockType getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }

        public String getValue() {
            return value;
        }
    }

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
    private final List<ChatBlock> blocks;

    private ChatResponse(String text, List<ChatImage> images) {
        this(text, images, List.of());
    }

    private ChatResponse(String text, List<ChatImage> images, List<ChatBlock> blocks) {
        this.text = text == null ? "" : text;
        this.images = images == null ? List.of() : List.copyOf(images);
        this.blocks = blocks == null ? List.of() : List.copyOf(blocks);
    }

    public static ChatResponse text(String text) {
        return new ChatResponse(text, List.of());
    }

    public static ChatResponse formatted(String text, List<ChatBlock> blocks) {
        return new ChatResponse(text, List.of(), blocks);
    }

    public static ChatResponse withImage(String text, String imagePath) {
        return new ChatResponse(text, List.of(new ChatImage(null, null, imagePath)));
    }

    public static ChatResponse withImage(String text, String imagePath, List<ChatBlock> blocks) {
        return new ChatResponse(text, List.of(new ChatImage(null, null, imagePath)), blocks);
    }

    public static ChatResponse withImages(String text, List<ChatImage> images) {
        return new ChatResponse(text, images);
    }

    public static ChatResponse withImages(String text, List<ChatImage> images, List<ChatBlock> blocks) {
        return new ChatResponse(text, images, blocks);
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

    public List<ChatBlock> getBlocks() {
        return blocks;
    }

    public boolean hasBlocks() {
        return !blocks.isEmpty();
    }

    public boolean hasImage() {
        return images.stream().anyMatch(image -> image.getImagePath() != null);
    }
}
