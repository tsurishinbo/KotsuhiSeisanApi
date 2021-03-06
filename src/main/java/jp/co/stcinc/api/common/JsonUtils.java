/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.stcinc.api.common;

import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * JSONユーティリティクラス
 */
public class JsonUtils {

    /**
     * JSONデータを構造体に変換する
     * @param <T> 構造体の型
     * @param structure 構造体
     * @param json JSONデータ
     * @return 構造体
     */
    public static <T> T parseJson(Class<T> structure, String json) {
        ObjectMapper map = new ObjectMapper();
        T dto;
        try {
            dto = map.readValue(json, structure);
        } catch(IOException e) {
            dto = null;
        }
        return dto;
    }

    /**
     * 構造体をJSONデータに変換する
     * @param dto 構造体
     * @return JSONデータ
     */
    public static String makeJson(Object dto) {
        ObjectMapper map = new ObjectMapper();
        String json;
        try {
            // writerWithDefaultPrettyPrinter()はインデントのため
            json = map.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
        } catch(IOException e) {
            json = null;
        }
        return json;
    }
}
