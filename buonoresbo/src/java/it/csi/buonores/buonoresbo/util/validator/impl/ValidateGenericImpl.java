/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.util.validator.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import it.csi.buonores.buonoresbo.business.be.service.NotificatoreService;
import it.csi.buonores.buonoresbo.dto.Contact;
import it.csi.buonores.buonoresbo.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbo.dto.ModelFiltriDomandeAperte;
import it.csi.buonores.buonoresbo.dto.ModelFiltriRicercaBuoni;
import it.csi.buonores.buonoresbo.dto.ModelIsee;
import it.csi.buonores.buonoresbo.dto.ModelIseeBuono;
import it.csi.buonores.buonoresbo.dto.Preferences;
import it.csi.buonores.buonoresbo.exception.DatabaseException;
import it.csi.buonores.buonoresbo.util.Constants;
import it.csi.buonores.buonoresbo.util.Util;
import it.csi.buonores.buonoresbo.util.enumerator.CodeErrorEnum;
import it.csi.buonores.buonoresbo.util.enumerator.ErrorParamEnum;

@Service
public class ValidateGenericImpl extends BaseValidate {

	@Autowired
	NotificatoreService contattiService;

	public List<ErroreDettaglio> validateIsee(ModelIsee isee, String proviene) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<>();
		if (proviene.equalsIgnoreCase(Constants.AMMISSIBILE)) {
			if (isee.isIseeVerificatoConforme() != null && !isee.isIseeVerificatoConforme()) {
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(),
						ErrorParamEnum.VERIFICA_CONFORME.getCode()));
			}
			if (isee.getIseeValore() == null || isee.getIseeValore() == BigDecimal.ZERO) {
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.ISEE.getCode()));
			}
		} else if ((isee.isIseeVerificatoConforme() != null && isee.isIseeVerificatoConforme())
				|| isee.isIseeVerificatoConforme() == null) {
			if (isee.getIseeValore() == null || isee.getIseeValore() == BigDecimal.ZERO) {
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.VALORE_ISEE.getCode()));
			}
			if (isee.getIseeDataRilascio() == null) {
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.RILASCIO_ISEE.getCode()));
			}
		}
		return result;
	}

	public List<ErroreDettaglio> checkEmptyString(List<ErroreDettaglio> result, String element, String tipoElement)
			throws DatabaseException {

		if (StringUtils.isEmpty(element)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), tipoElement));
		}
		return result;
	}

	public List<ErroreDettaglio> checkContatti(List<ErroreDettaglio> result, String cfrichiedente, String cfutente)
			throws DatabaseException {
		boolean contattoesiste = false;
		// prendo i contatti del richiedente
		Contact contatto = new Contact();
		Preferences preferenze = new Preferences();
		try {
			contatto = contattiService.sendContact(cfrichiedente, cfutente);
			if (contatto.getEmail() != null) {
				contattoesiste = true;
			}
			preferenze = contattiService.sendPreferenze(cfrichiedente, cfutente);
			boolean canaliemail = false;
			if (preferenze.getChannels() != null) {
				if (preferenze.getChannels().toUpperCase().contains("EMAIL")) {
					canaliemail = true;
				}
			}
			if (!contattoesiste || !canaliemail) {
				result.add(getValueGenericError(CodeErrorEnum.ERR20.getCode(), ErrorParamEnum.CONTATTI.getCode()));
			}
		} catch (Exception e) {
			logError("checkContatti", e.getMessage(), e);
		}
		return result;
	}

	public List<ErroreDettaglio> checkCambioStatoCoerente(List<ErroreDettaglio> result, String statoDiArrivo,
			String statoDiPartenza) throws DatabaseException {
		boolean incoerente = false;

		switch (statoDiArrivo) {
			case Constants.ANNULLATA:
				if (!statoDiPartenza.equals(Constants.BOZZA))
					incoerente = true;
				break;
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
			case Constants.PERFEZIONATA_IN_PAGAMENTO:
				if (!statoDiPartenza.equals(Constants.PREAVVISO_DINIEGO)
						|| !statoDiPartenza.equals(Constants.AMMESSA_RISERVA))
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

	public List<ErroreDettaglio> validateBuonoIsee(ModelIseeBuono isee) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<ErroreDettaglio>();

		if (!Util.isValorizzato(isee.getIseeVerificaEsitoCod())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(),
					ErrorParamEnum.VERIFICA_CONFORME_BUONO.getCode()));
		} else if (Util.isValorizzato(isee.getIseeVerificaEsitoCod())
				&& isee.getIseeVerificaEsitoCod().equalsIgnoreCase("CONFORME")) {

			if (isee.getIseeValore() == null || isee.getIseeValore() == BigDecimal.ZERO) {
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.VALORE_ISEE.getCode()));
			}
			if (isee.getIseeDataRilascio() == null) {
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.RILASCIO_ISEE.getCode()));
			}
		} else {
			if (!Util.isValorizzato(isee.getIseeNote())) {
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), ErrorParamEnum.NOTE_ISEE.getCode()));
			}
		}
		return result;
	}

	public List<ErroreDettaglio> validateRicercaBuoni(ModelFiltriRicercaBuoni filtri) {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<ErroreDettaglio>();
		if (Util.isValorizzato(filtri.getDecessoResidenza())
				&& !Util.isNormalizedString(filtri.getDecessoResidenza())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), ErrorParamEnum.DECESSO_RESIDENZA.getCode()));
		}
		if (Util.isValorizzato(filtri.getDestinatario()) && !Util.isNormalizedString(filtri.getDestinatario())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), ErrorParamEnum.DESTINATARIO.getCode()));
		}
		if (Util.isValorizzato(filtri.getIseeConforme()) && !Util.isNormalizedString(filtri.getIseeConforme())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), ErrorParamEnum.ISEE_CONFORME.getCode()));
		}
		if (Util.isValorizzato(filtri.getIseeVerificaConforme())
				&& !Util.isNormalizedString(filtri.getIseeVerificaConforme())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(),
					ErrorParamEnum.ISEE_VERIFICA_CONFORME.getCode()));
		}
		if (Util.isValorizzato(filtri.getNumeroBuono()) && !Util.isNormalizedString(filtri.getNumeroBuono())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), ErrorParamEnum.NUMERO_BUONO.getCode()));
		}
		if (Util.isValorizzato(filtri.getNumeroDomanda()) && !Util.isNormalizedString(filtri.getNumeroDomanda())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), ErrorParamEnum.NUMERO_DOMANDA.getCode()));
		}
		if (Util.isValorizzato(filtri.getRichiedente()) && !Util.isNormalizedString(filtri.getRichiedente())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), ErrorParamEnum.RICHIEDENTE.getCode()));
		}
		if (Util.isValorizzato(filtri.getStatoBuono()) && !Util.isNormalizedString(filtri.getStatoBuono())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), ErrorParamEnum.STATO_BUONO.getCode()));
		}

		return result;
	}

	public List<ErroreDettaglio> validateDomandeAperte(ModelFiltriDomandeAperte filtri) {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

		List<ErroreDettaglio> result = new ArrayList<ErroreDettaglio>();

		if (Util.isValorizzato(filtri.getCodSportello()) && !Util.isNormalizedString(filtri.getCodSportello())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), ErrorParamEnum.SPORTELLO.getCode()));
		}
		if (Util.isValorizzato(filtri.getNumeroDomanda()) && !Util.isNormalizedString(filtri.getNumeroDomanda())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), ErrorParamEnum.NUMERO_DOMANDA.getCode()));
		}
		if (Util.isValorizzato(filtri.getStatoDomanda()) && !Util.isNormalizedString(filtri.getStatoDomanda())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), ErrorParamEnum.STATO_DOMANDA.getCode()));
		}
		if (Util.isValorizzato(filtri.getDestinatario()) && !Util.isNormalizedString(filtri.getDestinatario())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), ErrorParamEnum.DESTINATARIO.getCode()));
		}
		if (Util.isValorizzato(filtri.getRichiedente()) && !Util.isNormalizedString(filtri.getRichiedente())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), ErrorParamEnum.RICHIEDENTE.getCode()));
		}
		if (Util.isValorizzato(filtri.getMenu()) && !Util.isNormalizedString(filtri.getMenu())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), ErrorParamEnum.MENU.getCode()));
		}
		if (Util.isValorizzato(filtri.getStatoVerificaEnteGestore())
				&& !Util.isNormalizedString(filtri.getStatoVerificaEnteGestore())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(),
					ErrorParamEnum.STATO_VERIFICA_ENTE_GESTORE.getCode()));
		}
		if (Util.isValorizzato(filtri.getVerificaEnteGestore())
				&& !Util.isNormalizedString(filtri.getVerificaEnteGestore())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(),
					ErrorParamEnum.VERIFICA_ENTE_GESTORE.getCode()));
		}
		if (Util.isValorizzato(filtri.getEsitoVerificaEnteGestore())
				&& !Util.isNormalizedString(filtri.getEsitoVerificaEnteGestore())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(),
					ErrorParamEnum.ESITO_VERIFICA_ENTE_GESTORE.getCode()));
		}
		if (Util.isValorizzato(filtri.getDataVerificaDa()) && !Util.isNormalizedDate(filtri.getDataVerificaDa())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), ErrorParamEnum.DATA_VERIFICA_DA.getCode()));
		}
		if (Util.isValorizzato(filtri.getDataVerificaA()) && !Util.isNormalizedDate(filtri.getDataVerificaA())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), ErrorParamEnum.DATA_VERIFICA_A.getCode()));
		}
		if (Util.isValorizzato(filtri.getUltimaDataVerifica())
				&& !Util.isNormalizedDate(filtri.getUltimaDataVerifica())) {
			result.add(
					getValueGenericError(CodeErrorEnum.ERR02.getCode(), ErrorParamEnum.ULTIMA_DATA_VERIFICA.getCode()));
		}
		if (Util.isValorizzato(filtri.getDecessoResidenza())
				&& !Util.isNormalizedString(filtri.getDecessoResidenza())) {
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), ErrorParamEnum.DECESSO_RESIDENZA.getCode()));
		}

		return result;
	}
}
