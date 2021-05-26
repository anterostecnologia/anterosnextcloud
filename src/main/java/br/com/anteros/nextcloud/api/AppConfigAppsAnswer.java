package br.com.anteros.nextcloud.api;

import br.com.anteros.nextcloud.api.utils.XMLAnswer;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "ocs")
public class AppConfigAppsAnswer extends XMLAnswer
{
    private Data data;

    public List<String> getAppConfigApps()
    {
        return data.appConfigApps;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    private static final class Data
    {
        @XmlElementWrapper(name = "data")
        @XmlElement(name = "element")
        private List<String> appConfigApps;
    }
}