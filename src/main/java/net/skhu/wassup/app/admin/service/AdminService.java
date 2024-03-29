package net.skhu.wassup.app.admin.service;

public interface AdminService {

    void signup();

    void login();

    boolean isDuplicateId();

    void isMatchPassword();

    void sendCertificationNumber();

    void isMatchCertificationNumber();

}
