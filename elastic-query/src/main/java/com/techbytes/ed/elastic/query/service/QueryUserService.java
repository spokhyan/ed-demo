package com.techbytes.ed.elastic.query.service;


import com.techbytes.ed.elastic.query.dataaccess.entity.UserPermission;

import java.util.List;
import java.util.Optional;

public interface QueryUserService {

    Optional<List<UserPermission>> findAllPermissionsByUsername(String username);
}
