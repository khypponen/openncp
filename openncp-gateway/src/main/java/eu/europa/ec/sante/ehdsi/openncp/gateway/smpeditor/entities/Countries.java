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
  dk("Denmark"),
  ee("Estonia"),
  fi("Finland"),
  fr("France"),
  de("Germany"),
  gr("Greece"),
  hu("Hungary"),
  ie("Ireland"),
  it("Italy"),
  lu("Luxembourg"),
  mt("Malta"),
  pl("Poland"),
  pt("Portugal"),
  es("Spain"),
  sk("Slovakia"),
  si("Slovenia"),
  se("Sweden"),
  ch("Switzerland"),
  tr("Turkey"),
  eu("European Union");


  public static final Countries[] ALL = {at, be, hr, cy, cz, dk, ee, fi, fr, de, gr, hu, ie,
    it, lu, mt, pl, pt, es, sk, si, se, ch, tr, eu};
  
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
