package attrqa.framework.viewmodels.response.pii;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * view model to map json response for GET BatchAPIResponse
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchAPIResponse {

  Origin origin;
  Integer itemsParsed;
  Double parsedQuality;
  Integer itemsNormalized;
  Double piiQuality;
  Integer invalidEMailAddresses;
  Integer itemsUploaded;
  Integer invalids;
  Integer invalidCSVs;
  InvalidFields invalidFields;
  Integer invalidDates;
  FacebookPIIPayload facebookPIIPayload;
  FieldwiseQuality fieldwiseQuality;


  public Origin getOrigin() {
    return origin;
  }

  public Integer getItemsParsed() {
    return itemsParsed;
  }

  public Double getParsedQuality() {
    return parsedQuality;
  }

  public Integer getItemsNormalized() {
    return itemsNormalized;
  }

  public Double getPiiQuality() {
    return piiQuality;
  }

  public Integer getInvalidEMailAddresses() {
    return invalidEMailAddresses;
  }

  public Integer getItemsUploaded() {
    return itemsUploaded;
  }

  public Integer getInvalids() {
    return invalids;
  }

  public Integer getInvalidCSVs() {
    return invalidCSVs;
  }

  public InvalidFields getInvalidFields() {
    return invalidFields;
  }

  public Integer getInvalidDates() {
    return invalidDates;
  }

  public FacebookPIIPayload getFacebookPIIPayload() {
    return facebookPIIPayload;
  }

  public FieldwiseQuality getFieldwiseQuality() {
    return fieldwiseQuality;
  }
}
