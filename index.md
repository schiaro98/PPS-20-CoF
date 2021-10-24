# Circle Of Life

**Simone Luzi** - <simone.luzi@studio.unibo.it>

**Luca Rossi** - <luca.rossi147@studio.unibo.it>

**Davide Schiaroli** - <davide.schiaroli2@studio.unibo.it>

This report can also be viewed on the following website [Report](https://schiaro98.github.io/PPS-20-CoF)

- [Introduzione](#introduzione)
- [Processo di sviluppo](#processo-di-sviluppo)
  - [Modalità di divisione in itinere dei task](#modalità-di-divisione-in-itinere-dei-task)
  - [Meeting/Interazioni pianificate](#meetinginterazioni-pianificate)
  - [Modalità di revisione in itinere dei task](#modalità-di-revisione-in-itinere-dei-task)
  - [Scelta degli strumenti di test/build/continuous integration](#scelta-degli-strumenti-di-testbuildcontinuous-integration)
    - [Build](#build)
    - [Testing](#testing)
    - [Continuos Integration](#continuos-integration)
- [Requisiti](#requisiti)
  - [Requisiti di business](#requisiti-di-business)
  - [Requisiti utente](#requisiti-utente)
  - [Requisiti funzionali](#requisiti-funzionali)
  - [Requisiti non funzionali](#requisiti-non-funzionali)
      - [Cross-platform](#cross-platform)
      - [Performance](#performance)
      - [Interfaccia utente e usabilità](#interfaccia-utente-e-usabilità)
  - [Requisiti di implementazione](#requisiti-di-implementazione)
- [Design Architetturale](#design-architetturale)
  - [Architettura complessiva](#architettura-complessiva)
  - [Descrizione di pattern architetturali usati](#descrizione-di-pattern-architetturali-usati)
  - [Scelte tecnologiche cruciali ai fini architetturali](#scelte-tecnologiche-cruciali-ai-fini-architetturali)
    - [Immutabilità](#immutabilità)
    - [Disaccoppiamento della logica](#disaccoppiamento-della-logica)
    - [Scala e la programmazione funzionale](#scala-e-la-programmazione-funzionale)
- [Design di dettaglio](#design-di-dettaglio)
  - [Organizzazione dei package](#organizzazione-dei-package)
  - [Organizzazione del codice](#organizzazione-del-codice)
    - [Controller](#controller)
      - [Animal Manager](#animal-manager)
      - [Battle Manager](#battle-manager)
      - [Destination Manager](#destination-manager)
      - [Feed Manager](#feed-manager)
      - [Resource Manager](#resource-manager)
      - [Shift Manager](#shift-manager)
    - [Model](#model)
      - [Species](#species)
      - [Animal](#animal)
      - [Habitat](#habitat)
      - [Area](#area)
      - [Food](#food)
      - [Placeable e Point](#placeable-e-point)
  - [Pattern di progettazione](#pattern-di-progettazione)
    - [Factory](#factory)
    - [Singleton](#singleton)
    - [Strategy](#strategy)
    - [Flyweight (quasi)](#flyweight-quasi)
- [Implementazione](#implementazione)
  - [Simone Luzi](#simone-luzi)
    - [Model](#model-1)
    - [View](#view)
    - [Utility](#utility)
    - [Controller](#controller-1)
  - [Luca Rossi](#luca-rossi)
    - [Serializer](#serializer)
    - [Area](#area-1)
    - [Habitat](#habitat-1)
    - [Probability](#probability)
    - [ChooseHabitatGUI ( e logic)](#choosehabitatgui--e-logic)
    - [ResourceManager](#resourcemanager)
    - [ShiftManager](#shiftmanager)
      - [Point](#point)
    - [Statistics (e relativa GUI)](#statistics-e-relativa-gui)
  - [Davide Schiaroli](#davide-schiaroli)
    - [Controller](#controller-2)
      - [Battle Manager](#battle-manager-1)
      - [Destination Manager](#destination-manager-1)
      - [Feed manager](#feed-manager-1)
    - [Model](#model-2)
      - [Shape e RectangleArea](#shape-e-rectanglearea)
      - [Point](#point-1)
      - [Random e Grid Habitat](#random-e-grid-habitat)
- [Design di dettaglio](#design-di-dettaglio-1)
  - [Organizzazione dei package](#organizzazione-dei-package-1)
  - [Organizzazione del codice](#organizzazione-del-codice-1)
    - [Controller](#controller-3)
      - [Animal Manager](#animal-manager-1)
      - [Battle Manager](#battle-manager-2)
      - [Destination Manager](#destination-manager-2)
      - [Feed Manager](#feed-manager-2)
      - [Resource Manager](#resource-manager-1)
      - [Shift Manager](#shift-manager-1)
    - [Model](#model-3)
      - [Species](#species-1)
      - [Animal](#animal-1)
      - [Habitat](#habitat-2)
      - [Area](#area-2)
      - [Food](#food-1)
      - [Placeable e Point](#placeable-e-point-1)
  - [Pattern di progettazione](#pattern-di-progettazione-1)
    - [Factory](#factory-1)
    - [Singleton](#singleton-1)
    - [Strategy](#strategy-1)
    - [Flyweight (quasi)](#flyweight-quasi-1)
- [Retrospettiva](#retrospettiva)
  - [Risultato finale](#risultato-finale)
  - [Conclusione e commenti finali](#conclusione-e-commenti-finali)
  - [Sviluppi futuri](#sviluppi-futuri)
    - [Ui/Ux](#uiux)
    - [Simulazione](#simulazione)
  - [Descrizione sprint - Backlog](#descrizione-sprint---backlog)


# Introduzione


L'idea alla base del progetto è la creazione di un simulatore di un habitat, come ad esempio la savana, in cui degli animali terrestri possono svolgere azioni come spostarsi, mangiare, bere e cacciare.
In particolare l'habitat sarà composto da diversi tipi di aree (ad esempio con acqua potabile oppure zone rigogliose in cui crescono dei vegetali) e gli animali apparterranno ad una specie, che determinerà alcune caratteristiche come la taglia o la dieta (carnivora o erbivora). Ovviamente i carnivori potranno cacciare altri animali per mangiarne la carne mentre gli erbivori dovranno cercare i vegetali sparsi per la mappa.
La simulazione sarà visualizzabile in una grafica molto semplice, le aree saranno rappresentate attraverso dei rettangoli colorati a seconda della tipologia e si potranno osservare gli animali muoversi sotto forma di quadratini colorati (esemplari di una stessa specie avranno lo stesso colore), mentre il cibo sarà rappresentato con dei piccoli cerchi.
L'utente potrà scegliere tra una serie di habitat proposti ed avrà anche la possibilità di creare la sua specie personalizzata e di vedere come si comporta; in qualsiasi momento si potrà mettere la simulazione in pausa per controllare i parametri di ogni animale.

# Processo di sviluppo

Questo progetto ci ha permesso di lavorare seguendo alcune metodologie dello sviluppo *Agile*.
In particolare abbiamo dato molto peso alle seguenti caratteristiche fondamentali della gestione di progetti Agile,
derivanti proprio dal [Manifesto Agile](https://agilemanifesto.org/iso/it/principles.html):
* Brevi iterazioni di sviluppo note come sprint.
* Regolare ridefinizione delle priorità di lavoro.
* Approccio rapido e flessibile nell’indirizzare i cambiamenti di ambito.

Per quanto riguarda la fase progettuale infatti, abbiamo pensato di avere un prototipo funzionante il più presto possibile, a effettuare continue release e sopratutto di modellare l'applicativo in modo da renderlo facilmente estendibile e modificabile.
Questo punto ci ha notevolmente aiutato quando nelle fasi finali di progetto abbiamo voluto effettuare modifiche anche importanti al codice da noi prodotto. Abbiamo inoltre pensato di adottare parzialmente anche il "framework" Scrum. Non abbiamo infatti portato avanti il progetto con alcuni dei punti cardine di Scrum come i meeting giornalieri, ma abbiamo comunque voluto fare sprint settimanali, seguiti da una review degli stessi. Abbiamo poi utilizzato Trello come tool di collaborazione per controllare in tempo reale lo stato dei task, delle feature da implementare o bug da risolvere.

Per consentire uno sviluppo agile abbiamo organizzato il repository per avere due branch separati, il main che contenesse le release e il develop che contenesse tutte le modifiche alla nostra applicazione. Per poter rilasciare l'applicazione ad ogni fine sprint, abbiamo deciso che fosse necessario effettuare una pull request e un altro membro del gruppo doveva controllare (utilizzando anche i risultati della pipeline e confrontando le modifiche effettuate con i task ) che fosse tutto corretto. Abbiamo inoltre utilizzato un altro branch, docs, per tutto quello che riguarda la produzione della documentazione e della relazione finale.

## Modalità di divisione in itinere dei task

Dopo aver studiato i punti salienti del progetto ed esserci fatti un'idea approssimativa su cosa modellare e sul livello
di complessità abbiamo concordato quali fossero le entità da rappresentare.
In seguito dopo aver discusso tra di noi su quali fossero le più rilevanti e abbiamo suddiviso la loro modellazione e successiva implementazione.
Trovati i compiti ognuno di noi ne ha scelti uno o più da svolgere entro il meeting successivo.
Ad ogni meeting si discutevano i task svolti, motivando le scelte fatte e proponendo modifiche e migliorie e una volta conclusa questa prima parte si procedeva alla scelta di un nuovo task.

Man mano che il software cresceva in complessità questi meeting diventavano più vicini temporalmente, fino quasi a non essere neanche più definibili meeting ma piuttosto scambi di informazioni correlati da pochi ma efficaci note sulla prossima funzionalità da sviluppare o sul bug da correggere.
Questa modalità ha facilitato la comunicazione e il controllo sul progetto, permettendoci di conoscere bene il software prodotto
e ha velocizzato notevolmente il tempo da utilizzare per scrivere codice piuttosto che "perdere tempo" a dividerci i compiti.
Alla fine di ogni sprint si riguardava quindi il codice prodotto, ci si diceva quali task si sarebbero portati avanti e si procedeva all'implementazione.
In particolare si è cercato di seguire il più possibile l'approccio TDD.
Non lo si è portato all'estremo con l'approccio red-green-refactor, ma una volta create le interfacce o modellato le classi abbiamo proceduto con il testing in maniera più o meno esaustiva in base alla complessità e necessità.
Per il refactor non abbiamo usato particolari regole comuni, ma ogni errore o "Smeel code" veniva riportato tramite
bacheca Trello a chi doveva occuparsene, oppure riscritto direttamente da chi lo aveva incontrato.

## Meeting/Interazioni pianificate

Inizialmente sono stati pianificati 8 sprint settimanali, in modo da far coincidere l'ultimo meeting con la consegna del progetto.
Nel corso dello svolgimento del percorso abbiamo deciso di allungare il tempo tra un meeting e l'altro, rispettando sia il tempo di pausa di chiusura dell'università che gli impegni personali di tutti i partecipanti del progetto. Alla fine abbiamo deciso di effettuare due sprint in meno, quindi per un totale di 6, dove l'ultimo sprint ha previsto la consegna dell'applicativo.
Tutti gli sprint/meeting sono stati effettuati in modalità remota, utilizzando Trello come strumento per tenere traccia dello svolgimento degli sprint, dei task effettuati, delle feature da implementare e dei bug da risolvere .


## Modalità di revisione in itinere dei task

La revisione dei task, come già accennato, è stata effettuata al termine di ogni sprint ed è stata coadiuvata dalla pipeline di GitHub impostata in modo da controllare che tutti i test passassero.
In questa fase si provvedeva anche a dare dei commenti critici sul codice prodotto al fine di migliorare il prima possibile i punti critici con piccoli refactor che avrebbero permesso di diminuire il debito tecnico.

## Scelta degli strumenti di test/build/continuous integration
### Build
Per la fase di build del progetto è stato usato Sbt poichè è lo strumento nativo per la compilazione di sorgenti Scala.
Diversamente da Gradle infatti non richiede di aggiungere plugin o specificare alcuna configurazione poichè le impostazione vengono automaticamente create insieme al progetto. Non avendo particolari librerie o altro non abbiamo avuto bisogno di utilizzare strumenti più avanzati come Gradle. Inoltre sbt supporta nativamente ScalaTest, ovvero la libreria utilizzata per il Testing

### Testing
Per il testing abbiamo utilizzato la libreria ScalaTest, per la sua semplicità di uso e completezza. Abbiamo voluto anche testare alcune funzionalità avanzate rispetto alle classiche suite di test, utilizzando le flatSpec con i Matcher.
Inoltre è stato utilizzato il plugin sbt-coverage per poter controllare l'effettiva copertura dei test realizzati. Questa metrica infatti, per quanto non indicativa di quanto bene sia eseguita la fase di sviluppo dei test è comunque utile per poter avere una visione più ampia di quali porzioni di progetto siano più o meno soggette a test. Questo tool inoltre permette di visualizzare sotto forma di pagina web la copertura per ogni file, package e per l'intero progetto.

### Continuos Integration
Per distribuire e testare frequentemente la nostra applicazione abbiamo utilizzato la pipeline di Github, tramite le Github Actions. Abbiamo perciò elaborato un file yaml contenente le azione da compiere ad ogni push. In particolare abbiamo specificato di compilare ed eseguire tutti i test ad ogni push sui branch di sviluppo (develop) e di release (main).

# Requisiti
In questo capitolo verranno analizzati i requisiti del progetto, divisi nelle principali tipologie.

## Requisiti di business
I requisiti di business consistono nelle funzionalità di alto livello che l'applicazione dovrà fornire, in linea con le migliori speranze del committente riguardo al risultato finale.

Il progetto prevede lo sviluppo di un simulatore in cui degli animali terrestri vengono inseriti in un habitat specifico nel quale possono interagire tra di loro e con l'ambiente.
Un habitat è composto da più aree che possono essere anche di diverso tipo, e questo condiziona le azioni che gli animali possono compiere in quella zona.
Tutti gli animali appartenenti ad una stessa specie avranno delle caratteristiche in comune mentre ci saranno dei parametri specifici per ogni esemplare.
Per sopravvivere un animale deve periodicamente bere e mangiare del cibo a seconda della la propria dieta; i carnivori possono attaccare altri animali per procacciarsi il cibo.

Verrà anche fornita la possibilità di creare nuove specie di cui l'utente dovrà specificare le caratteristiche; prima di avviare la simulazione l'utente dovrà specificare quali specie inserire nell'habitat e in che quantità.

Durante la simulazione sarà possibile osservare tramite l'interfaccia grafica la mappa contenente le diverse tipologie di aree e gli animali muoversi all'interno di esse.
Si potranno consultare le statistiche di ogni animale in un qualsiasi momento e sarà sempre visibile il tempo trascorso dall'inizio della simulazione.
Attraverso degli appositi pulsanti quest'ultima potrà essere velocizzata e messa in pausa per studiare la situazione attuale e gli ultimi avvenimenti.
Al termine della simulazione verrà mostrata una grafica con le statistiche della simulazione.

## Requisiti utente
I requisiti utente indicano come gli stakeholders, ovvero gli utenti finali e le persone coinvolte nel progetto, utilizzeranno l'applicativo sviluppato e quali sono le azioni eseguibili.

L'utente potrà:
- osservare la simulazione dal pannello della schermata principale in cui verranno disegnati sotto forma di figure geometriche colorate le aree, gli animali e il cibo;
- consultare gli avvenimenti più importanti della simulazione in una area di testo sottostante;
- controllare la simulazione attraverso dei pulsanti che consentono di mettere in play, pausa e stop;
- velocizzare e rallentare la simulazione attraverso degli appositi pulsanti;
- creare una nuova specie attraverso una apposita finestra in cui personalizzare i vari parametri come nome, dieta, dimensione e forza;
- scegliere tramite una tabella di selezione quali specie inserire nella simulazione e quanti esemplari per ciascuna;
- scegliere di ambientare la simulazione tra quattro diverse tipologie di habitat (uno con aree ad hoc, uno privo di aree, e due con alcune aree a caso);
- visualizzare un diagramma contenente le statistiche della simulazione una volta terminata.

## Requisiti funzionali
I requisiti funzionali indicano le funzionalità che il sistema deve fornire e sono strettamente legati ai requisiti utente e al dominio del problema.

Il sistema permetterà di:
- creare una nuova specie e salvarla su file;
- recuperare le specie già create da file;
- recuperare i tipi di cibo da file;
- avviare una nuova simulazione con determinati esemplari per ogni specie;
- creare un habitat con quante aree si desidera, purché non sovrapposte;
- posizionare casualmente all'inizio della simulazione gli animali ed il cibo nelle zone dall'habitat che lo permettono;
- calcolare i risultati delle operazioni da eseguire in ogni step della simulazione, ovvero:
    - definire una destinazione per ogni animale;
    - spostare ogni animale in base alla sua destinazione e all'ambiente in cui si trova;
    - far mangiare e bere gli animali;
    - decrementare i parametri vitali degli animali a causa dello scorrere del tempo;
    - far cacciare i carnivori;
    - uccidere casualmente degli animali se accadono eventi inaspettati come incendi o malattie;
    - far crescere del cibo nelle zone dell'habitat che lo permettono;
- salvare le statistiche relative alla simulazione;
- mettere in pausa e velocizzare la simulazione.

## Requisiti non funzionali
I requisiti non funzionali riguardano particolari proprietà del sistema.

#### Cross-platform
Il sistema sarà eseguibile sui principali sistemi operativi desktop, ovvero Windows, Linux e MacOS.

#### Performance
Il sistema deve eseguire i calcoli di uno step della simulazione con 40 animali in meno di 1000 ms in una macchina con processore Intel core i7 7th Gen e 8 Gb di ram; in generale la simulazione sarà eseguibile, magari con dei rallentamenti, anche su macchine non particolarmente potenti.

#### Interfaccia utente e usabilità
L'interfaccia grafica deve essere progettata in maniera tale da essere subito comprensibile da un nuovo utente, senza bisogno di spiegazioni o tutorial.
Deve essere subito chiaro l'insieme di procedure necessarie per svolgere l'operazione desiderata e l'utente non deve essere disorientato da situazioni equivoche.
Anche il grafico con le statistiche finali deve essere facilmente comprensibile da tutti.

## Requisiti di implementazione
I requisiti di implementazione definiscono i vincoli sulle tecnologie da utilizzare (ad esempio i linguaggi di programmazione, i tool e i software), e sulla qualità interna del codice.

Il sistema presenterà le seguenti caratteristiche:
* **Linguaggio di programmazione**: Scala nella versione 2.13.6;
* **Metodologia**: adottare una mentalità Agile;
* **Build tool**: sbt, dove verranno gestite tutte le dipendenze delle librerie utilizzate;
* **Test**: ScalaTest, utilizzando lo stile FunSuite (a parte qualche caso in cui si è adottato FlatSpec);
* **CI**: Github Actions, principalmente per controllare la corretta esecuzione dei test ad ogni push;
* **Coverage**: sbt-scoverage;
* **Grafica**: Scala Swing per realizzare le interfacce grafiche;
* **Patterns**: MVC per la struttura generale del progetto, inoltre devono essere utilizzati i pattern presenti in letteratura ogni qual volta se ne presenti la possibilità oltre ad evitare i code smell.

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

Partendo dai requisiti abbiamo dapprima sviluppato un diagramma una prima stesura prototipale dei componenti principali dell'applicazione così da notare eventuali scelte errate in fase di progettazione
![Diagramma UML](/resources/UML1.png "Primo diagramma UML")

In questo diagramma abbiamo definito alcuni punti fondamentali, come la relazione tra animale e specie, la composizione di Habitat in aree, la differenziazione tra tipo di cibo e l'esistenza di zone dell'habitat non percorribili.

![Diagramma UML](/resources/UML2.png "Correzione primo diagramma UML")
In questo secondo diagramma possiamo invece vedere come vengano modificati alcuni aspetti, come ad esempio introducendo il concetto di *visualizable*, ovvero un entita che verrà effettivamente rappresentata nella mappa e quindi visibile. Un esempio sono il cibo e gli animali.

## Descrizione di pattern architetturali usati
Per quanto riguarda l'architettura è stato scelto di modellare l'applicazione attraverso il pattern MVC. In questo modo abbiamo potuto suddividere l'applicazione in 3 componenti *loosely coupled*. Il componente **View** infatti si occupa solamente di visualizzare le informazioni in una mappa. Il **Model** invece si occupa di modellare le entità di gioco come ad esempio gli Animali, gli Habitat o i cibi e di gestire i dati ricevuti dall'utente tramite la Gui. Il **Controller** invece ha il compito di modificare i dati forniti dal Model, elaborandoli e restituendoli aggiornati alla Gui.
Questa scelta architetturale ci consentirebbe in futuro di poter cambiare uno dei tre componenti principali senza dover riscrivere l'intera applicazione. Un esempio di possibile cambiamento potrebbe essere l'aggiornameto con un nuovo framework per l'interfaccia grafica come ScalaFX oppure ad una versione web.
![Diagramma Model View Controller](/resources/MVC.png "Diagramma MVC")

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

# Design di dettaglio
In questo capitolo andremo ad esplorare più nel dettaglio  le scelte progettuali che sono state attuate.

## Organizzazione dei package

![Diagramma dei package](https://github.com/schiaro98/PPS-20-CoF/blob/docs/resources/package.png)

## Organizzazione del codice
### Controller
Il controller contiene la logica necessaria all'esecuzione della simulazione, come la gestione del ciclo di vita degli animali, dei combattimenti, della gestione delle risorse e degli spostamenti.

#### Animal Manager
E' il controller degli animali presenti nella simulazione.
Si occupa di istanziare gli animali che sono presenti all'inzio della simulazione, aggiornare i valori degli animali ad ogni ciclo e ne fa morire alcuni se si verificano eventi inaspettati (ad esempio incendi o epidemie).

#### Battle Manager
E' il controller delle battaglie tra due animali.
Contiene un metodo principale battle che si occupa di calcolare ricorsivamente per tutti gli animali gli eventuali scontri tra animali.
E' necessario considerare alcuni fattori:
* Solo i carnivori possono incominciare una battaglia
* Le vittime (erbivori) per essere attaccate devono essere visibili, ovvero rientrare nella soglia del campo    visivo dell'animale attaccante
* Per il calcolo dell'esito vengono calcolate varie probabilità in base a distanza, stazza, forza.
  Per ogni battaglia è necessario aggiornare gli animali, eliminando gli animali deceduti e rilasciando le risorse

#### Destination Manager
E' il controller che descrive i movimenti dei vari animali.
Per ogni animale, vengono cercate le risorse (aree d'acqua dove bere o cibo) visibili e viene ritornata la posizione dove devono dirigersi. Se non ci sono risorse disponibili all'interno del campo visivo viene scelta una posizione casuale dove dirigersi.

#### Feed Manager
E' il controller che permette di consumare le risorse e permette agli animali di bere, sempre considerando la vicinanza degli animali alle risorse. Si occupa anche della rimozione del cibo mangiato dagli animali.

#### Resource Manager

#### Shift Manager

### Model
#### Species
Species rappresenta una specie animale e contiene campi che ne pregiudicano il comportamento all'interno della simulazione, come ad esempio "alimentationType" ovvero la dieta. Infatti gli animali possono compiere azione diverse se sono carnivori oppure erbivori. In oltre ogni specie ha una dimensione, una forza e un raggio visivo.

#### Animal
Animal raprresenta un'istanza di Species e contiene alcune informazione che variano da specie a specie, come ad esempio la vita (health) e la sete (thirst). Entrambi questi parametri vengono decrementati col passare del tempo e aggiornati quando l'animale mangia o beve. Durante il ciclo di vita dell'animale se uno dei parametri arriva a 0 l'animale muore, rilasciando risorse nella mappa

#### Habitat
Habitat rappresenta una composizione di aree di diverso tipo, dove gli animali possono muoversi e cibarsi.
Ogni habitat ha una probabilità di eventi inaspettati che determina la possibilità che un animale muoia per cause non calcolate nella simulazione.

#### Area
Area rappresenta una area all'interno di un Habitat. Ogni area ha un tipo (Water, Rock, Volcano) che può essere camminabile o meno e una rappresentazione logica dell'area occupata all'interno dell'habitat (attraverso il campo Rectangle). In caso l'area sia fertile, allora l'area avrà anche la possibilità di far crescere spontaneamente del cibo al suo interno

#### Food
Food rappresenta un tipologia di cibo, con una tipologia (Meat o Vegetable) e una quantità di energia che fornisce all'animale una volta mangiata.

#### Placeable e Point
Placeable è un trait che abbiamo usato per modellare tutti quei componenti che necessitavano di descrivere una posizione
all'interno della mappa, come i cibi e gli animali.
Il suo unico campo è un Point, ovvero una tupla due numeri.
Point inoltre fornisce numerosi metodi che permettono di verificare varie condizioni di uguaglianza o meno tra due Point,
tra un Point e un asse cartesiano e di calcolare la distanza tra due Point

## Pattern di progettazione
### Factory
Scala ci permette di implementare il design pattern factory col minimo sforzo.
Il costrutto che abbiamo usato nella quasi totalità dei file è stato quello di dichiarare un trait, un companion object con la apply (spesso con valori di default) e una classe privata all'interno del companion object che implementasse il trait.
Questo ci ha permesso di nascondere l'implementazione delle classi e dei metodi e di istanziare molto più comodamente gli oggetti.

### Singleton
Il pattern Singleton ci viene fornito gratuitamente da scala.
In particolare ha avuto una grandissima utilità in quelle classi di utility, utilizzate da più classi e che avessero una logica comune, più nel dettaglio, il logger e le statistiche sono risultati molto semplici da sviluppare grazie ad un costrutto del genere.

### Strategy
Il core funzionale di scala permette una definizione veloce di lambda, utilizzate a volte come strategy.

### Flyweight (quasi)
Abbiamo usato delle strutture che ci permettono di usare il pattern Flyweight.
Il pattern Flyweight consiste nel depositare parti fissi comuni e immutabili di dati in oggetti immutabili che sono usati come campi da altri oggetti che modellano la parte dinamica dell'esecutivo.
Nel nostro codice abbiamo diverse strutture organizzate come sopra descritto, ma come anticipato, abbiamo deciso di utilizzare solamente oggetti immutabili, anche le parti dinamiche sono modellate tramite oggetti immutabili.
Quindi il vantaggio di performance dato dal pattern flyweight viene un po' perso ma non avendo requisiti sotto quel punto di vista non ci siamo posti il problema e se in futuro dovessimo decidere di implementare il pattern flyweight nella sua totalità sarebbe molto facile convertire il codice prodotto. 

# Implementazione

In questo capitolo verrà presentata la suddivisione del lavoro; gni studente infatti elencherà gli elementi del progetto che ha realizzato ai quali aggiungerà alcuni aspetti implementativi dove occorrono.

## Simone Luzi

Inizialmente mi sono dedicato allo sviluppo dei primi elementi di Model del progetto, un compito molto delicato visto che sarebbero stati adoperati in gran parte delle computazioni della simulazione.
In seguito ho lavorato ad alcuni aspetti di View, in particolare a come far visualizzare lo stato della simulazione (ovvero i diversi tipi di aree, gli animali di diverse specie ed il cibo) e i parametri degli animali.
In contemporanea ho prodotto alcuni metodi di utility per gli animali ed ho ampliato il serializzatore per poter decodificare anche aree, probabilità e colori.
Successivamente mi sono occupato di due elementi di Controller, ovvero il gestore degli animali nella simulazione ed il game loop.

### Model
Dopo aver definito le caratteristiche degli elementi visualizzabili (che hanno un colore, **Visualizable**) e di quelli posizionoabili (che hanno una posizione, **Placeable**) ho potuto realizzare **Species**, **Animal**, **Food** e **FoodType**, concetti fondamentali per la logica della simulazione; essendo elementi del sistema molto importanti questi ultimi hanno subito diverse fasi di modifica e refactoring.
Ognuno dei quattro è stato realizzato come sealed trait + companion object nel quale l'apply restituisce una classe privata, in questo modo, rispettivamente, si definiscono le funzionalità, i parametri necessari per la creazione e si realizza una delle possibili implementazioni che può essere facilmente sostituita con un'altra.
Spesso si sono dovute modellare delle caratteristiche che potevano essere solamente una di diverse possibilità; per realizzare questi elementi ho scelto di utilizzare una delle particolarità di Scala, i case object. L'alternativa sarebbe stata utilizzare le enumerazioni che avrebbero fornito come vantaggio la possibilità di restituire tutti i possibili valori ma al costo di avere del codice più verboso e meno immediato.
Altre peculiarità di Scala di cui ho fatto uso e che si sono rivelati moltu utili sono i valori di default, i named arguments, gli Option ed i match cases.

### View
Ho realizzato, insieme all'aiuto di Schiaroli, l'interfaccia grafica in cui osservare la simulazione (**SimulationGUI**), costituita da tre parti, in alto il pannello con la vera e propria simulazione, al centro il tempo trascorso e i pulsanti per gestirla ed in basso l'area di testo in cui vengono riportati gli ultimi eventi. Per rendere il file il più leggibile possibile ho racchiuso tutto il codice necessario alla creazione dei pulsanti in una case class in cui si definiscono le reazioni al click grazie all'utilizzo di funzioni higher-order.

All'interno del pannello (**SimulationPanel**), oltre a disegnare le figure geometriche colorate per aree, animali e cibi, ho creato i mouse listener utilizzati per mostrare i popup contenenti i parametri di ogni animale; una particolarità di questo **AnimalPopup** è che, per essere utile, il frame che lo contiene deve essere posizionato vicino al mouse posizionato sopra all'animale che si vuole osservare, e siccome il frame contenente la simulazione può essere spostato, il popup può avere una posizione diversa ogni volta. Per questo motivo ho utilizzato la strategia "call-by-name" che permette di valutare ogni volta i parametri per posizionare il popup, infatti questi ultimi cambieranno in base ad un offset applicato alla location del frame.

### Utility
**AnimalUtils** contiene dei metodi di utility utilizzati per ottenere un punto random dell'habitat in cui è possibile piazzare un animale, i pixel utilizzati per un animale di una certa dimensione e i vertici del quadrato utilizzato per disegnare un animale.
Per renderli il più funzionali possibili sono stati utilizzate ricorsioni tail, match cases e funzioni di libreria apposite.

### Controller
**AnimalManager** contiene gli animali presenti all'interno della simulazione, si occupa di creare e posizionare gli animali delle specie scelti dall'utente nel numero selezionato, di decrementare i parametri di fame e sete degli animali per simulare il passaggio del tempo e calcolare gli eventi inaspettati che potrebbero casualmente uccidere degli animali.
Visto che sia l'aggiornamento dei parametri vitali che il calcolo degli eventi inaspettati potrebbero causare la morte degli animali, in entrambi casi ho utilizzato una pair come valore di ritorno in modo da poter comunicare sia gli animali ancora in vita che l'eventuale carne creata a causa della loro morte.
Per rendere il codice il più funzionale possibile è stato creato un metodo privati ad hoc con higher-order per riusare del codice ed anche in questo caso ho utilizzato ricorsioni tail, match cases e funzioni di libreria come filter, foreach e map.

**GameLoop** rappresenta il cuore del sistema, infatti definisce tutte le operazioni da svolgere ad ogni step della simulazione, crea e aggiorna l'interfaccia grafica dove viene visualizzata e fornisce i metodi per metterla in play/pausa/stop e modificarne la velocità.
Questa classe è stata progettata come Runnable in modo da poter essere eseguita in un nuovo thread; in questo modo i calcoli eseguiti non andranno ad inficiare sulla reattività della GUI.
Il loop vero è proprio è stato implementato con una ricorsione tail; in questo modo, oltre ad ottenere una ottimizzazione, Scala assicura che non si verifichi mai un errore di stack overflow.
Dopo la sua implementazione ho rifattorizzato il codice insieme a Rossi per rendere più leggibili e funzionali i calcoli da effettuare ad ogni step; si è scelto di suddividerli in metodi che prendono compe parametri una pair di AnimalManager e ResourcesManager che restituiscono aggiornati come valore finale, in questo modo è stato possibile richiamarli uno di seguito all'altro.

## Luca Rossi
Dopo aver dato una rappresentazione ad alto livello del dominio applicativo nella fase di modellazione ho inziato a scrivere il codice elencato di seguito:

1. Serializer
2. Area
3. Habitat
4. Probability
5. ChooseHabitatGUI (e Logic)
6. ResourceManager
7. ShiftManager
8. Statistics (e relativa GUI)

Ognuno dei file sopra citato è corredato dall'apposita classe di test che testa più o meno approfonditamente i desiderata delle classi.

### Serializer
La prima classe ad aver modellato è stato il serializzatore, che nella sua versione di base serializza oggetti in formato json tramite la libreria di java **GSON** .
Ho deciso di modellare il serializzatore come un sealed trait + companion object e classi private, questa strutturazione verrà riutilizzata anche nella maggior parte dei file scritti nel progetto.
Ho costruito il serializzatore in modo tale che avesse pochi metodi molto facilmente riutilizzabili testando ognuno di essi per essere sicuro del corretto funzionamento.
Purtroppo GSON non è in grado di rappresentare strutture dati complesse, o meglio, può farlo ma solo definendo dei serializzatori/deserializzatori customizzati che destrutturino gli oggetti in un insieme di dati primitivi JSON e a tal proposito ho creato un paio di classi specifiche, una per **Species** e una per **Food**.
Queste due tipologie di serializzatori possono essere istanziate tramite una **apply** che preso un case object ritornano un serializzatore (default, di specie  o di food).
Questo file è stato poi ampliato dai miei colleghi nel momento della necesità di un altro tipo di serializzatore customizzato.

### Area
Dopo lo sviluppo di serializer ho sviluppato la prima versione del file **Area**.
Un area modella una parte di spazio all'interno del nostro **Habitat**.
Le aree attualmente possono essere di 4 tipi, alcune di esse (le aree fertili) possono far crescere del cibo, in particolare dei vegetali, mentre le altre sono aree che gli animali non possono calpestare, l'habitat può anche non avere zone di interesse, che quindi non sono modellate come aree.
Tutte le aree hanno lo stesso comportamento di base, ovvero rappresentare un tipo di spazio, non cambiano nel tempo e si prestano bene ad essere modellate come case class.
Prima di scrivere il codice che le rappresentasse ho però modellato il trait.
Per rappresentare il tipo delle aree (Fertile, Water, Rock, Volcano) sono stati usati i case object preferibili alle enum in scala.
Le aree sono rettangolari e composte da un angolo in alto a sinistra e da uno in basso a destra e per evitare problemi in fase di sviluppo ho usato il costrutto ***require***  che avrebbe generato eccezione nel caso di angoli mal posizionati o illegali.
Questo require veniva eseguito nel trait **Area**, si è poi deciso, di incapsulare il concetto di area rettangolare e quel require è stato spostato nella classe (**RectangleArea**).
Per Modellare un'area Fertile, che potesse far crescere del cibo ho deciso di non estendere la classe area, ma di utilizzare un trait che modellasse la creazione di cibo, **GrowFood**.
Questo trait è poi stato usato come mixin in concomitanza con Area per modellare aree in grado di far crescere cibo.


### Habitat
Dopo la creazione delle aree ho provveduto alla creazione dell'**Habitat**, che per definizione doveva contenere al suo interno delle aree, avere delle dimensioni e una certa probabilità che potessero avvenire degli eventi inaspettati dannosi per gli animali.
Ho quindi costruito il file con la struttura già usata in precedenza e qualche test di base.
Abbiamo poi deciso quali deciso che tipi di habitat costruire e le varie classi e apply che ne avrebbero poi permesso la creazione sono state fatte dai miei colleghi.
Anche in questa classe ho usato dei require per fare in modo che due aree non si accavallino e
che le aree non escano dall'habitat.

### Probability
Per la probabilità ho costruito un semplicissimo trait che prende un *Int* in input fra zero e cento.
Chiamando il metodo **calculate** su di esso si ha un *Boolean* in uscita.
Sono stati poi aggiunti altri metodi  da Davide Schiaroli.

### ChooseHabitatGUI ( e logic)
Dopo aver programmato fondamentalmente in backend ho avuto la parte di frontend di scelta della gui, non ci sono particolari aspetti implementativi da discutere.

### ResourceManager
Il **ResourceManager** è il luogo in cui risiedono tutte le risorse.
È incaricato della creazione di tutti i cibi all'interno dell'Habitat a inizio simulazione ed è il punto in cui vengono riposti ad ogni aggiornamento.
Per modellare la crescita dei cibi è stato creato il solo metodo grow che, in base alla probabilità (**fertility**) delle aree fertili istanzia nuovo cibo nella mappa.
Come già accennato per favorire l'immutabilità in caso di crescita di cibi anziché restiturie un nuovo set di cibi si restituisce interamente un nuovo ResourceManager con la lista di cibi aggiornata.
Per rendere il codice più leggibile ho usato i **type** per definire un alias per Seq[Food].
Inoltre dopo una prima implementazione ho ristrutturato il codice per far sì che la grow fosse solo una concatenazione di filter e map.

### ShiftManager
Lo **ShiftManager** è il manager incaricato allo spostamento degli animali.
Questa versione dello ShiftManager richiede che nessun animale che gli viene passato sia all'interno di un'area non calpesabile, pena IllegalArgumentException.
Il Manager prende una coppia (Animal, Point) (o una Map) e chiamando il metodo walk viene restituita una verisione aggiornata dello stesso.
Sono riuscito a rappresentare questa classe in modo abbastanza sintetico e ordinato grazie a costrutti quali, filter, map, fold e for comprehension e dovendo questo metodo fare calcoli sulle distanze tra punti, che sarebbero potuti risultare pesanti, ho usato anche la parallelizzazione di scala che permette in modo molto sintetico e veloce di parallelizzare operazioni su strutture di dati.

#### Point
Questa classe non è stata implementata da me ma per la scrittura dello ShiftManager ho alcuni metodi e ho modificato il calcolo della distanza.
Prima la distanza era calcolata come distanza euclidea tra due punti ma per renderlo un pò più veloce ho eliminato la radice quadrata che sarebbe stato un fattore comune per tutte quante le operazioni.

### Statistics (e relativa GUI)
Essendo le statistiche una raccolta di dati provenienti da diverse fonti mi è parso opportuno definirlo come **Singleton**.
È particolarmente facile utilizzare questo costrutto in scala e questo ha consentito una scrittura molto veloce di questa componente.
Le statistiche sono costituite da una misura di tempo che può essere incrementata dalla chiamata all'apposito metodo e una case class con tutti i dati di interesse.
In particolare questa struttura usa una mappa da tempo a statistiche in modo da capire cosa è successo in ogni momento della simulazione.
La relativa gui si occupa solo della visualizzazione di questi dati su un LineChart.


## Davide Schiaroli

Inizialmente, prima di iniziare con la analisi del dominio del problema, mi sono occupato di alcuni aspetti di CI/CD e di setup del progetto.
Ho creato poi il repository GitHub e il progetto Scala/Sbt, infine mi sono occupato della pipeline, che inizialmente aveva il solo compito di verificare che il progetto compilasse, poi estesa ad eseguire automaticamente tutti i test.
Ho anche valutato l'opportunità di usare uno strumento come Scalafix per la rifatorizzazione del codice, ma abbiamo poi optato per utilizzare lo strumento di default dell'IDE che abbiamo utilizzato, cioè Intellij IDEA. Nel caso avessimo usato un IDE diverso tra i componenti del gruppo, sarebbe sicuramente la scelta di uno strumento di refactor automatico sarebbe stata più idonea.
Durante le fasi intermedie del progetto ho aggiunto alcune funzionalità alla pipeline come l'esecuzione di test e release di Jar automatizzata.
Inizialmente mi sono occupato della View, sviluppando un primo prototipo di quello che poi è diventata la Gui della nostra applicazione utilizzando la libreria scalaSwing. Ho optato per questa libreria invece di ScalaFx per la mancanza di documentazione che avrebbe senz'altro portato a numerosi problemi in futuro. Successivamente ho sviluppato la logica degli animali, cioè come venivano usati all'interno della applicazione quando inseriti da utente.
Successivamente mi sono occupato dello sviluppo del codice necessario a costruire le mappe random e a griglia.
Dopo avere visto questi aspetti di View mi sono occupato principalmente di sviluppare i Controller per le varie fasi.
E' stato necessario creare dei controller per la fasi di battaglia, di movimento (solamente per quello che riguarda le decisione sulle destinazioni)
e di alimentazione (permettere ovvero di far mangiare e bere gli animali).
Ho inoltre lavorato sulle classi Point, sulla implementazione di un Logger e nella creazione delle aree negli Habitat.

### Controller
Per quanto riguarda l'implementazione del Controller, mi sono occupato della realizzazione dei manager di combattimento, alimentazione e
destinazione. Per tutti e 3 i manager si è deciso di costruire meno metodi possibili, cercando di evitare side effects e sopratutto cercando di costruire metodi tail-recursive.

#### Battle Manager
Questo manager aveva il compito di prendere come input gli animali presenti nella mappa e di far combattere gli animali che rientravano in una distanza molto limitata, chiamata *Hitbox*. Un'ulteriore filtro che andava considerato è che solamente gli animali carnivori potessero dare il via ad una battaglia verso altri animali. Per avere un responso randomico della battaglia, ma ponderando comunque alcuni fattori come taglia, forza e distanza tra animali,ho creato dei metodi che calcolassero la probabilità utilizzando questi discriminanti. Ogni metodo dopo aver calcolato la probabilità, mi restituiva un valore Booleano. Se il numero di *true* superava il numero di *false* la battaglia era vinta dall'attaccante, altrimenti dal difensore. Questo metodo, oltre a restituire la lista di animali aggiornata, cioè gli animali presi in input meno quelli deceduti in combattimento, restituiva anche tutte le risorse rilasciate dagli animali deceduti.
Quando una battaglia va a termine, viene anche visualizzato nella area di testo della simulazione l'esito della battaglia, questo per dare un'idea di cosa succede nei vari momenti della simulazione.

#### Destination Manager
Questo manager invece si occupa di dare delle posizioni agli animali verso il quale andare. Sostanzialmente serve ad indicare agli animali le prede da attaccare, il cibo (carne e verdure) da mangiare e le zone d'acqua da dove bere.
Questo metodo, nella ricerca di eventuali punti di interesse, dava la priorità alle zone d'acqua in quanto più rare e più utili per la sopravvivenza.
Se non vengono trovate zone d'acqua, vengono cercate, in base alla dieta dell'animale eventuali verdure, oppure carni se si tratta di un carnivoro. Inoltre se si tratta di carnivori vengono anche segnalate le prede vicine.

#### Feed manager
Il feed manager si occupa di gestire effettivamente le fasi di alimentazione degli animali nella mappa. Come negli altri manager sono stati utilizzati solo metodi tail recursive. Anche qua venivano eseguiti controlli sulle diete degli animali, per permettere ad erbivori e carnivori di mangiare solo cibi adeguati e sulle distanze tra animali e aree d'acqua e verso cibi.

### Model
Nella parte di Model mi sono occupato di parti legate alla **View** come le RectangleArea e Shape, al concetto di Point, alla creazione di Habitat randomici e a griglia

#### Shape e RectangleArea
Shape è un Trait che viene implementato dalle classi Rectangle e Circle. In particolare è necessario che le classi implementino il metodo **Draw** che prendi in input un parametro di tipo Graphics2D, ovvero API di Java utilizzata per disegnare grafiche in 2 dimensioni. La classe Rectangle è stata usata per disegnare le aree e gli animali mentre la classe Circle per le verdure e la carne.
RectangleArea contiene alcuni metodi utili a verificare alcune condizioni come ad esempio se due rettangoli fanno *overlap* ovvero si sovrappongono e per creare le aree in modo random

#### Point
Point è la modellazione di un punto nella mappa e permette, oltre ad evitare di utilizzare Tuple2 o metodi che prendono due elementi, di rendere chiaro l'intento quando viene usato come parametro o come campo in una classe. Contiene inoltre numerosi metodi di utility per verificarne la posizione rispetto ad altri punti, rispetto ad assi cartesiano oppure per la creazione randomica.

#### Random e Grid Habitat
Queste due diverse implementazioni di Habitat sono utili a verificare come gli animali si comportano in presenza di habitat diversi. L'habitat random è stato realizzato in modo da creare 4 aree Random di dimensione e tipologia variabile. E' possibile che creando aree totalmente random non esistano aree d'acqua, in questo caso gli animali muoriranno quando il loro livello di sete arriverà a zero. Nella area a griglia ho pensato invece di eliminare questo problema creando solamente aree d'acqua o fertili, in modo che ci sia la possibilità che gli animali continuino a vivere per molto tempo.

# Design di dettaglio
In questo capitolo andremo ad esplorare più nel dettaglio  le scelte progettuali che sono state attuate.

## Organizzazione dei package

![Diagramma dei package](https://github.com/schiaro98/PPS-20-CoF/blob/docs/resources/package.png)

## Organizzazione del codice
### Controller
Il controller contiene la logica necessaria all'esecuzione della simulazione, come la gestione del ciclo di vita degli animali, dei combattimenti, della gestione delle risorse e degli spostamenti.

#### Animal Manager
E' il controller degli animali presenti nella simulazione.
Si occupa di istanziare gli animali che sono presenti all'inzio della simulazione, aggiornare i valori degli animali ad ogni ciclo e ne fa morire alcuni se si verificano eventi inaspettati (ad esempio incendi o epidemie).

#### Battle Manager
E' il controller delle battaglie tra due animali.
Contiene un metodo principale battle che si occupa di calcolare ricorsivamente per tutti gli animali gli eventuali scontri tra animali.
E' necessario considerare alcuni fattori:
* Solo i carnivori possono incominciare una battaglia
* Le vittime (erbivori) per essere attaccate devono essere visibili, ovvero rientrare nella soglia del campo    visivo dell'animale attaccante
* Per il calcolo dell'esito vengono calcolate varie probabilità in base a distanza, stazza, forza.
  Per ogni battaglia è necessario aggiornare gli animali, eliminando gli animali deceduti e rilasciando le risorse

#### Destination Manager
E' il controller che descrive i movimenti dei vari animali.
Per ogni animale, vengono cercate le risorse (aree d'acqua dove bere o cibo) visibili e viene ritornata la posizione dove devono dirigersi. Se non ci sono risorse disponibili all'interno del campo visivo viene scelta una posizione casuale dove dirigersi.

#### Feed Manager
E' il controller che permette di consumare le risorse e permette agli animali di bere, sempre considerando la vicinanza degli animali alle risorse. Si occupa anche della rimozione del cibo mangiato dagli animali.

#### Resource Manager

#### Shift Manager

### Model
#### Species
Species rappresenta una specie animale e contiene campi che ne pregiudicano il comportamento all'interno della simulazione, come ad esempio "alimentationType" ovvero la dieta. Infatti gli animali possono compiere azione diverse se sono carnivori oppure erbivori. In oltre ogni specie ha una dimensione, una forza e un raggio visivo.

#### Animal
Animal raprresenta un'istanza di Species e contiene alcune informazione che variano da specie a specie, come ad esempio la vita (health) e la sete (thirst). Entrambi questi parametri vengono decrementati col passare del tempo e aggiornati quando l'animale mangia o beve. Durante il ciclo di vita dell'animale se uno dei parametri arriva a 0 l'animale muore, rilasciando risorse nella mappa

#### Habitat
Habitat rappresenta una composizione di aree di diverso tipo, dove gli animali possono muoversi e cibarsi.
Ogni habitat ha una probabilità di eventi inaspettati che determina la possibilità che un animale muoia per cause non calcolate nella simulazione.

#### Area
Area rappresenta una area all'interno di un Habitat. Ogni area ha un tipo (Water, Rock, Volcano) che può essere camminabile o meno e una rappresentazione logica dell'area occupata all'interno dell'habitat (attraverso il campo Rectangle). In caso l'area sia fertile, allora l'area avrà anche la possibilità di far crescere spontaneamente del cibo al suo interno

#### Food
Food rappresenta un tipologia di cibo, con una tipologia (Meat o Vegetable) e una quantità di energia che fornisce all'animale una volta mangiata.

#### Placeable e Point
Placeable è un trait che abbiamo usato per modellare tutti quei componenti che necessitavano di descrivere una posizione
all'interno della mappa, come i cibi e gli animali.
Il suo unico campo è un Point, ovvero una tupla due numeri.
Point inoltre fornisce numerosi metodi che permettono di verificare varie condizioni di uguaglianza o meno tra due Point,
tra un Point e un asse cartesiano e di calcolare la distanza tra due Point

## Pattern di progettazione
### Factory
Scala ci permette di implementare il design pattern factory col minimo sforzo.
Il costrutto che abbiamo usato nella quasi totalità dei file è stato quello di dichiarare un trait, un companion object con la apply (spesso con valori di default) e una classe privata all'interno del companion object che implementasse il trait.
Questo ci ha permesso di nascondere l'implementazione delle classi e dei metodi e di istanziare molto più comodamente gli oggetti.

### Singleton
Il pattern Singleton ci viene fornito gratuitamente da scala.
In particolare ha avuto una grandissima utilità in quelle classi di utility, utilizzate da più classi e che avessero una logica comune, più nel dettaglio, il logger e le statistiche sono risultati molto semplici da sviluppare grazie ad un costrutto del genere.

### Strategy
Il core funzionale di scala permette una definizione veloce di lambda, utilizzate a volte come strategy.

### Flyweight (quasi)
Abbiamo usato delle strutture che ci permettono di usare il pattern Flyweight.
Il pattern Flyweight consiste nel depositare parti fissi comuni e immutabili di dati in oggetti immutabili che sono usati come campi da altri oggetti che modellano la parte dinamica dell'esecutivo.
Nel nostro codice abbiamo diverse strutture organizzate come sopra descritto, ma come anticipato, abbiamo deciso di utilizzare solamente oggetti immutabili, anche le parti dinamiche sono modellate tramite oggetti immutabili.
Quindi il vantaggio di performance dato dal pattern flyweight viene un po' perso ma non avendo requisiti sotto quel punto di vista non ci siamo posti il problema e se in futuro dovessimo decidere di implementare il pattern flyweight nella sua totalità sarebbe molto facile convertire il codice prodotto. 

# Retrospettiva
## Risultato finale
Qui possiamo vedere come si presenta l'applicazione, con la spiegazione di tutti i campi presenti nelle varie parti della Gui.

Nel primo frame possiamo aumentare o diminuire la quantità di animali per ogni specie in modo da rendere la simulazione più movimentata o meno. Sono poi presenti i pulsanti per creare una nuova specie o per passare alla creazione di una nuova specie.
![View scelta animali](/resources/view1.png "View scelta animali")

Nel pannello di creazione specie possiamo specificare alcuni campi come nome, forza, vista, taglia e dieta della specie. E' presente poi un pannello per la scelta del colore con cui verrà disegnato l'animale nella mappa.
![View creazione nuova specie](/resources/view5.png "View creazione nuova specie")

Se invece procediamo alla scelta di un habitat, possiamo scegliere tra l'habitat di default, oppure scegliere tra habitat con zone random, con zone a griglia oppure vuoto. Per ogni habitat possiamo specificare una probabilità che avvengano eventi casuali che possono uccidere o meno gli animali presenti. Per tutti gli habitat meno quello casuale è possibiile specificare una dimensione per la mappa.
![View scelta habitat](/resources/view2.png "View scelta habitat")

Una volta partita la simulazione è possibile visualizzare, oltre ovviamente alla simulazione che avviene nella parte superiore, gli eventi che accadono e fermare o velocizzare il tempo tramite l'area di testo e i pulsanti presenti nella parte inferiore.
![View simulazione](/resources/view3.png "View simulazione")

Una volta completata la simulazione, ovvero quando non sono più presenti animali, viene presentato un report contenente le statistiche della simulazione. Ovvero di come i dati riguardanti il cibo e gli animali presenti siano evoluti nel tempo.
![View statistiche](/resources/view4.png "View statistiche")
## Conclusione e commenti finali
In conclusione ci riteniamo soddisfatti del lavoro svolto, sulle metodologie utilizzate e sul risultato finale.
L'obiettivo più graficante secondo noi è stato quello di riuscire a costruire una applicazione pensando in modo funzionale.
Col passare del tempo infatti, prendendo dimestichezza con un nuovo linguaggio, siamo riusciti ad esprimere concetti
anche complicati con strumenti tipici della programmazione funzionale. Questa cosa è evidente nel codice sorgente dei manager e del gameLoop.
I strumenti tipici della FP sono stati l'utilizzo delle funzioni ricorsive, il pattern matching e l'utilizzo raro di variabili mutabiili.
Nonostante non abbiamo ritenuto necessario adottare tutti i principi della metodologia Scrum o più in generale della
filosofia Agile e che alla fine dei conti si tratta comunque di idee che sicuramente sono più coerenti con ambiti "enterprise"
riteniamo che siano stati molto d'aiuto durante le varie fasi di questo progetto.
In particolare, il fatto che il progetto abbia avuto una deadline fissa, come se fosse un progetto dato in carico a
dei sviluppatori all'interno di una azienda, ci ha fatto pensare ed agire come se fossimo stati veramente all'interno di un vero team di svilupoo.
La stesura di feature da implementare e i meeting settimanali, per quanto hanno sicuramente prodotto un overhead temporale, ci ha permesso di risparmiare in quello che invece viene chiamato *debito tecnico*.

## Sviluppi futuri

Durante il progetto per portare a termine tutti gli obiettivi, abbiamo dovuto rinunciare a qualche caratteristica del
sistema che ci sarebbe piaciuto implementare. Qui possiamo vederle divise per ambito:

### Ui/Ux
Per quanto riguarda l'esperienza utente, avremmo voluto rappresentare gli animali e il cibo attraverso delle immagini, o sprite in modo da rendere più accattivante l'interfaccia grafica. Inoltre avremmo anche voluto dare la possibilità agli utenti di costruire delle proprie mappe con un editor.

### Simulazione
Inoltre avremmo voluto modellare alcuni aspetti avanzati per quantoo riguarda l'interazione degli animali nella mappa, come ad esempio costruire diversi tipi di cibi, alcuni mangiabili solo da alcuni tipi di animali, diversificando ed estendendo così le diete. Inoltre avremmo voluto anche modellare il fatto che nel tempo le risorse, sia carne che verdura potesso deteriorarsi e poter essere dannose per gli animali.
Un altra possibile modifica risiede nella possibilità di implementare diverse tipologie di animali, più aderenti al tipo di habitat. Ad esempio sarebbe interessante poter simulare habitat con animali acquatici, animali di montagna nelle zone rocciose, etc.

## Descrizione sprint - Backlog

| ID | Priority | Type          | Item                                                                                                                                                            | Theme           | Developer   |
| -- | -------- | ------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------- | ----------- |
| 1  | 3        | Nuova feature | Creare Gui per aggiunta delle specie                                                                                                                            | View            | Davide      |
| 2  | 3        | Nuova feature | Creare Gui per lettura delle specie                                                                                                                             | View            | Davide      |
| 3  | 3        | Nuova feature | Lettura di oggetti json da file                                                                                                                                 | Controller      | Luca        |
| 4  | 2        | Nuova feature | Scrittura di oggetti json su file                                                                                                                               | Controller      | Luca        |
| 5  | 2        | Nuova feature | Implementazione di un Visualizzabile                                                                                                                            | Model           | Simone      |
| 6  | 3        | Nuova feature | Implementazione di una Specie                                                                                                                                   | Model           | Simone      |
| 7  | 3        | Nuova feature | Implementazione del cibo                                                                                                                                        | Model           | Simone      |
|    |          |               | Fine sprint 1                                                                                                                                                   |                 |             |
| 8  | 3        | Nuova feature | Implementazione Habitat                                                                                                                                         | Model           | Luca        |
| 9  | 3        | Nuova feature | Implementazione Verdura                                                                                                                                         | Model           | Simone      |
| 10 | 3        | Nuova feature | Implementazione Carne                                                                                                                                           | Model           | Simone      |
| 11 | 3        | Nuova feature | Modifica Food e creazione FoodInstance                                                                                                                          | Model           | Luca        |
| 12 | 3        | Nuova feature | Implementazione Zona                                                                                                                                            | Model           | Luca        |
| 13 | 2        | Nuova feature | Implementazione Zona Percorribile                                                                                                                               | Model           | Luca        |
| 14 | 2        | Nuova feature | Implementazione Zona non percorribile                                                                                                                           | Model           | Luca        |
| 15 | 3        | Nuova feature | Implementazione Animale                                                                                                                                         | Model           | Simone      |
| 16 | 2        | Nuova feature | Implementazione Carnivoro                                                                                                                                       | Model           | Simone      |
| 17 | 3        | Nuova feature | Implementazione scelta animali Gui                                                                                                                              | View            | Davide      |
| 18 | 3        | Nuova feature | Implementazione Lettura animali Gui                                                                                                                             | View            | Davide      |
|    |          |               | Fine sprint 2                                                                                                                                                   |                 |             |
| 19 | 3        | Nuova feature | Completare mappa Grid                                                                                                                                           | View            | Davide      |
| 20 | 1        | Bug Fixing    | Chiudere simulation non interrompe il programma                                                                                                                 | View            | Davide      |
| 21 | 2        | Bug Fixing    | (FIXED) Lanciando da gui la mappa random con dimensioni 800x500 c'è un errore su un requirement dei punti (tutto ok con 1000x1000, 500x500, 500x800 e 1000x800) | View            | Davide      |
| 22 | 1        | Nuova feature | Creare varie tipologie di mappe random con le aree già posizionate                                                                                              | View            | Davide      |
| 23 | 1        | Bug Fixing    | (FIXED) In base alla dimensione che si imposta la gui diventa più grande o più piccola, andrebbe tenuta ad una dimensione fissa                                 | View            | Davide      |
| 24 | 1        | Bug Fixing    | Fixato mappa random, ora crea aree casuale fatte per bene                                                                                                       | View            | Davide      |
| 25 | 1        | Nuova feature | Impostare una probability Random alla creazione delle aree                                                                                                      | View            | Davide      |
| 26 | 1        | Nuova feature | Modificare nomi su MainGui                                                                                                                                      | View            | Davide      |
| 27 | 2        | Nuova feature | Disegnare aree nella gui con colori                                                                                                                             | View            | Davide      |
| 28 | 2        | Nuova feature | Aggiungere classe Point                                                                                                                                         | Utility         | Davide      |
| 29 | 2        | Nuova feature | Creare e utilizzare classe Rectangle                                                                                                                            | Model           | Davide      |
| 30 | 2        | Nuova feature | Implementare mappa random                                                                                                                                       | View            | Davide      |
| 31 | 2        | Nuova feature | Modificare case class Area                                                                                                                                      | Model           | Luca        |
| 32 | 2        | Nuova feature | Collegare la simulazione con il menu dell'habitat                                                                                                               | View            | Luca        |
| 33 | 1        | Bug Fixing    | Da overlapping con i seguenti : (A with coordinates ((900,900), (910,918) is overlapping with B (900,800), (910,811) )                                          | View            | Luca        |
| 34 | 2        | Nuova feature | Scelta habitat nella gui                                                                                                                                        | View            | Luca        |
| 35 | 2        | Nuova feature | connessione gui scelta specie a gui scelta habitat                                                                                                              | View            | Luca        |
| 36 | 1        | Nuova feature | Aggiungere le lables sulla creazione di una specie                                                                                                              | View            | Luca        |
| 37 | 1        | Nuova feature | Rifare check di overlapping                                                                                                                                     | Model           | Luca        |
| 38 | 2        | Nuova feature | Creare mappa hardcoded (500\*500) su file con tutte le possibili tipologie di aree                                                                              | Model           | Simone      |
| 39 | 2        | Nuova feature | Aggiungere i colori e sistemare gli apply in Area                                                                                                               | Model           | Simone      |
| 40 | 3        | Nuova feature | Serializzazione della mappa                                                                                                                                     | Model           | Simone      |
| 41 | 3        | Nuova feature | Serializzazione e deserializzazione di Aree (con distinzione per le Fertile) e Probability                                                                      | Model           | Simone      |
| 42 | 2        | Nuova feature | Diversificazione della mappa creata a seconda del radio button selezionato                                                                                      | View            | Simone      |
|    |          |               | FINE SPRINT 3                                                                                                                                                   |                 |             |
| 43 | 3        | Bug Fixing    | Gli animali tolti dalla selezione non vengono riprosti nella combobox \[IMPORTANTE\]                                                                            | View            | Davide      |
| 44 | 1        | Setup / CICD  | Unire due pipeline usando if e riferimento al branch                                                                                                            | Setup / CICD    | Davide      |
| 45 | 2        | Nuova feature | Aggiunta Herbivore/Carnivore su specie e modifiche correlate                                                                                                    | Model           | Davide      |
| 46 | 3        | Nuova feature | Manager combattimenti                                                                                                                                           | Controller      | Davide      |
| 47 | 2        | Nuova feature | Sistemare gli apply in Habitat                                                                                                                                  | Model           | Davide      |
| 48 | 3        | Setup / CICD  | aggiunta pagina della documentazione su github                                                                                                                  | Setup / CICD    | Luca        |
| 49 | 1        | Setup / CICD  | creati i file di markdown                                                                                                                                       | Setup / CICD    | Luca        |
| 50 | 3        | Nuova feature | creazione prima versione manager spostamenti                                                                                                                    | Controller      | Luca        |
| 51 | 2        | Nuova feature | Creazione degli animali delle diverse specie (a seconda del numero inserito da utente) e relativa visualizzazione (ancora solo a colori) in mappa               | Model/View      | Simone      |
| 52 | 2        | Bug Fixing    | Creazione degli animali solo su zone fertili                                                                                                                    | Controller      | Simone      |
| 53 | 2        | Nuova feature | Animali di Size diverse hanno dimensioni diverse in mappa                                                                                                       | View            | Simone      |
| 54 | 3        | Nuova feature | Mostrare le info dell'animale se il cursore è sopra all'icona                                                                                                   | View            | Simone      |
|    |          |               | FINE SPRINT 4                                                                                                                                                   |                 |             |
| 55 | 2        | Nuova feature | Creato il color serializer per risolvere il warning di Gson + Color                                                                                             | View            | Simone      |
| 56 | 2        | Nuova feature | Disegnare il cibo nella gui come cerchi colorati (creare la nuova Shape cerchio)                                                                                | View            | Simone      |
| 57 | 1        |               | Dividere i test in sotto cartelle, ad es gui, serializer, model ecc                                                                                             |                 | Simone      |
| 58 | 3        | Nuova feature | Fare game loop con il passaggio del tempo e aggiornamento elementi in mappa                                                                                     | Controller      | Simone      |
| 59 | 2        | Nuova feature | Inserire un colore per ogni specie (al posto dell'icona)                                                                                                        | View            | Simone      |
| 60 | 3        | Nuova feature | Ridisegnare gli animali con le nuove posizioni                                                                                                                  | Controller      | Simone      |
| 61 | 1        | Nuova feature | Rimuovere la creazione dei rettangoli da GameLoop                                                                                                               | Controller      | Simone      |
| 62 | 2        | Nuova feature | rivedere habitat                                                                                                                                                | Model           | Tutti       |
| 63 | 3        | Bug Fixing    | Sistemare GameLoop, SimulationGui e ShapePanel (diventato SimulationPanel)                                                                                      | View            | Simone      |
| 64 | 3        | Bug Fixing    | aggiungere Gui con le statistiche                                                                                                                               | View            | Luca        |
| 65 | 3        | Nuova feature | aggiungere utility per la gestione delle statistiche                                                                                                            | View/Controller | Luca        |
| 66 | 2        | Nuova feature | aggiunto metodo di utils in AnimalUtils                                                                                                                         |                 | Luca        |
| 67 | 3        | Nuova feature | Completato ShiftManager                                                                                                                                         |                 | Luca        |
| 68 | 3        | Nuova feature | Fare un logger di quello che succede nella simulazione ("tigre" attacca x, mangia y ecc...)                                                                     | Controller      | Davide     |
| 69 | 3        | Nuova feature | Generare cibo random all'inizio della simulazione                                                                                                               | Controller      | Luca        |
| 70 | 2        | Nuova feature | Inserire il tipo di cibo in Food e gestire di conseguenza                                                                                                       | Controller      | Luca        |
| 71 | 3        | Nuova feature | Inserire nella gui delle specie 3 textbox (rgb) per il colore della specie                                                                                      | View            | Davide     |
| 72 | 2        | Nuova feature | Modificare lo shiftManager per controllare che tutti i punti del quadrato dell'animale non vadano nell'acqua ad ogni spostamento                                | Controller      | Luca        |
|    |          |               | FINE SPRINT 5                                                                                                                                                   |                 |             |
| 73 | 2        | Bug Fixing    | Diminuire lo spawn dei Vegetable e controllare che non vadano nelle non walkable areas                                                                          | Model/View      | Luca        |
| 74 | 1        | Bug Fixing    | aggiungere documentazione in Probability                                                                                                                        | Utility         | Davide      |
| 75 | 1        | Bug Fixing    | Eliminare le print dal codice e dai test                                                                                                                        | Utility         | Davide      |
| 76 | 2        | Nuova feature | Gestire erediterietà tra Rectangle e RectangleArea; mettere Recentagle in Area al posto di RectangleArea+Color                                                  | Model           | Simone      |
| 77 | 2        | Bug Fixing    | Risolvere che gli animali lasciano la scia nelle mappe senza habitat                                                                                            | View            | Simone      |
| 78 | 3        | Nuova feature | Implementare pausa, velocità e aggiungere in SimulationGui i pulsanti play, pausa, velocità (+ e -) e stop                                                      | View/Controller | Simone      |
| 79 | 2        | Nuova feature | Creazione AnimalManager e calcolo eventi inaspettati                                                                                                            | Controller      | Simone      |
| 80 | 2        |               | Riordinare il GameLoop per renderlo più leggibile                                                                                                               | Controller      | Simone      |
| 81 | 2        | Nuova feature | Mettere già del cibo all'inizio della simulazione                                                                                                               | Controller      | Luca        |
| 82 | 2        | Nuova feature | Text area sotto la simulazione con il logger                                                                                                                    | View            | Davide      |
| 83 | 3        | Nuova feature | Aggiungere una GUI a fine simulazione con le statistiche (e creazione statistiche)                                                                              | View            | Luca        |
| 84 | 2        | Bug Fixing    | reso lo shiftManager molto piu' efficiente                                                                                                                      | Controller      | Luca        |
| 85 | 2        | Bug Fixing    | impostare le dimensioni massime dell'habitat fisse/limitate ad un certo valore                                                                                  | View            | Luca        |
| 86 | 2        | Bug Fixing    | Rinominare ResourceManager.foodInstances\_                                                                                                                      | Controller      | Luca        |
| 87 | 2        | Nuova feature | Aggiungere un limite massimo di vegetable spawnabili in mappa                                                                                                   | Controller      | Luca        |
| 88 | 2        | Bug Fixing    | Rivedere GameLoopTest                                                                                                                                           | Controller      | Simone      |
| 89 | 2        | Nuova feature | Inserire walkable e nonWalkable nel model delle aree                                                                                                            | Model           | Tutti       |
| 90 | 2        | Nuova feature | non far overlappare il cibo con le nonWalkableAreas                                                                                                             | Model           | Luca        |
| 91 | 1        | Nuova feature | Rifattorizzato AnimalUtils e concluso AnimalUtilsTest                                                                                                           | Controller      | Simone      |
| 92 | 2        | Nuova feature | Reso GameLoop molto più funzionale                                                                                                                              | Controller      | Simone/Luca |
|    |          |               | FINE SPRINT 6   
