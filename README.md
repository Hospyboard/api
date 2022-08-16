# Hospyboard API

L'api Hospyboard est dédiée à la gestion des différetes données, entitées du projet. elle est utilisée sur une
application mobile et sur un dashboard orienté administrateur pour la gestion des app mobiles.

Site vitrine du projet pour en savoir d'avantage : https://hospyboard.eu/

## Développeurs

- [@FunixG](https://www.github.com/funixg)

## Documentation

[Documentation API Postman](https://documenter.getpostman.com/view/12690794/UyrGADgt)

## Utilisation

Vous devez utiliser docker pour créer un environement api local. Pensez à l'installer si vous ne l'avez pas sur votre station. Pour lancer le build et executer le serveur api vous devez aller dans le dossier docker-hospyboard du repo. Ensuite vous pourrez lancer les commandes suivantes pour utiliser l'app.

### Lancer le build de l'environnement
```
$ docker-compose up --build
```

### Lancer l'environnement si vous avez déjà build

Ajoutez un -d si vous voulez que l'app se lance en tache de fond

```
$ docker-compose up
```

## Tech Stack

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)

![Spring app](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)

![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white)

![Database](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)

