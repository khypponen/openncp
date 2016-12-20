package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities;

public enum Countries {

  PT("Portugal"),
  IT("Italy"),
  GC("Greece");

  public static final Countries[] ALL = {PT, IT, GC};
  
  private final String description;

  private Countries(final String description) {
    this.description = description;
  }

  public String getDescription() {
    return this.description;
  }

  @Override
  public String toString() {
    return getDescription();
  }
}
