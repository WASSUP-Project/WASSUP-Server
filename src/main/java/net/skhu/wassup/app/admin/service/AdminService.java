package net.skhu.wassup.app.admin.service;

import net.skhu.wassup.app.admin.api.dto.RequestLogin;
import net.skhu.wassup.app.admin.api.dto.RequestSignup;
import net.skhu.wassup.app.admin.api.dto.ResponseLogin;

public interface AdminService {

    void certification(String phoneNumber);

    boolean verify(String phoneNumber, String inputCode);

    boolean isDuplicateId(String adminId);

    void signup(RequestSignup requestSignup);

    ResponseLogin login(RequestLogin requestLogin);

}
