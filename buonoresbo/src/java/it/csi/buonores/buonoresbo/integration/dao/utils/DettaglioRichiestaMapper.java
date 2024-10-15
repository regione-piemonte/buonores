/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelBozzaRichiestaDomicilioDestinatario;
import it.csi.buonores.buonoresbo.dto.ModelIsee;
import it.csi.buonores.buonoresbo.dto.ModelPersona;
import it.csi.buonores.buonoresbo.dto.ModelProtocollo;
import it.csi.buonores.buonoresbo.dto.ModelRichiesta;
import it.csi.buonores.buonoresbo.dto.ModelRichiestaContratto;
import it.csi.buonores.buonoresbo.dto.ModelStruttura;
import it.csi.buonores.buonoresbo.dto.ModelStrutturaSedeLegale;
import it.csi.buonores.buonoresbo.dto.ModelVerifiche;
import it.csi.buonores.buonoresbo.util.Util;

public class DettaglioRichiestaMapper implements RowMapper<ModelRichiesta> {

	@Override
	public ModelRichiesta mapRow(ResultSet rs, int rowNum) throws SQLException {

		// Richiesta
		ModelRichiesta richiesta = new ModelRichiesta();
		ModelProtocollo protocollo = new ModelProtocollo();
		richiesta.setNumero(rs.getString("domanda_numero"));
		richiesta.setDomandaDetId(rs.getBigDecimal("domanda_det_id"));
		richiesta.setSportelloId(rs.getBigDecimal("sportello_id"));
		richiesta.setStato(rs.getString("domanda_stato_cod"));
		richiesta.setDomandaStatoDesc(rs.getString("domanda_stato_desc"));
		richiesta.setDataAggiornamento(rs.getDate("data_creazione"));
		richiesta.setNote(rs.getString("note"));
		richiesta.setStudioDestinatario(rs.getString("titolo_studio_cod"));
		richiesta.setStudioDestinatarioDesc(rs.getString("titolo_studio_desc"));
		richiesta.setAslDestinatario(rs.getString("asl_cod"));
		richiesta.setAslDestinatarioDesc(rs.getString("asl_azienda_desc"));
		richiesta.setPunteggioBisognoSociale(rs.getBigDecimal("punteggio_sociale"));
		richiesta.setLavoroDestinatario(rs.getString("situazione_lavorativa_attiva"));
		richiesta.setDelegaCod(rs.getString("delega_cod"));
		richiesta.setDelegaDesc(rs.getString("delega_desc"));
		protocollo.setNumero(rs.getString("protocollo_cod"));
		protocollo.setData(Util.getDataWithoutTime(rs.getString("data_protocollo")));
		protocollo.setTipo(rs.getString("tipo_protocollo"));
		richiesta.setProtocollo(protocollo);

		richiesta.setAttestazioneIsee((Boolean) rs.getObject("isee_conforme"));
		richiesta.setValutazioneMultidimensionale(rs.getString("valutazione_multidimensionale_cod"));
		richiesta.setNessunaIncompatibilita((Boolean) rs.getObject("nessuna_incompatibilita"));
		// Richiedente
		ModelPersona richiedente = new ModelPersona();
		richiedente.setCf(rs.getString("richiedente_cf"));
		richiedente.setNome(rs.getString("richiedente_nome"));
		richiedente.setCognome(rs.getString("richiedente_cognome"));
		richiedente.setStatoNascita(rs.getString("richiedente_nascita_stato"));
		richiedente.setDataNascita(rs.getDate("richiedente_nascita_data"));
		richiedente.setComuneNascita(rs.getString("richiedente_nascita_comune"));
		richiedente.setProvinciaNascita(rs.getString("richiedente_nascita_provincia"));
		richiedente.setIndirizzoResidenza(rs.getString("richiedente_residenza_indirizzo"));
		richiedente.setComuneResidenza(rs.getString("richiedente_residenza_comune"));
		richiedente.setProvinciaResidenza(rs.getString("richiedente_residenza_provincia"));
		richiesta.setRichiedente(richiedente);
		// destinatario
		ModelPersona destinatario = new ModelPersona();
		destinatario.setCf(rs.getString("beneficiario_cf"));
		destinatario.setNome(rs.getString("destinatario_nome"));
		destinatario.setCognome(rs.getString("destinatario_cognome"));
		destinatario.setDataNascita(rs.getDate("destinatario_nascita_data"));
		destinatario.setStatoNascita(rs.getString("destinatario_nascita_stato"));
		destinatario.setComuneNascita(rs.getString("destinatario_nascita_comune"));
		destinatario.setProvinciaNascita(rs.getString("destinatario_nascita_provincia"));
		destinatario.setIndirizzoResidenza(rs.getString("destinatario_residenza_indirizzo"));
		destinatario.setComuneResidenza(rs.getString("destinatario_residenza_comune"));
		destinatario.setProvinciaResidenza(rs.getString("destinatario_residenza_provincia"));
		richiesta.setDestinatario(destinatario);
		// domicilioDestinatario
		ModelBozzaRichiestaDomicilioDestinatario domicilioDestinatario = new ModelBozzaRichiestaDomicilioDestinatario();
		domicilioDestinatario.setComune(rs.getString("destinatario_domicilio_comune"));
		domicilioDestinatario.setProvincia(rs.getString("destinatario_domicilio_provincia"));
		domicilioDestinatario.setIndirizzo(rs.getString("destinatario_domicilio_indirizzo"));
		richiesta.setDomicilioDestinatario(domicilioDestinatario);

		// Contratto
		ModelRichiestaContratto contratto = new ModelRichiestaContratto();
		contratto.setTipo(rs.getString("contratto_tipo_cod"));
		contratto.setDataInizio(rs.getTimestamp("contratto_data_inizio"));
		contratto.setTitoloPrivato((Boolean) rs.getObject("titolo_privato"));
		contratto.setIncompatibilitaPerContratto((Boolean) rs.getObject("incompatibilita_per_contratto"));
		;

		// RSA
		ModelStruttura strutturaRsa = new ModelStruttura();
		strutturaRsa.setId(rs.getString("rsa_cod"));
		strutturaRsa.setIdArpe(rs.getString("rsa_arpe_cod"));
		strutturaRsa.setNome(rs.getString("rsa_denominazione"));
		strutturaRsa.setIndirizzo(rs.getString("rsa_indirizzo"));
		strutturaRsa.setComune(rs.getString("rsa_comune"));
		strutturaRsa.setProvincia(rs.getString("rsa_provincia"));
		strutturaRsa.setPiva(rs.getString("rsa_cfpiva"));
		ModelStrutturaSedeLegale sedeLegale = new ModelStrutturaSedeLegale();
		sedeLegale.setNome(rs.getString("rsa_sede_legale_denominazione"));
		sedeLegale.setIndirizzo(rs.getString("rsa_sede_legale_indirizzo"));
		sedeLegale.setComune(rs.getString("rsa_sede_legale_comune"));
		sedeLegale.setProvincia(rs.getString("rsa_sede_legale_provincia"));
		strutturaRsa.setSedeLegale(sedeLegale);

		contratto.setStruttura(strutturaRsa);
		richiesta.setContratto(contratto);

		richiesta.setContributoTipoDesc(rs.getString("contributo_tipo_desc"));
		richiesta.setAreaDesc(rs.getString("area_desc"));

		ModelIsee isee = new ModelIsee();
		isee.setIseeValore((BigDecimal) rs.getObject("isee_valore"));
		isee.setIseeDataRilascio(Util.getDataWithoutTime(rs.getString("isee_data_rilascio")));
		isee.setIseeScadenza(Util.getDataWithoutTime(rs.getString("isee_scadenza")));
		isee.setIseeVerificatoConforme((Boolean) rs.getObject("isee_verificato_conforme"));
		isee.setIseeVerificatoInData(rs.getTimestamp("isee_verificato_in_data"));
		richiesta.setIsee(isee);

		ModelVerifiche verifiche = new ModelVerifiche();
		verifiche.setVerificaEgConclusa((Boolean) rs.getObject("verifica_eg_conclusa"));
		verifiche.setVerificaEgIncompatibilita((Boolean) rs.getObject("verifica_eg_incompatibilita"));
		verifiche.setVerificaEgInCorso((Boolean) rs.getObject("verifica_eg_in_corso"));
		verifiche.setVerificaEgPunteggio(rs.getString("verifica_eg_punteggio_sociale"));
		verifiche.setVerificaEgRichiesta((Boolean) rs.getObject("verifica_eg_richiesta"));
		verifiche.setNoteEnteGestore(rs.getString("note_ente_gestore"));
		richiesta.setVerifiche(verifiche);

		richiesta.setNoteInterna(rs.getString("nota_interna"));
		richiesta.setNoteRichiedente(rs.getString("note_richiedente"));
		richiesta.setDataDecesso(rs.getDate("destinatario_decesso_data"));
		richiesta.setDataResidenzaExtraregione(rs.getDate("destinatario_residenza_extraregione"));

		return richiesta;
	}
}
