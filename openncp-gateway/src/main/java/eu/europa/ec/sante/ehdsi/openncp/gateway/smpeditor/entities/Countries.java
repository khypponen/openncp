package eu.europa.ec.sante.ehdsi.openncp.gateway.smpeditor.entities;

/**
 *
 * @author Inês Garganta
 */

public enum Countries {

  at("Austria"),
  be("Belgium"),
  hr("Croatia"),
  cy("Cyprus"),
  cz("Czech Republic"),
  de("Germany"),
  ee("Estonia"),
  fi("Finland"),
  fr("France"),
  gr("Greece"),
  hu("Hungary"),
  ie("Ireland"),
  it("Italy"),
  lu("Luxembourg"),
  mt("Malta"),
  pl("Poland"),
  pt("Portugal"),
  se("Sweden"),
  ch("Switzerland"),
  eu("European Union");

  
  public static final Countries[] ALL = {at, be, hr, cy, cz, de, ee, fi, fr, gr, hu, ie, it, lu, mt, pl, pt, se, ch, eu};
  
  private final String description;

  private Countries(final String description) {
    this.description = description;
  }

  public String getDescription() {
    return this.description;
  }
  
  public static Countries[] getALL() {
    return ALL;
  }

  @Override
  public String toString() {
    return getDescription();
  }
}
