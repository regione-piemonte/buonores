/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbff.dto.ModelBozzaRichiestaDomicilioDestinatario;
import it.csi.buonores.buonoresbff.dto.ModelPersona;
import it.csi.buonores.buonoresbff.dto.ModelRichiestaContratto;
import it.csi.buonores.buonoresbff.dto.ModelStruttura;
import it.csi.buonores.buonoresbff.dto.ModelStrutturaSedeLegale;
import it.csi.buonores.buonoresbff.dto.custom.ModelRichiestaExt;
import it.csi.buonores.buonoresbff.util.Constants;
import it.csi.buonores.buonoresbff.util.Util;

public class DettaglioRichiestaExtMapper implements RowMapper<ModelRichiestaExt> {

	@Override
	public ModelRichiestaExt mapRow(ResultSet rs, int rowNum) throws SQLException {

		// Richiesta
		ModelRichiestaExt richiesta = new ModelRichiestaExt();
		richiesta.setNumero(rs.getString("domanda_numero"));
		richiesta.setStato(rs.getString("domanda_stato_cod"));
		richiesta.setDomandaDetCod(rs.getString("domanda_det_cod"));
		richiesta.setSportelloId(rs.getBigDecimal("sportello_id"));
		richiesta.setDomandaDetId(rs.getBigDecimal("domanda_det_id"));
		richiesta.setDataAggiornamento(Util.getData(rs.getString("data_creazione"), Constants.DATE_FORMAT_PATTERN));
		richiesta.setNote(rs.getString("note"));
		richiesta.setStudioDestinatario(rs.getString("titolo_studio_cod"));
		richiesta.setAslDestinatario(rs.getString("asl_cod"));
		richiesta.setPunteggioBisognoSociale(rs.getBigDecimal("punteggio_sociale"));
		richiesta.setLavoroDestinatario((Boolean) rs.getObject("situazione_lavorativa_attiva"));
		richiesta.setDelega(rs.getString("delega"));
		richiesta.setNoteRichiedente(rs.getString("note_richiedente")); // MODIFICA TAG 003 buonodom
		richiesta.setAttestazioneIsee((Boolean) rs.getObject("isee_conforme"));
		richiesta.setValutazioneMultidimensionale(rs.getString("valutazione_multidimensionale_cod"));
		richiesta.setNessunaIncompatibilita((Boolean) rs.getObject("nessuna_incompatibilita"));
		// Richiedente
		ModelPersona richiedente = new ModelPersona();
		richiedente.setCf(rs.getString("richiedente_cf"));
		richiedente.setNome(rs.getString("richiedente_nome"));
		richiedente.setCognome(rs.getString("richiedente_cognome"));
		richiedente.setStatoNascita(rs.getString("richiedente_nascita_stato"));
		richiedente
				.setDataNascita(Util.getData(rs.getString("richiedente_nascita_data"), Constants.DATE_FORMAT_PATTERN));
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
		destinatario
				.setDataNascita(Util.getData(rs.getString("destinatario_nascita_data"), Constants.DATE_FORMAT_PATTERN));
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
		// contratto.setRelazioneDestinatario(rs.getString("relazione_destinatario"));
		contratto.setDataInizio(rs.getTimestamp("contratto_data_inizio"));
		// contratto.setDataFine(rs.getTimestamp("contratto_data_fine"));
		// ModelPersonaSintesi intestatario = new ModelPersonaSintesi();
		// intestatario.setCf(rs.getString("datore_di_lavoro_cf"));
		// intestatario.setNome(rs.getString("datore_di_lavoro_nome"));
		// intestatario.setCognome(rs.getString("datore_di_lavoro_cognome"));
		// intestatario.setDataNascita(Util.getData(rs.getString("datore_di_lavoro_nascita_data"),
		// Constants.DATE_FORMAT_PATTERN));
		// intestatario.setStatoNascita(rs.getString("datore_di_lavoro_nascita_stato"));
		// intestatario.setComuneNascita(rs.getString("datore_di_lavoro_nascita_comune"));
		// intestatario.setProvinciaNascita(rs.getString("datore_di_lavoro_nascita_provincia"));
		// contratto.setIntestatario(intestatario);
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
		strutturaRsa.setNome(rs.getString("rsa_sede_legale_denominazione"));
		strutturaRsa.setIndirizzo(rs.getString("rsa_sede_legale_indirizzo"));
		strutturaRsa.setComune(rs.getString("rsa_sede_legale_comune"));
		strutturaRsa.setProvincia(rs.getString("rsa_sede_legale_provincia"));
		strutturaRsa.setSedeLegale(sedeLegale);

		contratto.setStruttura(strutturaRsa);
		richiesta.setContratto(contratto);

		return richiesta;

	}

}
