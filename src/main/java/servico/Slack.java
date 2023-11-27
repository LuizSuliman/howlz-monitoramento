package servico;

import com.squareup.okhttp.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Base64;

public class Slack {
    public static void enviarMensagem(String mensagem) throws Exception {
        String chaveCriptografada = "vUrzwzy9WdxidIrA+eqcTYL2ZjTeK3qnybeVkQ+E0fOU8QoBea8JQczWmDhiZI82OYYo+t3I4IeZzo1kRW4Fxg==";

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"text\": \"%s\"}".formatted(mensagem));
        Request request = new Request.Builder()
                .url(String.format("https://hooks.slack.com/services/%s", descriptografar(chaveCriptografada)))
                .method("POST", body)
                .addHeader("Content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        response.body().close();
    }

    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "claudIA_howlz987";
    private static final String CHARSET = "UTF-8";

    /*public static String criptografar(String textoOriginal) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(CHARSET), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(textoOriginal.getBytes(CHARSET));
        return Base64.getEncoder().encodeToString(encrypted);
    }*/

    public static String descriptografar(String textoCriptografado) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(CHARSET), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(textoCriptografado));
        return new String(decrypted, CHARSET);
    }
}
