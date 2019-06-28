package attrqa.framework.viewmodels.response.maestro;

import java.util.List;

/**
 * view model to map json response for POST CreateClientV1
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class Brands {

  String brandName;
  List<String> subBrands;

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public List<String> getSubBrands() {
    return subBrands;
  }

  public void setSubBrands(List<String> subBrands) {
    this.subBrands = subBrands;
  }
}
