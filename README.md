# Oceandroid

Oceandroid est une application mobile Android dédiée à la gestion des plongeurs et des plongées. Cette application permet aux utilisateurs de créer, modifier et visualiser des informations sur les plongeurs et leurs plongées, ainsi que de consulter des statistiques. L'application utilise Jetpack Compose pour l'interface utilisateur et s'intègre avec une base de données locale et des API externes pour récupérer et stocker les données.

## Fonctionnalités

### Gestion des Plongeurs
- **Liste des plongeurs**: Affiche une liste de tous les plongeurs enregistrés.
- **Création d'un plongeur**: Permet d'ajouter un nouveau plongeur.
- **Modification d'un plongeur**: Permet de modifier les informations d'un plongeur existant.

### Gestion des Plongées
- **Liste des plongées**: Affiche une liste de toutes les plongées enregistrées.
- **Création d'une plongée**: Permet d'ajouter une nouvelle plongée.
- **Modification d'une plongée**: Permet de modifier les informations d'une plongée existante.

### Statistiques
- **Statistiques des plongeurs**: Affiche des graphiques statistiques sur le nombre de plongeurs par niveau.

## Installation

1. Clonez ce dépôt :
   ```bash
   git clone https://github.com/votre-utilisateur/Oceandroid.git
2. Ouvrez le projet dans Android Studio.
3. Ajoutez l'URL de l'API dans le fichier local.properties :

    ```bash
    url="https://votre-api-url.com"
4. Compilez et exécutez l'application sur un appareil ou un émulateur Android.

## Structure du Code
### MainActivity

`MainActivity` est le point d'entrée de l'application. Elle gère la navigation entre les différentes pages et initialise la base de données locale.

### Pages

L'application est structurée en plusieurs pages, chacune ayant une responsabilité spécifique :

- **DiverList** : Affiche la liste des plongeurs.
- **DiverCreation** : Formulaire pour créer un nouveau plongeur.
- **DiverModification** : Formulaire pour modifier un plongeur existant.
- **DiveList** : Affiche la liste des plongées.
- **DiveCreation** : Formulaire pour créer une nouvelle plongée.
- **DiveModification** : Formulaire pour modifier une plongée existante.
- **Stats** : Affiche les statistiques des plongeurs.

### API
L'application utilise des requêtes HTTP pour communiquer avec un serveur distant et récupérer les données des plongeurs et des plongées. Toutes les classes qui font des requêtes vers l'API héritent de la classe `ApiRequest`.

### Base de Données
L'application utilise Room pour gérer la base de données locale. Les données sont récupérées et stockées dans des DAO (Data Access Objects).

## Licence
Ce projet est sous licence MIT - voir le fichier LICENSE pour plus de détails.