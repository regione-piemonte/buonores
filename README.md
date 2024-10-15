# Prodotto

Pwa e servizi per la gestione del buono di residenzialità

## Versione

2.0.0

## Descrizione del prodotto

Si tratta di una PWA e relativi servizi per raccogliere e gestire le richieste di buoni per la residenzialità da parte dei cittadini. Il prodotto si occupa anche di tutta la parte di gestione dell'istruttoria della domanda del cittadino; settore welfare della Regione Piemonte.

Elenco componenti:

* [BUONORESBANBATCH](buonoresbanbatch) batch java per l'invio delle domande del cittadino verso i servizi di gestione bandi
* [BUONORESBANDISRV](buonoresbandisrv) componente server di comunicazione con i servizi di gestione bandi
* [BUONORESBATCH](buonoresbatch)  batch java per la gestione di alcuni passaggi di stato delle domande del cittadino
* [BUONORESBFF](buonoresbff)  API per la gestione della raccolta delle domande presentate dal cittadino
* [BUONORESBO](buonoresbo)  API per la gestione dell'istruttoria della domanda da parte di operatori regionali e di back office
* [BUONORESCALLBAN](buonorescallban) API di callback per i servizi di gestione bandi 
* [BUONORESSRV](buonoressrv)    API per i servizi in comune tra la web app per il cittadino e il back office 
* [BUONORESSTARDA](buonoresstarda) API di callback per i servizi del protocollo
* [BUONORESWCL](buonoreswcl)     Componente web app per la web app del front office
* [BUONORESFE](buonoresfe)     Componente web app per la gestione della raccolta delle domande presentate dal cittadino
* [BUONORESREND](buonoresrend)    Componente API per l'integrazione con applicativo di rendicontazione RSA

LINK ai repository:..

## Configurazioni iniziali

Si rimanda ai readme delle singole componenti

* [BUONORESBANBATCH](buonoresbanbatch/README.md)
* [BUONORESBANDISRV](buonoresbandisrv/README.md)
* [BUONORESBATCH](buonoresbatch/README.md)
* [BUONORESBFF](buonoresbff/README.md)
* [BUONORESBO](buonoresbo/README.md)
* [BUONORESCALLBAN](buonorescallban/README.md)
* [BUONORESSRV](buonoressrv/README.md)
* [BUONORESSARDA](buonoresstarda/README.md)
* [BUONORESWCL](buonoreswcl/README.md)
* [BUONORESFE](buonoresfe/README.md)
* [BUONORESREND](buonoresrend/README.md)

## Prerequisiti di sistema

Server Web:
Apache 2.4.54

Application Server:
Wildfly 23

Tipo di database:
postgres v12

Dipendenze elencate nella cartella docs/wsdl

## Versioning (Obbligatorio)

Per il versionamento del software si usa la tecnica Semantic Versioning (http://semver.org).

## Authors

* Antonino Lofaro
* Simona Massa
* Egidio Bosio
* Annarita Losurdo
* Maurizio Peisino


## Copyrights

“© Copyright Regione Piemonte – 2024”

## License

SPDX-License-Identifier: inserire il codice SPDX delle licenza
Veder il file LICENSE per i dettagli.