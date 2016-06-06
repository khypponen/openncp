package epsos.ccd.posam.tm.util;

public class ModelValidatorResult {
	private boolean schemaValid;
	private boolean modelValid;
	private boolean resultValid;
	private boolean validationError;

	public boolean isSchemaValid() {
		return schemaValid;
	}

	public void setSchemaValid(boolean schemaValid) {
		this.schemaValid = schemaValid;
	}

	public boolean isModelValid() {
		return modelValid;
	}

	public void setModelValid(boolean modelValid) {
		this.modelValid = modelValid;
	}

	public boolean isResultValid() {
		return resultValid;
	}

	public void setResultValid(boolean resultValid) {
		this.resultValid = resultValid;
	}

	public void setValidationError(boolean validationError) {
		this.validationError = validationError;
	}

	public boolean isValidationError() {
		return validationError;
	}

	@Override
	public String toString() {
		return "ModelValidatorResult [schemaValid=" + schemaValid
				+ ", modelValid=" + modelValid + ", resultValid=" + resultValid
				+ ", validationError=" + validationError + "]";
	}
	
	

}
