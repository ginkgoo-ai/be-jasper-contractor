package com.jasper.core.contractor.service.cslb;

import com.jasper.core.contractor.dto.response.CslbContractor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.format.CellFormat;
import org.apache.poi.ss.format.CellFormatResult;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class CslbClient implements Closeable {
    private static final String HOST = "https://www.cslb.ca.gov";
    private static final String API_URL = HOST + "/onlineservices/dataportal/ListByClassification";
    private static final String MOCK_UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36 Edg/135.0.0.0";

    private final CloseableHttpClient client;

    public CslbClient() {
        client = HttpClients.createDefault();
    }

    private SiteInfo getSiteInfo() throws IOException {
        HttpGet get = new HttpGet(API_URL);
        CloseableHttpResponse response = client.execute(get);

        Header[] cookies = response.getHeaders(HttpHeaders.SET_COOKIE);

        StringBuilder builder = new StringBuilder();
        for (Header cookie : cookies) {
            String str = cookie.getValue().substring(0, cookie.getValue().indexOf(";") + 1);
            builder.append(str);
        }
        String cookie = builder.toString();

        String html = EntityUtils.toString(response.getEntity());
        IOUtils.close(response);
        Document document = Jsoup.parse(html);
        Element eventValidationElement = document.getElementById("__EVENTVALIDATION");
        String eventValidation = Optional.ofNullable(eventValidationElement).orElse(new Element("input")).val();

        Element viewStateElement = document.getElementById("__VIEWSTATE");
        String viewState = Optional.ofNullable(viewStateElement).orElse(new Element("input")).val();

        Element viewStateGeneratorElement = document.getElementById("__VIEWSTATEGENERATOR");
        String viewStateGenerator = Optional.ofNullable(viewStateGeneratorElement).orElse(new Element("input")).val();

        return SiteInfo.builder()
                .cookie(cookie)
                .eventValidation(eventValidation)
                .viewState(viewState)
                .viewStateGenerator(viewStateGenerator)
                .build();
    }

    public List<CslbContractor> search(List<String> classificationList) throws IOException {
        List<CslbContractor> result = new ArrayList<>();

        SiteInfo siteInfo = getSiteInfo();
        HttpPost post = new HttpPost(API_URL);

        post.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        post.addHeader(HttpHeaders.ORIGIN, HOST);
        post.addHeader(HttpHeaders.REFERER, API_URL);
        post.addHeader(HttpHeaders.COOKIE, siteInfo.cookie);
        post.addHeader(HttpHeaders.USER_AGENT, MOCK_UA);

        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("__EVENTTARGET", "ctl00$MainContent$btnSearch"));
        nameValuePairList.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
        for (String classification : classificationList) {
            nameValuePairList.add(new BasicNameValuePair("ctl00$MainContent$lbClassification", classification));
        }

        nameValuePairList.add(new BasicNameValuePair("__VIEWSTATE", siteInfo.viewState));
        nameValuePairList.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", siteInfo.viewStateGenerator));
        nameValuePairList.add(new BasicNameValuePair("__EVENTVALIDATION", siteInfo.eventValidation));


        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairList);
        post.setEntity(entity);
        CloseableHttpResponse response = client.execute(post);
        String contentType = response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue();
        if (contentType.contains("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            File tmpFile = Files.createTempFile("cslb_",".xlsx").toFile();
            HttpEntity body = response.getEntity();
            IOUtils.copy(body.getContent(), new FileOutputStream(tmpFile));

            try (XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(tmpFile)) {
                FormulaEvaluator evaluator = new XSSFFormulaEvaluator(workbook);
                Sheet sheet = workbook.getSheetAt(0);
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);

                    try {
                        CslbContractor contractor = new CslbContractor();
                        String licenseNumber = getCellStringValue(row.getCell(0), evaluator);
                        contractor.setLicenseNumber(licenseNumber);
                        contractor.setLastUpdated(row.getCell(1).getStringCellValue());
                        contractor.setBusinessType(row.getCell(2).getStringCellValue());
                        contractor.setBusinessName(row.getCell(3).getStringCellValue());
                        contractor.setAddress(row.getCell(4).getStringCellValue());
                        contractor.setCity(row.getCell(5).getStringCellValue());
                        contractor.setState(row.getCell(6).getStringCellValue());
                        contractor.setZip(row.getCell(7).getStringCellValue());
                        contractor.setCounty(row.getCell(8).getStringCellValue());
                        contractor.setPhoneNumber(row.getCell(9).getStringCellValue());
                        contractor.setIssueDate(row.getCell(10).getStringCellValue());
                        contractor.setExpirationDate(row.getCell(11).getStringCellValue());
                        contractor.setClassification(row.getCell(12).getStringCellValue());
                        contractor.setStatus(row.getCell(13).getStringCellValue());
                        result.add(contractor);
                    } catch (Exception e) {
                        log.error("Parse excel error at row {}", i, e);
                    }
                }
            } finally {
                tmpFile.delete();
            }

        }
        IOUtils.close(response);
        return result;
    }

    public static String getCellStringValue(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING -> {
                return StringUtils.trim(cell.getStringCellValue());
            }
            case NUMERIC -> {
                Short dateFormat = cell.getCellStyle().getDataFormat();
                CellFormat cf = CellFormat.getInstance(String.valueOf(dateFormat));
                CellFormatResult result = cf.apply(cell);
                return StringUtils.trim(result.text);
            }
            case BOOLEAN -> {
                return String.valueOf(cell.getBooleanCellValue());
            }
            case FORMULA -> {

                Cell refCell = evaluator.evaluateInCell(cell);
                return StringUtils.trim(getCellStringValue(refCell, evaluator));
            }
            default -> {
                return "";
            }
        }

    }


    @Override
    public void close() throws IOException {
        IOUtils.close(client);
    }

    @Builder
    private static class SiteInfo {
        private String cookie;
        private String eventValidation;
        private String viewState;
        private String viewStateGenerator;
    }
}
