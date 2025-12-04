package com.challenge.api.model;

import java.util.List;

public class GetAllEmployeesResponse {
    private List<Employee> data;
    private String nextCursor;
    private String previousCursor;

    public GetAllEmployeesResponse(List<Employee> data, String nextCursor, String previousCursor) {
        this.data = data;
        this.nextCursor = nextCursor;
        this.previousCursor = previousCursor;
    }

    public List<Employee> getData() {
        return data;
    }

    public void setData(List<Employee> data) {
        this.data = data;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }

    public String getPreviousCursor() {
        return previousCursor;
    }

    public void setPreviousCursor(String previousCursor) {
        this.previousCursor = previousCursor;
    }
}
