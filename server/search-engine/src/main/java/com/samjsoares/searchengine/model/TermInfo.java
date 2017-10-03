package com.samjsoares.searchengine.model;

public class TermInfo{
    private int count;
    private int position;
    private Type type;

    public TermInfo() {}

    public TermInfo(int count, int position, Type type) {
        this.count = count;
        this.position = position;
        this.type = type;
    }

    public TermInfo(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        bold,
        italics,
        title,
        paragraph
    }


}
