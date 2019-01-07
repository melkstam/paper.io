# paper.io
Java game based on [paper.io](http://paper-io.com). Made by [Vilhelm Melkstam](https://github.com/vilhelmmelkstam) and [Adam Halim](https://github.com/adamhalim).

Paper.io är ett relativt nytt spel online som kom i samma våg som många andra .io-spel så som agar.io och slither.io. Spelet finns under länken paper-io.com. Spelet är ett online multiplayer där man på en värld möter andra spelare.

När en spelare går med hamnar man på en slumpmässig server med andra spelare. Världen består av ett avgränsat kvadratiskt område där man först får ett litet område. Man kan sedan utöka sitt område genom att åka ut ur sitt område och rama in ett nytt område som sedan läggs till i ditt ursprungliga område när spelaren återvänder till området. Dock kan andra spelare köra över din svans medan du är på jakt efter nytt område för att förstöra dig. Spelet går då ut på att skaffa så stort område som möjligt.

### Funktionella krav
* Programmet ska kunna hantera minst en spelare och en datorspelare.
* Det ska finnas en live scoreboard medan man spelar.
* Programmet ska tillåta användare att välja namn.
* Spelet ska ha ett grafiskt gränssnitt.
* Spelet ska följa regler för Paper.io
	* När en spelare kör på en annan spelares svans utanför dess område ska den som blir överkörd förstöras.
	* Spelare genereras på slumpmässig, icke ockuperad, plats.
	* Varje spelare ska ha annorlunda färg
	* Om två spelare kolliderar rakt på ska den spelaren med minst andel av totalytan förstöras. 
	* Om en spelare kolliderar med väggen förstörs spelaren.

### Icke-funktionella krav
* Spelet ska vara skrivet i Java.
* Programmet ska ha en god objektorienterad design. * Designen ska finnas dokumenterad, t.ex. i diagramform.
* Programmet ska vara så lätt att lära sig att en normalbegåvad labbassistent kan hantera det efter några minuters utbildning.

### Ideer till ytterligare funktionalitet
* Local multiplayer för fler spelare.
* Online multiplayer för fler spelare. (Väldigt extra funktionalitet)
* Smartare bots som analyserar närheten för att avgöra rörelser.
* Styra storlek på banan, antal datorspelare och hastighet själv.
* Möjlighet att spela med Power-Ups.
* Spara och visa highscores.



## Dokumentation
[Javadocs](https://vilhelmmelkstam.github.io/paper.io) finns tillgängligt.

