package com.dapp.docuchain.model;

public enum DocumentHolderType {
    EXPIRY_TYPE("ET"), CUSTOM_TYPE("CT");

    private String documentHolderType;

    DocumentHolderType(String documentType) {
        this.documentHolderType = documentType;
    }

    public String getDocumentHolderType() {
        return documentHolderType;
    }

    public void setDocumentHolderType(String documentHolderType) {
        this.documentHolderType = documentHolderType;
    }


}
