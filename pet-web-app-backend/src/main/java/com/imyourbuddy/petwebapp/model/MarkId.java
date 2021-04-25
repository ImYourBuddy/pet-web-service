package com.imyourbuddy.petwebapp.model;

import java.io.Serializable;

public class MarkId implements Serializable {
    private long postId;

    private long userId;

    public MarkId() {
    }

    public MarkId(long postId, long userId) {
        this.postId = postId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarkId markId = (MarkId) o;
        return postId == markId.postId && userId == markId.userId;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Long.hashCode(postId);
        result = 31 * result + Long.hashCode(userId);
        return result;
    }
}
