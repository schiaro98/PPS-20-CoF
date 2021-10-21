# DesignArchitetturale
## Architettura complessiva
Partendo dai requisiti abbiamo dapprima sviluppato un diagramma UML dei componenti principali dell'applicazione così da notare eventuali scelte errate in fase di progettazione
![Diagramma UML](/resources/UML1.png)

In questo diagramma abbiamo definito alcuni punti fondamentali, come la relazione tra animale e specie, la composizione di Habitat in aree, la differenziazione tra tipo di cibo e l'esistenza di zone dell'habitat non percorribili.
## Da qui fino alla fine di Model è da spostare
### Controller
#### Animal Manager
E' il controller degli animali presenti nella simulazione.
Si occupa di istanziare gli animali che sono presenti all'inzio della simulazione, aggiornare i valori degli animali ad ogni ciclo e di calcolare gli eventuali esiti dei eventi inaspettati

#### Battle Manager
E' il controller delle battaglie tra due animali.
Contiene un metodo principale battle che si occupa di calcolare ricorsivamente per tutti gli animali gli eventuali scontri tra animali. 
E' necessario considerare alcuni fattori:
  - Solo i carnivori possono incominciare una battaglia
  - Le vittime (erbivori) per essere attaccate devono essere visibili, ovvero rientrare nella soglia del campo    visivo dell'animale attaccante
  - Per il calcolo dell'esito vengono calcolate varie probabilità in base a distanza, stazza, forza.
Per ogni battaglia è necessario aggiornare gli animali, eliminando gli animali deceduti e rilasciando le risorse

#### Destination Manager
E' il controller che descrive i movimenti dei vari animali.
Per ogni animale, vengono cercate le risorse (aree d'acqua dove bere o cibo) visibili e viene ritornata la posizione dove devono dirigersi. Se non ci sono risorse disponibili all'interno del campo visivo viene scelta una posizione casuale dove dirigersi.

#### Feed Manager
E' il controller che permette di consumare le risorse e permette agli animali di bere, sempre considerando la vicinanza degli animali alle risorse. Si occupa anche della rimozione del cibo mangiato dagli animali.

#### Resource Manager

#### Feed Manager
### Model
#### Species
Species rappresenta una specie animale e contiene campi che ne pregiudicano il comportamento all'interno della simulazione, come ad esempio "alimentationType" ovvero la dieta. Infatti gli animali possono compiere azione diverse se sono carnivori oppure erbivori. In oltre ogni specie ha una dimensione, una forza e un raggio visivo.

#### Animal
Animal raprresenta un'istanza di Species e contiene alcune informazione che variano da specie a specie, come ad esempio la vita (health) e la sete (thirst). Entrambi questi parametri vengono decrementati col passare del tempo e aggiornati quando l'animale mangia o beve. Durante il ciclo di vita dell'animale se uno dei parametri arriva a 0 l'animale muore, rilasciando risorse nella mappa

#### Habitat
Habitat rappresenta una composizione di aree di diverso tipo, dove gli animali possono muoversi e cibarsi. 
Ogni habitat ha una probabilità di eventi inaspettati che determina la possibilità che un animale muoia per cause non calcolate nella simulazione (cacciatori, avvelenamento, etc...)

#### Area
Area rappresenta una area all'interno di un Habitat. Ogni area ha un tipo (Water, Rock, Volcano) che può essere camminabile o meno e una rappresentazione logica dell'area occupata all'interno dell'habitat (attraverso il campo Rectangle). In caso l'area sia fertile, allora l'area avrà anche la possibilità di far crescere spontaneamente del cibo al suo interno

#### Food
Food rappresenta un tipologia di cibo, con una tipologia (Meat o Vegetable) e una quantità di energia che fornisce all'animale una volta mangiata.

#### FoodInstance
Rappresenta un'istanza di Food e contiene un campo quantity, che ne descrive la quantità.

#### Placeable e Point
Placeable è un trait che abbiamo usato per modellare tutti quei componenti che necessitavano di descrivere una posizione all'interno della mappa, come FoodIstance e Animal. Il suo unico campo è un Point, ovvero una tupla due numeri. Point inoltre fornisce numerosi metodi che permettono di verificare varie condizioni di uguaglianza o meno tra due Point, tra un Point e un asse cartesiano e di calcolare la distanza tra due Point

## Descrizione di pattern architetturali usati
###MVC
###Model
###Controller
###View

## Scelte tecnologiche cruciali ai fini architetturali
corredato da pochi ma efficaci diagrammi

Aggiungere magari diagramma UML di come si connettono MVC