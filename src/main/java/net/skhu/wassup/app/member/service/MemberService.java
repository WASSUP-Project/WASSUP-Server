package net.skhu.wassup.app.member.service;

import net.skhu.wassup.app.member.api.dto.RequestMember;

public interface MemberService {
    void save(RequestMember requestMember);
}