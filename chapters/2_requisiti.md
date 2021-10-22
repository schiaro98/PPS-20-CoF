# Requisiti
In questo capitolo verranno analizzati i requisiti del progetto, divisi nelle principali tipoligie.

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
La simulazione deve essere eseguibile anche su macchine non particolarmente potenti.

#### Interfaccia utente e usabilità
L'interfaccia grafica deve essere progettata in maniera tale da essere subito comprensibile da un nuovo utente, senza bisogno di spiegazioni o tutorial.
Deve essere subito chiaro l'insieme di procedure necessarie per svolgere l'operazione desiderata e l'utente non deve essere disorientato da situazioni equivoche.
Anche il grafico con le statistiche finali deve essere facilmente comprensibile da tutti.

## Requisiti di implementazione
I requisiti di implementazione definiscono i vincoli sulle tecnologie da utilizzare (ad esempio i linguaggi di programmazione, i tool e i software), e sulla qualità interna del codice.

Il sistema presenterà le seguenti caratteristiche:
- **Linguaggio di programmazione**: Scala nella versione 2.13.6;
- **Metodologia**: adottare una mentalità Agile ed utilizzare Scrum dove possibile;
- **Build tool**: sbt, dove verranno gestite tutte le dipendenze delle librerie utilizzate;
- **Test**: ScalaTest, utilizzando lo stile FunSuite;
- **CI**: Github Actions, principalmente per controllare la corretta esecuzione dei test ad ogni push;
- **Grafica**: Scala Swing per realizzare le interfacce grafiche;
- **Patterns**: MVC per la struttura generale del progetto, inoltre devono essere utilizzati i pattern presenti in letteratura ogni qual volta se ne presenti la possibilità oltre ad evitare i code smell.