/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresrend.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresrend.dto.ModelBuono;
import it.csi.buonores.buonoresrend.dto.custom.ModelVerificaIsee;
import it.csi.buonores.buonoresrend.util.Constants;
import it.csi.buonores.buonoresrend.util.Util;

public class ListaBuoniMapper implements RowMapper<ModelBuono> {

	@Override
	public ModelBuono mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		ModelBuono result = new ModelBuono();
		result.setBuonoId(resultSet.getInt("buono_id"));
		result.setBuonoCod(resultSet.getString("buono_cod"));
		result.setBuonoStatoCod(resultSet.getString("buono_stato_cod"));
		result.setSportelloId(resultSet.getInt("sportello_id"));
		result.setDomandaNumero(resultSet.getString("domanda_numero"));
		result.setBeneficiarioCf(resultSet.getString("beneficiario_cf"));
		result.setDataFineValiditaBuono(Util.getDataWithoutTime(resultSet.getString("data_fine_validita"), Constants.DATE_FORMAT));
		result.setDataInizioRendicontazione(Util.getDataWithoutTime(resultSet.getString("data_rendicontazione"), Constants.DATE_FORMAT));
		result.setRsaCode(resultSet.getString("rsa_cod"));
		result.setRsaCfPiva(resultSet.getString("rsa_cfpiva"));
		result.setContrattoDataInizio(Util.getDataWithoutTime(resultSet.getString("contratto_data_inizio"), Constants.DATE_FORMAT));
		result.setContrattoDataFine(Util.getDataWithoutTime(resultSet.getString("contratto_data_fine"), Constants.DATE_FORMAT));
		result.setDataInvioContrattoABandi(Util.getDataWithoutTime(resultSet.getString("data_invio_a_bandi"), Constants.DATE_FORMAT));
		
		// 2025:CONFORME,2024:CONFORME,2023:CONFORME gestione aggregato verifiche isee
		List<ModelVerificaIsee> verificaIseeCorrente = new ArrayList<ModelVerificaIsee>();
		String verificaIsee = resultSet.getString("verifica_isee_corrente");
		String isee[] = verificaIsee.split(",");
		for(String dato : isee) {
			String stato[] = dato.split(":");
			ModelVerificaIsee modelIsee = new ModelVerificaIsee();
			modelIsee.setAnno(stato[0]);
			modelIsee.setStato(stato[1]);
			
			verificaIseeCorrente.add(modelIsee);
		}
		
		result.setVerificaIseeCorrente(verificaIseeCorrente);
		
		return result;
	}
}
