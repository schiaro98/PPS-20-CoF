# Design Architetturale 
Visti i requisiti del punto 2, abbiamo ritenuto opportuno modellare il nostro sistema basandoci su due punti chiave:
* Modificabilità
* Riuso

Inoltre abbiamo dato particolare peso alle prestazioni, in quanto un simulatore con prestazione troppo basse rispetto 
alle aspettative di un utente non avrebbe suscitato il necessario interesse. 

## Architettura complessiva
Il progetto ha lo scopo di simulare il ciclo di vita di alcuni animali, creabili dall'utente, all'interno di un Habitat, di dimensioni e caratteristiche variabili, nel tempo.
Ogni animale appartiene ad una specie e ogni specie ha alcuni parametri specificabili dall'utente:
  - Taglia
  - Alimentazione (carnivoro o erbivoro)
  - Nome
  - Forza
  - Raggio visivo
  - Colore

Ogni specie, una volta inserita nel simulatore viene salvata ed è quindi utilizzabile una volta riaperto il programma. E' possibile poi specificare per ogni specie la quantità, in modo da avere una simulazione più frenetica (con molti animali) o più blanda (con meno animali).

Per quanto riguarda invece l'habitat, è possibile scegliere tra l'habitat standard, un habitat con zone create in modo randomico, un habitat con zone disposte a griglia oppure un habitat vuoto. Per ognuno di essi è possibile definire un campo *unexpected events* che definisce la probabilità che un un animale possa morire a causa di avvenimenti non ordinari come incendi o malattie.

Partendo dai requisiti abbiamo dapprima sviluppato un diagramma UML dei componenti principali dell'applicazione così da notare eventuali scelte errate in fase di progettazione
![Diagramma UML](/resources/UML1.png "Primo diagramma UML")

In questo diagramma abbiamo definito alcuni punti fondamentali, come la relazione tra animale e specie, la composizione di Habitat in aree, la differenziazione tra tipo di cibo e l'esistenza di zone dell'habitat non percorribili.

![Diagramma UML](/resources/UML2.png "Correzione primo diagramma UML")
In questo secondo diagramma possiamo invece vedere come vengano modificati alcuni aspetti, come ad esempio introducendo il concetto di *visualizable*, ovvero un entita che verrà effettivamente rappresentata nella mappa e quindi visibile. Un esempio sono il cibo e gli animali. 
Per quanto riguarda l'architettura è stato scelto di modellare l'applicazione attraverso il pattern MVC. In questo modo abbiamo potuto suddividere l'applicazione in 3 componenti *loosely coupled*. Il componente **View** infatti si occupa solamente di visualizzare le informazioni in una mappa. Il **Model** invece si occupa di modellare le entità di gioco come ad esempio gli Animali, gli Habitat o i cibi e di gestire i dati ricevuti dall'utente tramite la Gui. Il **Controller** invece si occupa di modificare i dati forniti dal Model, elaborandoli e restituendoli aggiornati alla Gui.

Per la simulazione vera e propria abbiamo invece utilizzato un metodo ricorsivo che prendesse come parametri gli animali e le risorse presenti nella mappa ad ogni aggiornamento.

## Descrizione di pattern architetturali usati
###MVC
###Model
###Controller
###View

## Scelte tecnologiche cruciali ai fini architetturali
Per permettere a questo progetto di essere il più aderente possibile a quello che abbiamo visto durante il corso, abbiamo deciso di utilizzare fortemente i metodi con ricorsione tail. Questo particolare metodo di ricorsione visto a lezione permette infatti di eseguire cicli di iterazione successivi riutilizzando lo stesso Stack Frame.

corredato da pochi ma efficaci diagrammi

Aggiungere magari diagramma UML di come si connettono MVC
