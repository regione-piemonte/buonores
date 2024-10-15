
/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.external.startdas.services.stardascommontypes;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per StatoRichiestaType.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;simpleType name="StatoRichiestaType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="IN_CORSO_DI_ACQUISIZIONE"/&gt;
 *     &lt;enumeration value="DA_TRATTARE"/&gt;
 *     &lt;enumeration value="TRATTAMENTO_IN_ESECUZIONE"/&gt;
 *     &lt;enumeration value="ESEGUITA"/&gt;
 *     &lt;enumeration value="ERRORE_IN_FASE_DI_TRATTAMENTO"/&gt;
 *     &lt;enumeration value="NON_TRATTATA"/&gt;
 *     &lt;enumeration value="ERRORE_IN_FASE_DI_ACQUISIZIONE"/&gt;
 *     &lt;enumeration value="PRESA_IN_CARICO_ASSISTENZA"/&gt;
 *     &lt;enumeration value="ATTESA_TRATTAMENTO_ALLEGATI"/&gt;
 *     &lt;enumeration value="ATTESA_ARCHIVIAZIONE_PRINCIPALE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "StatoRichiestaType")
@XmlEnum
public enum StatoRichiestaType {

    IN_CORSO_DI_ACQUISIZIONE,
    DA_TRATTARE,
    TRATTAMENTO_IN_ESECUZIONE,
    ESEGUITA,
    ERRORE_IN_FASE_DI_TRATTAMENTO,
    NON_TRATTATA,
    ERRORE_IN_FASE_DI_ACQUISIZIONE,
    PRESA_IN_CARICO_ASSISTENZA,
    ATTESA_TRATTAMENTO_ALLEGATI,
    ATTESA_ARCHIVIAZIONE_PRINCIPALE;

    public String value() {
        return name();
    }

    public static StatoRichiestaType fromValue(String v) {
        return valueOf(v);
    }

}
