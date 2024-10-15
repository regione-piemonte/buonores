/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.util.validator.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import it.csi.buonores.buonoresbff.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbff.dto.ModelAllegato;
import it.csi.buonores.buonoresbff.dto.ModelBozzaRichiesta;
import it.csi.buonores.buonoresbff.dto.ModelContratto;
import it.csi.buonores.buonoresbff.dto.ModelContrattoAllegati;
import it.csi.buonores.buonoresbff.dto.ModelIsee;
import it.csi.buonores.buonoresbff.dto.ModelPersona;
import it.csi.buonores.buonoresbff.dto.ModelRichiesta;
import it.csi.buonores.buonoresbff.dto.ModelStatoBuono;
import it.csi.buonores.buonoresbff.dto.custom.ModelGetAllegato;
import it.csi.buonores.buonoresbff.dto.custom.ModelGetAllegatoExt;
import it.csi.buonores.buonoresbff.dto.custom.ModelRichiestaExt;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.integration.dao.custom.AllegatoBuonoDao;
import it.csi.buonores.buonoresbff.integration.dao.custom.AllegatoDao;
import it.csi.buonores.buonoresbff.util.Constants;
import it.csi.buonores.buonoresbff.util.Util;
import it.csi.buonores.buonoresbff.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoresbff.util.enumerator.ErrorParamEnum;

@Service
public class ValidateGenericImpl extends BaseValidate {

	@Autowired
	AllegatoDao allegatoDao;

	@Autowired
	AllegatoBuonoDao allegatoBuonoDao;

	public List<ErroreDettaglio> validateAnagrafica(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String cf, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws DatabaseException {
		logInfo(new Object() {
		}.getClass().getEnclosingMethod().getName(), "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<>();

		commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		// inserimento controllo specifico citid
		checkIsValorizzato(result, cf, ErrorParamEnum.CF.getCode());

		if (!formalCheckCitId(cf)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR06.getCode(), cf));
		}

		return result;
	}

