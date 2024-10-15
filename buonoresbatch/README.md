# Componente di Prodotto

BUONORESBATCH

## Versione

2.0.0

## Descrizione del prodotto

Si tratta di batch sviluppato con linguaggio java che serve a verificare lo stato anagrafico di tutti i cittadini che sono beneficiari di domande buono residenzialità, a mandare le notifiche ai cittadini che hanno eseguito la richiesta di domanda, a verificare se sono necessari cambi di stato o meno delle domande stesse. 

## Configurazioni iniziali

I servizi richiamati da questo batch sono i servizi della componente [buonoressrv](../buonoressrv/) del prodotto medesimo.

## Prerequisiti di sistema

Java:
Jdk 11

ANT:
Ant version 1.9.6

Application Server:
Java stand alone application

Tipo di database:
postgres v12

## Installazione

Per generare il pacchetto lanciare il comando ant -Dtarget prod  per generare l'alberatura di deploy

## Deployment

Impostare il classpath alla cartella lib del progetto e far partire il batch lanciando il comando: $JAVA_HOME/bin/java it.csi.buonores.buonoresbatch.Main 

## Versioning

Per il versionamento del software si usa la tecnica Semantic Versioning (http://semver.org).

## Copyrights

“© Copyright Regione Piemonte – 2024”
