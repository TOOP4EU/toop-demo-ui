/**
 * Copyright (C) 2018-2019 toop.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.demoui.bean;

import java.io.Serializable;

import com.helger.commons.collection.impl.ICommonsList;

import eu.toop.commons.exchange.AsicReadEntry;

public class DocumentDataBean implements Serializable {

    private String documentName;
    private String documentDescription;
    private String documentIssueDate;
    private String legalReference;
    private String documentURI;
    private String documentIdentifier;
    private String documentIssuePlace;
    private String documentMIMEType;
    private ICommonsList<AsicReadEntry> attachments = null;

    public DocumentDataBean(final ICommonsList<AsicReadEntry> attachments) {
        this.attachments = attachments;
    }

    public DocumentDataBean() {

    }

    public String getDocumentMIMEType() {
        return documentMIMEType;
    }

    public void setDocumentMIMEType(String documentMIMEType) {
        this.documentMIMEType = documentMIMEType;
    }

    public String getDocumentIssueDate() {
        return documentIssueDate;
    }

    public void setDocumentIssueDate(String documentIssueDate) {
        this.documentIssueDate = documentIssueDate;
    }

    public String getLegalReference() {
        return legalReference;
    }

    public void setLegalReference(String legalReference) {
        this.legalReference = legalReference;
    }

    public String getDocumentURI() {
        return documentURI;
    }

    public void setDocumentURI(String documentURI) {
        this.documentURI = documentURI;
    }

    public String getDocumentIdentifier() {
        return documentIdentifier;
    }

    public void setDocumentIdentifier(String documentIdentifier) {
        this.documentIdentifier = documentIdentifier;
    }

    public String getDocumentIssuePlace() {
        return documentIssuePlace;
    }

    public void setDocumentIssuePlace(String documentIssuePlace) {
        this.documentIssuePlace = documentIssuePlace;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    public ICommonsList<AsicReadEntry> getAttachments() {
        return attachments;
    }

    public void setAttachments(ICommonsList<AsicReadEntry> attachments) {
        this.attachments = attachments;
    }
}
