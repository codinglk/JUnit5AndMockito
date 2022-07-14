package com.codinglk.io;

import java.util.Map;

public interface UsersDatabase {
    void init();
    void close();
    Map save(String userId, Map userDetails);
    Map update(String userId, Map user);
    Map find(String userId);
    void delete(String userId);
}
