package attrqa.framework.viewmodels.response.pii;

/**
 * view model to map json response for GET BatchAPIResponse
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class Origin {

  String bucket;
  String key;
  String region;

  public String getBucket() {
    return bucket;
  }

  public String getKey() {
    return key;
  }

  public String getRegion() {
    return region;
  }
}
