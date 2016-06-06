package ec.europa.eu.tsamsync2.services;

import ec.europa.eu.tsamsync2.MVC.MVCReader;
import ec.europa.eu.tsamsync2.search.CodeSystem;
import ec.europa.eu.tsamsync2.search.CodeSystemEntity;
import ec.europa.eu.tsamsync2.search.CodeSystemVersion;
import ec.europa.eu.tsamsync2.search.ListCodeSystemConceptsRequestType;
import ec.europa.eu.tsamsync2.search.ListCodeSystemConceptsResponse;
import ec.europa.eu.tsamsync2.search.ListCodeSystemsRequestType;
import ec.europa.eu.tsamsync2.search.ListCodeSystemsResponse;
import ec.europa.eu.tsamsync2.search.ListValueSetContentsRequestType;
import ec.europa.eu.tsamsync2.search.ListValueSetContentsResponse;
import ec.europa.eu.tsamsync2.search.ListValueSetsRequestType;
import ec.europa.eu.tsamsync2.search.ListValueSetsResponse;
import ec.europa.eu.tsamsync2.search.PagingType;
import ec.europa.eu.tsamsync2.search.Search;
import ec.europa.eu.tsamsync2.search.Search_Service;
import ec.europa.eu.tsamsync2.search.ValueSet;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;

public class SearchServices {

    Search_Service service;
    private Search port;
    private static SearchServices instance;

    public SearchServices() {
        try {
            service = new Search_Service(new URL(MVCReader.url + "Search?wsdl"), new QName("http://search.ws.terminologie.fhdo.de/", "Search"));
            port = service.getSearchPort();
        } catch (MalformedURLException ex) {
            Logger.getLogger(SearchServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static SearchServices get() {
        if (instance == null)
            instance = new SearchServices();
        return instance;

    }

    public List<CodeSystem> listCodeSystems() {
        try {
            // invoke method
            ListCodeSystemsResponse.Return response = port.listCodeSystems(new ListCodeSystemsRequestType());

            // handle response
            Utils.responseMessage(response.getReturnInfos());
            return response.getCodeSystem();
        } catch (Exception ex) {
              Logger.getLogger(SearchServices.class.getName()).log(Level.SEVERE, null, ex);
      }
        return null;
    }
    
    public CodeSystem findCodeSystem(String name){
        for(CodeSystem l : listCodeSystems())
            if(l.getName().equalsIgnoreCase(name))
                return l;
        return null;
    }

    public List<ValueSet> listValueSets() {
        try {
            ListValueSetsResponse.Return response = port.listValueSets(new ListValueSetsRequestType());

            Utils.responseMessage(response.getReturnInfos());
            return response.getValueSet();
        } catch (Exception ex) {
                Logger.getLogger(SearchServices.class.getName()).log(Level.SEVERE, null, ex);
    }
        return null;
    }

    public List<CodeSystemEntity> listValueSetContents(String loginToken, ValueSet value_set) {
        try {

            ListValueSetContentsRequestType request = new ListValueSetContentsRequestType();
            request.setLoginToken(loginToken);
            request.setValueSet(value_set);
            ListValueSetContentsResponse.Return response = port.listValueSetContents(request);

            Utils.responseMessage(response.getReturnInfos());
            return response.getCodeSystemEntity();
        } catch (Exception ex) {
                  Logger.getLogger(SearchServices.class.getName()).log(Level.SEVERE, null, ex);
  }
        return null;
    }

    public List<CodeSystemEntity> listCodeSystemConcepts(String loginToken, CodeSystem code_system) {
        PagingType pageType = new PagingType();
        pageType.setPageSize("10000");
        pageType.setPageIndex(0);
        List<CodeSystemEntity> list = new ArrayList<>();
        ListCodeSystemConceptsResponse.Return page;
//        boolean exitCondition = false;
        List<CodeSystemVersion> versions = code_system.getCodeSystemVersions();
        for (CodeSystemVersion v : versions) {
            code_system.getCodeSystemVersions().clear();
            code_system.getCodeSystemVersions().add(v);
            do {
                page = listCodeSystemConceptsPage(loginToken, code_system, pageType);
                list.addAll(page.getCodeSystemEntity());
                pageType.setPageIndex(pageType.getPageIndex() + 1);
            } while (page.getPagingInfos() != null && page.getPagingInfos().getCount() != list.size());
        }
        return list;
    }

    private ListCodeSystemConceptsResponse.Return listCodeSystemConceptsPage(String loginToken, CodeSystem code_system, PagingType page) {

        try {
            // define parameter
            ListCodeSystemConceptsRequestType request = new ListCodeSystemConceptsRequestType();
            request.setLoginToken(loginToken);
            request.setCodeSystem(code_system);
            request.setPagingParameter(page);
            // invoke method
            ListCodeSystemConceptsResponse.Return response = port.listCodeSystemConcepts(request);

            // handle response
            Utils.responseMessage(response.getReturnInfos());
            return response;
        } catch (Exception ex) {
            Logger.getLogger(SearchServices.class.getName()).log(Level.SEVERE, "", ex);
        }
        return null;
    }

}
