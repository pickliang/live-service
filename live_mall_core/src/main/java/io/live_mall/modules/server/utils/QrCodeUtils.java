package io.live_mall.modules.server.utils;

import com.github.binarywang.utils.qrcode.MatrixToImageWriter;
import com.google.common.collect.Maps;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;

/**
 * @author yewl
 * @date 2023/1/4 15:31
 * @description
 */
public class QrCodeUtils {
    /**
     * 生成Base64二维码
     * @param url 域名地址
     */
    public static String qrCode(String url) {
        String base64Url = "data:image/png;base64,";
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        HashMap<EncodeHintType, Object> hints = Maps.newHashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix ;
        String text = "";
        try {
            bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 600, 600, hints);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            Base64.Encoder encoder = Base64.getEncoder();
            text = encoder.encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64Url + text;
    }
}
