package com.liceu.demo.dto;

import com.liceu.demo.models.Share;

public record SharedWithCanvas(int drawId,
                               int userId,
                               Share.SharePermission permission,
                               String shapes) {
}
