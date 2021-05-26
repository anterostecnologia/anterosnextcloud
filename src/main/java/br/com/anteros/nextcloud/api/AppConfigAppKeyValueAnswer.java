package br.com.anteros.nextcloud.api;

import br.com.anteros.nextcloud.api.utils.XMLAnswer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ocs")
public class AppConfigAppKeyValueAnswer extends XMLAnswer {
    private Data data;

    public String getAppConfigAppKeyValue(){
        return data.getAppConfigAppKeyValue;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    private static final class Data {
        @XmlElement(name = "data")
        private String getAppConfigAppKeyValue;
    }
}