package cn.luowb.clubrecruitment.common.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 访客令牌工具：生成高强度随机令牌，并用服务端密钥计算匿名摘要。
 */
public final class VisitorTokenUtil {

    /** 令牌随机字节数。 */
    private static final int TOKEN_BYTE_LENGTH = 32;

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private VisitorTokenUtil() {
    }

    /**
     * 生成 Base64URL 编码的高强度随机访客令牌。
     *
     * @return 不含填充的 Base64URL 令牌
     */
    public static String generateToken() {
        byte[] bytes = new byte[TOKEN_BYTE_LENGTH];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * 计算访客令牌的 HMAC-SHA256 匿名摘要，用于落库而不暴露原始令牌。
     *
     * @param token  访客令牌
     * @param secret 服务端签名密钥
     * @return 32 字节摘要
     */
    public static byte[] hashToken(String token, String secret) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("访客令牌不能为空");
        }
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
            return mac.doFinal(token.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("HMAC-SHA256 摘要计算失败", e);
        }
    }
}
