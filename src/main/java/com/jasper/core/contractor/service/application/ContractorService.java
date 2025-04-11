package com.jasper.core.contractor.service.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractorService {


    public void sync() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        HttpGet get=new HttpGet("https://www.cslb.ca.gov/onlineservices/dataportal/ListByClassification");
        HttpResponse response=client.execute(get);
        Header[] cookies=response.getHeaders("Set-Cookie");
        String html= EntityUtils.toString(response.getEntity());

        StringBuilder builder=new StringBuilder();
        for(Header cookie:cookies){
            String str=cookie.getValue().substring(0,cookie.getValue().indexOf(";")+1);
            builder.append(str);
        }

        Document document= Jsoup.parse(html);
        Element eventvalidation=document.getElementById("__EVENTVALIDATION");
        Element viewState=document.getElementById("__VIEWSTATE");
        Element viewstategenerator=document.getElementById("__VIEWSTATEGENERATOR");

        HttpPost post=new HttpPost("https://www.cslb.ca.gov/onlineservices/dataportal/ListByClassification");
        post.addHeader("content-type","application/x-www-form-urlencoded");
        post.addHeader("origin","https://www.cslb.ca.gov");
        post.addHeader("referer","https://www.cslb.ca.gov/onlineservices/dataportal/ListByClassification");
        post.addHeader("cookie",builder.toString());
        post.addHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36 Edg/135.0.0.0");

        List<NameValuePair> nameValuePairList=new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("__EVENTTARGET","ctl00$MainContent$btnSearch"));
        nameValuePairList.add(new BasicNameValuePair("__VIEWSTATE",viewState.val()));
        nameValuePairList.add(new BasicNameValuePair("__EVENTARGUMENT",""));
        nameValuePairList.add(new BasicNameValuePair("__VIEWSTATEGENERATOR",viewstategenerator.val()));
        nameValuePairList.add(new BasicNameValuePair("__EVENTVALIDATION",eventvalidation.val()));
        nameValuePairList.add(new BasicNameValuePair("ctl00$MainContent$lbClassification","A"));


        UrlEncodedFormEntity entity=new UrlEncodedFormEntity(nameValuePairList);
        post.setEntity(entity);
        response=client.execute(post);
        String contentType=response.getFirstHeader("Content-Type").getValue();
        if(contentType.contains("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")){
            HttpEntity body=response.getEntity();
            IOUtils.copy(body.getContent(),new FileOutputStream("C:/Users/xiaow/Desktop/test.xls"));
        }
    }
}
