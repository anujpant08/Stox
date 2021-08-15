package com.example.stox;

import java.util.List;

public class AlphaVantageAPICall {
    protected final String APIKEY;
    protected final String baseURL;
    protected List<String> params;

    public AlphaVantageAPICall(String baseURL, String apiKey) {
        this.baseURL = baseURL;
        this.APIKEY = apiKey;
    }
    public void setParams(List<String> params){
        this.params = params;
    }
    public List<String> getParams(){
        return this.params;
    }
    public String getURL(){
        StringBuilder finalURL = new StringBuilder(this.baseURL);
        //Construct final URL using List of params
        for(String param : this.params){
            finalURL.append(param).append("&");
        }
        finalURL.append("apikey=").append(this.APIKEY);
        return finalURL.toString();
    }
}