	public List<ErroreDettaglio> validateGeneric(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws DatabaseException {
		logInfo(new Object() {
		}.getClass().getEnclosingMethod().getName(), "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<>();
		commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);
		return result;
	}

	public List<ErroreDettaglio> validateLight(String shibIdentitaCodiceFiscale,
			String xForwardedFor, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws DatabaseException {
		logInfo(new Object() {
		}.getClass().getEnclosingMethod().getName(), "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<>();
		commonCheckLight(result, shibIdentitaCodiceFiscale, xForwardedFor);
		return result;
	}

	public List<ErroreDettaglio> validateListaDecodificaStrutture(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest, String comune) throws DatabaseException {
		logInfo(new Object() {
		}.getClass().getEnclosingMethod().getName(), "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<>();
		commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);
		checkIsValorizzato(result, comune, ErrorParamEnum.COMUNE.getCode());

		return result;
	}

	public List<ErroreDettaglio> validateCronologia(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest, String cf) throws DatabaseException {
		logInfo(new Object() {
		}.getClass().getEnclosingMethod().getName(), "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<>();
		checkCodFiscaleAndShibIden(result, cf, shibIdentitaCodiceFiscale);
		commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);
		return result;
	}

	public List<ErroreDettaglio> validatePostCronologia(List<ErroreDettaglio> listError, String xRequestId,
			String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest, String stato) throws DatabaseException {
		logInfo(new Object() {
		}.getClass().getEnclosingMethod().getName(), "BEGIN");

		commonCheck(listError, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);
		checkEmptyString(listError, stato, ErrorParamEnum.STATO.getCode());

		return listError;
	}

	public List<ErroreDettaglio> notaControdeduzione(List<ErroreDettaglio> result, String nota)
			throws DatabaseException {
		if (!Util.isValorizzato(nota)) {
			result.add(
					getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.NOTA_CONTRODEDUZIONE.getCode()));
		}

		return result;
	}

	public List<ErroreDettaglio> validatePostRichieste(String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest, ModelBozzaRichiesta richiesta,
			Long idStato, Long idContributo, Long idSportello, Long idTitolo, Long idAsl, Long idRelazione)
			throws DatabaseException {
		logInfo(new Object() {
		}.getClass().getEnclosingMethod().getName(), "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<>();
		commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		checkNull(result, idStato, ErrorParamEnum.STATO_DOMANDA.getCode(), CodeErrorEnum.ERR02);
		checkNull(result, idContributo, ErrorParamEnum.TIPO_CONTRIBUTO.getCode(), CodeErrorEnum.ERR02);
		checkNull(result, idSportello, ErrorParamEnum.SPORTELLO_ATTIVO.getCode(), CodeErrorEnum.ERR02);
		checkCodFiscaleAndShibIden(result, richiesta.getRichiedente().getCf(), shibIdentitaCodiceFiscale);
		checkEmptyString(result, richiesta.getStudioDestinatario(),
				ErrorParamEnum.TITOLO_STUDIO_DESTINATARIO.getCode());

		result = (!StringUtils.isEmpty(richiesta.getStudioDestinatario()))
				? checkNull(result, idTitolo, ErrorParamEnum.TITOLO_STUDIO_DESTINATARIO.getCode(), CodeErrorEnum.ERR02)
				: result;

		result = (!StringUtils.isEmpty(richiesta.getDelega()))
				? checkNull(result, idRelazione, ErrorParamEnum.TIPO_RAPPORTO.getCode(), CodeErrorEnum.ERR02)
				: result;

		checkEmptyString(result, richiesta.getAslDestinatario(), ErrorParamEnum.ASL_DESTINATARIO.getCode());
		result = (!StringUtils.isEmpty(richiesta.getAslDestinatario()))
				? checkNull(result, idAsl, ErrorParamEnum.ASL_DESTINATARIO.getCode(), CodeErrorEnum.ERR02)
				: result;

		personaCheck(result, richiesta.getRichiedente(), Constants.RICHIEDENTE);
		personaCheck(result, richiesta.getDestinatario(), Constants.DESTINATARIO);
		return result;
	}

	public List<ErroreDettaglio> validatePostAllegati(String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest, byte[] allegato,
			String xFilenameOriginale) throws DatabaseException {
		logInfo(new Object() {
		}.getClass().getEnclosingMethod().getName(), "BEGIN");

		CharSequence arr[] = { "\\", "|", "/", "?", "*", ":", "\"", "<", ">", ";" };
		boolean trovato = false;

		// 2. Verifica parametri in input (Criteri di validazione della richiesta)
		// 2a) Obbligatorieta'
		List<ErroreDettaglio> result = new ArrayList<>();

		commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);
		if (allegato == null || allegato.length < 1000) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.ALLEGATO.getCode()));
		}
		if (!Util.isValorizzato(xFilenameOriginale)) {
			result.add(
					getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.ALLEGATO_NOME_FILE.getCode()));
		} else {
			for (CharSequence a : arr) {
				if (xFilenameOriginale.contains(a)) {
					trovato = true;
				}
			}
			if (trovato) {
				result.add(getValueGenericError(CodeErrorEnum.ERR16.getCode(),
						ErrorParamEnum.ALLEGATO_NOME_FILE.getCode() + ":" + xFilenameOriginale));
			}
		}

		return result;
	}

	public List<ErroreDettaglio> validatePutRichieste(String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest, ModelRichiesta richiesta, Long idTitolo,
			Long idRapporto, Long idAsl, Long idRelazione, Long idValutazione, Long idContratto, String numeroDomanda,
			ModelRichiesta richiestaDb) throws DatabaseException {
		logInfo(new Object() {
		}.getClass().getEnclosingMethod().getName(), "BEGIN");

		// 2. Verifica parametri in input (Criteri di validazione della richiesta)
		// 2a) Obbligatorieta'
		List<ErroreDettaglio> result = new ArrayList<>();
		commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);
		checkNumeroDomanda(result, numeroDomanda, richiesta.getNumero());
		personaCheck(result, richiesta.getRichiedente(), Constants.RICHIEDENTE);
		personaCheck(result, richiesta.getDestinatario(), Constants.DESTINATARIO);
		checkCodFiscaleAndShibIden(result, richiestaDb.getRichiedente().getCf(), shibIdentitaCodiceFiscale);
		congruitaPersonaCheck(result, richiesta.getRichiedente(), richiestaDb.getRichiedente(),
				Constants.RICHIEDENTE);
		congruitaPersonaCheck(result, richiesta.getDestinatario(), richiestaDb.getDestinatario(),
				Constants.DESTINATARIO);
		checkEmptyString(result, richiesta.getStudioDestinatario(),
				ErrorParamEnum.TITOLO_STUDIO_DESTINATARIO.getCode());
		result = (!StringUtils.isEmpty(richiesta.getStudioDestinatario()))
				? checkNull(result, idTitolo, ErrorParamEnum.TITOLO_STUDIO_DESTINATARIO.getCode(), CodeErrorEnum.ERR02)
				: result;

		checkEmptyString(result, richiesta.getAslDestinatario(), ErrorParamEnum.ASL_DESTINATARIO.getCode());
		result = (!StringUtils.isEmpty(richiesta.getAslDestinatario()))
				? checkNull(result, idAsl, ErrorParamEnum.ASL_DESTINATARIO.getCode(), CodeErrorEnum.ERR02)
				: result;

		result = (!StringUtils.isEmpty(richiesta.getDelega()))
				? checkNull(result, idRapporto, ErrorParamEnum.TIPO_RAPPORTO.getCode(), CodeErrorEnum.ERR02)
				: result;

		result = (!StringUtils.isEmpty(richiesta.getValutazioneMultidimensionale())) ? checkNull(result, idValutazione,
				ErrorParamEnum.VALUTAZIONE_MULTIDIMENSIONALE.getCode(), CodeErrorEnum.ERR02) : result;

		if (richiesta.getContratto() != null) {
			if (!StringUtils.isEmpty(richiesta.getContratto().getTipo())) {
				result = checkNull(result, idContratto, ErrorParamEnum.TIPO_CONTRATTO.getCode(), CodeErrorEnum.ERR02);
			}
			// RIMOZIONE_INTESTATARIO_DATA_FINE POST_DEMO 14_04_2023
			// if
			// (!StringUtils.isEmpty(richiesta.getContratto().getRelazioneDestinatario())) {
			// result = checkNull(result, idRelazione,
			// ErrorParamEnum.TIPO_RELAZIONE.getCode(), CodeErrorEnum.ERR02);
			// }
		}

		// MODIFICA TAG 003 buonodom
		if (richiesta.getStato().equalsIgnoreCase(Constants.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA)) {
			result = notaControdeduzione(result, richiesta.getNoteRichiedente());
		}

		return result;
	}

	// 2. Verifica parametri in input (Criteri di validazione della richiesta)
	// 2a) Obbligatorieta'
	private List<ErroreDettaglio> commonCheck(List<ErroreDettaglio> result, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio) throws DatabaseException {

		checkEmptyString(result, xRequestId, ErrorParamEnum.X_REQUEST_ID.getCode());
		checkEmptyString(result, xForwardedFor, ErrorParamEnum.X_FORWARDED_FOR.getCode());
		checkEmptyString(result, xCodiceServizio, ErrorParamEnum.X_CODICE_SERVIZIO.getCode());

		if (StringUtils.isEmpty(shibIdentitaCodiceFiscale)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(),
					ErrorParamEnum.SHIB_IDENTITA_CODICEFISCALE.getCode()));
		} else if (!formalCheckCitId(shibIdentitaCodiceFiscale)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), shibIdentitaCodiceFiscale));
		}

		return result;
	}

	// 2. Verifica parametri in input (Criteri di validazione della richiesta)
	// 2a) Obbligatorieta'
	private List<ErroreDettaglio> commonCheckLight(List<ErroreDettaglio> result, String shibIdentitaCodiceFiscale,
			String xForwardedFor) throws DatabaseException {

		checkEmptyString(result, xForwardedFor, ErrorParamEnum.X_FORWARDED_FOR.getCode());

		if (StringUtils.isEmpty(shibIdentitaCodiceFiscale)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(),
					ErrorParamEnum.SHIB_IDENTITA_CODICEFISCALE.getCode()));
		} else if (!formalCheckCitId(shibIdentitaCodiceFiscale)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), shibIdentitaCodiceFiscale));
		}

		return result;
	}

	private List<ErroreDettaglio> checkNull(List<ErroreDettaglio> result, Object id, String tipoId,
			CodeErrorEnum codError)
			throws DatabaseException {
		if (id == null) {
			result.add(getValueGenericError(codError.getCode(), tipoId));
		}
		return result;
	}

	private List<ErroreDettaglio> checkNumeroDomanda(List<ErroreDettaglio> result, String numeroParam,
			String numeroPaylod) throws DatabaseException {

		if (!numeroParam.equals(numeroPaylod)) {
			String param = numeroParam + " diverso da " + numeroPaylod;
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
		}
		return result;
	}

	public List<ErroreDettaglio> checkCodFiscaleAndShibIden(List<ErroreDettaglio> result, String codFiscale,
			String shibIdentita) throws DatabaseException {

		if (codFiscale != null && !codFiscale.equals(shibIdentita)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR12.getCode(), codFiscale));
		}
		return result;
	}

	private List<ErroreDettaglio> checkEmptyString(List<ErroreDettaglio> result, Object element,
			String tipoElement) throws DatabaseException {
		if (StringUtils.isEmpty(element)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), tipoElement));
		}
		return result;
	}

	private List<ErroreDettaglio> checkIsValorizzato(List<ErroreDettaglio> result, String element,
			String tipoElement) throws DatabaseException {
		if (!Util.isValorizzato(element)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), tipoElement));
		}

		return result;
	}

	private List<ErroreDettaglio> cfCheck(List<ErroreDettaglio> result, String cf, String tipoPersona)
			throws DatabaseException {
		String param = "";
		if (StringUtils.isEmpty(cf)) {
			switch (tipoPersona) {
				case Constants.RICHIEDENTE:
					param = ErrorParamEnum.CF_RICHIEDENTE.getCode();
					break;
				case Constants.DESTINATARIO:
					param = ErrorParamEnum.CF_DESTINATARIO.getCode();
					break;
				case Constants.CONTRATTO_RSA:
					param = ErrorParamEnum.CONTRATTO_RSA_PIVA.getCode();
					break;
			}

			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		} else if (!formalCheckCitId(cf)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), cf));
		}
		return result;
	}

	private List<ErroreDettaglio> personaCheck(List<ErroreDettaglio> result, ModelPersona persona,
			String tipoPersona) throws DatabaseException {
		cfCheck(result, persona.getCf(), tipoPersona);

		Boolean isRichiedente = tipoPersona.equals(Constants.RICHIEDENTE) ? true : false;
		checkEmptyString(result, persona.getNome(),
				isRichiedente ? ErrorParamEnum.NOME_RICHIEDENTE.getCode() : ErrorParamEnum.NOME_DESTINATARIO.getCode());
		checkEmptyString(result, persona.getCognome(), isRichiedente ? ErrorParamEnum.COGNOME_RICHIEDENTE.getCode()
				: ErrorParamEnum.COGNOME_DESTINATARIO.getCode());
		checkEmptyString(result, persona.getDataNascita(),
				isRichiedente ? ErrorParamEnum.DATA_NASCITA_RICHIEDENTE.getCode()
						: ErrorParamEnum.DATA_NASCITA_DESTINATARIO.getCode());
		checkEmptyString(result, persona.getStatoNascita(),
				isRichiedente ? ErrorParamEnum.STATO_NASCITA_RICHIEDENTE.getCode()
						: ErrorParamEnum.STATO_NASCITA_DESTINATARIO.getCode());
		checkEmptyString(result, persona.getComuneNascita(),
				isRichiedente ? ErrorParamEnum.COMUNE_NASCITA_RICHIEDENTE.getCode()
						: ErrorParamEnum.COMUNE_NASCITA_DESTINATARIO.getCode());
		checkEmptyString(result, persona.getProvinciaNascita(),
				isRichiedente ? ErrorParamEnum.PROVINCIA_NASCITA_RICHIEDENTE.getCode()
						: ErrorParamEnum.PROVINCIA_NASCITA_DESTINATARIO.getCode());
		checkEmptyString(result, persona.getIndirizzoResidenza(),
				isRichiedente ? ErrorParamEnum.INDIRIZZO_RESIDENZA_RICHIEDENTE.getCode()
						: ErrorParamEnum.INDIRIZZO_RESIDENZA_DESTINATARIO.getCode());
		checkEmptyString(result, persona.getComuneResidenza(),
				isRichiedente ? ErrorParamEnum.COMUNE_RESIDENZA_RICHIEDENTE.getCode()
						: ErrorParamEnum.COMUNE_RESIDENZA_DESTINATARIO.getCode());
		checkEmptyString(result, persona.getProvinciaResidenza(),
				isRichiedente ? ErrorParamEnum.PROVINCIA_RESIDENZA_RICHIEDENTE.getCode()
						: ErrorParamEnum.PROVINCIA_RESIDENZA_DESTINATARIO.getCode());

		return result;
	}

	public List<ErroreDettaglio> checkCambioStatoCoerente(List<ErroreDettaglio> result, String statoDiArrivo,
			String statoDiPartenza) throws DatabaseException {
		boolean incoerente = false;

		switch (statoDiArrivo) {
			case Constants.ANNULLATA:
			case Constants.INVIATA:
				if (!statoDiPartenza.equals(Constants.BOZZA))
					incoerente = true;
				break;
			case Constants.RETTIFICATA:
				if (!statoDiPartenza.equals(Constants.IN_RETTIFICA))
					incoerente = true;
				break;
			case Constants.DA_RETTIFICARE:
				if (!statoDiPartenza.equals(Constants.PRESA_IN_CARICO))
					incoerente = true;
				break;
			case Constants.IN_RETTIFICA:
				if (!statoDiPartenza.equals(Constants.DA_RETTIFICARE))
					incoerente = true;
				break;
			// Modifica TAG 2.2.0v002 buonodom
			case Constants.PERFEZIONATA_IN_PAGAMENTO:
				if (!statoDiPartenza.equals(Constants.PREAVVISO_DI_DINIEGO_IN_PAGAMENTO)
						&& !statoDiPartenza.equals(Constants.AMMESSA_CON_RISERVA_IN_PAGAMENTO))
					incoerente = true;
				break;
			case Constants.CONTRODEDOTTA:
				if (!statoDiPartenza.equals(Constants.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA))
					incoerente = true;
				break;
			// Modifica TAG 2.2.0v002 buonodom
			case Constants.RINUNCIATA:
				if (statoDiPartenza.equals(Constants.ANNULLATA)
						|| statoDiPartenza.equals(Constants.BOZZA) || statoDiPartenza.equals(Constants.DINIEGO))
					incoerente = true;
				break;
			default:
				incoerente = false;
		}

		if (statoDiArrivo.equals(statoDiPartenza))
			incoerente = true;

		if (incoerente) {
			result.add(getValueGenericError(CodeErrorEnum.ERR13.getCode(), ErrorParamEnum.STATO.getCode()));
		}
		return result;
	}

	public List<ErroreDettaglio> checkSeCambioStatoPossibile(List<ErroreDettaglio> result,
			ModelRichiestaExt richiesta) throws DatabaseException {
		if (richiesta.getPunteggioBisognoSociale() != null
				&& richiesta.getPunteggioBisognoSociale().compareTo(BigDecimal.valueOf(7)) < 0) {
			result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), ErrorParamEnum.PUNTEGGIO_SOCIALE.getCode()));
		}
		if (richiesta.isNessunaIncompatibilita() == null) {
			result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(),
					ErrorParamEnum.NESSUNA_INCOMPATIBILITA.getCode()));
		}
		if (richiesta.isAttestazioneIsee() == null || !richiesta.isAttestazioneIsee()) {
			result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), ErrorParamEnum.ISEE_CONFORME.getCode()));
		}
		if (Util.isAgeBetween18and65(richiesta.getDestinatario().getDataNascita())) {
			// boolean incoerente = richiesta.isLavoroDestinatario() == null ? true : false;
			if (richiesta.isLavoroDestinatario() == null) {
				result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(),
						ErrorParamEnum.SITUAZIONE_LAVORATIVA_ATTIVA.getCode()));
			}
		}
		if ((!richiesta.getDestinatario().getCf().equals(richiesta.getRichiedente().getCf()))
				&& !Util.isValorizzato(richiesta.getDelega())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), ErrorParamEnum.TIPO_RAPPORTO.getCode()));
		}

		checkIsValorizzato(result, richiesta.getAslDestinatario(), ErrorParamEnum.ASL_DESTINATARIO.getCode());
		checkIsValorizzato(result, richiesta.getStudioDestinatario(),
				ErrorParamEnum.TITOLO_STUDIO_DESTINATARIO.getCode());
		checkIsValorizzato(result, richiesta.getValutazioneMultidimensionale(),
				ErrorParamEnum.VALUTAZIONE_MULTIDIMENSIONALE.getCode());

		if (richiesta.getContratto() != null) {
			if (richiesta.getContratto().getTipo() != null
					&& !richiesta.getContratto().getTipo().equals(Constants.NESSUN_CONTRATTO)) {
				checkIsValorizzato(result, richiesta.getContratto().getTipo(), ErrorParamEnum.CONTRATTO_RSA.getCode());
				// RIMOZIONE POST_DEMO 14_04_2023
				// checkNull(result,richiesta.getContratto().getIntestatario(),
				// ErrorParamEnum.INTESTATARIO.getCode(), CodeErrorEnum.ERR14);
				checkNull(result, richiesta.getContratto().getDataInizio(),
						ErrorParamEnum.DATA_INIZIO_CONTRATTO.getCode(), CodeErrorEnum.ERR14);
				checkNull(result, richiesta.getContratto().getStruttura(), ErrorParamEnum.STRUTTURA.getCode(),
						CodeErrorEnum.ERR14);
				// Controllo per evitare struttura not null ma con oggetto vuoto all'interno
				if (richiesta.getContratto().getStruttura() != null) {
					checkNull(result, richiesta.getContratto().getStruttura().getNome(),
							ErrorParamEnum.STRUTTURA_NOME.getCode(), CodeErrorEnum.ERR14);
				}
				// RIMOZIONE POST_DEMO 14_04_2023
				// if((!richiesta.getDestinatario().getCf().equals(richiesta.getContratto().getIntestatario().getCf()))
				// &&
				// (!richiesta.getRichiedente().getCf().equals(richiesta.getContratto().getIntestatario().getCf()))
				// ) {
				// checkNull(result,richiesta.getContratto().getRelazioneDestinatario(),
				// ErrorParamEnum.TIPO_RELAZIONE.getCode(), CodeErrorEnum.ERR14);
				// }
				//
				// if(richiesta.getContratto().getDataFine() != null) {
				// if (richiesta.getContratto().getDataFine().compareTo(new Date()) <= 0) {
				// result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(),
				// ErrorParamEnum.DATA_FINE_CONTRATTO.getCode()));
				// } else if (richiesta.getContratto().getDataFine()
				// .compareTo(richiesta.getContratto().getDataInizio()) <= 0) {
				// result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(),
				// ErrorParamEnum.DATA_FINE_CONTRATTO.getCode()));
				// }
				// }

				if (richiesta.getContratto().isIncompatibilitaPerContratto() != null
						&& richiesta.getContratto().isIncompatibilitaPerContratto()) {
					result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(),
							ErrorParamEnum.INCOMPATIBILITA_PER_CONTRATTO.getCode()));
				}

				if (richiesta.getContratto().isTitoloPrivato() != null
						&& !richiesta.getContratto().isTitoloPrivato()) {
					result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(),
							ErrorParamEnum.TITOLO_PRIVATO.getCode()));
				}
			}
		}

		return result;
	}

	private List<ErroreDettaglio> checkNullableAndEquity(List<ErroreDettaglio> result, String element,
			String dbElement, String param) throws DatabaseException {
		if (element != null && !element.equalsIgnoreCase(dbElement)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR15.getCode(), param));
		}

		return result;
	}

	private List<ErroreDettaglio> checkNullableAndEquity(List<ErroreDettaglio> result, Date element,
			Date dbElement, String param) throws DatabaseException {
		if (element != null && !element.equals(dbElement)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR15.getCode(), param));
		}

		return result;
	}

	private List<ErroreDettaglio> congruitaPersonaCheck(List<ErroreDettaglio> result, ModelPersona persona,
			ModelPersona personaDb, String tipoPersona) throws DatabaseException {
		Boolean isRichiedente = tipoPersona.equals(Constants.RICHIEDENTE) ? true : false;
		checkNullableAndEquity(result, persona.getCf(), personaDb.getCf(),
				isRichiedente ? ErrorParamEnum.CF_RICHIEDENTE.getCode() : ErrorParamEnum.CF_DESTINATARIO.getCode());
		checkNullableAndEquity(result, persona.getNome(), personaDb.getNome(),
				isRichiedente ? ErrorParamEnum.NOME_RICHIEDENTE.getCode() : ErrorParamEnum.NOME_DESTINATARIO.getCode());
		checkNullableAndEquity(result, persona.getCognome(), personaDb.getCognome(),
				isRichiedente ? ErrorParamEnum.COGNOME_RICHIEDENTE.getCode()
						: ErrorParamEnum.COGNOME_DESTINATARIO.getCode());
		checkNullableAndEquity(result, persona.getDataNascita(), personaDb.getDataNascita(),
				isRichiedente ? ErrorParamEnum.DATA_NASCITA_RICHIEDENTE.getCode()
						: ErrorParamEnum.DATA_NASCITA_DESTINATARIO.getCode());
		checkNullableAndEquity(result, persona.getStatoNascita(), personaDb.getStatoNascita(),
				isRichiedente ? ErrorParamEnum.STATO_NASCITA_RICHIEDENTE.getCode()
						: ErrorParamEnum.STATO_NASCITA_DESTINATARIO.getCode());
		checkNullableAndEquity(result, persona.getComuneNascita(), personaDb.getComuneNascita(),
				isRichiedente ? ErrorParamEnum.COMUNE_NASCITA_RICHIEDENTE.getCode()
						: ErrorParamEnum.COMUNE_NASCITA_DESTINATARIO.getCode());
		checkNullableAndEquity(result, persona.getProvinciaNascita(), personaDb.getProvinciaNascita(),
				isRichiedente ? ErrorParamEnum.PROVINCIA_NASCITA_RICHIEDENTE.getCode()
						: ErrorParamEnum.PROVINCIA_NASCITA_DESTINATARIO.getCode());
		checkNullableAndEquity(result, persona.getIndirizzoResidenza(), personaDb.getIndirizzoResidenza(),
				isRichiedente ? ErrorParamEnum.INDIRIZZO_RESIDENZA_RICHIEDENTE.getCode()
						: ErrorParamEnum.INDIRIZZO_RESIDENZA_DESTINATARIO.getCode());
		checkNullableAndEquity(result, persona.getComuneResidenza(), personaDb.getComuneResidenza(),
				isRichiedente ? ErrorParamEnum.COMUNE_RESIDENZA_RICHIEDENTE.getCode()
						: ErrorParamEnum.COMUNE_RESIDENZA_DESTINATARIO.getCode());
		checkNullableAndEquity(result, persona.getProvinciaResidenza(), personaDb.getProvinciaResidenza(),
				isRichiedente ? ErrorParamEnum.PROVINCIA_RESIDENZA_RICHIEDENTE.getCode()
						: ErrorParamEnum.PROVINCIA_RESIDENZA_DESTINATARIO.getCode());

		return result;
	}

	private void checkStati(List<ErroreDettaglio> result, ModelRichiestaExt richiesta, String stato,
			String tipoAllegato, String param, boolean statidacontrollare) throws DatabaseException {
		if (!statidacontrollare) {
			ModelAllegato allegato = richiesta.getAllegati().stream()
					.filter(f -> f.getTipo().equals(tipoAllegato)).findFirst().orElse(null);
			if (allegato == null) {
				result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
			}
		}
	}

	private void deleteFile(ModelRichiestaExt richiesta, String tipoAllegato, String detCod, String cf,
			String... tipoAllegatoToDelete) throws DatabaseException {
		ModelAllegato allegato = richiesta.getAllegati().stream()
				.filter(f -> f.getTipo().equals(tipoAllegato)).findFirst().orElse(null);
		if (allegato != null) {
			ModelGetAllegato a = allegatoDao.selectAllegato(detCod, allegato.getTipo(),
					richiesta.getDomandaDetId());
			allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, allegato.getTipo(), cf);
			File f = new File(a.getFilePath() + File.separator + a.getFileName());
			logInfo("cancello allegato " + tipoAllegato
					+ ((tipoAllegatoToDelete != null) ? "con " + tipoAllegatoToDelete
							: " per " + richiesta.getDelega()),
					a.getFilePath() + File.separator + a.getFileName());
			boolean isFileDeleted = f.delete();
			if (isFileDeleted) {
				logInfo(" allegato  eliminato correttamente" + tipoAllegato
						+ ((tipoAllegatoToDelete != null) ? "con " + tipoAllegatoToDelete
								: " per " + richiesta.getDelega()),
						a.getFilePath() + File.separator + a.getFileName());
			}
		}
	}

	public List<ErroreDettaglio> checkAllegato(List<ErroreDettaglio> result, ModelRichiestaExt richiesta,
			String detCod, String cf, String stato, boolean statidacontrollare) throws DatabaseException {
		CharSequence arr[] = { "\\", "|", "/", "?", "*", ":", "\"", "<", ">", ";" };
		boolean trovato = false;
		try {
			if (Util.isValorizzato(richiesta.getValutazioneMultidimensionale())) {
				if (richiesta.getValutazioneMultidimensionale().equals(Constants.PERSONA_PIU_65_ANNI)) {
					checkStati(result, richiesta, stato, Constants.VERBALE_UVG,
							ErrorParamEnum.VERBALE_VALUTAZIONE_UVG.getCode(), statidacontrollare);
					deleteFile(richiesta, Constants.VERBALE_UMVD, detCod, cf, Constants.VERBALE_UVG);
				}
				if (richiesta.getValutazioneMultidimensionale().equals(Constants.PERSONA_DISABILE)) {
					checkStati(result, richiesta, stato, Constants.VERBALE_UMVD,
							ErrorParamEnum.VERBALE_VALUTAZIONE_UMVD.getCode(), statidacontrollare);
					deleteFile(richiesta, Constants.VERBALE_UVG, detCod, cf, Constants.VERBALE_UMVD);
				}
			}

			if (richiesta.getContratto() != null && Util.isValorizzato(richiesta.getContratto().getTipo())) {
				if (richiesta.getContratto().getTipo().equals(Constants.NESSUN_CONTRATTO)) {
					deleteFile(richiesta, Constants.CONTRATTO_RSA, detCod, cf);
				}
				if (richiesta.getContratto().getTipo().equals(Constants.CONTRATTO_RSA)) {
					checkStati(result, richiesta, stato, Constants.CONTRATTO_RSA,
							ErrorParamEnum.CONTRATTO_RSA.getCode(), statidacontrollare);
				}
			}

			if (Util.isValorizzato(richiesta.getDelega())) {
				if (richiesta.getDelega().equals(Constants.NUCLEO_FAMILIARE)
						|| richiesta.getDelega().equals(Constants.CONIUGE)
						|| richiesta.getDelega().equals(Constants.PARENTE_PRIMO_GRADO)
						|| richiesta.getDelega().equals(Constants.CURATELA)
						|| richiesta.getDelega().equals(Constants.AMMINISTRAZIONE_SOSTEGNO)
						|| richiesta.getDelega().equals(Constants.ALTRO)) {

					checkStati(result, richiesta, stato, Constants.PROCURA_SPECIALE,
							ErrorParamEnum.PROCURA_SPECIALE.getCode(), statidacontrollare); // modifica specifiche
																							// buonodom 28.02.2023
					checkStati(result, richiesta, stato, Constants.CARTA_IDENTITA,
							ErrorParamEnum.DOCUMENTO_IDENTITA.getCode(), statidacontrollare);

					deleteFile(richiesta, Constants.NOMINA_TUTORE, detCod, cf);

				} else if (richiesta.getDelega().equals(Constants.TUTELA)) {

					checkStati(result, richiesta, stato, Constants.NOMINA_TUTORE, ErrorParamEnum.TUTELA.getCode(),
							statidacontrollare);

					// cancello nominaTutore e ci
					deleteFile(richiesta, Constants.CARTA_IDENTITA, detCod, cf);
					deleteFile(richiesta, Constants.PROCURA_SPECIALE, detCod, cf);

				} else {
					deleteFile(richiesta, Constants.CARTA_IDENTITA, detCod, cf);
					deleteFile(richiesta, Constants.PROCURA_SPECIALE, detCod, cf);
					deleteFile(richiesta, Constants.NOMINA_TUTORE, detCod, cf);
				}
			}

			// MODIFICA TAG 002v2 buonodom
			String param = "";
			for (ModelAllegato allegato : richiesta.getAllegati()) {
				for (CharSequence a : arr) {
					if (allegato.getFilename().contains(a)) {
						if (!trovato) {
							param += allegato.getFilename();
						} else {
							param += ", " + allegato.getFilename();
						}
						trovato = true;
					}
				}
			}
			if (trovato) {
				result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
			}
		} catch (Exception e) {
			logError("controlla tipo allegati", "Errore generico ", e);
		}

		return result;
	}

	public List<ErroreDettaglio> validateAddContratto(String numeroRichiesta,
			Long contrattoTipoId, Long fornitoreTipoId, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelContrattoAllegati contrattoAllegati,
			String destinatario, int fornitoreId)
			throws DatabaseException {
		List<ErroreDettaglio> result = new ArrayList<ErroreDettaglio>();

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);
		if (contrattoAllegati == null) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), "Contratto"));
		} else if (contrattoAllegati.getContratto() == null) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), "Contratto"));
		} else if (fornitoreTipoId == null) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), ErrorParamEnum.RSA.getCode()));
		} else if (contrattoAllegati.getAllegati() == null || contrattoAllegati.getAllegati().size() == 0) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), "Allegati"));
		} else if (contrattoTipoId == null) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), ErrorParamEnum.TIPO_CONTRATTO.getCode()));
		} else if (contrattoAllegati.getContratto().getTipo().equals(Constants.NESSUN_CONTRATTO)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), ErrorParamEnum.TIPO_CONTRATTO.getCode()));
		} else if (contrattoAllegati.getContratto().getDataInizio() == null) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), "Data Inizio Contratto"));
		} else {
			if (contrattoAllegati.getContratto().getTipo().equalsIgnoreCase(Constants.CONTRATTO_RSA)) {
				if (contrattoAllegati.getContratto().getStruttura() == null) {
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(),
							ErrorParamEnum.CONTRATTO_RSA.getCode()));
				} else if (contrattoAllegati.getContratto().getStruttura().getPiva() == null) {
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(),
							ErrorParamEnum.CONTRATTO_RSA.getCode()));
				}

				result = cfCheck(result, contrattoAllegati.getContratto().getStruttura().getPiva(),
						Constants.CONTRATTO_RSA);

				ModelAllegato contrattoRsa = contrattoAllegati.getAllegati().stream()
						.filter(f -> f.getTipo().equals(Constants.CONTRATTO_RSA)).findFirst().orElse(null);
				if (contrattoRsa == null) {
					result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), "Copia contratto RSA"));
				}
				if (contrattoAllegati.getAllegati().size() > 1) {
					result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), "Numero allegati"));
				}
			}
		}

		return result;
	}

	public List<ErroreDettaglio> checkAllegatoContrattoFileSystem(ModelContrattoAllegati contrattoAllegati)
			throws DatabaseException {
		List<ErroreDettaglio> result = new ArrayList<ErroreDettaglio>();

		for (int i = 0; i < contrattoAllegati.getAllegati().size(); i++) {
			File file = null;

			ModelGetAllegato allegato = allegatoBuonoDao
					.selectAllegatoBuono(contrattoAllegati.getAllegati().get(i).getId().intValue());

			file = new File(allegato.getFilePath() + File.separator + allegato.getFileName());

			if (!file.exists()) {
				result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), "allegato: " + allegato.getFileName()));
			}
		}

		return result;
	}

	public List<ErroreDettaglio> validateGetContratto(String numeroRichiesta, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale) throws DatabaseException {
		List<ErroreDettaglio> result = new ArrayList<ErroreDettaglio>();
		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		if (!Util.isValorizzato(numeroRichiesta)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), "Numero Richiesta"));
		}

		return result;
	}

	public List<ErroreDettaglio> validatePutContratto(String numeroRichiesta, Integer idContratto, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelContratto contratto)
			throws DatabaseException {
		List<ErroreDettaglio> result = new ArrayList<ErroreDettaglio>();

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		if (!Util.isValorizzato(numeroRichiesta)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), "Numero Richiesta"));
		}
		if (contratto == null) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), "Contratto"));
		}

		if (idContratto <= 0) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), "idContratto"));
		}

		if (contratto != null && contratto.getDataFine() == null) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), "Data fine contratto"));
		}
		return result;
	}

	public List<ErroreDettaglio> validateDeleteContratto(String numeroRichiesta, Integer idContratto, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale)
			throws DatabaseException {
		List<ErroreDettaglio> result = new ArrayList<ErroreDettaglio>();
		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		if (!Util.isValorizzato(numeroRichiesta)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), "Numero Richiesta"));
		}

		if (idContratto <= 0) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), "idContratto"));
		}

		return result;
	}

	public List<ErroreDettaglio> validatePostCronologiaBuono(String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest, ModelStatoBuono stato)
			throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<ErroreDettaglio>();
		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		if (stato == null) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), "Dati decorrenza"));
		} else {
			if (!Util.isValorizzato(stato.getStato())) {
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.STATO.getCode()));
			}
			if (stato.getDecorrenza() == null) {
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), "Data decorrenza"));
			} else if (stato.getDecorrenza()
					.compareTo(Util.getData(Util.getData(new Date()), Constants.DATE_FORMAT_PATTERN)) < 0) {
				result.add(
						getValueGenericError(CodeErrorEnum.ERR02.getCode(), "Data revoca inferiore alla data di oggi"));
			}
		}

		return result;
	}

	public List<ErroreDettaglio> checkCambioStatoCoerenteBuono(List<ErroreDettaglio> result, String statoDiArrivo,
			String statoDiPartenza) throws DatabaseException {
		boolean incoerente = false;

		if (statoDiArrivo.equals(statoDiPartenza))
			incoerente = true;

		if (incoerente) {
			result.add(getValueGenericError(CodeErrorEnum.ERR13.getCode(), ErrorParamEnum.STATO.getCode()));
		}
		return result;
	}

	public List<ErroreDettaglio> checkAllegatoFileSystem(List<ErroreDettaglio> result,
			ModelRichiestaExt richiesta, String detCod, String cf, String stato) throws DatabaseException {
		// verifico coerenza tra allegati db e allegati file system

		List<ModelGetAllegatoExt> allegati = new ArrayList<ModelGetAllegatoExt>();
		allegati = allegatoDao.selectAllegati(richiesta.getDomandaDetId());
		Optional<Boolean> allegato = allegati.stream().map(a -> getAllegato(a.getFilePath(), a.getFileName()))
				.findFirst();
		if (allegato.isEmpty()) {
			result.add(getValueGenericError(CodeErrorEnum.ERR18.getCode(), ""));
		}

		return result;
	}

	public List<ErroreDettaglio> checkSportelloCorrente(List<ErroreDettaglio> result, boolean inCorso)
			throws DatabaseException {
		if (!inCorso) {
			result.add(getValueGenericError(CodeErrorEnum.ERR21.getCode(), ErrorParamEnum.SPORTELLO_CHIUSO.getCode()));
		}

		return result;
	}

	public List<ErroreDettaglio> checkEliminazioneIncompatibilita(List<ErroreDettaglio> result,
			ModelRichiestaExt richiesta) throws DatabaseException {
		if (richiesta.isNessunaIncompatibilita() != null && !richiesta.isNessunaIncompatibilita()) {
			// errore
			result.add(getValueGenericError(CodeErrorEnum.ERR22.getCode()));
		} else if (richiesta.getContratto() != null) {
			if (richiesta.getContratto().isIncompatibilitaPerContratto() != null
					&& richiesta.getContratto().isIncompatibilitaPerContratto()) {
				// errore
				result.add(getValueGenericError(CodeErrorEnum.ERR22.getCode()));
			}
		}

		return result;
	}

	private boolean getAllegato(String filePath, String fileName) {
		File file = null;

		try {
			file = new File(filePath + File.separator + fileName);
			if (!file.exists()) {
				return false;
			} else
				return true;
		} catch (Exception e) {
			logError("verifica allegato", "Errore generico ", e);
			return false;
		}
	}

	public List<ErroreDettaglio> validateGetConformitaIsee(String numeroRichiesta, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale) throws DatabaseException {
		List<ErroreDettaglio> result = new ArrayList<ErroreDettaglio>();

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		if (!Util.isValorizzato(numeroRichiesta)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), "Numero Richiesta"));
		}

		return result;
	}

	public List<ErroreDettaglio> validateAddConformitaIsee(String numeroRichiesta, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelIsee isee)
			throws DatabaseException {
		List<ErroreDettaglio> result = new ArrayList<ErroreDettaglio>();

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		if (!Util.isValorizzato(numeroRichiesta)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), "Numero Richiesta"));
		}

		if (isee == null || !Util.isValorizzato(isee.getAnno())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), "Anno"));
		} else if (!Util.isInt(isee.getAnno())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), "Anno"));
		}

		return result;
	}
}
