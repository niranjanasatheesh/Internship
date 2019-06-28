package attrqa.framework.viewmodels.response.maestro;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * view model to map json response for POST CreateDSConfig
 *
 *
 * @author  Deepika Rajkumar
 * @version 1.0
 * @since   2019-05-22
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateDSConfigSiteMediaVM {
    private boolean blacklist;
    private List<String> siteIds = null;

    public boolean isBlacklist() {
        return blacklist;
    }

    public void setBlacklist(boolean blacklist) {
        this.blacklist = blacklist;
    }

    public List<String> getSiteIds() {
        return siteIds;
    }

    public void setSiteIds(List<String> siteIds) {
        this.siteIds = siteIds;
    }
}
