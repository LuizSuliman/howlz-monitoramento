package servico;

import com.squareup.okhttp.*;

import java.io.IOException;

public class Slack {
    public static void enviarMensagem(String mensagem) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n  \"channel\": \"C065BU9GAQG\",\r\n  \"text\": \"%s\"\r\n}".formatted(mensagem));
        Request request = new Request.Builder()
                .url("https://slack.com/api/chat.postMessage")
                .method("POST", body)
                .addHeader("Content-type", "application/json")
                .addHeader("Authorization", "Bearer ???") // REMOVER TOKEN ANTES DE SUBIR NO GIT
                .build();
        Response response = client.newCall(request).execute();
        response.body().close();
    }
}
