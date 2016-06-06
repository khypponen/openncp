/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2013 SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact email: epsos@iuz.pt
 */
package eu.epsos.protocolterminators.integrationtest.ihe.cda.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Marcelo Fonseca <marcelo.fonseca@iuz.pt>
 */
@XmlRootElement
public class DetailedResult {

    /* PARAMETERS */
    private DocumentValidXsd documentValidXsd;
    private DocumentWellFormed documentWellFormed;
    private MdaValidation mdaValidation;
    private ValidationResultsOverview valResultsOverview;
    private TemplateDesc templateDesc;

    /* GETTERS AND SETTERS */
    /**
     * @return the documentValidXsd
     */
    @XmlElement(name = "DocumentValidXSD")
    public DocumentValidXsd getDocumentValidXsd() {
        return documentValidXsd;
    }

    /**
     * @param documentValidXsd the documentValidXsd to set
     */
    public void setDocumentValidXsd(DocumentValidXsd documentValidXsd) {
        this.documentValidXsd = documentValidXsd;
    }

    /**
     * @return the documentWellFormed
     */
    @XmlElement(name = "DocumentWellFormed")
    public DocumentWellFormed getDocumentWellFormed() {
        return documentWellFormed;
    }

    /**
     * @param documentWellFormed the documentWellFormed to set
     */
    public void setDocumentWellFormed(DocumentWellFormed documentWellFormed) {
        this.documentWellFormed = documentWellFormed;
    }

    /**
     * @return the mdaValidation
     */
    @XmlElement(name = "MDAValidation")
    public MdaValidation getMdaValidation() {
        return mdaValidation;
    }

    /**
     * @param mdaValidation the mdaValidation to set
     */
    public void setMdaValidation(MdaValidation mdaValidation) {
        this.mdaValidation = mdaValidation;
    }

    /**
     * @return the valResultsOverview
     */
    @XmlElement(name = "ValidationResultsOverview")
    public ValidationResultsOverview getValResultsOverview() {
        return valResultsOverview;
    }

    /**
     * @param valResultsOverview the valResultsOverview to set
     */
    public void setValResultsOverview(ValidationResultsOverview valResultsOverview) {
        this.valResultsOverview = valResultsOverview;
    }

    /**
     * @return the templateDesc
     */
    @XmlElement(name = "TemplateDesc")
    public TemplateDesc getTemplateDesc() {
        return templateDesc;
    }

    /**
     * @param templateDesc the templateDesc to set
     */
    public void setTemplateDesc(TemplateDesc templateDesc) {
        this.templateDesc = templateDesc;
    }
}
