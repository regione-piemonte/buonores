/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoresbo.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbo.dto.Errore;
import it.csi.buonores.buonoresbo.dto.ModelAllegato;
import it.csi.buonores.buonoresbo.dto.ModelArea;
import it.csi.buonores.buonoresbo.dto.ModelDomandeAmmissibili;
import it.csi.buonores.buonoresbo.dto.ModelDomandeGraduatoria;
import it.csi.buonores.buonoresbo.dto.ModelGraduatoriaOrdinamento;
import it.csi.buonores.buonoresbo.dto.ModelNuovaGraduatoria;
import it.csi.buonores.buonoresbo.dto.ModelParametriFinanziamento;
import it.csi.buonores.buonoresbo.dto.ModelPersona;
import it.csi.buonores.buonoresbo.dto.ModelRichiesta;
import it.csi.buonores.buonoresbo.dto.ModelSportello;
import it.csi.buonores.buonoresbo.dto.ModelUpdateCronologia;
import it.csi.buonores.buonoresbo.dto.UserInfo;
import it.csi.buonores.buonoresbo.dto.custom.ModelGetAllegatoExt;
import it.csi.buonores.buonoresbo.exception.DatabaseException;
import it.csi.buonores.buonoresbo.filter.IrideIdAdapterFilter;
import it.csi.buonores.buonoresbo.integration.dao.custom.AllegatoDao;
import it.csi.buonores.buonoresbo.integration.dao.custom.GraduatoriaDao;
import it.csi.buonores.buonoresbo.integration.dao.custom.RichiesteDao;
import it.csi.buonores.buonoresbo.util.Constants;
import it.csi.buonores.buonoresbo.util.validator.impl.ValidateGenericImpl;

@Service
public class GraduatoriaService extends BaseService {
    @Autowired
    GraduatoriaDao graduatoriaDao;

    @Autowired
    ValidateGenericImpl validateGeneric;

    @Autowired
    RichiesteDao richiesteDao;

    @Autowired
    GetRichiesteService getRichiesteService;

    @Autowired
    AllegatoDao allegatoDao;

