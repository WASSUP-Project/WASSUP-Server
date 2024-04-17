package net.skhu.wassup.app.member.service;

import net.skhu.wassup.app.member.api.dto.RequestMember;
import net.skhu.wassup.app.member.api.dto.ResponseMember;

public interface MemberService {

    void save(RequestMember requestMember);

    ResponseMember getMember(Long id);

}