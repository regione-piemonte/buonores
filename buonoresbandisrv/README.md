# Componente di Prodotto

BUONORESBANDISRV

## Versione

2.0.0

## Descrizione del prodotto

Si tratta di servizi REST esposti sia per le componenti on-line: [BUONORESBFF](../buonoresbff) e [BUONORESBO](../buonoresbo) sia daille componenti batch: [BUONORESBATCH](../buonoresbatch) e [BUONORESBANBATCH](../buonoresbanbatch/). Questi servizi si occupano di inviare domande e rendicontazioni del cittadino, di inviare tutte le segnalazioni tra il cittadino e la piattaforma bandi.


## Configurazioni iniziali

I servizi della piattaforma "bandi" richiamati si trovano ai seguenti link [SERVIZI BANDI](docs/yaml/pbservwelfare.yaml) 
Per generare il pacchetto lanciare il comando ant -Dtarget prod.

## Prerequisiti di sistema

Java:
Jdk 11

ANT:
Ant version 1.9.6

Server Web:
Apache 2.4.54

Application Server:
Wildfly 23

Tipo di database:
postgres v12

## Installazione

Per generare il pacchetto lanciare il comando ant -Dtarget prod  per generare l'ear

## Deployment

Inserire il file ear generato durante l'installazione sotto la cartella deployments del Wildfly

## Versioning

Per il versionamento del software si usa la tecnica Semantic Versioning (http://semver.org).

## Copyrights

“© Copyright Regione Piemonte – 2024”