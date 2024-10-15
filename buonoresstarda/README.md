# Componente di Prodotto

BUONORESSTARDA

## Versione

1.1.0

## Descrizione del prodotto

Si tratta di servizi REST esposti per accogliere le risposte dalla piattaforma ACTA in seguito all'invio della documentazione del cittadino da protocollare.


## Configurazioni iniziali

Utilizza i servizi della componente [buonoressrv](../buonoressrv/) del prodotto medesimo.
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