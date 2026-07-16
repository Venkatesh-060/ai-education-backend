package com.example.backend.dto;

import java.time.LocalDateTime;

public class WhiteboardResponse {

    private String id;
    private String sessionId;
    private String userId;
    private String drawingData;
    private String toolType;
    private String color;
    private Integer strokeWidth;
    private LocalDateTime timestamp;

    public WhiteboardResponse() {
    }

    public WhiteboardResponse(
            String id,
            String sessionId,
            String userId,
            String drawingData,
            String toolType,
            String color,
            Integer strokeWidth,
            LocalDateTime timestamp) {

        this.id = id;
        this.sessionId = sessionId;
        this.userId = userId;
        this.drawingData = drawingData;
        this.toolType = toolType;
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDrawingData() {
        return drawingData;
    }

    public void setDrawingData(String drawingData) {
        this.drawingData = drawingData;
    }

    public String getToolType() {
        return toolType;
    }

    public void setToolType(String toolType) {
        this.toolType = toolType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(Integer strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}