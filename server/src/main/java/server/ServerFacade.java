package server;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;

import service.*;
import com.google.gson.Gson;

public class ServerFacade {

    private static final HttpClient userClient = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url){
        serverUrl = url;
    }

    public RegisterResult register(String username, String password, String email){
        String path = "/user";
        String method = "POST";
        RegisterRequest r = new RegisterRequest(username, password, email);
        HttpRequest req = makeRequest(path, method, null, r);
        HttpResponse<String> resp = sendRequest(req);
        return handleResponse(resp, RegisterResult.class);
    }

    public LoginResult login(String username, String password){
        String path = "/session";
        String method = "POST";
        LoginRequest r = new LoginRequest(username, password);
        HttpRequest req = makeRequest(path, method, null, r);
        HttpResponse<String> resp = sendRequest(req);
        return handleResponse(resp, LoginResult.class);
    }

    public void logout(String token){
        String path = "/session";
        String method = "DELETE";
        HttpRequest req = makeRequest(path, method, token, null);
        HttpResponse<String> resp = sendRequest(req);
        handleResponse(resp, null);
    }

    public void joinGame(String token, int id, String playerColor){
        String path = "/game";
        String method = "PUT";
        JoinGameRequest r = new JoinGameRequest(playerColor, id);
        HttpRequest req = makeRequest(path, method, token, r);
        HttpResponse<String> resp = sendRequest(req);
        handleResponse(resp, null);
    }

    public ListGamesResult listGames(String token){
        String path = "/game";
        String method = "GET";
        HttpRequest req = makeRequest(path, method, token,null);
        HttpResponse<String> resp = sendRequest(req);
        return handleResponse(resp, ListGamesResult.class);
    }

    public CreateGameResult createGame(String token, String gameName){
        String path = "/game";
        String method = "POST";
        CreateGameRequest r = new CreateGameRequest(gameName);
        HttpRequest req = makeRequest(path, method, token, r);
        HttpResponse<String> resp = sendRequest(req);
        return handleResponse(resp, CreateGameResult.class);
    }

    public void delete(){
        String path = "/db";
        String method = "DELETE";
        HttpRequest req = makeRequest(path, method, null,null);
        HttpResponse<String> resp = sendRequest(req);
        handleResponse(resp, null);
    }

    private HttpRequest makeRequest(String path, String method, String token, Record body){
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeBody(body));
        if (body != null){
            request.setHeader("Content-Type", "application/json");
        }
        if (token != null){
            request.setHeader("authorization", token);
        }
        return request.build();
    }

    private BodyPublisher makeBody(Record body){
        if (body != null){
            return BodyPublishers.ofString(new Gson().toJson(body));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request){
        try {
            return userClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new ResponseException(e.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> desiredClass){
        int code = response.statusCode();
        if (!isSuccessfulCode(code)){
            var respBody = response.body();
            if (respBody!= null){
                throw ResponseException.fromJson(respBody);
            }
            throw new ResponseException("Other failure");
        }
        if (desiredClass == null){
            return null;
        } else {
            return new Gson().fromJson(response.body(), desiredClass);
        }
    }

    private boolean isSuccessfulCode(int code){
        return code / 100 == 2;
    }

}
