# Componente di Prodotto

BUONORESSRV

## Versione

2.3.0

## Descrizione del prodotto

Si tratta di servizi REST esposti sia per le componenti on-line: [BUONORESBFF](../buonoresbff) e [BUONORESBO](../buonoresbo) sia daille componenti batch: [BUONORESBATCH](../buonoresbatch) e [BUONORESBANBATCH](../buonoresbanbatch/). Questi servizi si occupano di creare domande e lettere del cittadino, a inviare documenti ai servizi di per protocollare e le notifiche verso il cittadino.


## Configurazioni iniziali

I servizi del piattaforma acta per la protocollazione della documentazione richiamati si trovano ai seguenti link [SERVIZI ACTA](docs/yaml/pbservwelfare.yaml) 
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

“© Copyright Regione Piemonte – 2023”