# Implementazione

per ogni studente, una sotto-sezione descrittiva di cosa fatto/co-fatto e con chi, e descrizione di aspetti implementativi importanti non già presenti nel design
Cosa ha fatto, classi più rappresentative

Luzi

Rossi

Schiaroli

Per prima cosa, prima di iniziare con la analisi del dominio del problema, mi sono occupato di alcuni aspetti di CI/CD e di setup del progetto. Ho creato il repository GitHub e il progetto Scala/Sbt e poi ho proceduto a creare una prima bozza di pipeline, che doveva solamente compilare il programma. Durante le fasi intermedie del progetto ho aggiunto alcune funzionalità alla pipeline come l'esecuzione di test e release di Jar automatizzata.
Inizialmente mi sono occupato della View, sviluppando un primo prototipo di quello che poi è diventata la Gui della nostra applicazione utilizzando la libreria scalaSwing. Ho optato per questa libreria invece di ScalaFx per la mancanza di documentazione che avrebbe senz'altro portato a numerosi problemi in futuro. Successivamente ho sviluppato la logica degli animali, cioè come venivano usati all'interno della applicazione quando inseriti da utente.
Successivamente mi sono occupato dello sviluppo del codice necessario a costruire le mappe random e a griglia.
Dopo avere visto questi aspetti di View mi sono occupato principalmente di sviluppare i Controller per le fasi di battaglia, di movimento (solamente per quello che riguarda le decisione sulle destinazioni) e di alimentazione ( permettere ovvero di far mangiare e bere gli animali).
Ho inoltre lavorato sulle classi Point, sulla implementazione di un Logger e nella creazione delle aree negli Habitat.
