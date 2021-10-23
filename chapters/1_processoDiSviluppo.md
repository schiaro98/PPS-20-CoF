# Processo di sviluppo {#Processo-di-sviluppo}

Questo progetto ci ha permesso di lavorare seguendo alcune metodologie dello sviluppo *Agile*. In particolare abbiamo dato particolare peso alle seguenti caratteristiche fondamentali della gestioni di progetti Agile, derivanti proprio dal [Manifesto Agile](https://agilemanifesto.org/iso/it/principles.html):
* Brevi iterazioni di sviluppo note come sprint.
* Regolare ridefinizione delle priorità di lavoro.
* Approccio rapido e flessibile nell’indirizzare i cambiamenti di ambito.

Per quanto riguarda la fase progettuale infatti, abbiamo pensato di avere un prototipo funzionante il più presto possibile, a effettuare continue release e sopratutto di modellare l'applicativo in modo da renderlo facilmente estendibile e modificabile. Quest'ultimo punto ci ha notevolmente aiutato quando nelle fasi finali di progetto abbiamo voluto effettuare modifiche anche importanti al codice da noi prodotto. Abbiamo inoltre pensato di adottare parzialmente anche il "framework" Scrum. Non abbiamo infatti portato avanti il progetto con alcuni dei punti cardine di Scrum come i meeting giornalieri, ma abbiamo comunque voluto fare dei sprint settimanali, seguiti da una review degli stessi. Abbiamo poi utilizzato Trello come tool di collaborazione per controllare in tempo reale lo stato dei task, delle feature da implementare o bug da risolvere.

Per consentire uno sviluppo agile abbiamo organizzato il repository per avere due branch separati, il main che contenesse le release e il develop che contenesse tutte le modifiche alla nostra applicazione. Per poter rilasciare l'applicazione ad ogni fine sprint, abbiamo deciso che fosse necessario effettuare una pull request e un altro membro del gruppo doveva controllare (utilizzando anche i risultati della pipeline e confrontando le modifiche effettuate con i task ) che fosse tutto corretto. Abbiamo inoltre utilizzato un altro branch, docs, per tutto quello che riguarda la produzione della documentazione e della relazione finale.

## Modalità di divisione in itinere dei task

Dopo aver studiato i punti salienti del progetto ed esserci fatti un'idea approssimativa sul cosa modellare e sul livello di complessità abbiamo concordato quali fossero le entità da rappresentare più urgenti da modellare e abbiamo identificato vari compiti.
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
Per distribuire e testare frequentemente la nostra applicazione abbiamo utilizzato la pipeline di Github, tramite le Github Actions. Abbiamo perciò elaborato un file yaml contenente le azione da compiere ad ogni push. In particolare abbiamo specificato di compilare ed eseguire tutti i test ad ogni push sui branch di sviluppo (develop) e main e di pacchettizare l'applicazione in un jar ad ogni push sul branch main. 
