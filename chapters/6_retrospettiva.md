# Retrospettiva 


descrizione finale dettagliata dell' andamento dello sviluppo, del backlog, delle iterazioni. Commenti finali.
Si noti che la retrospettiva è l'unica sezione che può citare aneddoti di cosa è successo in itinere, mentre le altre sezioni fotografino il risultato finale. Se gli studenti decideranno (come auspicato) di utilizzare un product backlog e/o dei backlog delle varie iterazioni/sprint, è opportuno che questi siano file testuali tenuti in versione in una cartella (process), così che sia ri-verificabile a posteriori la storia del progetto.

## Conclusione
In conclusione ci riteniamo soddisfatti del lavoro svolto, sulle metodologie utilizzate e sul risultato finale.
L'obiettivo più graficante secondo noi è stato quello di riuscire a costruire una applicazione pensando in modo funzionale.
Col passare del tempo infatti, prendendo dimestichezza con un nuovo linguaggio, siamo riusciti ad esprimere concetti 
anche complicati con strumenti tipici della programmazione funzionale. Questa cosa è evidente nel codice sorgente dei manager e del gameLoop.
I strumenti tipici della FP sono stati l'utilizzo delle funzioni ricorsive, il pattern matching e l'utilizzo raro di variabili mutabiili.
Nonostante non abbiamo ritenuto necessario adottare tutti i principi della metodologia Scrum o più in generale della 
filosofia Agile e che alla fine dei conti si tratta comunque di idee che sicuramente sono più coerenti con ambiti "enterprise" 
riteniamo che siano stati molto d'aiuto durante le varie fasi di questo progetto. 
In particolare, il fatto che il progetto abbia avuto una deadlin fissa, come se fosse un progetto dato in carico a 
dei sviluppatori all'interno di una azienda, ci ha fatto pensare ed agire come se fossimo stati veramente all'interno di una azienda.
La stesura di feature da implementare, i meeting settimanali e la produzione continua di versione funzionanti 
dell'applicativo, per quanto hanno sicuramente prodotto un overhead temporale, ci ha permesso di risparmiare in quello che invece viene chiamato *debito tecnico*

## Sviluppi futuri

Durante il progetto per portare a termine tutti gli obiettivi, abbiamo dovuto rinunciare a qualche caratteristica del 
sistema che ci sarebbe piaciuto implementare. Qui possiamo vederle divise per ambito:

### Ui/Ux 
Per quanto riguarda l'esperienza utente, avremmo voluto rappresentare gli animali e il cibo attraverso delle immagini, o sprite in modo da rendere più accattivante l'interfaccia grafica. Inoltre avremmo anche voluto dare la possibilità agli utenti di costruire delle proprie mappe con un editor.

### Simulazione
Inoltre avremmo voluto modellare alcuni aspetti avanzati per quantoo riguarda l'interazione degli animali nella mappa, come ad esempio costruire diversi tipi di cibi, alcuni mangiabili solo da alcuni tipi di animali, diversificando ed estendendo così le diete. Inoltre avremmo voluto anche modellare il fatto che nel tempo le risorse, sia carne che verdura potesso deteriorarsi e poter essere dannose per gli animali.



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
