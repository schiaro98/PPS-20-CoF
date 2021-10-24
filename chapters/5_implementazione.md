# Implementazione 

per ogni studente, una sotto-sezione descrittiva di cosa fatto/co-fatto e con chi, e descrizione di aspetti implementativi importanti non già presenti nel design
Cosa ha fatto, classi più rappresentative

## Simone Luzi

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
