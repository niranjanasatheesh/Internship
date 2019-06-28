package attrqa.framework.viewmodels.response.maestro;


import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * view model to map json response for POST CreateDSConfig
 *
 *
 * @author  Deepika Rajkumar
 * @version 1.0
 * @since   2019-05-22
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateDSConfigResponseVM {

    private String createdUser;
    private String createdAt;
    private String updatedUser;
    private String updatedAt;
    private CreateDSConfigCampaignMediaVM campaignMedia;
    private CreateDSConfigSiteMediaVM siteMedia;


    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public CreateDSConfigCampaignMediaVM getCampaignMedia() {
        return campaignMedia;
    }

    public void setCampaignMedia(CreateDSConfigCampaignMediaVM campaignMedia) {
        this.campaignMedia = campaignMedia;
    }

    public CreateDSConfigSiteMediaVM getSiteMedia() {
        return siteMedia;
    }

    public void setSiteMedia(CreateDSConfigSiteMediaVM siteMedia) {
        this.siteMedia = siteMedia;
    }
}
