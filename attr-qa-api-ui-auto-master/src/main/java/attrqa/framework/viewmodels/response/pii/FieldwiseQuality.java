package attrqa.framework.viewmodels.response.pii;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * view model to map json response for GET BatchAPIResponse
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class FieldwiseQuality {

  @JsonProperty("EMAIL")
  Double eMAIL;

  @JsonProperty("DATE_OF_BIRTH")
  Double dATE_OF_BIRTH;

  @JsonProperty("LAST_NAME")
  Double lAST_NAME;

  @JsonProperty("FIRST_NAME")
  Double fIRST_NAME;

  @JsonProperty("US_STATE")
  Double uS_STATE;

  @JsonProperty("CITY")
  Double cITY;

  @JsonProperty("ZIP")
  Double zIP;

  @JsonProperty("COUNTRY_CODE")
  Double cOUNTRY_CODE;

  @JsonProperty("MOBILE_DEVICE_ID")
  Double mOBILE_DEVICE_ID;

  @JsonProperty("USER_ID")
  Double uSER_ID;


  @JsonProperty("EMAIL")
  public Double geteMAIL() {
    return eMAIL;
  }

  @JsonProperty("DATE_OF_BIRTH")
  public Double getdATE_OF_BIRTH() {
    return dATE_OF_BIRTH;
  }

  @JsonProperty("LAST_NAME")
  public Double getlAST_NAME() {
    return lAST_NAME;
  }

  @JsonProperty("FIRST_NAME")
  public Double getfIRST_NAME() {
    return fIRST_NAME;
  }

  @JsonProperty("US_STATE")
  public Double getuS_STATE() {
    return uS_STATE;
  }

  @JsonProperty("CITY")
  public Double getcITY() {
    return cITY;
  }

  @JsonProperty("ZIP")
  public Double getzIP() {
    return zIP;
  }

  @JsonProperty("COUNTRY_CODE")
  public Double getcOUNTRY_CODE() {
    return cOUNTRY_CODE;
  }

  @JsonProperty("MOBILE_DEVICE_ID")
  public Double getmOBILE_DEVICE_ID() {
    return mOBILE_DEVICE_ID;
  }

  @JsonProperty("USER_ID")
  public Double getuSER_ID() {
    return uSER_ID;
  }
}
