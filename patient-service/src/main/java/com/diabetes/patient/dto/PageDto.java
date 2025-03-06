package com.diabetes.patient.dto;

import java.util.List;



/**
 * DTO pour encapsuler les informations de pagination d'une liste d'éléments.
 * Contient le contenu de la page, le nombre total d'éléments et de pages,
 * ainsi que les informations sur le numéro de page, la taille et si c'est la première ou la dernière page.
 */

public class PageDto<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int number;    // Numéro de page actuel
    private int size;      // Taille de la page
    private boolean first; // Indique si c'est la première page
    private boolean last;  // Indique si c'est la dernière page

    public PageDto() {
    }

    // Getters et Setters

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
