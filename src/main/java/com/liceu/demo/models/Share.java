package com.liceu.demo.models;

public class Share {

    private int drawId;
    private int userId;
    private SharePermission permission;

    public enum SharePermission {
        VIEW,
        EDIT
    }

    public int getDrawId() {
        return drawId;
    }

    public void setDrawId(int drawId) {
        this.drawId = drawId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public SharePermission getPermission() {
        return permission;
    }

    public void setPermission(SharePermission permission) {
        this.permission = permission;
    }
}
