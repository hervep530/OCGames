# HGames - Projet 3 du Parcours DA Java sur OpenClassroom (en cours...)

Petite application en mode console permettant le lancer 2 jeu :
- PlusMoins
- MasterMind

...en 3 modes via un menu :
- Challenger
- Défenseur
- Duel

Instruction d'installation et d'utilisation :
1. Creer un repertoire, puis se positionner dans ce repertoire
2. Cloner le repository OCGames (git clone https://github.com/hervep530/OCGames)
3. Se positionner dans le repertoire OCGames
4. Creer un repertoire target
5. Compiler le programme (javac -sourcepath src/main/java -d target -cp lib/log4j/2.11.1/log4j-api-2.11.1.jar:lib/log4j/2.11.1/log4j-core-2.11.1.jar src/main/java/com/herve/ocgames/{*/*/*,*/*,Main}.java)
6. A partir du repertoire target, copier le fichier  src/main/java/log4j2.xml dans target/
7. Lancer l'application avec la commande (java -cp ./target:lib/log4j/2.11.1/log4j-api-2.11.1.jar:lib/log4j/2.11.1/log4j-core-2.11.1.jar com.herve.ocgames.Main) - ajouter l'option " -d" en fin de commande pour activer le mode debug.

Avant de relancer une éventuelle compilation, il est recommander de supprimer les fichiers .class 