    public Response getUltimoSportelloChiuso(SecurityContext securityContext, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest) {
        Errore error = null;
        String metodo = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            ModelSportello sportello = graduatoriaDao.getUltimoSportelloChiuso();

            return Response.ok().entity(sportello).build();
        } catch (DatabaseException e) {
            error = handleDatabaseException(metodo, e);
        } catch (Exception e) {
            error = handleException(metodo, e);
        }
        return error.generateResponseError();
    }

    public Response getSportelliChiusi(SecurityContext securityContext, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest) {
        Errore error = null;
        String metodo = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            List<ModelSportello> sportelli = graduatoriaDao.getSportelliChiusi();

            return Response.ok().entity(sportelli).build();
        } catch (DatabaseException e) {
            error = handleDatabaseException(metodo, e);
        } catch (Exception e) {
            error = handleException(metodo, e);
        }
        return error.generateResponseError();
    }

    public Response getGraduatoriaOrdinamento(SecurityContext securityContext, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest) {
        Errore error = new Errore();
        String metodo = new Object() {
        }.getClass().getEnclosingMethod().getName();

        try {
            List<ModelGraduatoriaOrdinamento> ordinamenti = new ArrayList<ModelGraduatoriaOrdinamento>();
            ordinamenti = graduatoriaDao.getGraduatoriaOrdinamento();

            return Response.ok().entity(ordinamenti).build();
        } catch (DatabaseException e) {
            error = handleDatabaseException(metodo, e);
        } catch (Exception e) {
            error = handleException(metodo, e);
        }
        return error.generateResponseError();
    }

    public Response creaNuovaGraduatoria(ModelNuovaGraduatoria popUpGraduatoria, SecurityContext securityContext,
            HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        Errore error = null;
        String metodo = new Object() {
        }.getClass().getEnclosingMethod().getName();
        UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);

        try {
            /*
             * E' possibile creare una nuova graduatoria nel caso in cui: - Non esista già
             * una graduatoria
             */
            if (graduatoriaDao.checkEsistenzaGraduatoria(popUpGraduatoria.getCodSportello())) {
                return Response.ok().entity(false).build();
            } else {
                long idGraduatoria = initGraduatoria(popUpGraduatoria.getCodSportello(),
                        popUpGraduatoria.getDescrizione(), userInfo.getCodFisc());
                List<ModelGraduatoriaOrdinamento> ordinamenti = insertGraduatoria1(idGraduatoria,
                        userInfo.getCodFisc());
                List<ModelArea> aree = insertGraduatoria2(popUpGraduatoria, idGraduatoria, userInfo.getCodFisc());
                cambiaStatoAmmissibili(popUpGraduatoria.getCodSportello(), userInfo.getCodFisc(), securityContext,
                        httpHeaders, httpRequest, ordinamenti);
                insertGraduatoria3(popUpGraduatoria.getCodSportello(), idGraduatoria, ordinamenti, aree,
                        userInfo.getCodFisc());

                List<ModelDomandeAmmissibili> domandeGraduatoria = getDomandeFinanziate(
                        popUpGraduatoria.getCodSportello(), userInfo.getCodFisc());
                graduatoriaDao.updateImportoDomandeGraduatoria(
                        popUpGraduatoria.getNumeroMesi() * popUpGraduatoria.getImportoMensile(),
                        domandeGraduatoria, idGraduatoria);
                logInfo("creaNuovaGraduatoria", "Graduatoria creata con successo");
                return Response.ok().entity(true).build();
            }
        } catch (DatabaseException e) {
            error = handleDatabaseException(metodo, e);
        } catch (Exception e) {
            error = handleException(metodo, e);
        }
        return error.generateResponseError();
    }

    private long initGraduatoria(String codSportello, String desc, String cf) throws DatabaseException {
        graduatoriaDao.deleteGraduatoria(codSportello);
        return graduatoriaDao.insertTGraduatoria(codSportello, desc, cf);
    }

    // Popola tabelle graduatoria_stato e graduatoria_ordinamento, restituisce gli
    // ordinamenti
    private List<ModelGraduatoriaOrdinamento> insertGraduatoria1(long idGraduatoria, String cf)
            throws DatabaseException {
        List<ModelGraduatoriaOrdinamento> ordinamenti = graduatoriaDao.getGraduatoriaOrdinamento();
        for (ModelGraduatoriaOrdinamento ordinamento : ordinamenti) {
            graduatoriaDao.insertRGraduatoriaOrdinamento(idGraduatoria, ordinamento.getOrdinamentoId(), cf);
        }
        graduatoriaDao.insertRGraduatoriaStato(idGraduatoria, Constants.PROVVISORIA, cf);

        return ordinamenti;
    }

    // Inizia a popolare la tabella graduatoria_finanziamento, restituisce le aree
    private List<ModelArea> insertGraduatoria2(ModelNuovaGraduatoria popUpGraduatoria, long idGraduatoria, String cf)
            throws DatabaseException {
        List<ModelArea> aree = graduatoriaDao.getAree();
        graduatoriaDao.insertTGraduatoriaFinanziamento(popUpGraduatoria, idGraduatoria, aree, cf);
        return aree;
    }

    // popola graduatoria_dettaglio e finisce di popolare graduatoria_finanziamento
    private void insertGraduatoria3(String sportelloCod, long idGraduatoria,
            List<ModelGraduatoriaOrdinamento> ordinamenti, List<ModelArea> aree, String cf) throws DatabaseException {
        // GET DOMANDE AMMESSA/AMMESSA_CON_RISERVA/IN PAGAMENTO/AMMESSE CON RISERVA IN
        // PAGAMENTO -----------------------------------------------------
        List<Long> domandeGraduatoria = graduatoriaDao.getDomandeAmmesseSportelli(sportelloCod, ordinamenti);
        graduatoriaDao.insertDomandeGraduatoria(sportelloCod, domandeGraduatoria, cf);
        graduatoriaDao.updateGraduatoriaFinanziamento(idGraduatoria, aree);
    }

    private void cambiaStatoAmmissibili(String codSportello, String cf, SecurityContext securityContext,
            HttpHeaders httpHeaders, HttpServletRequest httpRequest, List<ModelGraduatoriaOrdinamento> ordinamenti)
            throws DatabaseException {
        // GET DOMANDE AMMISSIBILI -----------------------------------------------------
        List<ModelDomandeAmmissibili> domandeAmmissibili = graduatoriaDao.getDomandeAmmissibili(codSportello);

        // CAMBIO STATO AMMISSIBILI --> AMMESSA/AMMESSA_CON_RISERVA
        for (ModelDomandeAmmissibili domandaAmmissibile : domandeAmmissibili) {
            // Ottengo il dettaglio della domanda per il cambio di stato
            ModelRichiesta rich = new ModelRichiesta();
            rich.setDomandaDetId(BigDecimal.valueOf(domandaAmmissibile.getDettId()));
            rich.setNumero(domandaAmmissibile.getNumeroDomanda());
            rich.setSportelloId(BigDecimal.valueOf(domandaAmmissibile.getSportelloId()));
            ModelPersona richiedente = new ModelPersona();
            richiedente.setCf(domandaAmmissibile.getRichiedenteCF());
            rich.setRichiedente(richiedente);
            // Ottengo gli allegati della domanda
            List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
            allegati = richiesteDao.selectAllegatiFromNumeroRichiesta(rich.getDomandaDetId());
            if (!allegati.isEmpty()) {
                rich.setAllegati(allegati);
            }
            if (domandaAmmissibile.isRiserva()) {
                getRichiesteService.cambioStatoDomanda(rich, Constants.AMMESSA_RISERVA,
                        cf, rich.getNumero(), null, null, securityContext, httpHeaders, httpRequest);
            } else {
                getRichiesteService.cambioStatoDomanda(rich, Constants.AMMESSA,
                        cf, rich.getNumero(), null, null, securityContext, httpHeaders, httpRequest);
            }
        }
    }

    public Response getDomandeGraduatoria(SecurityContext securityContext, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest, String sportelloCod) {
        Errore error = new Errore();
        String metodo = new Object() {
        }.getClass().getEnclosingMethod().getName();
        try {
            List<ModelDomandeGraduatoria> domandeInGraduatoria = graduatoriaDao.getDomandeGraduatoria(sportelloCod);
            return Response.ok().entity(domandeInGraduatoria).build();
        } catch (DatabaseException e) {
            handleDatabaseException(metodo, e);
        } catch (Exception e) {
            handleException(metodo, e);
        }

        return error.generateResponseError();
    }

    public Response getParametriFinanziamento(SecurityContext securityContext, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest, String sportelloCod) {
        Errore error = new Errore();
        String metodo = new Object() {
        }.getClass().getEnclosingMethod().getName();
        try {
            List<ModelParametriFinanziamento> parametriFinanziamento = graduatoriaDao
                    .getParametriFinanziamento(sportelloCod);
            return Response.ok().entity(parametriFinanziamento).build();
        } catch (DatabaseException e) {
            handleDatabaseException(metodo, e);
        } catch (Exception e) {
            handleException(metodo, e);
        }

        return error.generateResponseError();
    }

    public Response selectGraduatoriaDesc(SecurityContext securityContext, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest, String sportelloCod) {
        Errore error = new Errore();
        String metodo = new Object() {
        }.getClass().getEnclosingMethod().getName();
        try {
            return Response.ok().entity(graduatoriaDao.selectGraduatoriaDesc(sportelloCod)).build();
        } catch (DatabaseException e) {
            handleDatabaseException(metodo, e);
        } catch (Exception e) {
            handleException(metodo, e);
        }
        return error.generateResponseError();
    }

    public Response checkPubblicazioneGraduatoria(SecurityContext securityContext, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest, String sportelloCod) {
        Errore error = new Errore();
        String metodo = new Object() {
        }.getClass().getEnclosingMethod().getName();
        try {
            return Response.ok().entity(graduatoriaDao.checkPubblicazioneGraduatoria(sportelloCod)).build();
        } catch (DatabaseException e) {
            handleDatabaseException(metodo, e);
        } catch (Exception e) {
            handleException(metodo, e);
        }
        return error.generateResponseError();
    }

    // Pubblicazione GRADUATORIA
    public Response pubblicaGraduatoria(String sportelloCod, SecurityContext securityContext, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest) {
        Errore error = new Errore();
        String metodo = new Object() {
        }.getClass().getEnclosingMethod().getName();
        UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
        try {
            // Se la graduatoria non è in PROVVISORIA oppure già pubblicata
            if (!graduatoriaDao.checkStatoGraduatoria(sportelloCod, Constants.PROVVISORIA)) {
                if (!graduatoriaDao.checkPubblicazioneGraduatoria(sportelloCod)) {
                    return Response.ok().entity(false).build();
                }
            } else {
                long idGraduatoria = graduatoriaDao.selectGraduatoriaIdBySportelloCod(sportelloCod);
                cambiaStatoPubblicaGraduatoria(sportelloCod, userInfo.getCodFisc(), securityContext, httpHeaders,
                        httpRequest);
                graduatoriaDao.updateRGraduatoriaStato(idGraduatoria, Constants.PUBBLICATA, userInfo.getCodFisc());
                return Response.ok().entity(true).build();
            }
        } catch (DatabaseException e) {
            handleDatabaseException(metodo, e);
        } catch (Exception e) {
            handleException(metodo, e);
        }
        return error.generateResponseError();
    }

    private void cambiaStatoPubblicaGraduatoria(String codSportello, String cf, SecurityContext securityContext,
            HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws DatabaseException {

        List<ModelDomandeAmmissibili> domandeGraduatoria = getDomandeFinanziate(codSportello, cf);

        // CAMBIO STATO AMMISSIBILI --> AMMESSA/AMMESSA_CON_RISERVA
        for (ModelDomandeAmmissibili domanda : domandeGraduatoria) {
            // Ottengo il dettaglio della domanda per il cambio di stato
            ModelRichiesta rich = new ModelRichiesta();
            rich.setDomandaDetId(BigDecimal.valueOf(domanda.getDettId()));
            rich.setNumero(domanda.getNumeroDomanda());
            rich.setSportelloId(BigDecimal.valueOf(domanda.getSportelloId()));
            ModelPersona richiedente = new ModelPersona();
            richiedente.setCf(domanda.getRichiedenteCF());
            rich.setRichiedente(richiedente);
            // Ottengo gli allegati della domanda
            List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
            allegati = richiesteDao.selectAllegatiFromNumeroRichiesta(rich.getDomandaDetId());
            if (!allegati.isEmpty()) {
                rich.setAllegati(allegati);
            }
            if (domanda.isRiserva()) {
                cambioStatoDomandaDaPubblicare(rich, Constants.AMMESSA_RISERVA_IN_PAGAMENTO, cf, rich.getNumero());
            } else {
                cambioStatoDomandaDaPubblicare(rich, Constants.IN_PAGAMENTO, cf, rich.getNumero());
            }
        }
    }

    private List<ModelDomandeAmmissibili> getDomandeFinanziate(String codSportello, String cf)
            throws DatabaseException {

        List<ModelArea> aree = graduatoriaDao.getAree();

        // GET DOMANDE AMMISSIBILI -----------------------------------------------------
        List<ModelDomandeAmmissibili> domandeGraduatoria = new ArrayList<ModelDomandeAmmissibili>();
        List<ModelParametriFinanziamento> gFinanziamento = graduatoriaDao.getParametriFinanziamento(codSportello);
        List<ModelDomandeAmmissibili> temp = graduatoriaDao.getDomandePubblicaGraduatoria(codSportello,
                aree.get(0).getAreaId());
        domandeGraduatoria.addAll(temp.subList(0, gFinanziamento.get(1).getSoggettiFinanziati()));
        temp = graduatoriaDao.getDomandePubblicaGraduatoria(codSportello, aree.get(1).getAreaId());
        domandeGraduatoria.addAll(temp.subList(0, gFinanziamento.get(2).getSoggettiFinanziati()));
        temp = graduatoriaDao.getDomandePubblicaGraduatoria(codSportello, 0);
        domandeGraduatoria.addAll(temp.subList(0, gFinanziamento.get(0).getSoggettiFinanziati()));

        return domandeGraduatoria;
    }

    private ModelUpdateCronologia cambioStatoDomandaDaPubblicare(ModelRichiesta richiesta, String stato,
            String codFiscale, String numeroRichiesta) throws DatabaseException {

        ModelUpdateCronologia update = richiesteDao.insertTDettaglioDomandaCambioStato(richiesta, stato, codFiscale,
                null, null);

        richiesteDao.updateDataFineValDettaglioDomanda(richiesta.getDomandaDetId());
        long idDettaglio = update.getIdDettaglio();
        String detCod = update.getDetCod();

        if (!richiesta.getAllegati().isEmpty()) {
            List<ModelGetAllegatoExt> allegati = allegatoDao.selectAllegati(richiesta.getDomandaDetId());
            if (!allegati.isEmpty()) {
                for (ModelGetAllegatoExt a : allegati) {
                    if (!a.getAllegatoTipoCod().equalsIgnoreCase(Constants.DOMANDA)) {
                        allegatoDao.insertAllegatoConAllegatoTipoId(a.getFileName(), a.getFileType(), a.getFilePath(),
                                richiesta.getSportelloId(), BigDecimal.valueOf(idDettaglio), detCod, a.getAllegatoId(),
                                codFiscale, codFiscale);
                    }
                }
            }
        }
        return update;
    }

    // AGGIORNAMENTO GRADUATORIA
    public Response aggiornaGraduatoria(String sportelloCod, SecurityContext securityContext, HttpHeaders httpHeaders,
            HttpServletRequest httpRequest) {
        Errore error = new Errore();
        String metodo = new Object() {
        }.getClass().getEnclosingMethod().getName();
        UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
        try {
            // Se la graduatoria non è in AGGIORNAMENTO
            if (!graduatoriaDao.checkStatoGraduatoria(sportelloCod, Constants.DA_AGGIORNARE)) {
                return Response.ok().entity(false).build();
            } else {
                long idGraduatoria = graduatoriaDao.selectGraduatoriaIdBySportelloCod(sportelloCod);
                List<ModelGraduatoriaOrdinamento> ordinamenti = graduatoriaDao.getGraduatoriaOrdinamento();
                List<ModelArea> aree = graduatoriaDao.getAree();
                double importo = graduatoriaDao.selectImportoTotaleMensileByIdGraduatoria(idGraduatoria);
                cambiaStatoAmmissibili(sportelloCod, userInfo.getCodFisc(), securityContext, httpHeaders, httpRequest,
                        ordinamenti);
                graduatoriaDao.deleteGraduatoriaDettaglio(idGraduatoria);
                insertGraduatoria3(sportelloCod, idGraduatoria, ordinamenti, aree, userInfo.getCodFisc());
                graduatoriaDao.updateRGraduatoriaStato(idGraduatoria, Constants.PROVVISORIA, userInfo.getCodFisc());
                List<ModelDomandeAmmissibili> domandeGraduatoria = getDomandeFinanziate(sportelloCod,
                        userInfo.getCodFisc());
                graduatoriaDao.updateImportoDomandeGraduatoria(
                        importo, domandeGraduatoria, idGraduatoria);
                logInfo("aggiornaGraduatoria", "Graduatoria aggiornata con successo");
                return Response.ok().entity(true).build();
            }
        } catch (DatabaseException e) {
            handleDatabaseException(metodo, e);
        } catch (Exception e) {
            handleException(metodo, e);
        }
        return error.generateResponseError();
    }

    // CHECK
    public Response checkEsistenzaGraduatoria(String sportelloCod, SecurityContext securityContext,
            HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        Errore error = new Errore();
        String metodo = new Object() {
        }.getClass().getEnclosingMethod().getName();
        try {
            return Response.ok().entity(graduatoriaDao.checkEsistenzaGraduatoria(sportelloCod)).build();
        } catch (DatabaseException e) {
            handleDatabaseException(metodo, e);
        } catch (Exception e) {
            handleException(metodo, e);
        }
        Response esito = error.generateResponseError();
        return esito;
    }

    public Response checkStatoGraduatoria(String sportelloCod, String stato, SecurityContext securityContext,
            HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        Errore error = new Errore();
        String metodo = new Object() {
        }.getClass().getEnclosingMethod().getName();
        try {
            return Response.ok().entity(graduatoriaDao.checkStatoGraduatoria(sportelloCod, stato)).build();
        } catch (DatabaseException e) {
            handleDatabaseException(metodo, e);
        } catch (Exception e) {
            handleException(metodo, e);
        }
        return error.generateResponseError();
    }

    public Response simulaGraduatoria(ModelNuovaGraduatoria popUpGraduatoria, SecurityContext securityContext,
            HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        Errore error = new Errore();
        String metodo = new Object() {
        }.getClass().getEnclosingMethod().getName();
        UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
        try {
            if (graduatoriaDao.checkStatoGraduatoria(popUpGraduatoria.getCodSportello(), Constants.PROVVISORIA)) {
                long idGraduatoria = initGraduatoria(popUpGraduatoria.getCodSportello(),
                        popUpGraduatoria.getDescrizione(), userInfo.getCodFisc());
                List<ModelGraduatoriaOrdinamento> ordinamenti = insertGraduatoria1(idGraduatoria,
                        userInfo.getCodFisc());
                List<ModelArea> aree = insertGraduatoria2(popUpGraduatoria, idGraduatoria, userInfo.getCodFisc());
                cambiaStatoAmmissibili(popUpGraduatoria.getCodSportello(), userInfo.getCodFisc(), securityContext,
                        httpHeaders, httpRequest, ordinamenti);
                insertGraduatoria3(popUpGraduatoria.getCodSportello(), idGraduatoria, ordinamenti, aree,
                        userInfo.getCodFisc());

                List<ModelDomandeAmmissibili> domandeGraduatoria = getDomandeFinanziate(
                        popUpGraduatoria.getCodSportello(), userInfo.getCodFisc());
                graduatoriaDao.updateImportoDomandeGraduatoria(
                        popUpGraduatoria.getNumeroMesi() * popUpGraduatoria.getImportoMensile(),
                        domandeGraduatoria, idGraduatoria);
                logInfo("simulaGraduatoria", "Graduatoria simulata con successo");
                return Response.ok().entity(true).build();
            } else {
                logError("simulaGraduatoria", "Errore durante la simulazione della Graduatoria");
                return Response.ok().entity(false).build();
            }
        } catch (DatabaseException e) {
            handleDatabaseException(metodo, e);
        } catch (Exception e) {
            handleException(metodo, e);
        }
        return error.generateResponseError();
    }

    public Response getAree(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        Errore error = new Errore();
        String metodo = new Object() {
        }.getClass().getEnclosingMethod().getName();
        try {
            List<ModelArea> aree = graduatoriaDao.getAree();
            return Response.ok().entity(aree).build();
        } catch (DatabaseException e) {
            handleDatabaseException(metodo, e);
        } catch (Exception e) {
            handleException(metodo, e);
        }
        return error.generateResponseError();
    }
}