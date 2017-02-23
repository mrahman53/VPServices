package org.vp.scraping;

import com.google.gdata.data.contacts.SystemGroup;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by mrahman on 2/17/17.
 */
public class VcProilfeModel {
    public static void main(String[] args) throws IOException,JSONException,FileNotFoundException{
        //JsonReader.setLenient(true);
        Gson gson = new Gson();
        String url = "https://script.googleusercontent.com/macros/echo?user_content_key=e0Zyl99h_DjgwrcN9UDaF78lZhplU9sxvoL3V0S2KGzjxUgzGtpo104JoRuANXAV2PwvTOFPBIRgeGMfbgZYvrjpEcQUOVG_OJmA1Yb3SEsKFZqtv3DaNYcMrmhZHmUMWojr9NvTBuBLhyHCd5hHa1ZsYSbt7G4nMhEEDL32U4DxjO7V7yvmJPXJTBuCiTGh3rUPjpYM_V0PJJG7TIaKp2kfUMyyBmOzv8XwdtRmKLAr9QbJrXC3LQl3gfbFj2W9K3fGcx1i1MSqe9bP0ZUND8KiW3k6MDkf31SIMZH6H4k&lib=MbpKbbfePtAVndrs259dhPT7ROjQYJ8yx";
        JSONObject json = readJsonFromUrl(url);
        System.out.println(json.toString());
        System.out.println();

        /*
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new StringReader("/Users/mrahman/develop/vp/vc.json"));
        reader.setLenient(true);
        VcProilfeModel model = gson.fromJson(reader.toString(),VcProilfeModel.class);
        JsonElement json = gson.fromJson(new FileReader("/Users/mrahman/develop/vp/vc.json"), JsonElement.class);
        String result = gson.toJson(json);
        System.out.println(result);  */
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }


}
