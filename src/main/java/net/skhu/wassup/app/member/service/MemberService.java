package net.skhu.wassup.app.member.service;

import net.skhu.wassup.app.member.api.dto.RequestMember;
import net.skhu.wassup.app.member.api.dto.RequestUpdateMember;
import net.skhu.wassup.app.member.api.dto.ResponseMember;

public interface MemberService {

    void saveMember(RequestMember requestMember);

    ResponseMember getMember(Long id);

    void updateMember(Long id, RequestUpdateMember requestUpdateMember);

}