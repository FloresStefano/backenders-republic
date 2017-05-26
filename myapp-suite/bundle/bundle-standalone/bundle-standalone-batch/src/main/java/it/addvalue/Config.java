package it.addvalue;

import java.util.HashMap;
import java.util.Map;

public class Config
{
    private Map<String, String> attributes = null;

    public Config()
    {
        attributes = new HashMap<String, String>(10);
    }

    public void setAttribute(String attributeName,
                             String attributeValue)
    {
        attributes.put(attributeName, attributeValue);
    }

    public String getAttribute(String attributeName)
    {
        return attributes.get(attributeName);
    }

    public int size()
    {
        if ( attributes != null )
        {
            return attributes.size();
        }
        else
            return -1;
    }
}
