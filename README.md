# 🏥 Diabetes Monitoring - Système de Gestion des Patients et de l'Évaluation du Risque

## 📖 Introduction

Ce projet est un **système de gestion des patients et d’évaluation du risque de diabète** basé sur une **architecture microservices**. Il permet de :
- **Gérer les patients** (ajout, modification, consultation)
- **Stocker et récupérer les notes médicales**
- **Analyser les données médicales pour évaluer le niveau de risque du diabète**
- **Utiliser un front-end web** pour interagir avec le système

---

## 🎯 Objectifs du Projet

- **Adopter une architecture microservices** pour garantir **modularité et scalabilité**
- **Stocker les notes médicales avec une base NoSQL**
- **Optimiser les performances et la consommation d’énergie** (Refactoring Green)
- **Sécuriser les échanges entre microservices** via **Basic Auth** (en test) et une gestion des secrets évolutive

---

## 🛠️ Technologies Utilisées

### **🔹 Back-End**
- **Java 17**
- **Spring Boot 3.4.3**
- **Spring Cloud OpenFeign** (communication entre microservices)
- **Spring Security** (authentification)
- **Spring Data MongoDB** (Stockage NoSQL)
- **Spring Data JPA + MySQL** (Stockage relationnel)
- **Docker** (Containerisation)

### **🔹 Front-End**
- **Spring MVC (Thymeleaf)**
- **Bootstrap (UI simple et responsive)**

### **🔹 Outils DevOps**
- **Docker Compose** (orchestration des services)
- **FeignAuthInterceptor** (gestion des en-têtes d’authentification Basic)
- **Maven** (gestion des dépendances)

---

## 🏗️ Architecture des Microservices

### **📌 Découpage en Microservices**
| Microservice       | Port  | Description |
|--------------------|------|-------------|
| **Gateway Service** | `8080` | Proxy central redirigeant les requêtes vers les bons microservices |
| **Patient Service** | `8081` | Gestion des patients (CRUD) |
| **Notes Service**   | `8083` | Gestion des notes médicales des patients |
| **Risk Service**    | `8084` | Évaluation du risque de diabète basé sur les notes et le profil patient |
| **Front Service**   | `8082` | Interface utilisateur basée sur Spring MVC |

---

## 🚀 Installation et Lancement

### **📌 Prérequis**
- **Java 17**
- **Maven**
- **Docker & Docker Compose**
- **MongoDB (pour le service des notes)**
- **MySQL (pour le service patient)**

### **📌 Étapes d’installation**
1. **Cloner le projet**
   ```sh
   git clone https://github.com/votre-repo/diabetes-monitoring.git
   cd diabetes-monitoring

## Critères de Green Code

Le **Green Code** vise à réduire l'empreinte environnementale des applications en optimisant la consommation d'énergie, la gestion de la mémoire et les performances globales. Voici trois critères clés à intégrer dans notre projet :

1. **Optimisation de la consommation d'énergie**  
   *Objectif :* Réduire l'empreinte énergétique de l'application en limitant les appels redondants et en optimisant les algorithmes.  
   *Bonnes pratiques :*
   - Utiliser des algorithmes efficaces et éviter les traitements superflus.
   - Réduire la fréquence des appels aux services externes en mettant en cache les données fréquemment sollicitées.

2. **Gestion efficace de la mémoire**  
   *Objectif :* Minimiser l'utilisation et la fuite de la mémoire pour éviter une consommation excessive de ressources.  
   *Bonnes pratiques :*
   - Réutiliser les objets lorsque c'est possible et éviter les créations répétées d'instances inutiles.
   - Surveiller et profiler l'application pour détecter et corriger les fuites de mémoire.

3. **Efficience et performances optimisées**  
   *Objectif :* Améliorer la performance globale du code afin de réduire la charge sur le processeur et les ressources système.  
   *Bonnes pratiques :*
   - Optimiser les requêtes vers la base de données (indexation, requêtes ciblées).
   - Réduire la complexité algorithmique des traitements et éviter les itérations inutiles.

## CI/CD et Déploiement

Le projet intègre une pipeline CI/CD utilisant GitHub Actions pour :
- La compilation et l'exécution des tests (unitaires et d'intégration).
- La construction et le push des images Docker sur le GitHub Container Registry (GHCR).
- Le déploiement en production via Docker Compose.

Pour plus de détails sur la configuration et l'utilisation, veuillez consulter la documentation interne du projet.

## Ressources et Documentation

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Docker Documentation](https://docs.docker.com/)
- [Micrometer Documentation](https://micrometer.io/docs)
- [Green Code Best Practices](https://www.greenit.fr/green-code/)
