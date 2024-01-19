# TP Spring Boot Concurrency Testing

L'objectif de ce TP est d'étudier comment tester les performances et la cohérence de son backend SpringBoot dans un contexte transactionnel.
La plupart du temps le backend tombe en marche, sans aucun test de charge ni de validation des choix relatifs à la gestion des transactions.
Comment être sur que mon service de réservation assure bien les propriétés ACID, en particulier la cohérence des données, lorsque plusieurs centaines de transactions/réservations sont exécutées en même concurrence.

Pendant ce TP vous aurez à:
 - mettre en œuvre des tests au niveau du backend en utilisant l'exécution de plusieurs threads et en vérifiant la cohérence de la base via les assertions des tests (mvn install ou mvn test)
 - mettre en oeuvre des tests au niveau de l'API en utilisant la montée en charge des appels HTTP via l'outil JMeter.

## Contexte

Le cas d'utilisation est trivial. Il consiste en 1 entité: Account. Un compte possède 3 propriétés: Id, AccountNumber et Balance.
Le backend SpringBoot est disponible avec la classe de données, la classe Repository, la classe service et la classe contrôleur qui définit les routes du serveur web.
Une classe de test est également disponible pour vérifier le bon fonctionnement des couches basses.

## Installation

Vous devez récupérez l'archive du projet qui se base sur Java 17 (attention à installer ou configurer cette version si vous ne l'avez pas, mais cela doit passer sur une autre version).
Vous devez juste pour valider votre installation faire:
 - mvn clean
 - mvn install

Les tests doivent passer.
A noter que le Le projet est configuré pour s’exécuter sur une base H2 en mémoire.
Un fichier data.sql est fourni et peut être déposé dans le répertoire Ressources de src/main. Ce fichier ne doit pas être présent pour les tests maven car il fera échouer ces tests. Il pourra être utiliser pour des tests postman et JMeter afin d'initialiser la base.

## Réalisation

Vous trouverez dans:
 - /data le modèle de données avec le mapping
 - /repository la classes DAO d'accès JPA
 - /service la classe pour l'accès application aux données
 - /controller la classe de définition des routes du serveur
 - /exception les classes d'exception
 - /test la classe de tests
 - /ressources contient application.properties et éventuellement data.sql

Le code est déjà réalisé. SpringBoot et H2 sont configurés pour afficher en clair les commandes SQL envoyées à la base.
 
### Test backend via Maven
Vous devez complétez le code de la classe de test Concurrencytest.
Tous les tests basiques ont été réalisés, mais la fin de la méthode testConccurentTransfert est à votre charge.
Vous devrez mettre en oeuvre l'interface executorService pour lancer une dizaine de threads transferMoney afin de tester la cohérence dans différentes configurations.
Ces configurations porteront sur le mode d'isolation en modifiant le mode défini dans AccountService pour transferMoney (Red_COMMITTED, READ_UNCOMMITTED, REPEATABLE_READ, SERIALLIZABLE) et sur l'utilisation ou non d'un verrou au niveau du thread (moneytransferLock ou moneytransfer). Ce verrou n'est pas au niveau de la base mais au niveau de la gestion des threads.
La méthodede test devra envoyer SUCCESS si la cohérence de la base est assurée, FAILURE sinon.

### Test API du serveur Web via JMeter
Apache JMeter est une application de bureau open source basée sur Java  pour réaliser des tests de charge. Il ne permet pas directement des tests de cohérence de la base, mais en partenariat avec Postman ou un IDE de gestion de base de données il est possible d'observer facilement l'état de la base.

 1. Tester l'API de votre serveur à l'aide de postman afin de vous familiarisez avec les différentes routes (l'OpenAPI api-docs.json est disponible pour faciliter l'usage de postman)
 2. Récupérer la distribution de JMeter pour votre plateforme [ici](https://jmeter.apache.org/download_jmeter.cgi) et installer JMeter.
 3. Lire [ce tuto](https://www.guru99.com/fr/jmeter-performance-testing.html) pour apprendre comment définir une charge d'appels HTTP avec JMeter.
 4. Mettre en place une montée en charge sur la route transferMoney avec plusieurs configurations comme pour le test précédent afin d'étudier d'une part la cohérence de la base et d'autre part de comparer la charge de chaque configuration. Vous pourrez utiliser un affichage tabulaire et/ou graphique. Vous aurez besoin de postman ou d'un IDE de gestion de BD pour vérifier l'état de la base après les tests.
 5. Tester la montée en charge d'autres routes pour prendre en main JMeter.

## Rendu du TP

Vous rendrez dans un document pdf:
 - le code complémenté de testConcurrentTransfert()
 - le résultat des tests et observations des deux catégories de test pour les configurations étudiées.
 
 Vous devrez utiliser ce genre de méthode dans votre projet intégrateur pour valider la bonne gestion transactionnelle de votre application pour les services critiques.


