package com.hse.course.dto;

import lombok.Data;

import java.util.Set;

@Data
public class MLRequest {
    private Set<Integer> interests;
    private long timestamp;

    public Set<Integer> getInterests() {
        return interests;
    }

    public void setInterests(Set<Integer> interests) {
        this.interests = interests;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}