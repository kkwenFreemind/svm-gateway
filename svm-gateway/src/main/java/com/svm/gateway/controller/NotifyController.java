package com.svm.gateway.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.svm.gateway.security.AuthUtil;
import com.svm.gateway.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author : Kevin Chang
 * @create 2023/10/18 上午9:06
 */
@Slf4j
@RestController
@RequestMapping("/VM_MD/API")
public class NotifyController {

    @Autowired
    AuthUtil authUtil;

    @Autowired
    private ApiService apiService;

    @Value("${chatbot.url}")
    private String ChatBot_API_URL;

    @Value("${chatbot.username}")
    private String chatbotUsername;

    @Value("${chatbot.password}")
    private String chatbotPassword;

    private static String teamplus_url = "http://eim.gtcloud.net.tw/API/SuperHubService.ashx?ask=broadcastMessage&ch_sn=21&api_key=eab6215d83be40faa22b0bbd2e31dce9&content_type=1&text_content=";

    @RequestMapping(value = "/test_notify", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> getTestNotify(
            @RequestHeader(value = "username") String username,
            @RequestHeader(value = "password") String password,
            HttpServletRequest request,
            @RequestBody String injson) {

        String myjson = injson.replaceAll("[ \t\r\n]+", " ").trim();
        JSONObject jsonObject = JSONObject.parseObject(myjson);
        JSONArray jsonarray = jsonObject.getJSONArray("data");
        log.info("=>"+jsonarray.toJSONString());

        return apiService.sendNotifyInfo(jsonarray);
//        ArrayList<String> arrayList = new ArrayList<>();
//        return CommonResult.validateFailed("未通過參數檢查",arrayList);
    }


    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    @ResponseBody
    public String getNotify(
            @RequestHeader(value = "username") String username,
            @RequestHeader(value = "password") String password,
            HttpServletRequest request,
            @RequestBody String injson) {

        String JResult = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String getNow = sdf.format(new Date());

        boolean auth_flag = authUtil.checkNotifyAccount(username, password);
        if (auth_flag) {
            String myjson = injson.replaceAll("[ \t\r\n]+", " ").trim();
            log.info("source notify json->" + myjson);

            //send Notify to chatbot
            String result = SendNotifyToChatBot(myjson);
            log.info("SendNotifyToChatBot result:" + result);

            //send Notify to teamplus
            JSONObject json = JSONObject.parseObject(myjson);
            JSONArray jsonarray = json.getJSONArray("data");

            for (int i = 0; i < jsonarray.size(); i++) {
                JSONObject jobj = (JSONObject) jsonarray.get(i);

                if ("離線超過30分鐘".equalsIgnoreCase(jobj.get("type").toString()) ||
                        "溫度異常".equalsIgnoreCase(jobj.get("type").toString())) {

                    log.info("type:" + jobj.get("type"));
                    log.info("JsonObject:" + jobj.toJSONString());
                    String teamplus_result = sendNotifyToTeamplus(jobj.toJSONString());
                }
            }

            if ("OK".equalsIgnoreCase(result)) {
                JResult = "{ \"Code\":00, \"Reason\":\"成功\", \"Response_Time\":\"" + getNow + "\", \"Data\":[] }";
            } else {
                JResult = "{ \"Code\":200, \"Reason\":\"失敗\", \"Response_Time\":\"" + getNow + "\", \"Data\":[] }";
            }
        } else {
            JResult = "{ \"Code\":200, \"Reason\":\"auth失敗\", \"Response_Time\":\"" + getNow + "\", \"Data\":[] }";
        }
        log.info("SendNotify response:" + JResult);
        return JResult;

    }

    private String SendNotifyToChatBot(String injson) {

        String url = ChatBot_API_URL;
//        String url = "http://localhost:9091/VM_MD/API/simulate";
        String result = "";
        try {
            URL chatbot_url = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) chatbot_url.openConnection();
            log.info("CHATBOT Event URL=>" + chatbot_url);
            conn.setConnectTimeout(50000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("username", chatbotUsername);
            conn.setRequestProperty("password", chatbotPassword);
            conn.setRequestMethod("POST");
            String jsonbody = injson;

            log.info("Send Chatbot event jsonbody ===>" + jsonbody.replaceAll("[ \t\r\n]+", " ").trim());

            OutputStream os = conn.getOutputStream();
            os.write(jsonbody.getBytes("UTF-8"));
            os.close();
            StringBuilder buffer = new StringBuilder();
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            while ((str = bufferedReader.readLine()) != null)
                buffer.append(str);
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            conn.disconnect();
            String kkstr = buffer.toString();
            result = kkstr;
        } catch (Exception ex) {
            log.info(ex.toString());
        }
        return result;
    }

    private String sendNotifyToTeamplus(String injson) {
        JSONObject result = null;
        try {
            String kk_msg = URLEncoder.encode(injson, "UTF-8");
            String get_url = teamplus_url + kk_msg;
            log.info("URL=>" + get_url);
            URL team_url = new URL(get_url);
            HttpURLConnection conn = (HttpURLConnection) team_url.openConnection();
            log.info("team Event URL=>" + team_url);
            conn.setConnectTimeout(50000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            log.info("GET Response Code :: " + responseCode);
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer response = new StringBuffer();
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    response.append(inputLine);
                in.close();
                String result_format = response.toString().replaceAll("[ \t\r\n]+", " ").trim();
                log.info("Team Result==>" + result_format);
                result = JSONObject.parseObject(response.toString());
            } else {
                log.info("GET request not worked");
            }
        } catch (Exception ex) {
            log.info(ex.toString());
        }
        return result.toString();
    }
}
