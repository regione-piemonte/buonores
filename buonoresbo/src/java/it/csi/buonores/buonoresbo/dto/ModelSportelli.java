/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.util.List;

public class ModelSportelli {
	List<ModelSportello> sportelli;
	ModelSportello sportelloCorrente;

	public List<ModelSportello> getSportelli() {
		return sportelli;
	}

	public void setSportelli(List<ModelSportello> sportelli) {
		this.sportelli = sportelli;
	}

	public ModelSportello getSportelloCorrente() {
		return sportelloCorrente;
	}

	public void setSportelloCorrente(ModelSportello sportelloCorrente) {
		this.sportelloCorrente = sportelloCorrente;
	}

	@Override
	public String toString() {
		return "ModelSportelli [sportelli=" + sportelli + ", sportelloCorrente=" + sportelloCorrente + "]";
	}
}
