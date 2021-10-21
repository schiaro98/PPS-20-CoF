
# Design di dettaglio
In questo capitolo andremo ad esplorare più nel dettaglio  le scelte progettuali che sono state attuate.

## Scelte rilevanti
### Immutabilità 
Una delle caratteristiche che ci incuriosiva di più del paradigma funzionale era l'immutabilità dei dati, per questo motivo abbiamo cercato di favorire l'immutabilità degli oggetti e, un po' per fini didattici, un po' per capire a fondo come lavorare con queste strutture abbiamo deciso di modellare qualsiasi oggetto, almeno del model in maniera immutabile.
Nonostante la modellazione di alcuni campi in maniera mutabile fosse banale, e forse più naturale in alcune situazioni, come ad esempio nella modellazione della salute o della posizione di un animale, destinata a cambiare col passare del tempo, abbiamo deciso di costruire tutti gli oggetti del model in questa maniera.
Per ora le uniche strutture ad avere delle componenti variabili sono i manager e qualche classe o oggetto di utility.
Vorremmo comunque far notare che, almeno per i Manager le var utilizzate spesso contengono al loro interno strutture immutabili.
Scala permette di maneggiare molto agevolmente questi tipi di dati e anche se con un dispendio di tempo leggermente maggiore, questo tipo di progettazione ci ha permesso di capire meglio il codice prodotto e di limitare i bug, o comunque di trovarli più facilmente.
 
### Disaccoppiamento della logica
Essendo le entità del model immutabili ci è venuto naturale modellare gli oggetti spesso come case class, relegando la logica complessiva in altre strutture di più alto livello, ovvero i Manager.
La nostra applicazione ha numerosi Manager, ognuno dei quali ha un compito abbastanza specifico e presenta nell'interfaccia un numero irrisorio di metodi.
I Manager, come già anticipato a volte hanno delle

 ### Idiomatic scala? Pattern Matching? filter map
 ### Funzioni ricorsive tailRec
## Pattern di progettazione
Vari pattern di programmazione se usati

Factory e singleton default di scala?
## Organizzazione del codice 

![Diagramma dei package](https://github.com/schiaro98/PPS-20-CoF/blob/docs/resources/package.png)


