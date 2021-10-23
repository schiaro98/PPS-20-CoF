# Implementazione 

per ogni studente, una sotto-sezione descrittiva di cosa fatto/co-fatto e con chi, e descrizione di aspetti implementativi importanti non già presenti nel design
Cosa ha fatto, classi più rappresentative

## Simone Luzi

## Luca Rossi
Dopo aver dato una rappresentazione ad alto livello del dominio applicativo nella fase di modellazione ho inziato a scrivere il codice elencato di seguito:

 1. Serializer
 2. Area
 3. Habitat
 4. ChooseHabitatGUI (e Logic)
 5. ResourceManager
 6. ShiftManager
 7. Statistics (e relativa GUI) 
 
Ognuno file sopra citato è corredato dalla apposita classe di test che testa più o meno approfonditamente i desiderata delle classi. 

### Serializer
La prima classe ad aver modellato è stato il serializzatore, che nella sua versione di bas serializza oggetti in formato json tramite la libreria di java **GSON** .
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
