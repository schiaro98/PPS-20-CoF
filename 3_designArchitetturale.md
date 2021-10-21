# DesignArchitetturale
SCHIARO
## Architettura complessiva
### Species
Species rappresenta una specie animale e contiene campi che ne pregiudicano il comportamento all'interno della simulazione, come ad esempio "alimentationType" ovvero la dieta. Infatti gli animali possono compiere azione diverse se sono carnivori oppure erbivori. In oltre ogni specie ha una dimensione, una forza e un raggio visivo.

### Animal
Animal raprresenta un'istanza di Species e contiene alcune informazione che variano da specie a specie, come ad esempio la vita (health) e la sete (thirst). Entrambi questi parametri vengono decrementati col passare del tempo e aggiornati quando l'animale mangia o beve. Durante il ciclo di vita dell'animale se uno dei parametri arriva a 0 l'animale muore, rilasciando risorse nella mappa

### Area

## Descrizione di pattern architetturali usati
###MVC
###Model
###Controller
###View

## Scelte tecnologiche cruciali ai fini architetturali
corredato da pochi ma efficaci diagrammi

Aggiungere magari diagramma UML di come si connettono MVC
