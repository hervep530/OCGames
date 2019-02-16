# HGames - Projet 3 du Parcours DA Java sur OpenClassroom (en cours...)

Petite application en mode console permettant le lancer 2 jeu :
- PlusMoins
- MasterMind

...en 3 modes via un menu :
- Challenger
- Défenseur
- Duel

__Instruction d'installation et d'utilisation__ :
1. Creer un repertoire, puis se positionner dans ce repertoire
2. Cloner le repository OCGames (git clone https://github.com/hervep530/OCGames)
3. Se positionner dans le repertoire OCGames
4. Creer un repertoire target
5. Compiler le programme (javac -sourcepath src/main/java -d target -cp lib/log4j/2.11.1/log4j-api-2.11.1.jar:lib/log4j/2.11.1/log4j-core-2.11.1.jar src/main/java/com/herve/ocgames/{\*/\*/\*,\*/\*,Main}.java)
6. A partir du repertoire target, copier le fichier  src/main/java/log4j2.xml dans target/
7. Lancer l'application avec la commande (java -cp ./target:lib/log4j/2.11.1/log4j-api-2.11.1.jar:lib/log4j/2.11.1/log4j-core-2.11.1.jar com.herve.ocgames.Main) - ajouter l'option " -d" en fin de commande pour activer le mode debug.

Avant de relancer une éventuelle compilation, il est recommander de supprimer les fichiers .class


__Configuration__ :
Un fichier de configuration config.properties placé dans le répertoire resources du projet, permet de modifier quelques paramètres.
Entre autre, il est possible :
- de (des)activer le mode debug avec : core.debug = [0|no|false|1|yes|true]
- de changer la langue dans laquelle les messages s'affichent avec : core.language = [en_EN|fr_FR]

pour le jeu plus moins :
- de modifier le nombre de chiffre du code secret avec : plusmoins.codeLength = [4|5|6|7|8|9|10]
- de modifier le nombre de tentatives  avec : plusmoins.attempts = [4|5|6]

pour le jeu mastermind :
- de modifier le nombre de chiffre du code secret avec : mastermind.codeLength = [4|5|6|7|8|9|10]
- de modifier le nombre de tentatives  avec : mastermind.attempts = [4|5|6|7|8|9|10|12|15|20|25]
- de modifier le nombre de chiffres utilisés par le jeu avec : mastermind.digitsInGame = [6|7|8|9|10]

Remarque de développement - Un controle des paramètres est implémenté en utilisant l'enum ConfigEntry. Un parametre non conforme n'apporte donc aucune modification. Par ailleurs, une configuration par défaut assure le fonctionnement du jeu, en cas de parametres invalide, elle est implémentée dans la classe Config du package recovery.



