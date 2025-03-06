package com.diabetes.patient.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * Entité représentant un patient stocké dans la collection "patient" de MongoDB.
 * Contient les informations personnelles telles que nom, prénom, date de naissance, genre, adresse postale et numéro de téléphone.
 */

@Document(collection = "patient")
public class Patient {

    @Id
    private String id;

    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String genre;
    private String adressePostale;
    private String numeroTelephone;

    // Constructeurs, getters et setters

    public Patient() { }

    public Patient(String nom, String prenom, LocalDate dateNaissance, String genre, String adressePostale, String numeroTelephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.genre = genre;
        this.adressePostale = adressePostale;
        this.numeroTelephone = numeroTelephone;
    }

    // Getters et setters

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public LocalDate getDateNaissance() {
        return dateNaissance;
    }
    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public String getAdressePostale() {
        return adressePostale;
    }
    public void setAdressePostale(String adressePostale) {
        this.adressePostale = adressePostale;
    }
    public String getNumeroTelephone() {
        return numeroTelephone;
    }
    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", genre='" + genre + '\'' +
                ", adressePostale='" + adressePostale + '\'' +
                ", numeroTelephone='" + numeroTelephone + '\'' +
                '}';
    }
}
