# Design di dettaglio 
In questo capitolo andremo ad esplorare più nel dettaglio  le scelte progettuali che sono state attuate.

## Scelte rilevanti
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

## Organizzazione del codice 

![Diagramma dei package](https://github.com/schiaro98/PPS-20-CoF/blob/docs/resources/package.png)


