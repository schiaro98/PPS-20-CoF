# Processo di sviluppo
Fico il markdown

## Modalità di divisione in itinere dei task

Dopo aver studiato i punti salienti del progetto e esserci fatti un'idea approssimativa sul cosa modellare e sul livello di complessità abbiamo concordato quali fossero le entità da rappresentare più urgenti da modellare e abbiamo identificato vari compiti.
Trovati i compiti ognuno di noi ne ha scelti uno o più da svolgere entro il meeting successivo.
Ad ogni meeting si discutevano i task svolti, motivando le scelte fatte e proponendo modifiche e migliorie e una volta conclusa questa prima parte si procedeva alla scelta di un nuovo task.
Man mano che il software cresceva in complessità questi meeting diventavano più vicini temporalmente, fino quasi a non essere neanche più definibili meeting ma piuttosto scambi di informazioni correlati da pochi ma efficaci note sulla prossima funzionalità da sviluppare o sul bug da correggere.
Questa modalità ha facilitato la comunicazione e il controllo sul progetto, permettendoci di conoscere bene il software prodotto e ha velocizzato notevolmente il tempo da utilizzare per scrivere codice piuttosto che "perdere tempo" a dividerci i compiti.
Alla fine di ogni sprint si riguardava quindi il codice prodotto, ci si diceva quali task si sarebbero portati avanti e si  procedeva all'implementazione.
In particolare si è cercato di seguire il più possibile l'approccio TDD.
Non lo si è portato all'estremo con l'approccio red-green-refactor, ma una volta create le interfacce o modellato le classi abbiamo proceduto con il testing in maniera più o meno esaustiva in base alla complessità e necessità.
Per il refactor non abbiamo messo particolari regole, ma quando trovavamo qualcosa di non soddisfacente lo riportavamo e provvedevamo ad una rielaborazione.

## Meeting/Interazioni pianificate

Inizialmente sono stati pianificati 8 meeting/sprint settimanali, in modo da far coincidere l'ultimo meeting con la consegna 
del progetto.
Nel corso dello svolgimento del percorso abbiamo deciso di allungare il tempo tra un meeting e l'altro, rispettando sia il tempo di pausa di chiusura dell'università che gli impegni personali di tutti i partecipanti del progetto. Alla fine abbiamo deciso di effettuare due sprint in meno, quindi per un totale di 6, dove l'ultimo sprint ha previsto la consegna dell'applicativo.
Tutti gli sprint/meeting sono stati effettuati in modalità remota, utilizzando Trello come strumento per tenere traccia dello svolgimento degli sprint, dei task effettuati, delle feature da implementare e dei bug da risolvere .


## Modalità di revisione in itinere dei task

La revisione dei task, come già accennato,  è stata effettuata al termine di ogni sprint ed è stata coadiuvata dalla pipeline di GitHub impostata in modo da controllare che tutti i test passassero.
In questa fase si provvedeva anche a dare dei commenti critici sul codice prodotto al fine di migliorare il prima possibile i punti critici con piccoli refactor che avrebbero permesso di diminuire il debito tecnico.

## Scelta degli strumenti di test/build/continuous integration
### Build
Per compilare i sorgenti Scala del progetto è stato usato Sbt poichè è lo strumento nativo per il build di scala.
Diversamente da Gradle infatti non richiede di aggiungere plugin o specificare alcuna impostazione poichè le impostazione vengono automaticamente create insieme al progetto


---magari parlare anche della pipeline di github più nello specifico
