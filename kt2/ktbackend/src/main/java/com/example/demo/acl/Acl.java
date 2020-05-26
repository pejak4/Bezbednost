package com.example.demo.acl;


import com.example.demo.model.Users;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;
import java.util.Collections;
import java.util.EnumSet;

@Component
public class Acl {
    public void addRestorePermissions(String role) throws IOException
    {
        System.out.println(role);

        String username = "jovan";
        Path file = Paths.get("C://Users/jovan/OneDrive/Desktop/slike/jovan.txt");

        AclFileAttributeView aclAttr = Files.getFileAttributeView(file, AclFileAttributeView.class);

        UserPrincipalLookupService currULS = file.getFileSystem().getUserPrincipalLookupService();
        UserPrincipal principal = currULS.lookupPrincipalByName(username);


        AclEntry.Builder builder = AclEntry.newBuilder();
        if(role.equals("ADMIN")) {
            builder.setPermissions(EnumSet.of(
                    AclEntryPermission.READ_DATA, // Ne mozemo da citamo fajl tj otvorimo
                    AclEntryPermission.READ_ACL,
                    AclEntryPermission.READ_ATTRIBUTES,
                    AclEntryPermission.READ_NAMED_ATTRS,
                    AclEntryPermission.SYNCHRONIZE,
                    AclEntryPermission.DELETE, // Sa ovim mozemo da brisemo, bez ovoga samo admin moze da brise
                    AclEntryPermission.ADD_FILE,
                    AclEntryPermission.APPEND_DATA));
        } else {
            builder.setPermissions(EnumSet.of(
//                    AclEntryPermission.READ_DATA, // Ne mozemo da citamo fajl tj otvorimo
                    AclEntryPermission.READ_ACL,
                    AclEntryPermission.READ_ATTRIBUTES,
                    AclEntryPermission.READ_NAMED_ATTRS,
                    AclEntryPermission.SYNCHRONIZE,
//                    AclEntryPermission.DELETE, // Sa ovim mozemo da brisemo, bez ovoga samo admin moze da brise
                    AclEntryPermission.ADD_FILE,
                    AclEntryPermission.APPEND_DATA));
        }

        builder.setPrincipal(principal);
        builder.setType(AclEntryType.ALLOW);
        aclAttr.setAcl(Collections.singletonList(builder.build()));


        for(AclEntry entry : aclAttr.getAcl()) {
            System.out.println("---------------------------------");
            System.out.println(entry);
            System.out.println("---------------------------------");

            System.out.println("---------------------------------");
            System.out.println(entry.principal());
            System.out.println("---------------------------------");

            if(entry.principal().equals(principal)) {
                System.out.println("=== flags ===");
                for (AclEntryFlag flags : entry.flags()) {
                    System.out.println(flags.name());
                }

                System.out.println("=== permissions ===");
                for (AclEntryPermission permission : entry.permissions()) {
                    System.out.println(permission.name());
                }
            }
        }
    }
}
