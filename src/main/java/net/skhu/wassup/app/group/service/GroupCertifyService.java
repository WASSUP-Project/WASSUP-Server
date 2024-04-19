package net.skhu.wassup.app.group.service;

public interface GroupCertifyService {

    boolean isDuplicateName(String groupName);

    void certification(String email);

    boolean verify(String email, String inputCode);

}
