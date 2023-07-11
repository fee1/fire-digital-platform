package com.huajie.common.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

/**
 * @Description: 在图片上生成二维码并且在图片上添加文字response前端
 * @Auther: yjj
 * @Date: 2020/9/23 18:57
 */
public class QRCodeUtils {
 
	private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "jpg";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 130;
    // LOGO宽度
    private static final int WIDTH = 40;
    // LOGO高度
    private static final int HEIGHT = 40;

    /**
     * logo来源本地
     */
    public static final String IMG_RESOURCE_TYPE_LOCAL = "LOCAL";
    /**
     * logo来源网络
     */
    public static final String IMG_RESOURCE_TYPE_WEB = "WEB";

    /**
     * 生成二维码
     * @param content 二维码内容
     * @param logoPath logo路径，可为空
     * @param needCompress 是否压缩
     * @return
     * @throws Exception
     */
    public static BufferedImage encode(String content, String logoPath,String imgResourceType,boolean needCompress) throws Exception {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000: 0xFFFFFFFF);
            }
        }
        if (logoPath == "") {
            return image;
        }
        // 插入图片
        insertImage(image, logoPath,imgResourceType, needCompress);
        return image;
    }

    /**
     * 插入描述图片
     * @param source
     * @param imgPath
     * @param imgResourceType 图片来源 LOCAL本地 WEB网络
     * @param needCompress 是否压缩
     * @throws Exception
     */
    private static void insertImage(BufferedImage source, String imgPath,String imgResourceType,boolean needCompress) throws Exception {
        Image src = null;
        if(IMG_RESOURCE_TYPE_WEB.equals(imgResourceType)) {
            URL url = new URL(imgPath);
            //打开链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);
            //通过输入流获取图片数据
            src = ImageIO.read(conn.getInputStream());
        }else{
            File file = new File(imgPath);
            if (!file.exists()) {
                //文件不存在
                return;
            }
            src = ImageIO.read(new File(imgPath));
        }
        if(src==null){
            return;
        }
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height,
                    Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }
}
