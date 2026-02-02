package com.dapp.docuchain.dto;

public class EmailDTO {

    private Integer fileId;
    private String emailIds;
    private String fileName;
    private String fileExtn;

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getEmailIds() {
        return emailIds;
    }

    public void setEmailIds(String emailIds) {
        this.emailIds = emailIds;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtn() {
        return fileExtn;
    }

    public void setFileExtn(String fileExtn) {
        this.fileExtn = fileExtn;
    }


}
