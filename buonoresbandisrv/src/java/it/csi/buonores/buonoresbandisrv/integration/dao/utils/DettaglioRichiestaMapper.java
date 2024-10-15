/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbandisrv.dto.ModelPersona;
import it.csi.buonores.buonoresbandisrv.dto.ModelRichiesta;
import it.csi.buonores.buonoresbandisrv.dto.ModelRichiestaContratto;
import it.csi.buonores.buonoresbandisrv.dto.ModelRichiestaDomicilioDestinatario;
import it.csi.buonores.buonoresbandisrv.dto.ModelStruttura;
import it.csi.buonores.buonoresbandisrv.dto.ModelStrutturaSedeLegale;
import it.csi.buonores.buonoresbandisrv.util.Util;

public class DettaglioRichiestaMapper implements RowMapper<ModelRichiesta> {

	@Override
	public ModelRichiesta mapRow(ResultSet rs, int rowNum) throws SQLException {

		// Richiesta
		ModelRichiesta richiesta = new ModelRichiesta();
		richiesta.setNumero(rs.getString("domanda_numero"));
		richiesta.setStato(rs.getString("domanda_stato_cod"));
		richiesta.setDomandaStatoDesc(rs.getString("domanda_stato_desc"));
		richiesta.setContributoTipoDesc(rs.getString("contributo_tipo_desc"));
		richiesta.setSportelloId(rs.getBigDecimal("sportello_id"));
		richiesta.setDomandaDetId(rs.getBigDecimal("domanda_det_id"));
		richiesta.setDomandaDetCod(rs.getString("domanda_det_cod"));
		richiesta.setDomandaStatoDesc(rs.getString("domanda_stato_desc"));
		richiesta.setDataAggiornamento(Util.getDataWithoutTime(rs.getString("data_creazione")));
		richiesta.setNote(rs.getString("note"));
		richiesta.setStudioDestinatario(rs.getString("titolo_studio_cod"));
		richiesta.setAslDestinatario(rs.getString("asl_azienda_desc"));
		richiesta.setPunteggioBisognoSociale(rs.getBigDecimal("punteggio_sociale"));
		richiesta.setLavoroDestinatario((Boolean) rs.getObject("situazione_lavorativa_attiva"));
		richiesta.setDelega(rs.getString("delega"));

		richiesta.setAreaId(rs.getBigDecimal("area_id"));

		richiesta.setAttestazioneIsee(null);
		richiesta.setValutazioneMultidimensionale(rs.getString("valutazione_multidimensionale_cod"));
		richiesta.setNessunaIncompatibilita((Boolean) rs.getObject("nessuna_incompatibilita"));
		// Richiedente
		ModelPersona richiedente = new ModelPersona();
		richiedente.setCf(rs.getString("richiedente_cf"));
		richiedente.setNome(rs.getString("richiedente_nome"));
		richiedente.setCognome(rs.getString("richiedente_cognome"));
		richiedente.setStatoNascita(rs.getString("richiedente_nascita_stato"));
		richiedente.setDataNascita(Util.getDataWithoutTime(rs.getString("richiedente_nascita_data")));
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
		destinatario.setDataNascita(Util.getDataWithoutTime(rs.getString("destinatario_nascita_data")));
		destinatario.setStatoNascita(rs.getString("destinatario_nascita_stato"));
		destinatario.setComuneNascita(rs.getString("destinatario_nascita_comune"));
		destinatario.setProvinciaNascita(rs.getString("destinatario_nascita_provincia"));
		destinatario.setIndirizzoResidenza(rs.getString("destinatario_residenza_indirizzo"));
		destinatario.setComuneResidenza(rs.getString("destinatario_residenza_comune"));
		destinatario.setProvinciaResidenza(rs.getString("destinatario_residenza_provincia"));
		richiesta.setDestinatario(destinatario);
		// domicilioDestinatario
		ModelRichiestaDomicilioDestinatario domicilioDestinatario = new ModelRichiestaDomicilioDestinatario();
		domicilioDestinatario.setComune(rs.getString("destinatario_domicilio_comune"));
		domicilioDestinatario.setProvincia(rs.getString("destinatario_domicilio_provincia"));
		domicilioDestinatario.setIndirizzo(rs.getString("destinatario_domicilio_indirizzo"));
		richiesta.setDomicilioDestinatario(domicilioDestinatario);

		// Contratto
		ModelRichiestaContratto contratto = new ModelRichiestaContratto();
		contratto.setTipo(rs.getString("contratto_tipo_cod"));
		contratto.setDataInizio(rs.getTimestamp("contratto_data_inizio"));
		contratto.setDataFine(rs.getTimestamp("contratto_data_fine"));
		contratto.setTitoloPrivato((Boolean) rs.getObject("titolo_privato"));
		contratto.setIncompatibilitaPerContratto((Boolean) rs.getObject("incompatibilita_per_contratto"));

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

		richiesta.setNoteInterna(rs.getString("nota_interna"));
		richiesta.setNoteRichiedente(rs.getString("note_richiedente"));
		// CAMPI AGGIUNTIVI INSIEME A AREA_ID
		richiesta.setDataDomanda(rs.getString("domanda_data"));
		richiesta.setOraDomanda(rs.getString("domanda_data_ora"));
		richiesta.setDomandaId(rs.getBigDecimal("domanda_id"));

		return richiesta;

	}

}
