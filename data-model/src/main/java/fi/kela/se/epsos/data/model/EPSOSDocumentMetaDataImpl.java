package fi.kela.se.epsos.data.model;

import java.util.Date;

public class EPSOSDocumentMetaDataImpl implements EPSOSDocumentMetaData {
	private String id;
	private String patientId;
	private int documentFormat;
	private Date effectiveDate;
	private String classCode;
	private String repositoryId;
	private String title;
	private String author;

	public EPSOSDocumentMetaDataImpl(String id, String patientId, int documentFormat, Date effectiveDate, String classCode, String repositoryId, String title, String author) {
		this.id = id;
		this.patientId = patientId;
		this.documentFormat = documentFormat;
		this.effectiveDate = effectiveDate;
		this.classCode = classCode;
		this.repositoryId = repositoryId;
		this.title = title;
		this.author = author;
	}

	public EPSOSDocumentMetaDataImpl(EPSOSDocumentMetaData metaData) {
		this.id = metaData.getId();
		this.patientId = metaData.getPatientId();
		this.documentFormat = metaData.getFormat();
		this.effectiveDate = metaData.getEffectiveTime();
		this.classCode = metaData.getClassCode();
		this.repositoryId = metaData.getRepositoryId();
		this.title = metaData.getTitle();
		this.author = metaData.getAuthor();
	}
	
	public String getId() {
		return id;
	}

	public String getPatientId() {
		return patientId;
	}

	public int getFormat() {
		return documentFormat;
	}

	public Date getEffectiveTime() {
		return effectiveDate;
	}

	public String getClassCode() {
		return classCode;
	}

	public String getRepositoryId() {
		return repositoryId;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	@Override
	public String toString() {
		return "EPSOSDocumentMetaDataImpl [id=" + id + ", patientId="
				+ patientId + ", documentFormat=" + documentFormat
				+ ", effectiveDate=" + effectiveDate + ", classCode="
				+ classCode + ", repositoryId=" + repositoryId + ", title="
				+ title + ", author=" + author + "]";
	}
}
