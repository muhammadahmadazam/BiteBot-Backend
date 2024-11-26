package com.xcelerate.cafeManagementSystem.DTOs;

import java.util.List;

public class AnalysisDTO {
    private String emotion;
    private List<Integer> ids;

    public AnalysisDTO(String emotion, List<Integer> ids) {
        this.emotion = emotion;
        this.ids = ids;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}