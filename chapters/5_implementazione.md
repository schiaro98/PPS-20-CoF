# Implementazione 

per ogni studente, una sotto-sezione descrittiva di cosa fatto/co-fatto e con chi, e descrizione di aspetti implementativi importanti non già presenti nel design
Cosa ha fatto, classi più rappresentative

## Simone Luzi

## Luca Rossi

## Davide Schiaroli

Inizialmente, prima di iniziare con la analisi del dominio del problema, mi sono occupato di alcuni aspetti di CI/CD e di setup del progetto. 
Ho creato poi il repository GitHub e il progetto Scala/Sbt, infine mi sono occupato della pipeline, che inizialmente aveva il solo compito di compilare il programma. 
Durante le fasi intermedie del progetto ho aggiunto alcune funzionalità alla pipeline come l'esecuzione di test e release di Jar automatizzata.
Inizialmente mi sono occupato della View, sviluppando un primo prototipo di quello che poi è diventata la Gui della nostra applicazione utilizzando la libreria scalaSwing. Ho optato per questa libreria invece di ScalaFx per la mancanza di documentazione che avrebbe senz'altro portato a numerosi problemi in futuro. Successivamente ho sviluppato la logica degli animali, cioè come venivano usati all'interno della applicazione quando inseriti da utente.
Successivamente mi sono occupato dello sviluppo del codice necessario a costruire le mappe random e a griglia.
Dopo avere visto questi aspetti di View mi sono occupato principalmente di sviluppare i Controller per le varie fasi. 
E' stato necessario creare dei controller per la fasi di battaglia, di movimento (solamente per quello che riguarda le decisione sulle destinazioni) 
e di alimentazione (permettere ovvero di far mangiare e bere gli animali).
Ho inoltre lavorato sulle classi Point, sulla implementazione di un Logger e nella creazione delle aree negli Habitat.

## Controller
Per quanto riguarda l'implementazione del Controller, mi sono occupato della realizzazione dei manager di combattimento, alimentazione e 
destinazione. Per tutti e 3 i manager si è deciso di costruire meno metodi possibili, cercando di evitare side effects e sopratutto cercando di costruire metodi tail-recursive.

### Battle Manager
Questo manager aveva il compito di prendere come input gli animali presenti nella mappa e di far combattere gli animali che rientravano in una distanza molto limitata, chiamata *Hitbox*. Un'ulteriore filtro che andava considerato è che solamente gli animali carnivori potessero dare il via ad una battaglia verso altri animali. Per avere un responso randomico della battaglia, ma ponderando comunque alcuni fattori come taglia, forza e distanza tra animali,ho creato dei metodi che calcolassero la probabilità utilizzando questi discriminanti. Ogni metodo dopo aver calcolato la probabilità, mi restituiva un valore Booleano. Se il numero di *true* superava il numero di *false* la battaglia era vinta dall'attaccante, altrimenti dal difensore. Questo metodo, oltre a restituire la lista di animali aggiornata, cioè gli animali presi in input meno quelli deceduti in combattimento, restituiva anche tutte le risorse rilasciate dagli animali deceduti. 
Quando una battaglia va a termine, viene anche visualizzato nella area di testo della simulazione l'esito della battaglia, questo per dare un'idea di cosa succede nei vari momenti della simulazione.

### Destination Manager
Questo manager invece si occupa di dare delle posizioni agli animali verso il quale andare. Sostanzialmente serve ad indicare agli animali le prede da attaccare, il cibo (carne e verdure) da mangiare e le zone d'acqua da dove bere.
Questo metodo, nella ricerca di eventuali punti di interesse, dava la priorità alle zone d'acqua in quanto più rare e più utili per la sopravvivenza. 
Se non vengono trovate zone d'acqua, vengono cercate, in base alla dieta dell'animale eventuali verdure, oppure carni se si tratta di un carnivoro. Inoltre se si tratta di carnivori vengono anche segnalate le prede vicine.

### Feed manager
