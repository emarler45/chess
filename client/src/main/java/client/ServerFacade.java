package client;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class ServerFacade {

    private static final HttpClient userClient = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url){
        serverUrl = url;
    }

    public HttpRequest register(String username, String password, String email){
        String path = "/user";

    }

    public HttpRequest login(String username, String password){

    }

    public HttpRequest logout(){

    }

    public HttpRequest joinGame(int id){

    }

    public HttpRequest listGames(){

    }

    public HttpRequest createGame(String gameName){

    }

    public HttpRequest delete(){

    }

    private void makeRequest(String path, String ){

    }

}
