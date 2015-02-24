# fellesProsjekt37
#### Fellesprosjekt Gruppe 37: Dag Erik Homdrum Løvgren, Daniel lines, Lars Moe Ellefsen, Oscar Thån Conrad, Thomas Drabløs Frøysa.

## Git-kommandoer
#### Skrevet med tanke på en linux-maskin.
* Klone repository:
	* `git clone https://github.com/thomasdf/fellesProsjekt37.git`
	* evt. `git@github.com:thomasdf/fellesProsjekt37.git` hvis du bruker SSH
* Oppdatere repository:
	* PASS PÅ AT DU ER I MASTER!
	* `git pull origin`
* Liste branches:
	* `git branch` for lokale branches
	* `git branch -r` for å ta med remote branches i repoen
* Sjekke status på branchen/commiten du jobber i:
	* `git status`
* Lage ny branch:
	* `git branch <ny_branch_navn>`
* Bytte branch:
	* `git checkout <branch_navn>`
* Commite til repoen:
	1. Sjekk at du er på rett branch!
	2. `git add .` legger til alle filene du har laget/modifisert i commiten din
	3. `git commit -m "<commit-message>"` commiter (dvs. lager et checkpoint) med alle filene du added til commiten på denne branchen
	4. `git push --set-upstream origin <branch_navn>` pusher commiten din opp til GitHub
	5. Gå til GitHub, og så vil du se at branchen din er blitt lagt til. Trykk så på "Merge/Pull Request" for å forespør at branchen din skal merges inn i master

## Konvensjoner
* Git:
	* Branch ALLTID fra master
	* Aldri push direkte på master
	* Commit så mye du vil (checkpoints), men vær sikker på at det du har gjort er funksjonelt før du pusher det
* Java:
	* Skriv klasser med stor forbokstav, ex "MyClass.java"
	* Skriv funksjoner med liten forbokstav, og stor bokstav for hvert nytt ord, ex "myFunction()"
	* Skriv variabelnavn med små bokstaver og understretk, ex. "my_variable"
