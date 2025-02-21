package com.diabetes.patient.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

/**
 * Représente un patient dans le système.
 */
@Document(collection = "patients")
public class Patient {

    @Id
    private String id;
    private String prenom;
    private String nom;
    private LocalDate dateNaissance;
    private String genre;
    private String adressePostale;
    private String numeroTelephone;

    // Constructeurs
    public Patient() {
    }

    public Patient(String prenom, String nom, LocalDate dateNaissance, String genre, String adressePostale, String numeroTelephone) {
        this.prenom = prenom;
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.genre = genre;
        this.adressePostale = adressePostale;
        this.numeroTelephone = numeroTelephone;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
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
}
