# Gestion de garage : application Android

Application Android (Java) de gestion d'un garage automobile. Elle consomme l'API REST PHP du dépôt [garage-web-api](https://github.com/murengerapatrick8-ctrl/garage-web-api) via Retrofit (échanges JSON, base MySQL).

## Fonctionnalités
- Authentification (écran de connexion)
- Gestion des mécaniciens, des clients, des véhicules et des interventions
- Statistiques d'activité

## Technologies
Java · Android Studio · Retrofit · API REST (JSON) · MySQL (via l'API PHP)

## Structure
- `LoginActivity` : connexion
- `HomeActivity` : accueil et navigation
- `DataActivity` : affichage des données (mécaniciens, clients, véhicules, interventions, statistiques)

## Installation
1. Installer et lancer le backend : suivre le README de [garage-web-api](https://github.com/murengerapatrick8-ctrl/garage-web-api) (XAMPP, base `garage_db`).
2. Ouvrir ce projet dans Android Studio et attendre la synchronisation Gradle.
3. Dans le code, remplacer `BASE_URL` par l'adresse IP du PC qui exécute XAMPP, par exemple `http://192.168.x.x/GARAGE/`.
4. Brancher le téléphone (ou utiliser l'émulateur), connecté au même réseau que le PC, puis lancer l'application.

## Captures d'écran
(à ajouter)

## Auteur
MURENGERA Patrick · github.com/murengerapatrick8-ctrl
