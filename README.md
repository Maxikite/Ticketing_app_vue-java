# Atelier Tickets

Application de gestion de tickets destinée à centraliser les demandes de support ou les problèmes signalés par les utilisateurs d’un service ou d’un projet.

Elle permet de créer, consulter, modifier et supprimer des tickets, avec une interface simple et rapide à utiliser pour suivre les demandes au quotidien.

## À quoi sert ce projet ?

Ce projet est un petit outil de ticketing qui peut être utilisé pour :

- enregistrer une demande d’assistance ou un bug signalé ;
- classer les demandes par priorité ;
- suivre l’état d’un ticket (ouvert, en cours, etc.) ;
- consulter l’ensemble des demandes dans une liste claire ;
- modifier ou supprimer un ticket s’il doit être corrigé ou retiré.

C’est une base idéale pour un système de support interne, un outil de suivi de bugs ou un espace de gestion des demandes clients.

### Captures d’écran

Ajoutez ici vos captures pour illustrer l’usage réel du projet :

```md
![Formulaire de création d'un ticket](docs/screenshots/formulaire-ticket.png)
![Liste des tickets](docs/screenshots/liste-tickets.png)
![Édition d'un ticket](docs/screenshots/edition-ticket.png)
```

Exemples de vues attendues :

- formulaire de création d’une demande ;
- liste des tickets avec filtres ;
- écran d’édition pour modifier un ticket existant.

---

## Comment ça fonctionne ?

Le projet est séparé en deux parties :

### 1. Backend Spring Boot

Le backend expose une API REST pour gérer les tickets.

Fonctionnalités principales :

- récupération de tous les tickets ;
- création d’un ticket ;
- modification d’un ticket existant ;
- suppression d’un ticket ;
- validation des données envoyées par le frontend.

L’API est disponible sur :

- `http://localhost:8080/api/tickets`

Les données sont stockées localement dans la base H2 du projet, ce qui permet de faire tourner l’application sans installation externe de base de données.

### 2. Frontend Vue

Le frontend présente une interface utilisateur simple basée sur Vue.js.

Il permet :

- remplir un formulaire pour créer une demande ;
- afficher tous les tickets dans une liste ;
- filtrer les tickets par statut ;
- modifier un ticket ;
- supprimer un ticket après confirmation.

Le frontend est accessible sur :

- `http://localhost:5173`

---

## Architecture du projet

```text
project/
├── backend/
│   ├── src/main/java/          # code Java / Spring Boot
│   ├── src/main/resources/     # configuration de l'application
│   └── data/                   # données H2
├── frontend/
│   ├── src/                    # composants Vue.js
│   ├── package.json            # dépendances du frontend
│   └── vite.config.js          # configuration Vite
├── README.md
└── ...
```

---

## Comment lancer le projet

### Prérequis

- Java 17+
- Maven 3.9+
- Node.js 20+

### 1. Démarrer le backend

```powershell
cd backend
mvn spring-boot:run
```

### 2. Démarrer le frontend

```powershell
cd frontend
npm.cmd install
npm.cmd run dev
```

### 3. Ouvrir l’application

Dans le navigateur, ouvrir :

```text
http://localhost:5173
```

---

## Exemple de flux de fonctionnement

1. Un utilisateur remplit le formulaire de création d’un ticket.
2. Le frontend envoie les informations à l’API Spring Boot.
3. Le backend valide les données et les enregistre dans la base H2.
4. La liste des tickets se met à jour automatiquement dans l’interface.
5. L’utilisateur peut ensuite modifier ou supprimer une demande selon ses besoins.

---

## Objectif du projet

Ce projet a été conçu comme une petite application de démonstration pour montrer comment combiner :

- une API REST avec Java et Spring Boot ;
- une interface utilisateur moderne avec Vue.js ;
- une persistance locale avec H2 ;
- une gestion simple de demandes ou incidents de support.

Il est facile à comprendre, à exécuter et à étendre si vous voulez ajouter des fonctionnalités comme :

- gestion des utilisateurs ;
- commentaires internes sur les tickets ;
- filtres avancés ;
- statut détaillé (ouvert, en cours, résolu, rejeté) ;
- authentification.
