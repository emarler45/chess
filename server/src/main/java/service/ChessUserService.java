package service;


import dataaccess.*;
import handler.*;
import model.*;

import java.util.UUID;

public class ChessUserService {

    private final UserDAO userdao = new UserDAOMemory();
    private final AuthDAO authdao = new AuthDAOMemory();


}
