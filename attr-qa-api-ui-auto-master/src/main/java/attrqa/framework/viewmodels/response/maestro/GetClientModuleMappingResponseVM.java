package attrqa.framework.viewmodels.response.maestro;

/**
 * view model to map json response for POST CreateModuleMapping
 *
 *
 * @author  Deepika Rajkumar
 * @version 1.0
 * @since   2019-05-22
 */
public class GetClientModuleMappingResponseVM {private String mieClientId;
    private String refreshCadence;
    private String lookbackWindowDays;
    private String refreshDelayDays;
    private String fiscalWeekStart;
    private String refreshStartTime;
    private String createdByUserId;
    private String createdAtTime;
    private String updatedByUserId;
    private String updatedAtTime;
    private String version;

    public String getMieClientId() {
        return mieClientId;
    }

    public void setMieClientId(String mieClientId) {
        this.mieClientId = mieClientId;
    }

    public String getRefreshCadence() {
        return refreshCadence;
    }

    public void setRefreshCadence(String refreshCadence) {
        this.refreshCadence = refreshCadence;
    }

    public String getLookbackWindowDays() {
        return lookbackWindowDays;
    }

    public void setLookbackWindowDays(String lookbackWindowDays) {
        this.lookbackWindowDays = lookbackWindowDays;
    }

    public String getRefreshDelayDays() {
        return refreshDelayDays;
    }

    public void setRefreshDelayDays(String refreshDelayDays) {
        this.refreshDelayDays = refreshDelayDays;
    }

    public String getFiscalWeekStart() {
        return fiscalWeekStart;
    }

    public void setFiscalWeekStart(String fiscalWeekStart) {
        this.fiscalWeekStart = fiscalWeekStart;
    }

    public String getRefreshStartTime() {
        return refreshStartTime;
    }

    public void setRefreshStartTime(String refreshStartTime) {
        this.refreshStartTime = refreshStartTime;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getCreatedAtTime() {
        return createdAtTime;
    }

    public void setCreatedAtTime(String createdAtTime) {
        this.createdAtTime = createdAtTime;
    }

    public String getUpdatedByUserId() {
        return updatedByUserId;
    }

    public void setUpdatedByUserId(String updatedByUserId) {
        this.updatedByUserId = updatedByUserId;
    }

    public String getUpdatedAtTime() {
        return updatedAtTime;
    }

    public void setUpdatedAtTime(String updatedAtTime) {
        this.updatedAtTime = updatedAtTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
