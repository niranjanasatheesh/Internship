package attrqa.framework.viewmodels.response.maestro;

import java.util.List;

/**
 * view model to map json response for GET ClientV1
 *
 *
 * @author  Deepika Rajkumar
 * @version 1.0
 * @since   2019-05-22
 */
public class GetClientV1ResponseVM {

    private String mieClientId;
    private List<String> cmiClientIds;
    private String clientCode;
    private String clientName;
    private String clientType;
    private String language;
    private String locale;
    private String currency;
    private String country;
    private String phone;
    private String email;
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

    public List<String> getCmiClientIds() {
        return cmiClientIds;
    }

    public void setCmiClientIds(List<String> cmiClientIds) {
        this.cmiClientIds = cmiClientIds;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
