package com.diabetes.front.dto;

import java.util.List;

public class PageDto<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int number;    // Numéro de la page actuelle
    private int size;      // Taille de la page
    private boolean first; // Indique si c'est la première page
    private boolean last;  // Indique si c'est la dernière page

    public PageDto() {
    }

    public List<T> getContent() {
        return content;
    }
    public void setContent(List<T> content) {
        this.content = content;
    }
    public long getTotalElements() {
        return totalElements;
    }
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
    public int getTotalPages() {
        return totalPages;
    }
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public boolean isFirst() {
        return first;
    }
    public void setFirst(boolean first) {
        this.first = first;
    }
    public boolean isLast() {
        return last;
    }
    public void setLast(boolean last) {
        this.last = last;
    }
}
