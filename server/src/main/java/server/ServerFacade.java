package client;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class ServerFacade {

    private static final HttpClient userClient = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url){
        serverUrl = url;
    }

    public void register(String username, String password, String email){
        String path = "/user";
        String method = "POST";
        HttpRequest req = makeRequest(path, method, username, password, email);
    }

    public void login(String username, String password){
        String path = "/session";
        String method = "POST";
    }

    public void logout(){
        String path = "/session";
        String method = "DELETE";
    }

    public void joinGame(int id){
        String path = "/game";
        String method = "PUT";


    }

    public void listGames(){
        String path = "/game";
        String method = "GET";
    }

    public void createGame(String gameName){
        String path = "/game";
        String method = "POST";

    }

    public void delete(){
        String path = "/db";
        String method = "DELETE";

    }

    private HttpRequest makeRequest(String path, String method, Object... params){

    }



}
