package br.com.anteros.nextcloud.api;

import br.com.anteros.nextcloud.api.utils.ConnectorCommon;
import br.com.anteros.nextcloud.api.utils.NextcloudResponseHelper;
import br.com.anteros.nextcloud.api.utils.XMLAnswerParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ConfigConnector {
    private final static String CONFIG_PART = "ocs/v2.php/apps/provisioning_api/api/v1/config/";

    private final ConnectorCommon connectorCommon;

    public ConfigConnector(ServerConfig serverConfig){
        this.connectorCommon = new ConnectorCommon(serverConfig);
    }

    public List<String> getAppConfigApps(){
        return NextcloudResponseHelper.getAndWrapException(getAppConfigAppsAsync())
                .getAppConfigApps();
    }

    private CompletableFuture<AppConfigAppsAnswer> getAppConfigAppsAsync(){
        return connectorCommon.executeGet(CONFIG_PART + "apps", Collections.emptyList(),
                XMLAnswerParser.getInstance(AppConfigAppsAnswer.class));
    }

    public List<String> getAppConfigAppKeys(String appConfigApp){
        return NextcloudResponseHelper.getAndWrapException(getAppConfigAppsAsync(appConfigApp))
                .getAppConfigApps();
    }

    private CompletableFuture<AppConfigAppsAnswer> getAppConfigAppsAsync(String appConfigApp){
        return connectorCommon.executeGet(CONFIG_PART + "apps/" + appConfigApp,
                Collections.emptyList(), XMLAnswerParser.getInstance(AppConfigAppsAnswer.class));
    }

    public String getAppConfigAppKeyValue(String appConfigApp, String appConfigAppKey){
        return NextcloudResponseHelper
                .getAndWrapException(getAppConfigAppsKeyAsync(appConfigApp + "/" + appConfigAppKey))
                .getAppConfigAppKeyValue();
    }

    public String getAppConfigAppKeyValue(String appConfigAppKeyPath){
        return NextcloudResponseHelper
                .getAndWrapException(getAppConfigAppsKeyAsync(appConfigAppKeyPath))
                .getAppConfigAppKeyValue();
    }

    private CompletableFuture<AppConfigAppKeyValueAnswer> getAppConfigAppsKeyAsync(
            String appConfigAppKeyPath){
        return connectorCommon.executeGet(CONFIG_PART + "apps/" + appConfigAppKeyPath,
                Collections.emptyList(), XMLAnswerParser.getInstance(AppConfigAppKeyValueAnswer.class));
    }

    public boolean setAppConfigAppKeyValue(String appConfigApp, String appConfigAppKey,
                                           Object value){
        return NextcloudResponseHelper.isStatusCodeOkay(
                setAppConfigAppKeyValueAsync(appConfigApp + "/" + appConfigAppKey, value));
    }

    public boolean setAppConfigAppKeyValue(String appConfigAppKeyPath, Object value){
        return NextcloudResponseHelper
                .isStatusCodeOkay(setAppConfigAppKeyValueAsync(appConfigAppKeyPath, value));
    }

    public CompletableFuture<AppConfigAppKeyValueAnswer> setAppConfigAppKeyValueAsync(
            String appConfigAppKeyPath, Object value){
        List<NameValuePair> postParams = new LinkedList<>();
        postParams.add(new BasicNameValuePair("value", value.toString()));
        return connectorCommon.executePost(CONFIG_PART + "apps/" + appConfigAppKeyPath, postParams,
                XMLAnswerParser.getInstance(AppConfigAppKeyValueAnswer.class));
    }

    public boolean deleteAppConfigAppKeyEntry(String appConfigApp, String appConfigAppkey){
        throw new UnsupportedOperationException();
    }
}
