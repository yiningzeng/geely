package com.baymin.restroomapi.config.aspect.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;

/**
 * Created by baymin on 17-8-4.
 */
@Slf4j
public class TokenUtils {


    public static void main(String[] args) {


//     String xml="<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
//             "  <soap:Body>\n" +
//             "    <ns2:selectPrdListWithVinForServiceResponse xmlns:ns2=\"http://remote.mm.mes.infohow.cn/\"></ns2:selectPrdListWithVinForServiceResponse>\n" +
//             "  </soap:Body>\n" +
//             "</soap:Envelope>";
//        try {
//            Document document = DocumentHelper.parseText(xml);
//            Element element = document.getRootElement().element("Body").element("selectPrdListWithVinForServiceResponse").element("return");
//            String nextVin=element.element("vin").getText();
//            System.out.println(nextVin);
//            //region return的结果
//            /**
//             * <return>
//             *         <acceptqty>0</acceptqty>
//             *         <actsentqty>0</actsentqty>
//             *         <adaptseq/>
//             *         <autime/>
//             *         <carcode>MR7142L05RAJ</carcode>
//             *         <cartype>FE-6</cartype>
//             *         <conftime>2018-09-07 12:38:54</conftime>
//             *         <cpcode/>
//             *         <cpqty>0.0</cpqty>
//             *         <fccode/>
//             *         <flg/>
//             *         <monum/>
//             *         <moqtys>0</moqtys>
//             *         <mtcode>Z06FE0JMRAD5000A08</mtcode>
//             *         <ordernum>61034475</ordernum>
//             *         <pfdate/>
//             *         <prdid>20189285756</prdid>
//             *         <psdate/>
//             *         <qty>0</qty>
//             *         <recid/>
//             *         <rectype/>
//             *         <remark/>
//             *         <spcode/>
//             *         <stcode/>
//             *         <vin>L6T7824S2JN285756</vin>
//             *       </return>**/
//            //endregion
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }

       System.out.println(getJWTString("0001"));
//       isValid("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiYXltaW4iLCJhdWQiOiIwMDAxIiwiaXNzIjoiY29tLmdhbGlsZW9fYWkiLCJleHAiOjE1MzczMjgxMTIyMDYsImlhdCI6MTUzNDczNjExMjIwNiwianRpIjoiMi4wIn0.ZZAxmKn9sSRGx6qYa2EVKuU318SObEi6NEwH5-pIj3I");
    }
    // 版本
    public static String TOKEN_VERSION = "2.0";
    // 设置发行人
    public static String ISSUER = "com.galileo_ai";
    // 设置抽象主题
    public static String SUBJECT = "baymin";
    // HS256 私钥
    public static String HS256KEY = "baymin1024";

    public static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public static Key signingKey = new SecretKeySpec(Base64.decodeBase64(HS256KEY), signatureAlgorithm.getJcaName());

    /**
     * 生成
     * @param userId
     * @return
     */
    public static String getJWTString(String userId) {

        Map<String, Object> claims = new HashMap<>();
        long nowMillis = System.currentTimeMillis();
        claims.put(Claims.ID, TOKEN_VERSION);
        claims.put(Claims.ISSUER, ISSUER);
        claims.put(Claims.SUBJECT, SUBJECT);
        claims.put(Claims.AUDIENCE, userId);

        /*Date now=new Date(nowMillis);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
        Date date= new Date(nowMillis + 1000*60*60*30*24);//30天的有效期
        String strNow=sdf.format(now);
        String strAfter=sdf.format(date);
        System.out.println("当前时间："+strNow+"过期时间："+strAfter);*/

        //一定要分开写！否则1000*60*60*24*30 直接把int最大值爆了！！
        long oneHourMillis = 1000 * 60 * 60;//一小时毫秒数
        long exp = nowMillis + oneHourMillis * 30 * 24;//30天的有效期


        claims.put(Claims.EXPIRATION, exp);
        //claims.put(Claims.EXPIRATION, nowMillis + 10000);//10秒有效期
        claims.put(Claims.ISSUED_AT, nowMillis);

        JwtBuilder jwtBuilder = Jwts.builder().setClaims(claims);
        //System.out.println(System.currentTimeMillis() - nowMillis);
        jwtBuilder.signWith(signatureAlgorithm, signingKey);
        return "Bearer " + jwtBuilder.compact();
    }

    /**
     * 校验token
     * @param token
     * @return
     */
    public static String isValid(String token) {
        try {
            Jws<Claims> jwsClaims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token.trim());
            Claims cc = jwsClaims.getBody();
            Long exp = (Long) cc.get(Claims.EXPIRATION);

            System.out.println("token 值：");
            System.out.println(exp - System.currentTimeMillis());
            return exp - System.currentTimeMillis() > 0 ? cc.get(Claims.AUDIENCE).toString() : "overtime";
        } catch (Exception e) {
            e.printStackTrace();
            return "overtime";
        }
    }

    public static Map<String, Object> parseJWTtoMap(String token) {
        Claims claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token.trim()).getBody();
        return claims;
    }

    public static String getHS512Key() {
        Key key = MacProvider.generateKey(SignatureAlgorithm.HS512);
        String keyStr = Base64.encodeBase64String(key.getEncoded());
        return keyStr;
    }
}
