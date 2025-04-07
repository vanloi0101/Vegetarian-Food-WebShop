package com.devteria.identityservice.mapper;

import com.devteria.identityservice.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.devteria.identityservice.dto.request.RoleRequest;
import com.devteria.identityservice.dto.response.RoleResponse;
import com.devteria.identityservice.entity.Role;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    // Ánh xạ 'permissions' từ String (trong RoleRequest) thành Set<Permission> (trong Role)
    @Mapping(target = "permissions", source = "permissions", qualifiedByName = "mapPermissions")
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

    // Phương thức ánh xạ String thành Permission (dành cho mối quan hệ @ManyToMany)
    @Named("mapPermissions")
    default Set<Permission> mapPermissions(Set<String> permissionNames) {
        // Logic để chuyển đổi Set<String> thành Set<Permission>
        Set<Permission> permissions = new HashSet<>();
        for (String permissionName : permissionNames) {
            Permission permission = new Permission();
            permission.setName(permissionName);  // Set tên cho permission (có thể tùy chỉnh logic này)
            permissions.add(permission);
        }
        return permissions;
    }
}
