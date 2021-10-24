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

## Descrizione di pattern architetturali usati
Per quanto riguarda l'architettura è stato scelto di modellare l'applicazione attraverso il pattern MVC. In questo modo abbiamo potuto suddividere l'applicazione in 3 componenti *loosely coupled*. Il componente **View** infatti si occupa solamente di visualizzare le informazioni in una mappa. Il **Model** invece si occupa di modellare le entità di gioco come ad esempio gli Animali, gli Habitat o i cibi e di gestire i dati ricevuti dall'utente tramite la Gui. Il **Controller** invece ha il compito di modificare i dati forniti dal Model, elaborandoli e restituendoli aggiornati alla Gui.
Questa scelta architetturale ci consentirebbe in futuro di poter cambiare uno dei tre componenti principali senza dover riscrivere l'intera applicazione. Un esempio di possibile cambiamento potrebbe essere l'aggiornameto con un nuovo framework per l'interfaccia grafica come ScalaFX oppure ad una versione web.
### MVC
![Diagramma Model View Controller](/resources/MVC.png "Diagramma MVC")

### Model
### Controller
### View
La view si compone principalmente delle classi utili a visualizzare i 5 frame e per la gestione della rappresentazione visuale degli animali, delle aree e dei popup che mostrano le statistiche in tempo reale durante la simulazione.
Nel primo frame possiamo aumentare o diminuire la quantità di animali per ogni specie in modo da rendere la simulazione più movimentata o meno. Sono poi presenti i pulsanti per creare una nuova specie o per passare alla creazione di una nuova specie.
![View scelta animali](/resources/view1.png "View scelta animali")

Nel pannello di creazione specie possiamo specificare alcuni campi come nome, forza, vista, taglia e dieta della specie. E' presente poi un pannello per la scelta del colore con cui verrà disegnato l'animale nella mappa.
![View creazione nuova specie](/resources/view5.png "View creazione nuova specie")

Se invece procediamo alla scelta di un habitat, possiamo scegliere tra l'habitat di default, oppure scegliere tra habitat con zone random, con zone a griglia oppure vuoto. Per ogni habitat possiamo specificare una probabilità che avvengano eventi casuali che possono uccidere o meno gli animali presenti. Per tutti gli habitat meno quello casuale è possibiile specificare una dimensione per la mappa.
![View scelta habitat](/resources/view5.png "View scelta habitat")

Una volta partita la simulazione è possibile visualizzare, oltre ovviamente alla simulazione che avviene nella parte superiore, gli eventi che accadono e fermare o velocizzare il tempo tramite l'area di testo e i pulsanti presenti nella parte inferiore.
![View simulazione](/resources/view3.png "View simulazione")

Una volta completata la simulazione, ovvero quando non sono più presenti animali, viene presentato un report contenente le statistiche della simulazione. Ovvero di come i dati riguardanti il cibo e gli animali presenti siano evoluti nel tempo.
![View statistiche](/resources/view4.png "View statistiche")

## Scelte tecnologiche cruciali ai fini architetturali

### Immutabilità 
Una delle caratteristiche che ci incuriosiva di più del paradigma funzionale era l'immutabilità dei dati, per questo motivo abbiamo cercato di favorire l'immutabilità degli oggetti e, un po' per fini didattici, un po' per capire a fondo come lavorare con queste strutture abbiamo deciso di modellare qualsiasi oggetto, almeno del model in maniera immutabile.
Nonostante la modellazione di alcuni campi in maniera mutabile fosse banale, e forse più naturale in alcune situazioni, come ad esempio nella modellazione della salute o della posizione di un animale, destinata a cambiare col passare del tempo, abbiamo deciso di costruire tutti gli oggetti del model in questa maniera.
Quando per comodità o per essere più veloci nella scrittura del codice soono state usate delle var, sono state immagazzinate al loro interno strutture immutabili.
Scala permette di maneggiare molto agevolmente questi tipi di dati e anche se con un dispendio di tempo leggermente maggiore, questo tipo di progettazione ci ha permesso di capire meglio il codice prodotto e di limitare i bug, o comunque di trovarli più facilmente.

### Disaccoppiamento della logica
Essendo le entità del model immutabili ci è venuto naturale modellare gli oggetti spesso come case class, relegando la logica complessiva in altre strutture di più alto livello, ovvero i Manager.
La nostra applicazione ha numerosi Manager, ognuno dei quali ha un compito abbastanza specifico e presenta nell'interfaccia un numero irrisorio di metodi.
Questa scelta è stata fatta per seguire una elle linee guida della progettazione del software, ovvero SOC ( Separation of Concern).
Ogni manager nella nostra applicazione ha un compito ben preciso ed è, grazie al TDD, corredato da numerosi test, esaustivi a piacere, che provassero il corretto funzionamento dell'applicativo.
La comodità del disaccoppiamento chea abbiamo deciso di adottare sta anche nel fatto che se in un futuro decidessimo di modificare il comportamento complessivo dell'applicazione potrebbe essere sufficiente modificare un solo manager o aggiungerne uno diverso per raggiungere il risultato voluto. 

 ### Scala e la programmazione funzionale
Come già anticipato è stato fatto largo uso del paradigma funzionale e in particolare si è cercato di sfruttare il più possibile funzioni di libreria (filter, map, ecc) cercando  cercando di rendere il codice il più dichiarativo possibile.
Oltre a funzioni di libreria per la manipolazione dei dati abbiamo anche usato funzioni ricorsive (tail) che ci hanno permesso di maneggiare abbastanza agevolmente le strutture immutabili definite nel model.
Teniamo inoltre a far notar che, la nostra applicazione è governata da un loop infinito, che in un videogioco sarebbe il game loop, tuttavia, un pò per gioco e un pò per accentuare l'aspetto funzionale abbiamo deciso di cambiare questa struttura ben nota e conosciuta e l'abbiamo fatta diventare una funzione ricorsiva, composta a sua volta da più funzioni che modificano le strutture dati combinando i manager e cambiando lo stato dei nostri oggetti.
corredato da pochi ma efficaci diagrammi
