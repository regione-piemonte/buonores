/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonores.buonoresbo.business.be.GraduatoriaApi;
import it.csi.buonores.buonoresbo.business.be.service.GraduatoriaService;
import it.csi.buonores.buonoresbo.dto.ModelNuovaGraduatoria;

@Component
public class GraduatoriaServiceImpl implements GraduatoriaApi {
    @Autowired
    GraduatoriaService graduatoriaService;

    @Override
    public Response getUltimoSportelloChiuso(SecurityContext securityContext, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest) {
        return graduatoriaService.getUltimoSportelloChiuso(securityContext, httpHeaders, httpRequest);
    }

    @Override
    public Response getSportelliChiusi(SecurityContext securityContext, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest) {
        return graduatoriaService.getSportelliChiusi(securityContext, httpHeaders, httpRequest);
    }

    @Override
    public Response creaNuovaGraduatoria(ModelNuovaGraduatoria numeroRichiesta, SecurityContext securityContext,
            HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        return graduatoriaService.creaNuovaGraduatoria(numeroRichiesta, securityContext, httpHeaders, httpRequest);
    }

    @Override
    public Response getGraduatoriaOrdinamento(SecurityContext securityContext, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest) {
        return graduatoriaService.getGraduatoriaOrdinamento(securityContext, httpHeaders, httpRequest);
    }

    @Override
    public Response getDomandeGraduatoria(SecurityContext securityContext, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest, String sportelloCod) {
        return graduatoriaService.getDomandeGraduatoria(securityContext, httpHeaders, httpRequest, sportelloCod);
    }

    @Override
    public Response getParametriFinanziamento(SecurityContext securityContext, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest, String sportelloCod) {
        return graduatoriaService.getParametriFinanziamento(securityContext, httpHeaders, httpRequest, sportelloCod);
    }

    @Override
    public Response selectGraduatoriaDesc(SecurityContext securityContext, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest, String sportelloCod) {
        return graduatoriaService.selectGraduatoriaDesc(securityContext, httpHeaders, httpRequest, sportelloCod);
    }

    @Override
    public Response checkPubblicazioneGraduatoria(SecurityContext securityContext, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest, String sportelloCod) {
        return graduatoriaService.checkPubblicazioneGraduatoria(securityContext, httpHeaders, httpRequest,
                sportelloCod);
    }

    @Override
    public Response pubblicaGraduatoria(SecurityContext securityContext, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest, String sportelloCod) {
        return graduatoriaService.pubblicaGraduatoria(sportelloCod, securityContext, httpHeaders, httpRequest);
    }

    @Override
    public Response aggiornaGraduatoria(SecurityContext securityContext, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest, String sportelloCod) {
        return graduatoriaService.aggiornaGraduatoria(sportelloCod, securityContext, httpHeaders, httpRequest);
    }

    @Override
    public Response checkEsistenzaGraduatoria(SecurityContext securityContext, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest, String sportelloCod) {
        return graduatoriaService.checkEsistenzaGraduatoria(sportelloCod, securityContext, httpHeaders, httpRequest);
    }

    @Override
    public Response checkStatoGraduatoria(SecurityContext securityContext, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest, String sportelloCod, String stato) {
        return graduatoriaService.checkStatoGraduatoria(sportelloCod, stato, securityContext, httpHeaders, httpRequest);
    }

    @Override
    public Response simulaGraduatoria(ModelNuovaGraduatoria numeroRichiesta, SecurityContext securityContext,
            HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        return graduatoriaService.simulaGraduatoria(numeroRichiesta, securityContext, httpHeaders, httpRequest);
    }

    @Override
    public Response getAree(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        return graduatoriaService.getAree(securityContext, httpHeaders, httpRequest);
    }
}